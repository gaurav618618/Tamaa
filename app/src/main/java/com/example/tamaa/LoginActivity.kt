package com.example.tamaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    var fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_auth)


        var user_name = findViewById<EditText>(R.id.input_email)
        var password = findViewById<EditText>(R.id.input_password)
        password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)


        var btnLogin = findViewById<TextView>(R.id.link_signup)
        btnLogin.setOnClickListener {view ->
            if(user_name.text.toString().isEmpty()) {
                user_name.setError("Field incomplete")
            }
            if(password.text.toString().isEmpty()){
                password.setError("Field incomplete")
            }
            else {
                signIn(view, user_name.text.toString(), password.text.toString())
            }
        }

        var btnLogout = findViewById<Button>(R.id.btn_cancel)
        btnLogout.setOnClickListener {view ->
            val myIntent = Intent(this@LoginActivity, MainActivity::class.java)
            //myIntent.putExtra("key", value) //Optional parameters
            this@LoginActivity.startActivity(myIntent)
        }

        var btnRegisterAccount = findViewById<TextView>(R.id.link_signup)
        btnRegisterAccount.setOnClickListener { view ->
            val myIntent = Intent(this@LoginActivity, RegisterAccountActivity::class.java)
            //myIntent.putExtra("key", value) //Optional parameters
            this@LoginActivity.startActivity(myIntent)
        }
    }

    fun signIn(view: View,email: String, password: String){
        showMessage(view,"Authenticating...")

        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                var intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("id", fbAuth.currentUser?.email)
                startActivity(intent)

            }else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        })
    }

    fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }

}