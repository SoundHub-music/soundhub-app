package com.soundhub.presentation.pages.music_profile.ui.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.domain.model.LastFmUser
import com.soundhub.domain.states.IProfileUiState
import com.soundhub.presentation.pages.music.viewmodels.LastFmServiceViewModel
import com.soundhub.presentation.shared.containers.ContentContainer

@Composable
internal fun LastFmProfileContainer(serviceViewModel: LastFmServiceViewModel) {
	val profileUiState: State<IProfileUiState<LastFmUser>> = serviceViewModel
		.profileUiState
		.collectAsState()

	val profileOwner: LastFmUser? = profileUiState.value.profileOwner

	ContentContainer(
		modifier = Modifier.fillMaxSize(),
	) {
		Column {
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.Bottom,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				profileOwner?.let {
					Text(
						text = it.name,
						style = TextStyle(
							fontSize = 24.sp,
							fontWeight = FontWeight.Bold
						),
						modifier = Modifier
							.padding(bottom = 16.dp, start = 16.dp)
					)
				}

				Row {
					Button(
						onClick = serviceViewModel::logout,
						modifier = Modifier
							.padding(top = 16.dp, end = 16.dp)
//							.width(100.dp)
					) {
						Icon(Icons.AutoMirrored.Rounded.ExitToApp, contentDescription = "logout")
					}

					Button(
						onClick = {},
						modifier = Modifier
							.padding(top = 16.dp, end = 16.dp)
							.padding(top = 16.dp, end = 16.dp),
						colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
					) {
						Icon(
							painter = painterResource(R.drawable.last_fm),
							contentDescription = "Last fm"
						)
					}
				}
			}

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)
			) {
				profileOwner?.url?.let { url ->
					ProfileLink(url, "Перейти на профиль Last.fm")
				}

				ProfileCard("Статистика", profileOwner)
				ProfileCard("Личная информация", profileOwner)

				profileOwner?.let {
					ProfileAdditionalDetails(it)
				}
			}
		}
	}
}

@Composable
private fun ProfileCard(headerText: String, profileOwner: LastFmUser?) {
	ProfileSectionHeader(headerText)
	ProfilePersonalInfo(profileOwner)
}

@Composable
private fun ProfileSectionHeader(text: String) {
	Text(
		text = text,
		style = TextStyle(
			fontSize = 18.sp,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.primary
		),
		modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
	)
}

@Composable
private fun ProfileStats(user: LastFmUser?) {
	user?.let {
		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(4.dp)
		) {
			Column(
				modifier = Modifier.padding(16.dp)
			) {
				Text(
					"Всего прослушиваний: ${it.playCount ?: "0"}",
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					"Количество плейлистов: ${it.playlistCount ?: "0"}",
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					"Исполнителей: ${it.artistCount ?: "0"}",
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					"Альбомов: ${it.albumCount ?: "0"}",
					style = MaterialTheme.typography.bodyMedium
				)
				Text("Треков: ${it.trackCount ?: "0"}", style = MaterialTheme.typography.bodyMedium)
			}
		}
	}
}

@Composable
private fun ProfilePersonalInfo(user: LastFmUser?) {
	user?.let {
		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(4.dp)
		) {
			Column(
				modifier = Modifier.padding(16.dp)
			) {
				Text(
					"Страна: ${it.country ?: "Не указана"}",
				)
				Text(
					"Пол: ${it.gender ?: "Не указано"}",
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					"Дата регистрации: ${it.registered?.text ?: "Не указана"}",
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	}
}

@Composable
private fun ProfileAdditionalDetails(user: LastFmUser) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Column(
			modifier = Modifier.padding(16.dp)
		) {
			Text(
				"Тип аккаунта: ${user.type ?: "Не указано"}",
			)
			Text(
				"Подписчик: ${if (user.subscriber == "1") "Да" else "Нет"}",
			)
		}
	}
}

@Composable
private fun ProfileLink(url: String, description: String) {
	Text(
		text = buildAnnotatedString {
			with(LinkAnnotation.Url(url)) { append(description) }
		},
		style = TextStyle(
			color = MaterialTheme.colorScheme.primary,
			textDecoration = TextDecoration.Underline
		),
		modifier = Modifier
			.padding(16.dp)
	)
}