package com.example.mediclick

import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideosView : AppCompatActivity() {
    private var TAG = "VideoPlayer"
    private lateinit var videoView: VideoView
    private var mediaController: MediaController? = null
    private lateinit var videos: Videos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video) // Use your layout resource ID here

        videos = intent.getSerializableExtra("videos") as Videos

        val textVideoType = findViewById<TextView>(R.id.textVideoType)
        val textVideoTitle = findViewById<TextView>(R.id.textVideoTitle)

        textVideoType.text = "${videos.title}"
        textVideoTitle.text = "${videos.description}"

        configureVideoView()
    }

    private fun configureVideoView() {
        videoView = findViewById(R.id.videoView) // Use your VideoView resource ID here
        videoView.setVideoPath(videos.url)
        mediaController = MediaController(this)

        mediaController?.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            Log.i(TAG, "Duration = " + videoView.duration)
        }
        videoView.start()
    }

    override fun onPause() {
        super.onPause()
        videoView.stopPlayback()
    }
}
