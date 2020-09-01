package com.example.chatapp.ui.users

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.ui.authentication.RegisterActivity
import com.example.chatapp.ui.authentication.models.User
import com.example.chatapp.ui.messages.MessagesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {
    companion object{
        var currentUser:User? = null
    }
    private val items = mutableListOf<User>()
    private lateinit var adapter: UsersAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        init()


    }



    private fun init() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                adapter = UsersAdapter(items, object : OnclickListener{
                    override fun clickListener(position: Int) {
                        val user = items[position]
                        val intent = Intent(this@UsersActivity, MessagesActivity::class.java)
                        intent.putExtra("url", user.url)
                        intent.putExtra("userName", user.userName)
                        intent.putExtra("toUser", user.uid)
                        startActivity(intent)
                    }

                })
                usersRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                usersRecyclerView.adapter = adapter
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    items.add(user!!)
                }
                usersRecyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                adapter.notifyDataSetChanged()
                if (items.size<1){
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@UsersActivity, RegisterActivity::class.java)
                    startActivity(intent)


                }
            }
        })
    }
}