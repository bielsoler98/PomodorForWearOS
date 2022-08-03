package com.example.pomodoro.screen

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pomodoro.*
import com.example.pomodoro.R
import com.example.pomodoro.database.SettingsItem
import com.example.pomodoro.database.SettingsViewModel
import com.example.pomodoro.database.SettingsViewModelFactory
import com.example.pomodoro.navigation.AppScreens
import com.example.pomodoro.theme.WearAppTheme
import com.example.pomodoro.utils.Utility

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel,
    settings: List<SettingsItem>
) {
    LaunchedEffect(key1 = settings) {
        if (settings.isNotEmpty()) {
            viewModel.initMillisUntilFinished()
            navController.popBackStack()
            navController.navigate(AppScreens.MainScreen.route)
        }
    }

    WearAppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "Logo Description"
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
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND,
)
@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND,
    device = Devices.WEAR_OS_LARGE_ROUND
)
@Composable
fun SplashScreenPreview() {
    val context = LocalContext.current
    val mSettingsViewModel : SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context.applicationContext as Application)
    )
    val itemsList = listOf(
        SettingsItem(Utility.WORK, 3000),
        SettingsItem(Utility.SHORT_BREAK, 3000),
        SettingsItem(Utility.LONG_BREAK, 3000),
    )
    val navController = NavHostController(context)
    SplashScreen(navController, mSettingsViewModel, itemsList)
}