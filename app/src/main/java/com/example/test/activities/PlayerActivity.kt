package com.example.test.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.musicplayer.data.room.entity.SongEntity
import com.example.test.R
import com.example.test.utils.Constants.Companion.EXTRA_SONG
import com.example.test.utils.Constants.Companion.EXTRA_SONG_LIST
import com.example.test.viewmodels.PlayerViewModel
import com.example.test.withViewModel
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {
    private val playerViewModel by lazy {
        withViewModel({ PlayerViewModel() })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        if(!intent.hasExtra(EXTRA_SONG))
        {
            finish()
            return
        }
        val songList = intent.extras?.getSerializable(EXTRA_SONG_LIST) as List<SongEntity>?
        val songEntity = intent.extras?.getSerializable(EXTRA_SONG) as SongEntity?
        if (songEntity == null || songList.isNullOrEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.data_missing), Toast.LENGTH_LONG)
                .show()
            finish()
            return
        }

        playerViewModel.playSong(songList, songEntity)
        setUI(songEntity)
        setClickListeners()
        setPlayerButton()

    }

    private fun setClickListeners() {
        btn_play_pause.setOnClickListener {
            playerViewModel.playOrPause()
            if (playerViewModel.playbackStatus.value == true) {
                btn_play_pause.setImageResource(R.drawable.ic_play)
            } else {
                btn_play_pause.setImageResource(R.drawable.ic_pause)

            }
        }
    }

    private fun setUI(songEntity: SongEntity) {
        imageCover?.let {
            Glide
                .with(this)
                .load(songEntity.cover)
                .centerCrop()
                .placeholder(R.drawable.ic_music)
                .into(it)
        }

        txtTitle?.text = songEntity.name
        txtSubTitle?.text = songEntity.artist
    }

    private fun setPlayerButton() {
        if (playerViewModel.playbackStatus.value == true) {
            btn_play_pause.setImageResource(R.drawable.ic_play)
        } else {
            btn_play_pause.setImageResource(R.drawable.ic_pause)

        }
    }

    override fun onStop() {
        super.onStop()
        playerViewModel.releaseMediaPlayer()
    }
}