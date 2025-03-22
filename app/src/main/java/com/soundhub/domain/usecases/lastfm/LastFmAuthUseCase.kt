package com.soundhub.domain.usecases.lastfm

import com.soundhub.data.api.responses.internal.ErrorResponse
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.local_database.dao.LastFmDao
import com.soundhub.domain.model.LastFmUser
import com.soundhub.domain.repository.LastFmRepository
import com.soundhub.utils.mappers.LastFmMapper
import javax.inject.Inject

class LastFmAuthUseCase @Inject constructor(
	private val lastFmRepository: LastFmRepository,
	private val lastFmDao: LastFmDao
) {
	suspend fun logout() {
		val user = lastFmDao.getLastFmSessionUser()

		user?.let {
			lastFmDao.deleteLastFmSessionUser(user)
		}
	}

	suspend fun login(userName: String, password: String): HttpResult<LastFmUser> {
		try {
			val response = lastFmRepository.getMobileSession(userName, password).getOrNull()

			val user = LastFmMapper.impl.lastFmSessionBodyToLastFmUser(response?.session)
				?: throw Exception("User mapping failed")

			val fullData = lastFmRepository.getUserInfo(user.name)
				.getOrNull()
				?.user

			fullData?.apply { sessionKey = user.sessionKey }
			fullData?.let { lastFmDao.saveLastFmSessionUser(it) }
			fullData ?: throw Exception("User data is null")

			return HttpResult.Success(body = fullData)
		} catch (exception: Exception) {
			return HttpResult.Error(
				errorBody = ErrorResponse(detail = exception.localizedMessage),
				throwable = exception
			)
		}
	}

	suspend fun isAuthorized(): Boolean {
		val user = lastFmDao.getLastFmSessionUser()
		return user?.sessionKey != null
	}
}