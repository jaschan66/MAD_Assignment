package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R

import my.com.abibasInven.data.Product

class DeliveryItemAdapter (
    val fn: (DeliveryItemAdapter.ViewHolder, Product) -> Unit = { _, _ ->}
): ListAdapter<Product, DeliveryItemAdapter.ViewHolder>(DeliveryItemAdapter.DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(a: Product, b: Product)    = a.ID == b.ID
        override fun areContentsTheSame(a: Product, b: Product) = a.ID == b.ID
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblDeliveryItemID : TextView = view.findViewById(R.id.lblDeliveryItemID)
        val imgDeliveryItemPhoto : ImageView = view.findViewById(R.id.imgDeliveryItemPhoto)
        val lblDeliveryItemQty : TextView = view.findViewById(R.id.lblDeliveryItemQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryItemAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deliveryitemlisting_rv, parent, false)
        return DeliveryItemAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryItemAdapter.ViewHolder, position: Int) {
        val product = getItem(position)

        holder.lblDeliveryItemID.text = product.ID
        holder.imgDeliveryItemPhoto.setImageBitmap(product.photo.toBitmap())
        holder.lblDeliveryItemQty.text = product.qty.toString()


        fn(holder, product)
    }
}