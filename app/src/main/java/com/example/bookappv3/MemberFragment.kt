package com.example.bookappv3


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.droidman.ktoasty.KToasty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_member.*

/**
 * A simple [Fragment] subclass.
 */
class MemberFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member, container, false)
    }

    override fun onStart() {
        super.onStart()
        //編輯會員資料
        val userID = FirebaseAuth.getInstance().uid?:""
        val database = FirebaseFirestore.getInstance()
        val docRef = database.document("users/${userID}")
        docRef.get().addOnSuccessListener {
            val member = it.toObject(Member::class.java)
            val profileName = member?.username
            val profileEmail = member?.userEmail
            val profilePhone = member?.userPhone

            tvProfileName?.text = profileName
            tvProfileEmail?.text = profileEmail
            tvProfilePhone?.text = profilePhone

        }

        btnReceiveManagement.setOnClickListener {
            val intentReceiveM = Intent(this.requireContext(), ReceiveManagementActivity::class.java)
            startActivity(intentReceiveM)
        }

        btnSendManagement.setOnClickListener {
            val intentSendM = Intent(this.requireContext(), SendManagementActivity::class.java)
            startActivity(intentSendM)
        }


    }

}
