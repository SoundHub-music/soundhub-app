package com.soundhub.data.repository.implementations

import android.content.Context
import com.google.gson.Gson
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.LogoutResponse
import com.soundhub.data.api.services.AuthService
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.repository.AuthRepositoryImpl
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class AuthRepositoryTest {
	private lateinit var authService: AuthService
	private lateinit var userRepository: UserRepository
	private lateinit var context: Context
	private lateinit var gson: Gson
	private lateinit var authRepository: AuthRepositoryImpl

	@Before
	fun setup() {
		authService = mockk()
		userRepository = mockk()
		context = mockk()
		gson = mockk()
		authRepository = AuthRepositoryImpl(authService, userRepository, context, gson)
	}

	@Test
	fun `signIn should return success result on successful response`() = runBlocking {
		val requestBody = SignInRequestBody("test@example.com", "password")
		val userPreferences = UserPreferences()
		val response = Response.success(userPreferences)

		coEvery { authService.signIn(requestBody) } returns response

		val result = authRepository.signIn(requestBody)

		assertTrue(result is HttpResult.Success && result.body == userPreferences)
	}

	@Test
	fun `signUp should return success result on successful response`() = runBlocking {
		val registerRequestBody = RegisterRequestBody("username", "email", "password", "avatarUrl")
		val userPreferences = UserPreferences()
		val response = Response.success(userPreferences)

		val multipartBody: MultipartBody.Part? = mockk()
		coEvery { authService.signUp(any(), any()) } returns response
		every { gson.toJson(registerRequestBody) } returns "{}"
		every { context.getString(any()) } returns "test"

		val requestBody: RequestBody = "{}".toRequestBody("application/json".toMediaTypeOrNull())

		val result = authRepository.signUp(registerRequestBody)

		assertTrue(result is HttpResult.Success && result.body == userPreferences)
	}

	@Test
	fun `logout should call toggleUserOnline and return success result on successful response`() =
		runBlocking {
			val authorizedUser = User().apply { online = true }
			val toggledUser = User(id = authorizedUser.id).apply { online = false }
			val accessToken = "sampleAccessToken"
			val logoutResponse = LogoutResponse("message")
			val response = Response.success(logoutResponse)

			coEvery { authService.logout(any()) } returns response
			coEvery { userRepository.updateUserOnline(false) } returns HttpResult.Success(body = toggledUser)

			val result = authRepository.logout(authorizedUser, accessToken)

			assertTrue(result is HttpResult.Success && result.body == logoutResponse)
			coVerify { userRepository.updateUserOnline(false) }
		}

	@Test
	fun `refreshToken should return success result on successful response`() = runBlocking {
		val requestBody = RefreshTokenRequestBody("refreshToken")
		val userPreferences = UserPreferences()
		val response = Response.success(userPreferences)

		coEvery { authService.refreshToken(requestBody) } returns response

		val result = authRepository.refreshToken(requestBody)

		assertTrue(result is HttpResult.Success && result.body == userPreferences)
	}

	@Test
	fun `signIn should return error result on failure response`() = runBlocking {
		val requestBody = SignInRequestBody("test@example.com", "password")
		val responseBody = "{\"error\":\"Invalid credentials\"}"
			.toResponseBody("application/json".toMediaTypeOrNull())
		val errorResponse = Response.error<UserPreferences>(400, responseBody)

		coEvery { authService.signIn(requestBody) } returns errorResponse

		val result = authRepository.signIn(requestBody)

		assertTrue(result is HttpResult.Error)
	}

	@Test
	fun `signUp should return error result on failure response`() = runBlocking {
		val registerRequestBody = RegisterRequestBody("username", "email", "password", "avatarUrl")
		val responseBody = "{\"error\":\"Invalid credentials\"}"
			.toResponseBody("application/json".toMediaTypeOrNull())
		val errorResponse = Response.error<UserPreferences>(
			400, responseBody
		)

		coEvery { authService.signUp(any(), any()) } returns errorResponse

		val result = authRepository.signUp(registerRequestBody)

		assertTrue(result is HttpResult.Error)
	}
}
