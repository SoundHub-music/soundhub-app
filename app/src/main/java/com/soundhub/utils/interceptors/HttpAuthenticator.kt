package com.soundhub.utils.interceptors

import android.util.Log
import com.soundhub.Route
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.services.AuthService
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.domain.events.UiEvent
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants.UNAUTHORIZED_USER_ERROR_CODE
import com.soundhub.utils.extensions.request.withAppAuthorization
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import okhttp3.Route as HttpRoute

class HttpAuthenticator @Inject constructor(
	private val userCredsStore: UserCredsStore,
	private val uiStateDispatcher: UiStateDispatcher,
	private val authService: AuthService
) : Authenticator {
	private val refreshMutex = Mutex()
	private val maxRetryCount = 3

	override fun authenticate(route: HttpRoute?, response: Response): Request? {
		Log.d("HttpAuthenticator", "authenticate[response]: $response")

		val oldCreds = runBlocking { userCredsStore.getCreds().firstOrNull() }
		if (oldCreds?.refreshToken == null) {
			navigateToAuthForm()
			return null
		}

		val retry: Boolean = shouldRetry(response)

		if (!retry) return null

		val newCreds = runBlocking { refreshToken(oldCreds) }
		return newCreds?.accessToken?.let { token ->
			response.request.withAppAuthorization(token)
		}
	}

	private suspend fun refreshToken(oldCreds: UserPreferences): UserPreferences? {
		return refreshMutex.withLock {
			val requestBody = RefreshTokenRequestBody(oldCreds.refreshToken)
			val response = authService.refreshToken(requestBody)

			if (!response.isSuccessful) {
				navigateToAuthForm()
				return null
			}

			val creds: UserPreferences? = response.body()

			userCredsStore.updateCreds(creds)
			uiStateDispatcher.sendUiEvent(UiEvent.UpdateUserInstance)
			return creds
		}
	}

	private fun shouldRetry(response: Response): Boolean {
		val count = response.priorResponseCount()
		val isAttemptCountLowerThanMaxValue: Boolean = count < maxRetryCount

		val isUnauthorized: Boolean = response.code == UNAUTHORIZED_USER_ERROR_CODE
		val flag: Boolean = isAttemptCountLowerThanMaxValue && isUnauthorized

		return flag
	}

	private fun navigateToAuthForm() {
		CoroutineScope(Dispatchers.Main).launch {
			uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication))
			userCredsStore.clear()
		}
	}

	private fun Response.priorResponseCount(): Int {
		var count = 0
		var priorResponse: Response? = priorResponse

		while (priorResponse != null) {
			count++
			priorResponse = priorResponse.priorResponse
		}

		return count
	}
}
