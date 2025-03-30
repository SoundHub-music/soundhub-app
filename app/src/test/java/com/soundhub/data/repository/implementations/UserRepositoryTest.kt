package com.soundhub.data.repository.implementations

import android.content.Context
import com.google.gson.Gson
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.services.UserService
import com.soundhub.data.repository.UserRepositoryImpl
import com.soundhub.domain.enums.Gender
import com.soundhub.domain.model.User
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class UserRepositoryTest {
	private lateinit var userService: UserService
	private lateinit var loadAllUserDataUseCase: LoadAllUserDataUseCase
	private lateinit var context: Context
	private lateinit var gson: Gson
	private lateinit var userRepository: UserRepositoryImpl

	private lateinit var userId: UUID
	private lateinit var user: User

	@Before
	fun setup() {
		userService = mockk()
		loadAllUserDataUseCase = mockk(relaxed = true)
		context = mockk()
		gson = Gson()
		userRepository = UserRepositoryImpl(userService, loadAllUserDataUseCase, context, gson)

		userId = UUID.randomUUID()
		user = User(
			id = userId,
			gender = Gender.MALE,
			avatarUrl = "http://example.com/avatar.png",
			email = "test@example.com",
			firstName = "Test",
			lastName = "User",
			country = "USA",
			online = true,
			lastOnline = LocalDateTime.now(),
			birthday = LocalDate.now(),
			city = "New York",
			description = "Test user description",
			languages = mutableListOf("English", "Spanish"),
			friends = emptyList(),
			favoriteGenres = emptyList(),
			favoriteArtists = emptyList()
		)
	}

	@Test
	fun `getUserById returns success when user is found`() = runTest {
		val response = Response.success(user)

		coEvery { userService.getUserById(userId) } returns response

		val result = userRepository.getUserById(userId)

		assert(result is HttpResult.Success)
		val successResult = result as HttpResult.Success
		assert(successResult.body == user)
		coVerify { loadAllUserDataUseCase(user) }
	}

	@Test
	fun `getUserById returns error when exception is thrown`() = runBlocking {
		coEvery { userService.getUserById(userId) } throws Exception("Network Error")

		val result = userRepository.getUserById(userId)

		assert(result is HttpResult.Error)
		val errorResult = result as HttpResult.Error
		assert(errorResult.errorBody.detail == "Network Error")
	}

	@Test
	fun `updateUserById updates user successfully`() = runBlocking {
		val response = Response.success(user)

		coEvery { userService.updateUserById(eq(userId), any(), any()) } returns response

		val result = userRepository.updateUser(user)

		assert(result is HttpResult.Success)

		val successResult = result as HttpResult.Success
		assert(successResult.body == user)
	}

	@Test
	fun `addFriend adds a friend successfully`() = runBlocking {
		val friendId = UUID.randomUUID()
		val friendUser = User(
			id = friendId,
			firstName = "Friend",
			lastName = "User"
		)
		val response = Response.success(friendUser)

		coEvery { userService.addFriend(friendId) } returns response

		val result = userRepository.addFriend(friendId)

		assert(result is HttpResult.Success)
		val successResult = result as HttpResult.Success
		assert(successResult.body == friendUser)
	}

	@Test
	fun `deleteFriend deletes a friend successfully`() = runBlocking {
		val friendId = UUID.randomUUID()
		val response = Response.success(User(id = friendId))

		coEvery { userService.deleteFriend(friendId) } returns response

		val result = userRepository.deleteFriend(friendId)
		assert(result is HttpResult.Success)
	}
}
