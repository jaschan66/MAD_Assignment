package my.com.abibasInven.ui

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
                R.id.product -> nav.navigate(R.id.productFragment)

            }
            true
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}