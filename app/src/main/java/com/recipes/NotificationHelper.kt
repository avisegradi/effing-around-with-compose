package com.recipes

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.recipes.model.Recipe
import com.recipes.model.Task


infix fun <T> Boolean.guards(block: () -> T): T? {
    if (!this)
        return null
    return block()
}

class NotificationHelper(private val context: Context) {
    companion object {
        private const val CHANNEL_TIMERS = "timers"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService() ?: throw IllegalStateException()

    val areNotificationsEnabled: Boolean
        get() = notificationManager.areNotificationsEnabled()

    fun setupNotificationChannels() = areNotificationsEnabled guards {
        if (notificationManager.getNotificationChannel(CHANNEL_TIMERS) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_TIMERS,
                    "Timers",
                    NotificationManager.IMPORTANCE_DEFAULT,
                ).apply {
                    description = "Timers go here"
                }
            )
        }
    }

    @WorkerThread
    fun showNotification(
        recipe: Recipe,
        task: Task,
        timerState: TimerState,
        update: Boolean = false,
    ) = areNotificationsEnabled guards {
        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
        val builder = timerState.snapshot.let { snapshot ->
            NotificationCompat.Builder(context, CHANNEL_TIMERS)
                .setContentTitle("${recipe.title} - ${task.description} -- ${snapshot.remaining}")
                .setSmallIcon(R.drawable.kitty)
                .setCategory(Notification.CATEGORY_ALARM)
                .setProgress(timerState.startingDuration.inWholeSeconds.toInt(),
                             snapshot.elapsed.inWholeSeconds.toInt(),
                             false)
                .setShowWhen(true)
                .setContentIntent(resultPendingIntent)
                .setChronometerCountDown(true)
                .setUsesChronometer(true)
                // TODO .setWhen(timerState.doneAt?.)
        }

        if (update) {
            builder.setOnlyAlertOnce(true)
        }
        // TODO: task.id might not be unique enough later
        notificationManager.notify(task.id, 1, builder.build())
    }

    fun dismissNotification(task: Task) = areNotificationsEnabled guards {
        notificationManager.cancel(task.id, 1)
    }
}