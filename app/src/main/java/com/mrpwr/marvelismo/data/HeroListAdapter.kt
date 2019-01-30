package com.mrpwr.marvelismo.data

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mrpwr.marvelismo.API.Hero
import com.mrpwr.marvelismo.R
import java.net.URI

class HeroListAdapter(private val list: ArrayList<Hero>, private val context: Context) :
    RecyclerView.Adapter<HeroListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(hero: Hero) {
            var name: TextView = itemView.findViewById(R.id.heroName) as TextView
            var id: TextView = itemView.findViewById(R.id.heroId) as TextView
            var pic : ImageView= itemView.findViewById(R.id.picView) as ImageView
            name.text=hero.name
            id.text= hero.id.toString()
        }
    }


}