package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentAccountBinding


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val nav by lazy {findNavController()}
    private val email by lazy { requireArguments().getString("email", "") }
    private val vm : UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        val u = vm.get(email)


        binding.bottomNavigationView.selectedItemId = R.id.account

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> nav.navigate(R.id.homeFragment)
                R.id.account -> nav.navigate(R.id.accountFragment)
                R.id.product -> nav.navigate(R.id.productFragment)

            }
            true
        }

        binding.lblAccName.text = u?.name
        binding.lblAccountRole.text = u?.role
        binding.accountImg.setImageBitmap(u?.photo?.toBitmap())

        //binding.accountChgPass.setOnClickListener { nav.navigate() }
        //binding.accountChgPhoto.setOnClickListener { nav.navigate() }
        if (u?.role == "Manager"){
            binding.view3.isVisible = true
            binding.accountManageStaff.isVisible = true
        }

        binding.accountManageStaff.setOnClickListener { nav.navigate(R.id.staffListFragment) }




        return binding.root
    }


}