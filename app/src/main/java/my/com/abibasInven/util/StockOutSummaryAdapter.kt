package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.abibasInven.R
import my.com.abibasInven.data.StockIn
import my.com.abibasInven.data.StockOut

class StockOutSummaryAdapter (
    val fn: (StockOutSummaryAdapter.ViewHolder, StockOut) -> Unit = { _, _ ->}
): ListAdapter<StockOut, StockOutSummaryAdapter.ViewHolder>(StockOutSummaryAdapter) {

    companion object DiffCallback : DiffUtil.ItemCallback<StockOut>() {
        override fun areItemsTheSame(a: StockOut, b: StockOut)    = a.ID == b.ID
        override fun areContentsTheSame(a: StockOut, b: StockOut) = a.ID == b.ID
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblStockOutSummaryProductID : TextView = view.findViewById(R.id.lblStockOutSummaryProductID)
        val lblStockOutSummaryProductQty : TextView = view.findViewById(R.id.lblStockOutSummaryProductQty)
        val lblStockOutSummaryProductDateTime : TextView = view.findViewById(R.id.lblStockOutSummaryDateTime)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockOutSummaryAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.stockoutsummary_rv, parent, false)
        return StockOutSummaryAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockOutSummaryAdapter.ViewHolder, position: Int) {
        val stockOut = getItem(position)



        holder.lblStockOutSummaryProductID.text = stockOut.ID
        holder.lblStockOutSummaryProductQty.text = stockOut.qty.toString()
        holder.lblStockOutSummaryProductDateTime.text = stockOut.dateTime.toString()


        fn(holder, stockOut)
    }
}