package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.abibasInven.R
import my.com.abibasInven.data.Delivery

class DeliveryAdapter (
    val fn: (DeliveryAdapter.ViewHolder, Delivery) -> Unit = { _, _ ->}
): ListAdapter<Delivery, DeliveryAdapter.ViewHolder>(DeliveryAdapter) {

    companion object DiffCallback : DiffUtil.ItemCallback<Delivery>() {
        override fun areItemsTheSame(a: Delivery, b: Delivery)    = a.ID == b.ID
        override fun areContentsTheSame(a: Delivery, b: Delivery) = a.ID == b.ID
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblDeliveryID : TextView = view.findViewById(R.id.lblDeliveryItemID)
        val btnDeliveryListingAddDeliveryItem : Button = view.findViewById(R.id.btnDeliveryListingAddDeliveryItem)
        val btnDeliveryDetail : Button = view.findViewById(R.id.btnDeliveryDetail)
        val btnDeleteDelivery : Button = view.findViewById(R.id.btnDeleteDelivery)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.deliverylisting_rv, parent, false)
        return DeliveryAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryAdapter.ViewHolder, position: Int) {
        val delivery = getItem(position)

        holder.lblDeliveryID.text = delivery.ID

        if(delivery.deliveryStatus=="delivering"||delivery.deliveryStatus=="completed"){
            holder.btnDeliveryListingAddDeliveryItem.isVisible = false
            holder.btnDeleteDelivery.isVisible = false
        }

        fn(holder, delivery)
    }
}