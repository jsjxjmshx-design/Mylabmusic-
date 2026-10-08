package com.example.mylabmusic

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay = findViewById<Button>(R.id.btnPlay)

        btnPlay.setOnClickListener {
            val sampleAudioUri = "android.resource://$packageName/raw/sample_song"

            val intent = Intent(this, MusicService::class.java).apply {
                action = "ACTION_PLAY"
                putExtra("SONG_URI", sampleAudioUri)
                putExtra("SONG_TITLE", "Mylabmusic - Pista Inicial")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }
}
