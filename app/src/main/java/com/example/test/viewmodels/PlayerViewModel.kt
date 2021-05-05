package com.example.test.viewmodels

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.room.entity.SongEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {

    private val mPlaybackStatus = MutableLiveData<Boolean>()
    val playbackStatus: LiveData<Boolean> get() = mPlaybackStatus
    private val mCurrentSong = MutableLiveData<SongEntity>()

    private var mMediaPlayer: MediaPlayer? = null
    private var mTimerJob: Job? = null
    private var mTimerStatus: Boolean = false

    private var mPlaybackIndex = 0
    private var mPlaybackList = mutableListOf<SongEntity>()
    private var mIsMediaPlayerPrepared = false

    fun playSong(songList: List<SongEntity>?, songEntity: SongEntity?) {
        if (songList != null && songEntity != null) {
            mPlaybackIndex = songList.indexOf(songEntity)
            mPlaybackList.addAll(songList)
            playOrPause()
        }
    }


    fun playOrPause() {
        if (mPlaybackList.isEmpty()) return

        val player = mMediaPlayer ?: preparePlayer(mPlaybackIndex)

        if (player.isPlaying) {
            playOrPause(false)
        } else if (mIsMediaPlayerPrepared) {
            playOrPause(true)
        }
    }

    private fun playOrPause(play: Boolean) {
        if (play) {
            mPlaybackStatus.postValue(true)
            mMediaPlayer?.start()
            startTimer()
        } else {
            mPlaybackStatus.postValue(false)
            mMediaPlayer?.pause()
            stopTimer()
        }
    }

    private fun preparePlayer(index: Int): MediaPlayer {
        if (mMediaPlayer != null) {
            releaseMediaPlayer()
        }

        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setOnPreparedListener {
            mIsMediaPlayerPrepared = true
            playOrPause(true)
        }

        val entity = mPlaybackList[index]
        mCurrentSong.postValue(entity)

        mIsMediaPlayerPrepared = false
        val player = mMediaPlayer!!
        player.setDataSource(entity.link)
        player.prepareAsync()
        mMediaPlayer = player
        return player
    }

    private fun startTimer() {
        mTimerStatus = true
        mTimerJob = GlobalScope.launch {
            while (mTimerStatus) {
                delay(1000)
            }
        }
    }

     fun stopTimer() {
        mTimerStatus = false
        mTimerJob?.cancel()
    }

    fun releaseMediaPlayer() {
        stopTimer()
        mMediaPlayer?.stop()
        mMediaPlayer?.reset()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }
}
