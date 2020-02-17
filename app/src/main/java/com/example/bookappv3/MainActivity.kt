package com.example.bookappv3

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    enum class FragmentType{
        Home, Favorite, Member
    }

    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        title = "二手書拍拍Go"

        initView()
    }

    private fun initView() {
        //set navigation Listener
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        //顯示Home主頁
        changeFragmentTo(FragmentType.Home)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{

        when(it.itemId){
            R.id.navigationHome ->{
                changeFragmentTo(FragmentType.Home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationFavorite ->{
                changeFragmentTo(FragmentType.Favorite)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationMember ->{
                changeFragmentTo(FragmentType.Member)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private  fun changeFragmentTo(type: FragmentType){
        val transaction = manager.beginTransaction()
        when(type) {
            FragmentType.Home -> {
                val homeFragment = HomeFragment()
                transaction.replace(R.id.baseFragment, homeFragment)
            }

            FragmentType.Favorite -> {
                val favoriteFragment = FavoriteFragment()
                transaction.replace(R.id.baseFragment, favoriteFragment)
            }

            FragmentType.Member -> {
                val memberFragment = MemberFragment()
                transaction.replace(R.id.baseFragment, memberFragment)
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        val id = item.itemId
        if (id == R.id.action_add_auction) {
            val intent = Intent(this, AuctionActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)

/*        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }*/
    }
}
