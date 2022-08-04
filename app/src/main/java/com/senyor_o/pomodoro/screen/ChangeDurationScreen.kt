package com.senyor_o.pomodoro.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.*
import com.senyor_o.pomodoro.*
import com.senyor_o.pomodoro.database.SettingsItem
import com.senyor_o.pomodoro.database.SettingsViewModel
import com.senyor_o.pomodoro.database.SettingsViewModelFactory
import com.senyor_o.pomodoro.theme.WearAppTheme
import com.senyor_o.pomodoro.utils.Utility
import com.senyor_o.pomodoro.utils.Utility.formatTime

@Composable
fun ChangeDurationScreen(
    navController: NavHostController,
    id: String,
    duration: Long,
    viewModel: SettingsViewModel
) {
    WearAppTheme {
        var durationTime by remember {
            mutableStateOf(duration)
        }
        val context = LocalContext.current
        Scaffold(
            vignette = {
                // Only show a Vignette for scrollable screens. This code lab only has one screen,
                // which is scrollable, so we show it all the time.
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(text = id)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if(durationTime >= 300000) {
                                durationTime -= 300000
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.DarkGray
                        )
                    ) {
                        Icon(
                            imageVector =  Icons.Rounded.Remove,
                            contentDescription = "Minus",
                            modifier = Modifier
                                .size(24.dp)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                    Text(
                        text = durationTime.formatTime(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = {
                            if(durationTime <= 3600000) {
                                durationTime += 300000
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.DarkGray
                        )
                    ) {
                        Icon(
                            imageVector =  Icons.Rounded.Add,
                            contentDescription = "Add",
                            modifier = Modifier
                                .size(24.dp)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
                SaveButton {
                    viewModel.updateSetting(SettingsItem(id, durationTime))
                    Toast.makeText(
                        context,
                        "Saved",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigateUp()
                }
            }
        }
    }
}
@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onChipClicked: () -> Unit
) {
    Chip(
        modifier = modifier,
        onClick = {
            onChipClicked()
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color.DarkGray
        ),
        label = {
            Text(
                text = "Save",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Save,
                contentDescription = "Save new value",
                modifier = iconModifier
            )
        },
    )
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND,
)
@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND,
    device = Devices.WEAR_OS_LARGE_ROUND,
    name = "Round"
)
@Composable
fun ChangeDurationScreenPreview() {
    val context = LocalContext.current
    val mSettingsViewModel : SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context.applicationContext as Application)
    )
    val navController = NavHostController(context)
    ChangeDurationScreen(navController, Utility.WORK, 3000, mSettingsViewModel)
}