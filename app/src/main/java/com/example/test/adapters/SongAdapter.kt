package com.example.test.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.data.room.entity.SongEntity
import com.example.test.R

class SongAdapter(private val context: Context, var list: MutableList<SongEntity>) : RecyclerView.Adapter<SongAdapter.MyViewHolder>() {
    private var mItemClickListener: ((SongEntity) -> Unit)? = null

    fun setItemClickListener(listener: ((SongEntity) -> Unit)) {
        mItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.song_row,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val song = list.get(position)
        holder.image?.let {
            Glide
                .with(context)
                .load(song.cover)
                .centerCrop()
                .placeholder(R.drawable.ic_music)
                .into(it)
        }

        holder.txtTitle?.text=song.name
        holder.txtSubTitle?.text = song.artist

        holder.itemView.setOnClickListener {
            mItemClickListener?.invoke(song)
        }
    }
    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view){

        var image: ImageView? = null
        var txtTitle: TextView? = null
        var txtSubTitle: TextView? = null
        var txtDuration: TextView? = null

        init {
            image = view.findViewById(R.id.image)
            txtTitle = view.findViewById(R.id.txt_title)
            txtSubTitle = view.findViewById(R.id.txt_sub_title)
        }

    }

}