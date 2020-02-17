package com.example.bookappv3


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.droidman.ktoasty.KToasty
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var mAdapter: FirestorePagingAdapter<Post, PostViewHolder>
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mPostsCollection = mFirestore.collection("auction")
    private val mQuery = mPostsCollection.whereEqualTo("bookIsSold","N")
    //.orderBy("bookName", Query.Direction.DESCENDING)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        // ******** 2019.09.07 測試Login後是否能連結資料庫Auth ********
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = user.uid
            //KToasty.info(this.requireContext(), "連結資料庫：$uid", Toast.LENGTH_SHORT, true).show()
        }

        allRecyclerView.setHasFixedSize(true)
        allRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        setupAdapter()

        swipeRefreshLayout.setOnRefreshListener {
            mAdapter.refresh()
        }

        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

    private fun setupAdapter(){
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<Post>()
            .setLifecycleOwner(this)
            .setQuery(mQuery, config, Post::class.java)
            .build()

        mAdapter = object: FirestorePagingAdapter<Post, PostViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
                val view = layoutInflater.inflate(R.layout.book_item, parent, false)
                return PostViewHolder(view)
            }

            override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
                holder.bind(model)
            }

            override fun onError(e: Exception) {
                super.onError(e)
                Log.e("HomeFragment",e.message)
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                //super.onLoadingStateChanged(state)
                when(state){
                    LoadingState.LOADING_INITIAL ->{
                        swipeRefreshLayout.isRefreshing = true
                    }
                    LoadingState.LOADING_MORE ->{
                        swipeRefreshLayout.isRefreshing = true
                    }
                    LoadingState.LOADED ->{
                        swipeRefreshLayout.isRefreshing = false
                    }
                    LoadingState.ERROR ->{
                        swipeRefreshLayout.isRefreshing = false
                    }
                    LoadingState.FINISHED ->{
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }

        }
        allRecyclerView.adapter = mAdapter
    }

}
