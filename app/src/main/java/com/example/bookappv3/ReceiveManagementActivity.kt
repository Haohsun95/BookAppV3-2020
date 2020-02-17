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
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.activity_receive_management.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

class ReceiveManagementActivity : AppCompatActivity() {

    val buyUserID = FirebaseAuth.getInstance().uid?:""
    private lateinit var cAdapter: FirestorePagingAdapter<Cart, CartViewHolder>
    private val cFirestore = FirebaseFirestore.getInstance()
    private val mCartsCollection = cFirestore.collection("cart")
    private val cQuery = mCartsCollection.whereEqualTo("buyUserID",buyUserID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_management)

        cartReceiveRecyclerView.setHasFixedSize(true)
        cartReceiveRecyclerView.layoutManager = LinearLayoutManager(this)
        setupAdapter()

        swipeCartRefreshLayout.setOnRefreshListener {
            cAdapter.refresh()
        }

        cAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        cAdapter.stopListening()
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

        cAdapter = object: FirestorePagingAdapter<Cart, CartViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
                val view = layoutInflater.inflate(R.layout.cart_item, parent, false)
                return CartViewHolder(view)

            }

            override fun onBindViewHolder(holder: CartViewHolder, position: Int, model: Cart) {
                holder.bind(model)
            }

            override fun onError(e: Exception) {
                Log.e("RManagementActivity", e.message)
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                //super.onLoadingStateChanged(state)
                when(state){
                    LoadingState.LOADING_INITIAL ->{
                        swipeCartRefreshLayout.isRefreshing = true
                    }
                    LoadingState.LOADING_MORE ->{
                        swipeCartRefreshLayout.isRefreshing = true
                    }
                    LoadingState.LOADED ->{
                        swipeCartRefreshLayout.isRefreshing = false
                    }
                    LoadingState.ERROR ->{
                        swipeCartRefreshLayout.isRefreshing = false
                    }
                    LoadingState.FINISHED ->{
                        swipeCartRefreshLayout.isRefreshing = false
                    }
                }

                cartReceiveRecyclerView.adapter = cAdapter
            }
        }
    }
}
