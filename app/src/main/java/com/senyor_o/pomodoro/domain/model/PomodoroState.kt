package com.senyor_o.pomodoro.domain.model

import com.senyor_o.pomodoro.utils.Utility

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

fun PomodoroState.getNextState() : PomodoroState =
    when(this) {
        PomodoroState.WORK_1 -> PomodoroState.BREAK_1
        PomodoroState.BREAK_1 -> PomodoroState.WORK_2
        PomodoroState.WORK_2 -> PomodoroState.BREAK_2
        PomodoroState.BREAK_2 -> PomodoroState.WORK_3
        PomodoroState.WORK_3 -> PomodoroState.BREAK_3
        PomodoroState.BREAK_3 -> PomodoroState.WORK_4
        PomodoroState.WORK_4 -> PomodoroState.LONG_BREAK
        PomodoroState.LONG_BREAK -> PomodoroState.END
        else -> {
            PomodoroState.END
        }
    }

fun PomodoroState.hasEnded() : Boolean =
    this == PomodoroState.END
