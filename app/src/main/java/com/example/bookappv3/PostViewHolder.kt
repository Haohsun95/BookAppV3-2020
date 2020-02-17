package com.example.bookappv3

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.droidman.ktoasty.KToasty
import com.squareup.picasso.Picasso

class PostViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

    private var imgRecyclerPhoto: ImageView = itemView.findViewById(R.id.imgRecyclerPhoto)
    private var tvBookName: TextView = itemView.findViewById(R.id.tvBookName)
    private var tvBookPrice: TextView = itemView.findViewById(R.id.tvBookPrice)

    fun bind(post: Post){
        val x = 90
        val y = x.toFloat()
        val picasso = Picasso.get()
        picasso.load(post.bookImageUrl).fit().rotate(y).into(imgRecyclerPhoto)
        tvBookName.text = post.bookName
        tvBookPrice.text = post.bookPrice

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, PostDetailActivity::class.java)
            intent.putExtra("bookUID", post.bookUID)
            intent.putExtra("bookName", post.bookName)
            intent.putExtra("bookOPrice", post.bookOPrice)
            intent.putExtra("bookPrice", post.bookPrice)
            intent.putExtra("bookCategory", post.bookCategory)
            intent.putExtra("bookStatus", post.bookStatus)
            intent.putExtra("bookExplain", post.bookExplain)
            intent.putExtra("bookURI", post.bookImageUrl)
            intent.putExtra("userID", post.userID)
            intent.putExtra("bookTimeStamp", post.bookTimeStamp)
            intent.putExtra("bookIsSold", post.bookIsSold)
            itemView.context.startActivity(intent)
        }

    }

}