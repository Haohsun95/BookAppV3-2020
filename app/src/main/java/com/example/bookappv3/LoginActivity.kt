package com.example.bookappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.droidman.ktoasty.KToasty
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

    private fun performLogin(){
        val loginEmail = etLoginEmail.text.toString()
        val loginPassword = etLoginPassword.text.toString()

        if (loginEmail.isEmpty() || loginPassword.isEmpty()){
            KToasty.warning(this,"帳號密碼不完整!", Toast.LENGTH_SHORT, true).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    KToasty.success(this,"登入成功!", Toast.LENGTH_SHORT, true).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    KToasty.error(this,"帳號密碼錯誤!", Toast.LENGTH_SHORT, true).show()
                }
            }
            .addOnFailureListener {
                KToasty.error(this,"帳號密碼格式錯誤!", Toast.LENGTH_SHORT, true).show()
            }
    }
}
