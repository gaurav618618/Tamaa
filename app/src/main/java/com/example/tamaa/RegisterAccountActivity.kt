package com.example.tamaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterAccountActivity : AppCompatActivity() {

    var fbAuth = FirebaseAuth.getInstance()
    val currentUser = fbAuth.currentUser
    //updateUI(currentUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_account)

        var input_name = findViewById<EditText>(R.id.input_name)
        var input_email = findViewById<EditText>(R.id.input_email)
        var input_password = findViewById<EditText>(R.id.input_password)
        var btn_signup = findViewById<Button>(R.id.btn_signup)
        var link_login = findViewById<TextView>(R.id.link_login)

        btn_signup.setOnClickListener{ view ->
            registerAccount(view, input_email.text.toString(), input_password.text.toString())
        }
    }

    fun registerAccount(view: View,email: String, password: String){
        showMessage(view,"Authenticating...")
        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                val myIntent = Intent(this@RegisterAccountActivity, MainActivity::class.java)

                //myIntent.putExtra("key", value) //Optional parameters
                this@RegisterAccountActivity.startActivity(myIntent)
            }else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        })
    }

    fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }

}