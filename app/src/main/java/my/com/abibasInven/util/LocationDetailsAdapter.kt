package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.Product


class LocationDetailsAdapter (val fn: (ViewHolder, Product) -> Unit = { _,_  -> })
    :  ListAdapter<Product, LocationDetailsAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(a: Product, b: Product)    = a.ID == b.ID
        override fun areContentsTheSame(a: Product, b: Product) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblRackProductID : TextView = view.findViewById(R.id.lblRackProductID)
        val imgRackProductPhoto : ImageView = view.findViewById(R.id.imgRackProductPhoto)
        val lblRackProductQty : TextView = view.findViewById(R.id.lblRackProductQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.locationdetail_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.lblRackProductID.text = product.ID
        holder.imgRackProductPhoto.setImageBitmap(product.photo.toBitmap())
        holder.lblRackProductQty.text = product.qty.toString()

        fn(holder, product)
    }
}