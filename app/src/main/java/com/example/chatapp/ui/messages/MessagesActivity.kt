package com.example.chatapp.ui.messages

import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.ui.authentication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_messages.*

class MessagesActivity : AppCompatActivity() {
    companion object{
        var currentUser:User?=null
    }
    lateinit var auth: FirebaseAuth
    lateinit var senderMessage: User
    private lateinit var receiverMessage:User
    private val items = mutableListOf<User>()
    private lateinit var adapter: MessagesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        init()
        fetchCurrentUser()
    }

    private fun init() {
        val avatar = intent.getStringExtra("url")
        Glide.with(applicationContext).load(avatar).into(avatarPic)
        val userName = intent.getStringExtra("userName")
        userNameTV.text = userName
        adapter = MessagesAdapter(items)
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = adapter
        auth = FirebaseAuth.getInstance()
        val toUser = intent.getStringExtra("toUser")
        listenForMessages()
        sendIcon.setOnClickListener {
            if (messageET.text.isNotEmpty()) {
                senderMessage = User("", auth.currentUser!!.uid,toUser!!, "", messageET.text.toString(), System.currentTimeMillis()/1000 )
                val from = senderMessage.uid
                val to = receiverMessage.uid
                val reference = FirebaseDatabase.getInstance()
                    .getReference("/messages/$from/$to").push()
                val toReference = FirebaseDatabase.getInstance().getReference("/messages/$to/$from").push()
                reference.setValue(senderMessage)
                    .addOnSuccessListener {
                    }
                toReference.setValue(senderMessage)
                messageET.setText("")
            }
        }
    }

    private fun fetchCurrentUser(){
        val uid = auth.currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun listenForMessages() {
        val toUser = intent.getStringExtra("toUser")
        val image = intent.getStringExtra("url")
        d("image", image!!)
        receiverMessage = User("",toUser!!, auth.currentUser!!.uid, "", messageET.text.toString(), System.currentTimeMillis()/1000 )
        senderMessage = User(
            "",
            auth.currentUser!!.uid,
            toUser,
            "",
            messageET.text.toString(),
            System.currentTimeMillis()/1000
        )
        val from = senderMessage.uid
        val to = receiverMessage.uid
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$from/$to")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(User::class.java)
                if (message!!.uid!==auth.currentUser!!.uid){
                    message!!.url = image
                }
                else{
                    message!!.url = currentUser!!.url
                }
                d("messageLog", message.message)
                items.add(message)
                adapter.notifyDataSetChanged()
                messagesRecyclerView.scrollToPosition(items.size-1)


            }


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}






