package com.soundhub

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.soundhub.data.enums.ApiStatus
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MusicRepositoryTest {
    private val appContext: Context = InstrumentationRegistry
        .getInstrumentation()
        .targetContext

    private val musicService = MusicService(appContext)
    private val musicRepository = musicService.getMusicRepository()

    @Test
    fun getArtistsByGenres() = runTest {
        val state = MutableStateFlow(ArtistUiState())
        val response = musicRepository.loadArtistByGenresToState(
            genres = listOf("rock"),
            styles = listOf("death metal"),
            artistState = state
        )

        println(response)
        assertEquals(ApiStatus.SUCCESS, response.status)
    }
}