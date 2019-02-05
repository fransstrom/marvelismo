package com.mrpwr.marvelismo.data

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mrpwr.marvelismo.API.Comic
import com.mrpwr.marvelismo.R

class ComicListAdapter(private val list: ArrayList<Comic>, private val context: Context) :
    RecyclerView.Adapter<ComicListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row_comic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(comic: Comic) {
            var title: TextView = itemView.findViewById(R.id.comicTitle) as TextView
            var id: TextView = itemView.findViewById(R.id.comicId) as TextView
//            var pic : ImageView = itemView.findViewById(R.id.ComicView) as ImageView

            title.text = comic.title
            id.text = comic.id.toString()
        }
    }
}