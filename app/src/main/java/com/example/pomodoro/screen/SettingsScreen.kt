package com.example.pomodoro.screen

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.navigation.NavHostController
import androidx.wear.compose.material.*
import com.example.pomodoro.*
import com.example.pomodoro.database.SettingsItem
import com.example.pomodoro.theme.WearAppTheme
import com.example.pomodoro.utils.Utility
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(navController: NavHostController, settings: List<SettingsItem>) {
    WearAppTheme {
        val listState = rememberScalingLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Scaffold(
            vignette = {
                // Only show a Vignette for scrollable screens. This code lab only has one screen,
                // which is scrollable, so we show it all the time.
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(
                    scalingLazyListState = listState
                )
            }
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize()
                    .onRotaryScrollEvent {
                        coroutineScope.launch {
                            listState.scrollBy(it.verticalScrollPixels)
                        }
                        true
                    }
                    .focusRequester(focusRequester)
                    .focusable(),
                autoCentering = AutoCenteringParams(itemIndex = 0),
                state = listState
            ) {
                val contentModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                val iconModifier = Modifier
                    .size(24.dp)
                    .wrapContentSize(align = Alignment.Center)
                item {
                    Text(
                        text = "Durations"
                    )
                }
                items(settings) { settingsItem ->
                    ChipButton(
                        text = settingsItem.itemName,
                        contentModifier,
                        iconModifier
                    ) {
                        navController.navigate(route = "duration_screen/" + settingsItem.itemName)
                    }
                }
            }
        }
    }
}

@Composable
fun ChipButton(
    text: String,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onChipClicked: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = {
            onChipClicked()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.DarkGray
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text =  text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "triggers element Setting",
                modifier = iconModifier
            )
        }
    }
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
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
fun SettingsScreenPreview() {
    val context = LocalContext.current
    val itemsList = listOf(
        SettingsItem(Utility.WORK, 3000),
        SettingsItem(Utility.SHORT_BREAK, 3000),
        SettingsItem(Utility.LONG_BREAK, 3000),
    )
    val navController = NavHostController(context)
    SettingsScreen(navController, itemsList)
}