package my.com.abibasInven.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import my.com.abibasInven.R
import my.com.abibasInven.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val nav by lazy {supportFragmentManager.findFragmentById(R.id.host)!!.findNavController()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home     -> nav.navigate(R.id.action_global_stockMainReportFragment)
                R.id.outlet   -> nav.navigate(R.id.action_global_outletListFragment)
                R.id.account  -> nav.navigate(R.id.action_global_accountFragment)
                R.id.supplier -> nav.navigate(R.id.action_global_supplierListFragment)
                R.id.product  -> nav.navigate(R.id.action_global_productFragment)
            }
            true
        }
    }

    override fun onBackPressed() {
        val num = nav.currentDestination?.label

        when(num){
            "fragment_product"         -> super.finish()
            "fragment_account"         -> super.finish()
            "fragment_home"            -> super.finish()
            "fragment_login"           -> super.finish()
            "fragment_supplier_list"   -> super.finish()
            "fragment_outlet_list"     -> super.finish()
        }

        nav.popBackStack()
    }


    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}