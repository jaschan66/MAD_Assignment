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
import my.com.abibasInven.data.Delivery
import my.com.abibasInven.data.StockIn

class StockInSummaryAdapter (
    val fn: (StockInSummaryAdapter.ViewHolder, StockIn) -> Unit = { _, _ ->}
): ListAdapter<StockIn, StockInSummaryAdapter.ViewHolder>(StockInSummaryAdapter) {

    companion object DiffCallback : DiffUtil.ItemCallback<StockIn>() {
        override fun areItemsTheSame(a: StockIn, b: StockIn)    = a.ID == b.ID
        override fun areContentsTheSame(a: StockIn, b: StockIn) = a.ID == b.ID
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblStockINSummaryProductID : TextView = view.findViewById(R.id.lblStockInSummaryProductID)
        val lblStockINSummaryProductQty : TextView = view.findViewById(R.id.lblStockInSummaryProductQty)
        val lblStockINSummaryProductDateTime : TextView = view.findViewById(R.id.lblStockInSummaryDateTime)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockInSummaryAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.stockinsummary_rv, parent, false)
        return StockInSummaryAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockInSummaryAdapter.ViewHolder, position: Int) {
        val stockIn = getItem(position)


        //holder.lblDeliveryID.text = delivery.ID
        holder.lblStockINSummaryProductID.text = stockIn.ID
        holder.lblStockINSummaryProductQty.text = stockIn.qty.toString()
        holder.lblStockINSummaryProductDateTime.text = stockIn.dateTime.toString()


        fn(holder, stockIn)
    }


}