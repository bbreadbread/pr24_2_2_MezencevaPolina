package com.example.pract24_var2_var2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class HistoryAdapter(private val historyList: List<HistoryItem>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val incomeTextView: TextView = itemView.findViewById(R.id.incomeTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        val bankTextView: TextView = itemView.findViewById(R.id.bankTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        init {
            itemView.setOnClickListener {
                itemClickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.incomeTextView.text = historyItem.type
        holder.amountTextView.text = historyItem.money
        holder.bankTextView.text = historyItem.bank
        holder.dateTextView.text = historyItem.date

    }
    override fun getItemCount(): Int = historyList.size
}
