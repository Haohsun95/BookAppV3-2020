package com.example.bookappv3

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.droidman.ktoasty.KToasty
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            performLogin()
        }

        tvRegisterPerform.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        //確認使用者是否為登入狀態，若已登入不需再登入。
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun performLogin(){
        val loginEmail = etLoginEmail.text.toString()
        val loginPassword = etLoginPassword.text.toString()

        if (loginEmail.isEmpty() || loginPassword.isEmpty()){
            KToasty.warning(this,"帳號或密碼輸入不完全", Toast.LENGTH_SHORT, true).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    KToasty.success(this,"Connection Success", Toast.LENGTH_SHORT, true).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    KToasty.error(this,"Connection Failed", Toast.LENGTH_SHORT, true).show()
                }
            }
            .addOnFailureListener {
                KToasty.warning(this,"Format Error", Toast.LENGTH_SHORT, true).show()
            }
    }
}
