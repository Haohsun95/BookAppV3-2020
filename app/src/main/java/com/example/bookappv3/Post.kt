package com.example.bookappv3

data class Post(
    var bookUID: String? = null,
    var bookName: String? = null,
    var bookOPrice: String? = null,
    var bookPrice: String? = null,
    var bookCategory: String? = null,
    var bookStatus: String? = null,
    var bookExplain: String? = null,
    var bookImageUrl: String? = null,
    var userID: String? =null,
    var bookTimeStamp: String? = null,
    var bookIsSold: String? = null
    )