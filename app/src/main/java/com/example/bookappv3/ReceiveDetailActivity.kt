package com.example.bookappv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.droidman.ktoasty.KToasty
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_receive_detail.*
import java.text.SimpleDateFormat
import java.util.*

class ReceiveDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_detail)

        val bookName = intent.getStringExtra("bookName")
        val bookPrice = intent.getStringExtra("bookPrice")
        val bookUID = intent.getStringExtra("bookUID")
        val bookURI = intent.getStringExtra("bookURI")
        val bookUserID = intent.getStringExtra("bookUserID")
        val buyUserID = intent.getStringExtra("buyUserID")
        val cartAddress = intent.getStringExtra("cartAddress")
        val cartBuyer = intent.getStringExtra("cartBuyer")
        val cartPayment = intent.getStringExtra("cartPayment")
        val cartPhone = intent.getStringExtra("cartPhone")
        val cartUID = intent.getStringExtra("cartUID")
        val orderTime = intent.getStringExtra("orderTime")
        val receiveTime = intent.getStringExtra("receiveTime")
        val sendTime = intent.getStringExtra("sendTime")

        tvRCartBookName.text = bookName
        tvRCartBookPrice.text = bookPrice
        tvRCartAddress.text = cartAddress
        tvRCartBuyer.text = cartBuyer
        tvRCartPayment.text = cartPayment
        tvRCartPhone.text = cartPhone

        tvSCartOrderTime.text = orderTime
        tvSCartSendTime.text = sendTime
        tvSCartReceiveTime.text = receiveTime

        val receiveText = "尚未登錄取貨"
        if (tvSCartReceiveTime.equals(receiveText)){
            btnUpdateReceiveCart.setOnClickListener {
                val updateReceiveTime = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN).format(Date())
                val database = FirebaseFirestore.getInstance()
                val docRef = database.document("cart/${cartUID}")
                val updateCartInfo = hashMapOf<String, Any>(
                    "receiveTime" to updateReceiveTime,
                    "receiveDone" to "Y"
                )
                docRef.update(updateCartInfo).addOnSuccessListener {
                    KToasty.success(this,"狀態更新完成", Toast.LENGTH_SHORT, true).show()
                    finish()
                }.addOnFailureListener {
                    KToasty.error(this,"Error：狀態更新失敗", Toast.LENGTH_SHORT, true).show()
                }
            }
        }else{
            btnUpdateReceiveCart.isEnabled = true
        }
    }
}
