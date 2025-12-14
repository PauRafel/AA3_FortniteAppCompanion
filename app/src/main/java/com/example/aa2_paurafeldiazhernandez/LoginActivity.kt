package com.example.aa2_paurafeldiazhernandez

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var txtError: TextView
    private lateinit var txtRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("491085797124-ovgcgkh3tg7lm5dnjt960q6g16t79p5a.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        findViewById<SignInButton>(R.id.btn_login_google).setOnClickListener{signIn()}

        emailField = findViewById(R.id.input_email)
        passwordField = findViewById(R.id.input_password)
        txtError = findViewById(R.id.txt_error)
        txtRegister = findViewById(R.id.txt_register)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.btn_register).setOnClickListener{Register()}
        findViewById<Button>(R.id.btn_login).setOnClickListener{Login()}
    }

    private fun showError(msg: String) {
        txtError.text = msg
        txtError.visibility = View.VISIBLE
    }

    private fun clearError() {
        txtError.visibility = View.GONE
    }

    private fun showRegister(msg: String) {
        txtRegister.text = msg
        txtRegister.visibility = View.VISIBLE
    }

    private fun clearRegister() {
        txtRegister.visibility = View.GONE
    }


    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 9001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 9001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(task.isSuccessful){
                val account = task.getResult(ApiException::class.java)
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

    private fun Login(){
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        clearError()
        clearRegister()

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) {task->
            if(task.isSuccessful){
                clearError()
                startActivity(Intent(this, HomeActivity::class.java))
            }else{
                showError("The email or password are incorrect")
            }

        }
    }

    private fun Register(){
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        clearError()

        if (password.length < 6) {
            showError("The password must have more than 6 characters")
            return
        }

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) {task ->
            if (task.isSuccessful) {
                clearError()
                showRegister("Register successful")
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                showError("It already exist a user with this email")
                clearRegister()
            }
        }

    }

}