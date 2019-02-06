package com.mrpwr.marvelismo.views

import com.mrpwr.marvelismo.R
import com.mrpwr.marvelismo.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.text.SimpleDateFormat
import java.util.*


class ChatFromItem(val text: String, val timestamp: Long, val user: User): Item<ViewHolder>() {
  override fun bind(viewHolder: ViewHolder, position: Int) {
    val sdf = SimpleDateFormat("HH:mm")
    val resultdate = Date((timestamp*1000)+3600000)

    viewHolder.itemView.textview_from_row.text = text
    viewHolder.itemView.timeview_from_row.text = sdf.format(resultdate)

    val uri = user.profileImageUrl
    val targetImageView = viewHolder.itemView.imageview_chat_from_row
    Picasso.get().load(uri).into(targetImageView)
  }

  override fun getLayout(): Int {
    return R.layout.chat_from_row
  }
}

class ChatToItem(val text: String, val timestamp: Long, val user: User): Item<ViewHolder>() {
  override fun bind(viewHolder: ViewHolder, position: Int) {
    val sdf = SimpleDateFormat("HH:mm")
    val resultdate = Date((timestamp*1000)+3600000)

    viewHolder.itemView.textview_to_row.text = text
    viewHolder.itemView.timeview_to_row.text = sdf.format(resultdate)

    // load our user image into the star
    val uri = user.profileImageUrl
    val targetImageView = viewHolder.itemView.imageview_chat_to_row
    Picasso.get().load(uri).into(targetImageView)
  }

  override fun getLayout(): Int {
    return R.layout.chat_to_row
  }
}