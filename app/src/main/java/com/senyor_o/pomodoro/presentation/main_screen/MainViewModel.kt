package com.senyor_o.pomodoro.presentation.main_screen

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senyor_o.pomodoro.R
import com.senyor_o.pomodoro.domain.model.PomodoroState
import com.senyor_o.pomodoro.domain.model.getNextState
import com.senyor_o.pomodoro.domain.model.hasEnded
import com.senyor_o.pomodoro.domain.use_case.GetSetting
import com.senyor_o.pomodoro.domain.use_case.GetSettings
import com.senyor_o.pomodoro.presentation.MainActivity
import com.senyor_o.pomodoro.utils.CountDownTimer
import com.senyor_o.pomodoro.utils.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getSettings: GetSettings,
    private val getSetting: GetSetting,
    application: Application
) : AndroidViewModel(application){

    private val _state: MutableState<MainState> = mutableStateOf(MainState())
    val state: State<MainState> = _state

    init {
        getSettings().onEach { settings ->
            _state.value = state.value.copy(
                settings = settings,
                millisUntilFinished = settings[0].duration,
                currentDuration = settings[0].duration
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.StartTimer ->  {
                startTimer(_state.value.millisUntilFinished)
            }
            MainEvent.ResumeTimer ->  {
                _state.value.countDownTimer?.resume()
            }
            MainEvent.StopTimer -> viewModelScope.launch {
                pauseTimer()
            }
            MainEvent.RestartTimer -> viewModelScope.launch {
                cancelTimer()
            }
            is MainEvent.StartNextTimer -> viewModelScope.launch {
                if(_state.value.pomodoroState != PomodoroState.LONG_BREAK) {
                    getSetting(_state.value.pomodoroState.getNextState().label).also {
                        _state.value = state.value.copy(
                            pomodoroState = _state.value.pomodoroState.getNextState(),
                            millisUntilFinished = it.duration,
                            currentDuration = it.duration
                        )
                    }
                    onEvent(MainEvent.StartTimer)
                } else {
                    _state.value = state.value.copy(
                        isPlaying = false,
                        pomodoroState = _state.value.pomodoroState.getNextState(),
                        millisUntilFinished = 0L
                    )
                }
            }
            MainEvent.StartButtonClicked ->  {
                handleCountDownTimer()
            }
        }
    }


    fun handleCountDownTimer() {
        if(_state.value.pomodoroState.hasEnded()) {
            _state.value = state.value.copy(
                pomodoroState = PomodoroState.WORK_1,
            )
        }
        if (_state.value.isPlaying) {
            onEvent(MainEvent.StopTimer)
        } else {
            if (_state.value.countDownTimer?.isPaused() == true) {
                onEvent(MainEvent.ResumeTimer)
            } else {
                onEvent(MainEvent.StartTimer)
            }
        }
    }

    private fun pauseTimer() {
        val time = _state.value.countDownTimer?.pause()
        if(time != null) {
            val progressValue = time.toFloat() / _state.value.currentDuration
            handleTimerValues(false, time, progressValue)
        }
    }

    private suspend fun cancelTimer() {
        _state.value.countDownTimer?.cancel()
        getSetting(Utility.WORK).also {
            _state.value = state.value.copy(
                pomodoroState = PomodoroState.WORK_1,
                currentDuration = it.duration,
                isPlaying = false,
                millisUntilFinished = it.duration,
                progress = 1.0F,
            )
        }
    }

    private fun startTimer(duration: Long) {

        _state.value = state.value.copy(
            isPlaying = true,
            countDownTimer = object : CountDownTimer(duration, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    val progressValue: Float = millisUntilFinished.toFloat()/ duration
                    handleTimerValues(true, millisUntilFinished, progressValue)
                }

                override fun onFinish() {
                    displayNotification()
                    vibrate()
                    _state.value.countDownTimer?.cancel()
                    onEvent(MainEvent.StartNextTimer)
                }
            }.start()
        )
    }

    private fun handleTimerValues(isPlaying: Boolean, time: Long, progress: Float) {
        _state.value = state.value.copy(
            isPlaying = isPlaying,
            millisUntilFinished = time,
            progress = progress,
        )
    }

    fun displayNotification() {
        val context = getApplication<Application>().applicationContext
        NotificationManagerCompat.from(context).apply {
            notify(45, getNotificationBuilder().build())
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val context = getApplication<Application>().applicationContext
        val viewPendingIntent = Intent(context, MainActivity::class.java).let { viewIntent ->
            viewIntent.action = "android.intent.action.MAIN"
            viewIntent.addCategory("android.intent.category.LAUNCHER")
            PendingIntent.getActivity(context, 0, viewIntent, 0)
        }
        return NotificationCompat.Builder(context, Utility.CHANNEL_ID)
            .setContentTitle("Pomodoro for WearOS")
            .setContentText(_state.value.pomodoroState.label + " time has ended!")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(viewPendingIntent)
    }

    private fun vibrate() {
        val context = getApplication<Application>().applicationContext
        val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vib.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}