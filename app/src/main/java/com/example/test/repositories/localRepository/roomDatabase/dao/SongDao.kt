package com.example.musicplayer.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.musicplayer.data.room.entity.SongEntity

@Dao
interface SongDao {

    @Query("select * from songs order by name")
    fun getAllSongs(): List<SongEntity>

    @Query("select * from songs where id=:id")
    suspend fun getSongsById(id: String): SongEntity?

    @Query("select count(*) from songs")
    fun getTotalCount(): Int

    @Insert(onConflict = REPLACE)
    suspend fun insertSong(entity: SongEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertSongList(list: List<SongEntity>)

    @Update()
    suspend fun updateSong(list: SongEntity)
}
