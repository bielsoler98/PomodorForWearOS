package com.senyor_o.pomodoro.presentation.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senyor_o.pomodoro.domain.use_case.GetSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettings: GetSettings
) : ViewModel() {

    private val _state: MutableState<SettingsState> = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    init {
        getSettings().onEach { settings ->
            _state.value = state.value.copy(
                settings = settings,
            )
        }.launchIn(viewModelScope)
    }
}