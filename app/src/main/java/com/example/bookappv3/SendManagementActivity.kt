package com.example.bookappv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_receive_management.*
import kotlinx.android.synthetic.main.activity_send_management.*
import java.lang.Exception

class SendManagementActivity : AppCompatActivity() {

    val bookUserID = FirebaseAuth.getInstance().uid?:""
    private lateinit var cMAdapter: FirestorePagingAdapter<Cart, CartViewHolder>
    private val cMFirestore = FirebaseFirestore.getInstance()
    private val mCartsCollection = cMFirestore.collection("cart")
    private val cQuery = mCartsCollection.whereEqualTo("bookUserID",bookUserID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_management)

        cartSendRecyclerView.setHasFixedSize(true)
        cartSendRecyclerView.layoutManager = LinearLayoutManager(this)
        setupAdapter()

        swipeCartSRefreshLayout.setOnRefreshListener {
            cMAdapter.refresh()
        }

        cMAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        cMAdapter.stopListening()
    }

    private fun setupAdapter(){
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<Cart>()
            .setLifecycleOwner(this)
            .setQuery(cQuery, config, Cart::class.java)
            .build()

        cMAdapter = object: FirestorePagingAdapter<Cart, CartViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
                val view = layoutInflater.inflate(R.layout.cart_item, parent, false)
                return CartViewHolder(view)

            }

            override fun onBindViewHolder(holder: CartViewHolder, position: Int, model: Cart) {
                holder.bind(model)
            }

            override fun onError(e: Exception) {
                Log.e("SManagementActivity", e.message)
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                //super.onLoadingStateChanged(state)
                when(state){
                    LoadingState.LOADING_INITIAL ->{
                        swipeCartSRefreshLayout.isRefreshing = true
                    }
                    LoadingState.LOADING_MORE ->{
                        swipeCartSRefreshLayout.isRefreshing = true
                    }
                    LoadingState.LOADED ->{
                        swipeCartSRefreshLayout.isRefreshing = false
                    }
                    LoadingState.ERROR ->{
                        swipeCartSRefreshLayout.isRefreshing = false
                    }
                    LoadingState.FINISHED ->{
                        swipeCartSRefreshLayout.isRefreshing = false
                    }
                }

                cartSendRecyclerView.adapter = cMAdapter
            }
        }
    }
}
