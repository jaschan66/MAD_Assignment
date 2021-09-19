package my.com.abibasInven.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.logindemo.util.snackbar
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import my.com.abibasInven.R
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.SupplierViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentProductNotificationBinding
import my.com.abibasInven.util.ProductNotificationAdapter
import my.com.abibasInven.util.StaffAdapter


class ProductNotificationFragment : Fragment() {

    private lateinit var binding: FragmentProductNotificationBinding
    private val nav by lazy {findNavController()}
    private val vm: ProductViewModel by activityViewModels()
    private val vmSup: SupplierViewModel by activityViewModels()
    private lateinit var adapter: ProductNotificationAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProductNotificationBinding.inflate(inflater, container, false)


        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        vmSup.getAllSupplier()

        binding.btnBack.setOnClickListener { nav.navigate(R.id.action_productNotificationFragment_to_productFragment) }

        adapter = ProductNotificationAdapter() { holder, product ->
            holder.root.setOnClickListener {
                nav.navigate(R.id.supplierDetailsFragment, bundleOf("suppId" to product.supplierID))
            }

        }
        binding.productNotificationRV.adapter = adapter
        binding.productNotificationRV.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getProductLessQty().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        return binding.root
    }

}