package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.Product
import my.com.abibasInven.data.User
import my.com.abibasInven.data.UserViewModel

class ProductAdapter (
    val fn: (ViewHolder, Product) -> Unit = { _, _ ->}
): ListAdapter<Product,ProductAdapter.ViewHolder>(DiffCallback){


    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(a: Product, b:Product)      = a.ID == b.ID
        override fun areContentsTheSame(a: Product, b:Product) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto : ImageView = view.findViewById(R.id.imgProduct)
        val txtName : TextView = view.findViewById(R.id.lblProductName)
        val txtStock : TextView = view.findViewById(R.id.lblStockCount)
        val btnUpdate : Button = view.findViewById(R.id.btnProductUpdate)
        val btnDelete : Button = view.findViewById(R.id.btnDeleteProduct)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.product_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val product = getItem(position)
        holder.txtName.text = product.name
        holder.txtStock.text = "Stock: " + product.qty.toString()
        holder.imgPhoto.setImageBitmap(product.photo.toBitmap())

        fn(holder, product)
    }
}