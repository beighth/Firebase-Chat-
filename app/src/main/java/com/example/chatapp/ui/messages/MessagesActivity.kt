package com.example.chatapp.ui.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.ui.authentication.models.User
import com.example.chatapp.ui.users.UsersAdapter
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.activity_users.*

class MessagesActivity : AppCompatActivity() {
    private val items = mutableListOf<MessageModel>()
    private lateinit var adapter: MessagesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        init()
        sendIcon.setOnClickListener {
            serData()
        }
    }

    private fun serData() {
        val url = intent.getStringExtra("url")
        if (messageET.text.isNotEmpty()){
            items.add(MessageModel(messageET.text.toString(), url!!))
            adapter.notifyDataSetChanged()
        }
    }

    private fun init(){
        val url = intent.getStringExtra("url")
        val userName = intent.getStringExtra("userName")
        adapter = MessagesAdapter(items)
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = adapter
        Glide.with(this).load(url).into(avatar)
        userNameTV.text = userName
    }



}
