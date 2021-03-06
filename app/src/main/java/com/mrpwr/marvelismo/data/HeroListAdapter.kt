package com.mrpwr.marvelismo.data

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mrpwr.marvelismo.API.Hero
import com.mrpwr.marvelismo.R
import com.squareup.picasso.Picasso
import com.mrpwr.marvelismo.HeroViewActivity


class HeroListAdapter( val list: ArrayList<Hero>,  val context: Context) :
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
        val url=list[position].thumbnail.path+"."+list[position].thumbnail.extension
        Picasso.get().load(url).resize(250, 250).centerCrop().into(holder.itemView.findViewById(R.id.picView) as ImageView)

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(hero: Hero) {
            val name: TextView = itemView.findViewById(R.id.heroName) as TextView
            val id: TextView = itemView.findViewById(R.id.heroId) as TextView


            val context = itemView.context
            val showHeroIntent = Intent(context, HeroViewActivity::class.java)
            println(hero.id.toString()+ "heroID FROM ADAPTER")
            showHeroIntent.putExtra("HERO_ID", hero.id.toString())

            itemView.setOnClickListener{
                context.startActivity(showHeroIntent)
            }

            name.text=hero.name
            id.text= hero.id.toString()
        }


    }






}