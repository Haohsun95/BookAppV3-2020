package com.example.bookappv3

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.droidman.ktoasty.KToasty
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val userID = FirebaseAuth.getInstance().uid?:""
    private var imgRecyclerCartPhoto: ImageView = itemView.findViewById(R.id.imgRecyclerCartPhoto)
    private var tvCartName: TextView = itemView.findViewById(R.id.tvCartName)
    private var tvCartPrice: TextView = itemView.findViewById(R.id.tvCartPrice)
    private var tvCartOrderTime: TextView = itemView.findViewById(R.id.tvCartOrderTime)
    private var tvCartSendTime: TextView = itemView.findViewById(R.id.tvCartSendTime)
    private var tvCartReceiveTime: TextView = itemView.findViewById(R.id.tvCartReceiveTime)

    fun bind(cart: Cart){

        val x = 90
        val y = x.toFloat()
        val picasso = Picasso.get()
        picasso.load(cart.bookURI).fit().rotate(y).into(imgRecyclerCartPhoto)
        tvCartName.text = cart.bookName
        tvCartPrice.text = cart.bookPrice
        tvCartOrderTime.text = cart.orderTime
        tvCartSendTime.text = cart.sendTime
        tvCartReceiveTime.text = cart.receiveTime

        itemView.setOnClickListener {
            if (userID.equals(cart.bookUserID)) {
                val intentToSendAct = Intent(itemView.context, SendDetailActivity::class.java)
                intentToSendAct.putExtra("bookName", cart.bookName)
                intentToSendAct.putExtra("bookPrice", cart.bookPrice)
                intentToSendAct.putExtra("bookUID", cart.bookUID)
                intentToSendAct.putExtra("bookURI", cart.bookURI)
                intentToSendAct.putExtra("bookUserID", cart.bookUserID)
                intentToSendAct.putExtra("buyUserID", cart.buyUserID)
                intentToSendAct.putExtra("cartAddress", cart.cartAddress)
                intentToSendAct.putExtra("cartBuyer", cart.cartBuyer)
                intentToSendAct.putExtra("cartPayment", cart.cartPayment)
                intentToSendAct.putExtra("cartPhone", cart.cartPhone)
                intentToSendAct.putExtra("cartUID", cart.cartUID)
                intentToSendAct.putExtra("orderTime", cart.orderTime)
                intentToSendAct.putExtra("receiveDone", cart.receiveDone)
                intentToSendAct.putExtra("receiveTime", cart.receiveTime)
                intentToSendAct.putExtra("sendTime", cart.sendTime)
                itemView.context.startActivity(intentToSendAct)
            }
            else {
                val intentToReceiveAct = Intent(itemView.context, ReceiveDetailActivity::class.java)
                intentToReceiveAct.putExtra("bookName", cart.bookName)
                intentToReceiveAct.putExtra("bookPrice", cart.bookPrice)
                intentToReceiveAct.putExtra("bookUID", cart.bookUID)
                intentToReceiveAct.putExtra("bookURI", cart.bookURI)
                intentToReceiveAct.putExtra("bookUserID", cart.bookUserID)
                intentToReceiveAct.putExtra("buyUserID", cart.buyUserID)
                intentToReceiveAct.putExtra("cartAddress", cart.cartAddress)
                intentToReceiveAct.putExtra("cartBuyer", cart.cartBuyer)
                intentToReceiveAct.putExtra("cartPayment", cart.cartPayment)
                intentToReceiveAct.putExtra("cartPhone", cart.cartPhone)
                intentToReceiveAct.putExtra("cartUID", cart.cartUID)
                intentToReceiveAct.putExtra("orderTime", cart.orderTime)
                intentToReceiveAct.putExtra("receiveDone", cart.receiveDone)
                intentToReceiveAct.putExtra("receiveTime", cart.receiveTime)
                intentToReceiveAct.putExtra("sendTime", cart.sendTime)
                itemView.context.startActivity(intentToReceiveAct)
            }
        }

    }
}