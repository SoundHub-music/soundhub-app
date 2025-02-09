package com.soundhub.presentation.pages.music.ui.containers.music_service

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.presentation.pages.music.ui.buttons.MusicServiceButton
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceBottomSheetViewModel
import com.soundhub.presentation.pages.music.widgets.sheet.MusicServiceSheetContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicServiceList(modifier: Modifier = Modifier) {
	val viewModel: MusicServiceListViewModel = hiltViewModel()
	val services = viewModel.getServicePairs()

	val showModalSheet: Boolean by viewModel.showModalSheet.collectAsState()

	val sheetState = rememberBottomSheetScaffoldState(
		SheetState(
			skipPartiallyExpanded = true,
			initialValue = SheetValue.Hidden,
			density = LocalDensity.current
		)
	)

	val clickedServicePair: MusicServiceBottomSheetViewModel? by viewModel
		.clickedServicePair
		.collectAsState()

	ElevatedCard(
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.tertiaryContainer
		)
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(10.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.padding(15.dp)
		) {
			Text(
				text = stringResource(id = R.string.music_auth_service_text),
				fontSize = 24.sp,
				fontWeight = FontWeight.Black,
				lineHeight = 28.sp,
			)

			LazyRow(
				horizontalArrangement = Arrangement.spacedBy(
					space = 20.dp,
					alignment = Alignment.CenterHorizontally
				),
				modifier = Modifier.fillMaxWidth()
			) {
				items(items = services, key = { it.first }) { service ->
					MusicServiceButton(
						painter = painterResource(id = service.first),
						musicServiceBottomSheetViewModel = service.second,
						onClick = {
							viewModel.setShowModalSheet(true)
							viewModel.setClickedServicePair(service)
						}
					)
				}
			}
		}
	}

	if (showModalSheet) {
		ModalBottomSheet(
			onDismissRequest = viewModel::onDismissSheet,
			sheetState = sheetState.bottomSheetState,
			containerColor = MaterialTheme.colorScheme.outlineVariant
		) {
			clickedServicePair?.let {
				val logo = it.getServiceLogoResource()

				MusicServiceSheetContainer(
					musicServiceBottomSheetViewModel = it,
					formIcon = logo?.let { painterResource(logo) },
				)
			}
		}
	}
}

@Composable
@Preview
private fun UnauthorizedLibraryItemsPreview() {
	MusicServiceList()
}
