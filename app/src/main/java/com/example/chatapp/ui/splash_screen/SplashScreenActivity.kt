package com.example.chatapp.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.ui.authentication.LoginActivity
import com.example.chatapp.ui.users.UsersActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    private var runnable = Runnable {
        setLoginActivity()
    }
    private var handler = Handler()


    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)

    }


    private fun setLoginActivity() {
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        } else {
            val intent = Intent(this, UsersActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}