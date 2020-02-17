package com.example.bookappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.droidman.ktoasty.KToasty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            progressBarRegister.visibility = View.VISIBLE
            performRegister()
        }
    }

    private fun performRegister(){
        val username = etUsername.text.toString()
        val email = etEmailAccount.text.toString()
        val password = etPassword.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()){
            KToasty.warning(this,"註冊資訊不完整!", Toast.LENGTH_SHORT, true).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("Main", "Successful: ${it.result?.user?.uid}")
                    //  將user註冊資訊傳至Database
                    saveUserToFirebaseDatabase()
                    KToasty.success(this,"完成註冊!"+password, Toast.LENGTH_SHORT, true).show()
                    // 回至登入頁面
                    progressBarRegister.visibility = View.GONE
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener {
                KToasty.error(this,"格式錯誤!", Toast.LENGTH_SHORT, true).show()
            }
    }

    private fun saveUserToFirebaseDatabase(){
        val userID = FirebaseAuth.getInstance().uid?:""
        val database = FirebaseFirestore.getInstance()
        val user_store = hashMapOf(
            "userID" to userID,
            "username" to etUsername.text.toString(),
            "userEmail" to etEmailAccount.text.toString(),
            "userPhone" to etPhoneNumber.text.toString()
        )

        database.document("users/${userID}")
            .set(user_store)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "成功將會員資料傳至資料庫")
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "會員資料傳至資料庫失敗")
            }
    }
}

class User(val userID: String, val username: String, val userEmail: String, val userPhone: String)