package my.com.abibasInven.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import my.com.abibasInven.R
import my.com.abibasInven.data.User
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.ActivityMainBinding
import android.content.DialogInterface
import com.example.logindemo.util.snackbar
import com.google.firebase.auth.FirebaseAuth
import my.com.abibasInven.data.test


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val vm : UserViewModel by viewModels()
    private val nav by lazy {supportFragmentManager.findFragmentById(R.id.host)!!.findNavController()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> nav.navigate(R.id.homeFragment)
                R.id.account -> nav.navigate(R.id.accountFragment)
                R.id.product -> nav.navigate(R.id.action_global_productFragment)

            }
            true
        }
    }

    override fun onBackPressed() {

        val num = nav.currentDestination?.label

        when(num){
            "fragment_product" -> super.finish()
            "fragment_account" -> super.finish()
            "fragment_home"    -> super.finish()
            "fragment_login"   -> super.finish()
        }

        nav.popBackStack()

    }


    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}