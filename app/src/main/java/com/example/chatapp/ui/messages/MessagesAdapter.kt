package com.example.chatapp.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import kotlinx.android.synthetic.main.message_me.view.*

class MessagesAdapter(private val items: MutableList<MessageModel>) :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.message_me,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.onBind()
    }

    override fun getItemCount() = items.size

    private lateinit var model: MessageModel

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            model = items[adapterPosition]
            Glide.with(itemView.context).load(model.url)
                .into(itemView.avatarMessage)
            itemView.messageTV.text = model.message

        }
    }
}
