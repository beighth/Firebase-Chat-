package com.example.chatapp.ui.authentication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.tools.Tools
import com.example.chatapp.ui.authentication.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        uri = null
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (validateEmail(emailET.text.toString())) {
                    emailET.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_check_24px,
                        0
                    )
                } else {
                    emailET.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_mood_bad_24px,
                        0
                    )

                }
            }

        })

        avatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }



        repeatPasswordET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (repeatPasswordET.text.toString() == passwordET.text.toString()) {
                    repeatPasswordET.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_check_24px,
                        0
                    )
                } else {
                    repeatPasswordET.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_mood_bad_24px,
                        0
                    )
                }
            }

        })
        registerBtn.setOnClickListener {
            registerUser()
            validateEmail(emailET.text.toString())

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            uri = data!!.data!!
            val img = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            avatar.setImageBitmap(img)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }


    private fun registerUser() {
        if (emailET.text.isEmpty() || passwordET.text.isEmpty()) {
            Tools.initDialog(this, "Error", "Fill all the fields")
        } else {
            linearLayout.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())

                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        linearLayout.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                        uploadImageToTheStorage()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Tools.initDialog(this, "Error", "Error while registering")
                        linearLayout.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                    }
                })
        }
    }

    private fun uploadImageToTheStorage() {
        if (uri == null) {
            saveUserWithoutAvatar()
        } else {
            val fileName = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
            ref.putFile(uri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToDatabase(it.toString())
                    }
                }
        }
    }


    private fun saveUserToDatabase(profileImageUrl: String) {
        val uuid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uuid")
        val user = User(userNameET.text.toString(), uuid, profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                d("saveUser", "Saved to database")
            }
            .addOnFailureListener {
                d("saveUser", "Could not Save to database")
            }
    }

    private fun saveUserWithoutAvatar() {
        val uuid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uuid")
        val user =
            User(
                userNameET.text.toString(),
                uuid
            )
        ref.setValue(user)
    }
}


