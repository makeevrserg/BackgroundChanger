package com.makeevrserg.backgroundchanger.ui.main

import android.app.Application
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.MediaItem
import com.makeevrserg.backgroundchanger.utils.Event
import com.google.android.exoplayer2.SimpleExoPlayer


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainViewModel"


    private val _videoSelected = MutableLiveData<Uri?>()
    val videoSelected: LiveData<Uri?>
        get() = _videoSelected


    private val _backgroundSelected = MutableLiveData<Uri?>()
    val backgroundSelected: LiveData<Uri?>
        get() = _backgroundSelected

    /**
     * Происходит ли обработка видео в данный момент
     */
    private val _processing = MutableLiveData<Boolean>()
    val processing: LiveData<Boolean>
        get() = _processing

    /**
     * Сообщение в виде Snackbar'а
     */
    private val _snackBarMessage = MutableLiveData<Event<String>>()
    val snackBarMessage: LiveData<Event<String>>
        get() = _snackBarMessage

    /**
     * Сообщение выведится в виде Toast'а. Чтобы пользователь увидел, если свернул приложение
     */
    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage


    private val _showVideo = MutableLiveData<Event<Boolean>>()
    val showVideo: LiveData<Event<Boolean>>
        get() = _showVideo


    private val timer = object : CountDownTimer(5000, 5000) {

        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {

            _processing.postValue(false)
            _showVideo.postValue(Event(true))
        }
    }


    fun onVideoSelected(uri: Uri?) {
        Log.d(TAG, "onVideoSelected: ${uri}")
        _videoSelected.value = uri ?: return
    }

    fun onBackgroundSelected(uri: Uri?) {
        Log.d(TAG, "onBackgroundSelected: ${uri}")
        _backgroundSelected.value = uri ?: return
    }

    fun setOnStartProcessing() {
        if (_videoSelected.value == null) {
            _snackBarMessage.value = Event("Вы не выбрали видео")
            return
        }
        if (_backgroundSelected.value == null) {
            _snackBarMessage.value = Event("Вы не выбрали фон")
            return
        }
        _processing.value = true
        timer.start()


    }

    fun onPause() {
        if (processing.value != true)
            return
        _toastMessage.value = Event("Обработка отменена")
        timer.cancel()
        _processing.value = false
    }


}