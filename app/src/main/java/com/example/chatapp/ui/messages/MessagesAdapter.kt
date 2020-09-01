package com.example.chatapp.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.ui.authentication.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.message_it.view.*
import kotlinx.android.synthetic.main.message_me.view.*

class MessagesAdapter(
    private val items: MutableList<User>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val Sender = 1
        const val Receiver = 2
    }
    lateinit var auth: FirebaseAuth


    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind() {
            val model = items[adapterPosition]
            Glide.with(itemView.context).load(model.url)
                .into(itemView.avatarMessage)
            itemView.messageTV.text = model.message

        }
    }

    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            val model = items[adapterPosition]
            Glide.with(itemView.context).load(model.url)
                .into(itemView.avatarMessage2)
            itemView.messageTV2.text = model.message

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Sender) {
            SenderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.message_me,
                    parent,
                    false
                )
            )
        } else {
            ReceiverViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.message_it,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SenderViewHolder)
            holder.onBind()
        else if (holder is ReceiverViewHolder)
            holder.onBind()

    }

    override fun getItemViewType(position: Int): Int {
        val model = items[position]
        auth = FirebaseAuth.getInstance()
        return if (model.uid==auth.currentUser!!.uid) {
            Sender
        } else {
            Receiver
        }

    }
}