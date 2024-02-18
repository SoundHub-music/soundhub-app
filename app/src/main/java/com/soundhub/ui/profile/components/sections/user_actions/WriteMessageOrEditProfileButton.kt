package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import java.util.UUID

@Composable
internal fun WriteMessageOrEditProfileButton(
    modifier: Modifier = Modifier,
    isOriginProfile: Boolean,
    navController: NavHostController
) {
    val actionProfileButtonContent = if (isOriginProfile)
        stringResource(id = R.string.edit_profile_btn_content)
    else stringResource(id = R.string.write_message_btn_content)

    val profileActionButtonIcon: ImageVector = if (isOriginProfile)
        Icons.Rounded.Person
    else Icons.Rounded.MailOutline

    Button(
        colors = ButtonDefaults.buttonColors(),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        onClick = {
            if (isOriginProfile)
                navController.navigate(Route.EditUserData.route)
            else navController.navigate(
                // TODO: remove random UUID
                Route.Messenger.Chat(UUID.randomUUID().toString()).route
            )
        }
    ) {
        Row(
            modifier = Modifier.height(30.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = profileActionButtonIcon,
                contentDescription = "profile_action_button_icon"
            )
            Text(
                text = actionProfileButtonContent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        }
    }
}