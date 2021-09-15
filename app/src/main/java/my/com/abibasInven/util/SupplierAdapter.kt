package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.abibasInven.R
import my.com.abibasInven.data.Supplier

class SupplierAdapter(
    val fn: (ViewHolder, Supplier) -> Unit = { _, _ -> }
) : ListAdapter<Supplier, SupplierAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Supplier>() {
        override fun areItemsTheSame(a: Supplier, b: Supplier) = a.email == b.email
        override fun areContentsTheSame(a: Supplier, b: Supplier) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtName: TextView = view.findViewById(R.id.lblSupplierName)
        val btnUpdate: Button = view.findViewById(R.id.btnSupplierUpdate)
        val btnDelete: Button = view.findViewById(R.id.btnSupplierDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.supplier_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SupplierAdapter.ViewHolder, position: Int) {
        val supplier = getItem(position)
        holder.txtName.text = supplier.name

        fn(holder, supplier)
    }
}