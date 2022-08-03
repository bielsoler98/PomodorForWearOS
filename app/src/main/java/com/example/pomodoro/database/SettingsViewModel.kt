package com.example.pomodoro.database

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import com.example.pomodoro.MainActivity
import com.example.pomodoro.R
import com.example.pomodoro.utils.CountDownTimer
import com.example.pomodoro.utils.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<SettingsItem>>

    private val repository: SettingsRepository

    //region Properties
    private var countDownTimer: CountDownTimer? = null

    //endregion

    //region States
    private val _pomodoroState = MutableLiveData(PomodoroState.WORK_1)
    val pomodoroState: LiveData<PomodoroState> = _pomodoroState

    private val _millisUntilFinished = MutableLiveData( 0L)
    val millisUntilFinished: LiveData<Long> = _millisUntilFinished

    private val _progress = MutableLiveData(1.00f)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    init {
        val settingsDao = SettingsDatabase.getInstance(application).settingsDao()
        repository = SettingsRepository(settingsDao)
        readAllData = repository.readAllData
    }

    //region Public methods
    fun handleCountDownTimer() {
        if (isPlaying.value == true) {
            pauseTimer()
        } else {
            if (countDownTimer?.isPaused() == true) {
                countDownTimer?.resume()
            } else {
                startTimer()
            }
        }
    }

    fun setPomodoroState(pomodoroState: PomodoroState) {
        _pomodoroState.value = pomodoroState
    }

    //region Private methods
    fun cancelTimer() {
        countDownTimer?.cancel()
        handleTimerValues(false, getTotalTime(), 1.0F)
    }

    //region Private methods
    fun pauseTimer() {
        val time = countDownTimer?.pause() ?: 0L
        val progressValue = time.toFloat() / getTotalTime()
        handleTimerValues(false, time, progressValue)
    }

    private fun startWorkTimer() {

        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(getDurationTime(Utility.WORK), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val progressValue: Float = millisUntilFinished.toFloat()/ getTotalTime()
                handleTimerValues(true, millisUntilFinished, progressValue)
            }

            override fun onFinish() {
                displayNotification()
                vibrate()
                cancelTimer()
                _pomodoroState.value = getNextState()
                startTimer()
            }
        }.start()
    }

    private fun startShortBreakTimer() {

        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(getDurationTime(Utility.SHORT_BREAK), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val progressValue = millisUntilFinished.toFloat() / getTotalTime()
                handleTimerValues(true, millisUntilFinished, progressValue)
            }

            override fun onFinish() {
                displayNotification()
                vibrate()
                cancelTimer()
                _pomodoroState.value = getNextState()
                startTimer()
            }
        }.start()
    }

    private fun startLongBreakTimer() {

        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(getDurationTime(Utility.LONG_BREAK), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val progressValue = millisUntilFinished.toFloat() / getTotalTime()
                handleTimerValues(true, millisUntilFinished, progressValue)
            }

            override fun onFinish() {
                displayNotification()
                vibrate()
                _pomodoroState.value = getNextState()
                cancelTimer()
            }
        }.start()
    }

    private fun handleTimerValues(isPlaying: Boolean, time: Long, progress: Float) {
        _isPlaying.value = isPlaying
        _millisUntilFinished.value = time
        _progress.value = progress
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

    private fun getNextState() : PomodoroState =
        when(_pomodoroState.value) {
            PomodoroState.WORK_1 -> PomodoroState.BREAK_1
            PomodoroState.BREAK_1 -> PomodoroState.WORK_2
            PomodoroState.WORK_2 -> PomodoroState.BREAK_2
            PomodoroState.BREAK_2 -> PomodoroState.WORK_3
            PomodoroState.WORK_3 -> PomodoroState.BREAK_3
            PomodoroState.BREAK_3 -> PomodoroState.WORK_4
            PomodoroState.WORK_4 -> PomodoroState.LONG_BREAK
            PomodoroState.LONG_BREAK -> PomodoroState.END
            else -> { PomodoroState.END }
        }.also {
            _millisUntilFinished.value = getTotalTime()
        }

    fun getTotalTime() : Long =
        when(_pomodoroState.value) {
            PomodoroState.WORK_1 -> getDurationTime(Utility.WORK)
            PomodoroState.BREAK_1 -> getDurationTime(Utility.SHORT_BREAK)
            PomodoroState.WORK_2 -> getDurationTime(Utility.WORK)
            PomodoroState.BREAK_2 -> getDurationTime(Utility.SHORT_BREAK)
            PomodoroState.WORK_3 -> getDurationTime(Utility.WORK)
            PomodoroState.BREAK_3 -> getDurationTime(Utility.SHORT_BREAK)
            PomodoroState.WORK_4 -> getDurationTime(Utility.WORK)
            PomodoroState.LONG_BREAK -> getDurationTime(Utility.LONG_BREAK)
            else -> {0L}
        }

    fun startTimer() {
        when (_pomodoroState.value) {
            PomodoroState.WORK_1 -> startWorkTimer()
            PomodoroState.BREAK_1 -> startShortBreakTimer()
            PomodoroState.WORK_2 -> startWorkTimer()
            PomodoroState.BREAK_2 -> startShortBreakTimer()
            PomodoroState.WORK_3 -> startWorkTimer()
            PomodoroState.BREAK_3 -> startShortBreakTimer()
            PomodoroState.WORK_4 -> startWorkTimer()
            PomodoroState.LONG_BREAK -> startLongBreakTimer()
            else -> { }
        }
    }

    fun updateSetting(settingsItem: SettingsItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSetting(settingsItem = settingsItem)
        }

    }

    fun getDurationTime(name: String) : Long =
        readAllData.value?.firstOrNull {
            it.itemName == name
        }?.duration ?: 0L

    fun initMillisUntilFinished() {
        _pomodoroState.value = PomodoroState.WORK_1
        _millisUntilFinished.value = getTotalTime()
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
            .setContentText(_pomodoroState.value?.label + " time has ended!")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setLocalOnly(true)
            .setContentIntent(viewPendingIntent)
    }

    fun displayNotification() {
        val context = getApplication<Application>().applicationContext
        NotificationManagerCompat.from(context).apply {
            notify(45, getNotificationBuilder().build())
        }
    }
}

class SettingsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

enum class PomodoroState(val label: String) {
    WORK_1(Utility.WORK),
    BREAK_1(Utility.SHORT_BREAK),
    WORK_2(Utility.WORK),
    BREAK_2(Utility.SHORT_BREAK),
    WORK_3(Utility.WORK),
    BREAK_3(Utility.SHORT_BREAK),
    WORK_4(Utility.WORK),
    LONG_BREAK(Utility.LONG_BREAK),
    END("Restart")
}