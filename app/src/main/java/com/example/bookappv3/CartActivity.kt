package com.example.bookappv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.droidman.ktoasty.KToasty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_post_detail.*
import java.text.SimpleDateFormat
import java.util.*

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "購物車"

        val cartBookUID = intent.getStringExtra("bookUID")
        val cartBookName = intent.getStringExtra("bookName")
        val cartBookPrice = intent.getStringExtra("bookPrice")
        val cartBookURI = intent.getStringExtra("bookURI")
        val cartBookUserID = intent.getStringExtra("bookUserID")

        tvCartBookName.text = cartBookName
        tvCartBookPrice.text = cartBookPrice

        val x = 90
        val y = x.toFloat()
        val picasso = Picasso.get()
        picasso.load(cartBookURI).fit().rotate(y).into(imgCartPhoto)

        btnCart.setOnClickListener {
            val cartUID = UUID.randomUUID().toString()
            val bookUID =cartBookUID
            val bookUserID = cartBookUserID
            val buyUserID = FirebaseAuth.getInstance().uid?:""
            val cartBuyer = etCartBuyer.text.toString()
            val cartPhone = etCartPhone.text.toString()
            val cartAddress = etCartAddress.text.toString()
            val cartPayment = spCartPayment.selectedItem.toString()
            val orderTime = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN).format(Date())
            val sendTime = "尚未登錄出貨"
            val receiveTime = "尚未登錄取貨"
            val receiveDone = "N"

            if (cartBuyer.isEmpty() || cartPhone.isEmpty() || cartAddress.isEmpty()){
                KToasty.warning(this,"購物車資訊不完整", Toast.LENGTH_SHORT, true).show()
            }
            else{
                val database = FirebaseFirestore.getInstance()
                val cart_store = hashMapOf(
                    "cartUID" to cartUID,
                    "bookUID" to bookUID,
                    "bookName" to cartBookName,
                    "bookPrice" to cartBookPrice,
                    "bookURI" to cartBookURI,
                    "bookUserID" to bookUserID,
                    "buyUserID" to buyUserID,
                    "cartBuyer" to cartBuyer,
                    "cartPhone" to cartPhone,
                    "cartAddress" to cartAddress,
                    "cartPayment" to cartPayment,
                    "orderTime" to orderTime,
                    "sendTime" to sendTime,
                    "receiveTime" to receiveTime,
                    "receiveDone" to receiveDone
                )

                database.document("cart/${cartUID}")
                    .set(cart_store)
                    .addOnSuccessListener {
                        KToasty.success(this,"購物成功", Toast.LENGTH_SHORT, true).show()
                        updateAuctionStatus()
                        finish()
                    }
                    .addOnFailureListener {
                        KToasty.error(this,"Error：購物失敗", Toast.LENGTH_SHORT, true).show()
                    }
            }
        }

        btnCartCancel.setOnClickListener {
            finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun updateAuctionStatus(){
        val bookUID = intent.getStringExtra("bookUID")
        val database = FirebaseFirestore.getInstance()
        val docRef = database.document("auction/${bookUID}")
        docRef.update("bookIsSold", "Y")
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

}
