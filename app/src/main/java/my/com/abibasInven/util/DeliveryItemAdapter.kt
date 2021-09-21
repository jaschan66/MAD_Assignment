package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.DeliveryItem
import my.com.abibasInven.data.DeliveryViewModel

class DeliveryItemAdapter (
    val fn: (DeliveryItemAdapter.ViewHolder, DeliveryItem) -> Unit = { _, _ ->}
): ListAdapter<DeliveryItem, DeliveryItemAdapter.ViewHolder>(DeliveryItemAdapter.DiffCallback) {


    companion object DiffCallback : DiffUtil.ItemCallback<DeliveryItem>() {
        override fun areItemsTheSame(a: DeliveryItem, b: DeliveryItem)    = a.ID == b.ID
        override fun areContentsTheSame(a: DeliveryItem, b: DeliveryItem) = a.ID == b.ID
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblDeliveryItemID : TextView = view.findViewById(R.id.lblDeliveryItemID)
        val imgDeliveryItemPhoto : ImageView = view.findViewById(R.id.imgDeliveryItemPhoto)
        val lblDeliveryItemQty : TextView = view.findViewById(R.id.lblDeliveryItemQty)
        val btnDeliveryItemEdit : TextView = view.findViewById(R.id.btnDeliveryItemEdit)
        val btnDeliveryItemDelete : TextView = view.findViewById(R.id.btnDeliveryItemDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryItemAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deliveryitemlisting_rv, parent, false)
        return DeliveryItemAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryItemAdapter.ViewHolder, position: Int) {
        val deliveryItem = getItem(position)
        holder.lblDeliveryItemID.text = deliveryItem.ID
        holder.imgDeliveryItemPhoto.setImageBitmap(deliveryItem.deliveryItemPhoto.toBitmap())
        holder.lblDeliveryItemQty.text = deliveryItem.deliveryQty.toString()


        fn(holder, deliveryItem)
    }
}