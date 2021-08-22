package my.com.abibasInven

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import my.com.abibasInven.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accountFragment=AccountFragment()
        val homeFragment=HomeFragment()
        val productFragment=ProductFragment()

        setCurrentFragment(accountFragment)

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.account->setCurrentFragment(accountFragment)
                R.id.home->setCurrentFragment(homeFragment)
                R.id.product->setCurrentFragment(productFragment)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}