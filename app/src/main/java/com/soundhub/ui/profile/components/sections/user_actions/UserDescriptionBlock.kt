package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.soundhub.R
import com.soundhub.data.model.User
import com.soundhub.utils.DateFormatter

@Composable
internal fun UserDescriptionBlock(modifier: Modifier = Modifier, user: User? = null) {
    // TODO: expand the list of user data
    Column(modifier = modifier) {
        if (user?.description != null && user.description!!.isNotEmpty())
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        append(stringResource(id = R.string.profile_screen_user_description))
                    }
                    append(user.description)
                },
                fontWeight = FontWeight.Medium
            )
        if (user?.birthday != null)
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary)
                    ) {
                        append(stringResource(id = R.string.profile_screen_user_birthday))
                    }
                    append(" " + DateFormatter.getStringDate(date = user.birthday!!, includeYear = true))
                },
                fontWeight = FontWeight.Medium,
            )
    }
}