package com.example.test.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.data.room.AppDatabase
import com.example.musicplayer.data.room.entity.SongEntity
import com.example.test.R
import com.example.test.adapters.SongAdapter
import com.example.test.models.FeedEntry
import com.example.test.utils.Constants.Companion.EXTRA_SONG
import com.example.test.utils.Constants.Companion.EXTRA_SONG_LIST
import com.example.test.utils.Utility.Companion.hideProgressBar
import com.example.test.utils.Utility.Companion.isInternetAvailable
import com.example.test.utils.Utility.Companion.showProgressBar
import com.example.test.viewmodels.SongListViewModel
import com.example.test.withViewModel
import kotlinx.android.synthetic.main.activity_song_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SongListActivity : AppCompatActivity() {

    private lateinit var listSongs: MutableList<SongEntity>
    private lateinit var adapter: SongAdapter
    private val songViewModel by lazy {
        withViewModel({ SongListViewModel() })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        recycler_main.layoutManager = LinearLayoutManager(this@SongListActivity)
        listSongs = mutableListOf<SongEntity>()
        adapter = SongAdapter(this,
            listSongs
        )
        recycler_main.adapter = adapter
        val db = AppDatabase.getDatabase(this)
        val songList = db.songDao().getAllSongs()
        if(songList.isNullOrEmpty())
        {
            if(isInternetAvailable()) {
                showProgressBar()
                songViewModel.getData().observe(this,object:Observer<List<FeedEntry>>{
                    override fun onChanged(t: List<FeedEntry>?) {
                        listSongs.clear()
                        val data = addDataIntoSongTable(t as ArrayList<FeedEntry>)
                        GlobalScope.launch (Dispatchers.Main) { data?.let { AppDatabase.getDatabase(this@SongListActivity).songDao().insertSongList(it) } }

                        t?.let { listSongs.addAll(data) }
                        adapter.notifyDataSetChanged()
                        hideProgressBar()
                    }

                })
            }
            else
            {
                Toast.makeText(this,getString(R.string.internet_not_available),Toast.LENGTH_SHORT).show()
            }

        }
        else
        {
            songList.let { listSongs.addAll(it) }
            adapter.notifyDataSetChanged()
        }

        adapter.setItemClickListener { song ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(EXTRA_SONG_LIST, ArrayList(adapter.list))
            intent.putExtra(EXTRA_SONG, song)
            startActivity(intent)
        }
    }
    private fun addDataIntoSongTable(feedList: ArrayList<FeedEntry>): List<SongEntity> {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
        return feedList.map {
            val image = it.images?.maxByOrNull { it.height ?: 0 }
            val link = it.links?.firstOrNull { it.type == "audio/x-m4a" }

            SongEntity(
                id = it.id ?: 0L,
                name = it.name ?: "",
                title = it.title ?: "",
                link = link?.url ?: "",
                duration = link?.duration ?: 0,
                artist = it.artist?.value ?: "",
                cover = image?.value,
                rights = it.rights ?: "",
                updateAt = sdf.parse(it.updateAt)
            )
        }
    }
}
