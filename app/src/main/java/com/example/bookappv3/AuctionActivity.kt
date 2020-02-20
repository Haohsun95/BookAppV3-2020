package com.example.bookappv3

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.MediaCodec
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.droidman.ktoasty.KToasty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_auction.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.HashMap

class AuctionActivity : AppCompatActivity() {

    private val permissionRequestCode = 100
    private val cameraRequestCode = 0
    lateinit var imageFilePath: String
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "即時拍"

        btnSelectBookPhoto.setOnClickListener {
            requestPermission()
        }

        btnAuction.setOnClickListener {
            uploadAuction()
        }

        btnAuctionCancel.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun takePhoto(){
        try {
            val imageFile = createImageFile()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null){
                val authorities = packageName + ".fileprovider"
                imageUri = FileProvider.getUriForFile(this, authorities, imageFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(cameraIntent, cameraRequestCode)
            }
        }catch (e: Exception){
            KToasty.error(this,"無法新增檔案", Toast.LENGTH_SHORT, true).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            cameraRequestCode -> {
                if (resultCode == Activity.RESULT_OK){
                    //imgShowPhoto.setImageBitmap(setScaledBitmap())
                    val x = 90
                    val y = x.toFloat()
                    val picasso = Picasso.get()
                    picasso.load(imageUri).fit().rotate(y).into(imgShowPhoto)
                }
            }
            else -> {
                KToasty.error(this,"無法辨識授權代碼", Toast.LENGTH_SHORT, true).show()
            }
        }
    }

    //新增影像檔案
    @Throws(IOException::class)
    fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN).format(Date())
        val imageFileName = "JPEG_"+timestamp+"_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()){
            storageDir.mkdirs() }
        val imageFile = createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }

    fun setScaledBitmap(): Bitmap{

        val imageViewWidth = imgShowPhoto.width
        val imageViewHeight = imgShowPhoto.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scaleFactor = Math.min(bitmapWidth/imageViewWidth, bitmapHeight/imageViewHeight)
        bmOptions.inSampleSize = scaleFactor //設定縮小比率
        bmOptions.inJustDecodeBounds = false //關閉只載入圖檔資訊的選項

        return BitmapFactory.decodeFile(imageFilePath, bmOptions)
    }

    //流程1：照片上傳至雲端，上傳完成後進入流程2：performAuction，新增資料庫。
    private fun uploadAuction() {
        Log.d("AuctionActivity","***開始執行照片上傳雲端方法***")
        if (imageUri == null) return
        // 將照片傳送至Storage
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("image/${fileName}")
        ref.putFile(imageUri!!)
            .addOnSuccessListener {
                Log.d("AuctionActivity","***圖片上傳雲端成功***")
                ref.downloadUrl.addOnSuccessListener {
                    performAuction(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("AuctionActivity","***圖片上傳雲端失敗***")
            }
    }

    private fun performAuction(bookImageUrl: String) {
        val userID = FirebaseAuth.getInstance().uid?:""
        val bookName = etBookName.text.toString()
        val bookOPrice = etBookOPrice.text.toString()
        val bookPrice = etBookPrice.text.toString()
        val bookCategory = spBookCategory.selectedItem.toString()
        val bookStatus = spBookStatus.selectedItem.toString()
        val bookExplain = etBookExplain.text.toString()
        val bookTimeStamp = SimpleDateFormat("yyyy年MM月dd日", Locale.TAIWAN).format(Date())
        val bookIsSold = "N"

        if (bookName.isEmpty() || bookOPrice.isEmpty() || bookPrice.isEmpty() || bookCategory.isEmpty()
            || bookStatus.isEmpty() || bookExplain.isEmpty()){
            KToasty.warning(this,"拍賣資訊須完整填寫", Toast.LENGTH_SHORT, true).show()
            return
        }

        val auctionName = UUID.randomUUID().toString()
        val database = FirebaseFirestore.getInstance()
        val auction_store = hashMapOf(
            "bookUID" to auctionName,
            "bookName" to bookName,
            "bookOPrice" to bookOPrice,
            "bookPrice" to bookPrice,
            "bookCategory" to bookCategory,
            "bookStatus" to bookStatus,
            "bookExplain" to bookExplain,
            "bookImageUrl" to bookImageUrl,
            "userID" to userID,
            "bookTimeStamp" to bookTimeStamp,
            "bookIsSold" to bookIsSold
        )

        database.document("auction/${auctionName}")
            .set(auction_store)
            .addOnSuccessListener {
                KToasty.success(this,"新增書籍成功", Toast.LENGTH_SHORT, true).show()
                finish()
            }
            .addOnFailureListener {
                KToasty.error(this,"Error：刊登失敗", Toast.LENGTH_SHORT, true).show()
            }
    }

    // Android 6以上需取得裝置授權
    private fun requestPermission(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

        val listPermissionNeeded = ArrayList<String>()

        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(android.Manifest.permission.CAMERA)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (listPermissionNeeded.isNotEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toTypedArray(), permissionRequestCode)
            return false
        }else{
            takePhoto()
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            permissionRequestCode -> {
                val perms = HashMap<String, Int>()
                perms[android.Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[android.Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()){
                    for (i in permissions.indices){
                        perms[permissions[i]] = grantResults[i]
                    }
                    if (perms[android.Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                        && perms[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                        && perms[android.Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED){
                        KToasty.success(this,"已完成所有授權", Toast.LENGTH_SHORT, true).show()
                        takePhoto()
                    }else{
                        KToasty.warning(this,"尚未完成所有授權", Toast.LENGTH_SHORT, true).show()
                    }
                }
            }
        }
    }

}

//class Book (val bookName: String, val bookOPrice: String, val bookPrice: String, val bookCategory: String, val bookStatus: String, val bookExplain: String, val bookImageUrl: String, val userID: String)