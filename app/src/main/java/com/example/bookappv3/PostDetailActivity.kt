package com.example.bookappv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.droidman.ktoasty.KToasty
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val detailBookUID = intent.getStringExtra("bookUID")
        val detailBookName = intent.getStringExtra("bookName")
        val detailBookOPrice = intent.getStringExtra("bookOPrice")
        val detailBookPrice = intent.getStringExtra("bookPrice")
        val detailBookCategory = intent.getStringExtra("bookCategory")
        val detailBookStatus = intent.getStringExtra("bookStatus")
        val detailBookExplain = intent.getStringExtra("bookExplain")
        val detailBookURI = intent.getStringExtra("bookURI")
        val detailUserID = intent.getStringExtra("userID")
        val detailBookTimeStamp = intent.getStringExtra("bookTimeStamp")
        val detailBookIsSold = intent.getStringExtra("bookIsSold")

        tvDetailBookName.text = detailBookName
        tvDetailBookOPrice.text = detailBookOPrice
        tvDetailBookPrice.text = detailBookPrice
        tvDetailBookCategory.text = detailBookCategory
        tvDetailBookStatus.text = detailBookStatus
        tvDetailBookTimeStamp.text = detailBookTimeStamp
        tvDetailBookExplain.text = detailBookExplain

        val x = 90
        val y = x.toFloat()
        val picasso = Picasso.get()
        picasso.load(detailBookURI).fit().rotate(y).into(imgShowDetailPhoto)

        fabAddCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("bookUID", detailBookUID)
            intent.putExtra("bookName", detailBookName)
            intent.putExtra("bookPrice", detailBookPrice)
            intent.putExtra("bookURI", detailBookURI)
            intent.putExtra("bookUserID",detailUserID)
            startActivity(intent)
        }
    }
}
