package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.toBitmap
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentStaffDetailsBinding


class StaffDetailsFragment : Fragment() {

    private lateinit var binding: FragmentStaffDetailsBinding
    private val nav by lazy {findNavController()}
    private val email by lazy { requireArguments().getString("email", null) }
    private val vm: UserViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentStaffDetailsBinding.inflate(inflater, container, false)

        // TODO
        val s = vm.get(email)

        binding.btnBackStaffDetails.setOnClickListener { nav.navigate(R.id.action_staffDetailsFragment_to_staffListFragment) }
        binding.staffDetailImg.setImageBitmap(s?.photo?.toBitmap())
        binding.lblDetailStaffName.setText(s?.name)
        binding.lblRole.setText(s?.role)

        binding.btnClose.setOnClickListener { nav.navigateUp() }

        return binding.root
    }

}