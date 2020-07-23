package com.example.chatapp.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.R
import com.example.chatapp.tools.Tools
import com.example.chatapp.ui.users.UsersActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        registerTV.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            if (emailET.text.isEmpty() || passwordET.text.isEmpty()) {
                Tools.initDialog(this, "Error", "Fill all the fields")
            } else {
                auth.signInWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString()).addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val intent = Intent(this, UsersActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Tools.initDialog(this, "Error", "Error while signing in")
                    }
                })
            }
        }

    }
}