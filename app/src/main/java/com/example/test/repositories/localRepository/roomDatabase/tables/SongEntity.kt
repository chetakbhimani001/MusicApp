package com.example.musicplayer.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "songs")
data class SongEntity(

    @ColumnInfo
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val title: String,

    @ColumnInfo
    val link: String,

    @ColumnInfo
    val artist: String,

    @ColumnInfo
    val cover: String? = null,

    @ColumnInfo
    val duration: Int,

    @ColumnInfo(name = "rights")
    var rights: String? = null,

    @ColumnInfo(name = "update_at")
    var updateAt: Date? = null,
) : Serializable
