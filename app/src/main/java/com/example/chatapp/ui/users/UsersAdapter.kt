package com.example.chatapp.ui.users

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.ui.authentication.models.User
import kotlinx.android.synthetic.main.user_layout.view.*

class UsersAdapter(private val items: MutableList<User>, val onclickListener: OnclickListener) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.onBind()
    }

    override fun getItemCount() = items.size

    private lateinit var model: User

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            model = items[adapterPosition]
            Glide.with(itemView.context).load(model.url)
                .into(itemView.avatar)
            d("avatarPic", model.url)
            itemView.userNameTV.text = model.userName
            itemView.setOnClickListener{
                onclickListener.clickListener(adapterPosition)
            }
        }
    }
}

