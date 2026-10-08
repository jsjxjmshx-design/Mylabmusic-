package com.example.mylabmusic

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val CHANNEL_ID = "MylabmusicServiceChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "ACTION_PLAY") {
            val songUriStr = intent.getStringExtra("SONG_URI")
            val songTitle = intent.getStringExtra("SONG_TITLE") ?: "Reproduciendo..."

            songUriStr?.let {
                playAudio(Uri.parse(it), songTitle)
            }
        }
        return START_NOT_STICKY
    }

    private fun playAudio(uri: Uri, title: String) {
        mediaPlayer?.release()
        try {
            mediaPlayer = MediaPlayer.create(applicationContext, uri).apply {
                setOnPreparedListener { start() }
                setOnCompletionListener { stopSelf() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Mylabmusic")
            .setContentText(title)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Canal de Mylabmusic",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
