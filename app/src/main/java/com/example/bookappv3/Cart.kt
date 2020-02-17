package com.example.bookappv3

data class Cart(
    var bookName: String? = null,
    var bookPrice: String? = null,
    var bookUID: String? = null,
    var bookURI: String? = null,
    var bookUserID: String? = null,
    var buyUserID: String? = null,
    var cartAddress: String? = null,
    var cartBuyer: String? = null,
    var cartPayment: String? = null,
    var cartPhone: String? = null,
    var cartUID: String? = null,
    var orderTime: String? = null,
    var receiveDone: String? = null,
    var receiveTime: String? = null,
    var sendTime: String? = null
)