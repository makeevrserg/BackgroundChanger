package com.makeevrserg.backgroundchanger.ui.video

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.makeevrserg.backgroundchanger.R

class VideoViewModel(application: Application) : AndroidViewModel(application) {


    private val _player = MutableLiveData<SimpleExoPlayer>()
    val player: LiveData<SimpleExoPlayer>
        get() = _player


    /**
     * Получаем внедренное в APK видео
     */
    private fun buildRawMediaSource(): MediaSource {
        val rawDataSource = RawResourceDataSource(getApplication())
        rawDataSource.open(DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.video)))
        val mediaItem = MediaItem.fromUri(rawDataSource.uri!!)
        return ProgressiveMediaSource.Factory { rawDataSource }
            .createMediaSource(mediaItem)
    }

    /**
     * Создание плеера
     */
    private fun createPlayer() {
        _player.value = SimpleExoPlayer.Builder(getApplication()).build()
        _player.value?.setMediaSource(buildRawMediaSource())
        _player.value?.prepare()
        _player.value?.playWhenReady = true

    }

    init {
        createPlayer()
    }
    fun onResume(){
        _player.value?.play()
    }

    fun onPause() {
        _player.value?.pause()
    }

}