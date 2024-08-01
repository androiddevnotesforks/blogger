/*
 * Copyright 2024 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger.presentation.alertDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.peterchege.blogger.R
import com.peterchege.blogger.core.util.ThemeConfig


@Composable
fun ThemeDialog(
    changeTheme: (String) -> Unit,
    toggleThemeDialog: () -> Unit,
    currentTheme: String,
) {
    val configuration = LocalConfiguration.current
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 80.dp)
            .heightIn(max = configuration.screenHeightDp.dp - 80.dp),
        onDismissRequest = { toggleThemeDialog() },
        title = {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Column(modifier = Modifier.selectableGroup()) {
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.system_default),
                        selected = currentTheme == ThemeConfig.FOLLOW_SYSTEM,
                        onClick = { changeTheme(ThemeConfig.FOLLOW_SYSTEM) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.light),
                        selected = currentTheme == ThemeConfig.LIGHT,
                        onClick = { changeTheme(ThemeConfig.LIGHT) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.dark),
                        selected = currentTheme == ThemeConfig.DARK,
                        onClick = { changeTheme(ThemeConfig.DARK) },
                    )
                }
                HorizontalDivider(Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Text(
                text = stringResource(id = R.string.ok).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable(onClick = toggleThemeDialog),
            )
        },
    )
}


@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Preview
@Composable
fun ThemeDialogPreview() {
    ThemeDialog(
        changeTheme = {},
        toggleThemeDialog = { /*TODO*/ },
        currentTheme = ThemeConfig.DARK
    )

}