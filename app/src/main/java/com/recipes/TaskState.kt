package com.recipes

import com.recipes.model.Task
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

data class TaskState(
    val index: Int,
    val active: Boolean = false,
    val done: Boolean = false,
)

@OptIn(ExperimentalTime::class)
data class TimerState constructor(
    val startingDuration: Duration,
    var startedAt: TimeMark? = null,
) {
    data class TimerSnapshot(
        val isRunning: Boolean,
        val remaining: Duration,
        val elapsed: Duration,
        val progress: Float,
    ) {
        val remainingWholeSeconds: Duration
            get() = remaining.inWholeSeconds.seconds

        val isDone: Boolean
            get() = progress == 1.0f
    }

    fun start() {
        startedAt = TimeSource.Monotonic.markNow()
    }

    fun reset() {
        startedAt = null
    }

    val isRunning: Boolean
        get() = startedAt != null

    //TODO val doneAt: TimeMark?
    //    get() = startedAt?.let { it + startingDuration }

    val snapshot: TimerSnapshot
        get() = startedAt?.let {
            it.elapsedNow().let { elapsed ->
                TimerSnapshot(
                    true,
                    startingDuration - elapsed,
                    elapsed,
                    min(elapsed / startingDuration, 1.0).toFloat(),
                )
            }
        } ?: TimerSnapshot(false, startingDuration, 0.seconds, 0f)

    companion object {
        fun fromTask(task: Task) = task.timer?.let { TimerState(startingDuration = it) }
    }
}