package com.senyor_o.pomodoro.presentation.change_duration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senyor_o.pomodoro.domain.model.SettingsItem
import com.senyor_o.pomodoro.domain.use_case.GetSetting
import com.senyor_o.pomodoro.domain.use_case.InsertSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeDurationViewModel@Inject constructor(
    getSetting: GetSetting,
    private val insertSetting: InsertSetting,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableState<ChangeDurationState> = mutableStateOf(ChangeDurationState())
    val state: State<ChangeDurationState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("id")?.let {
            viewModelScope.launch {
                getSetting(it).also {
                    _state.value = _state.value.copy(
                        settingName = it.itemName,
                        settingDuration = it.duration
                    )
                }
            }
        }
    }

    fun onEvent(event: ChangeDurationEvent) {
        when(event) {
            ChangeDurationEvent.AddTime -> {
                if(_state.value.settingDuration <= 36000) {
                    _state.value = _state.value.copy(
                        settingDuration = _state.value.settingDuration + 3000
                    )
                }
            }
            ChangeDurationEvent.ReduceTime -> {
                if(_state.value.settingDuration >= 3000) {
                    _state.value = _state.value.copy(
                        settingDuration = _state.value.settingDuration - 3000
                    )
                }

            }
            ChangeDurationEvent.SaveTime -> viewModelScope.launch {
                insertSetting(
                    SettingsItem(
                        _state.value.settingName,
                        _state.value.settingDuration
                    )
                )
                _eventFlow.emit(UiEvent.SaveSetting)
            }
        }
    }

    sealed class UiEvent {
        object SaveSetting: UiEvent()
    }
}