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

class ProductNotificationAdapter (
    val fn: (ViewHolder, Product) -> Unit = { _, _ ->}
): ListAdapter<Product,ProductNotificationAdapter.ViewHolder>(DiffCallback){


    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(a: Product, b:Product)     = a.ID == b.ID
        override fun areContentsTheSame(a: Product, b:Product)  = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto : ImageView = view.findViewById(R.id.imgNotificationProduct)
        val txtName : TextView = view.findViewById(R.id.lblProductNotificationName)
        val txtStock : TextView = view.findViewById(R.id.lblProductNotificationQty)
        val txtStockThres : TextView = view.findViewById(R.id.lblProductNotificationQtyThres)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductNotificationAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.productnotification_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductNotificationAdapter.ViewHolder, position: Int) {
        val product = getItem(position)
        holder.txtName.text = product.name
        holder.txtStock.text = "Quantity: " + product.qty.toString()
        holder.txtStockThres.text = "Quantity Threshold: " + product.qtyThreshold.toString()
        holder.imgPhoto.setImageBitmap(product.photo.toBitmap())

        fn(holder, product)
    }
}