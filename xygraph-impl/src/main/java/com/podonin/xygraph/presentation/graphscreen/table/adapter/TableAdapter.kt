package com.podonin.xygraph.presentation.graphscreen.table.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.podonin.xygraph.databinding.ColumnItemBinding
import com.podonin.xygraph.presentation.graphscreen.table.TableItem

class TableAdapter : RecyclerView.Adapter<TableAdapter.ColumnViewHolder>() {

    private val asyncDiffer = AsyncListDiffer(this, TableDiffUtilCallback())

    fun setData(newData: List<TableItem>) {
        asyncDiffer.submitList(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnViewHolder {
        val binding = ColumnItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColumnViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColumnViewHolder, position: Int) {
        holder.bind(asyncDiffer.currentList[position])
    }

    override fun getItemCount() = asyncDiffer.currentList.size

    class ColumnViewHolder(
        private val itemBinding: ColumnItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(point: TableItem) {
            with(itemBinding) {
                indexTextView.text = point.title
                xTextView.text = point.xLabel
                yTextView.text = point.yLabel
            }
        }
    }

    private class TableDiffUtilCallback : DiffUtil.ItemCallback<TableItem>() {
        override fun areItemsTheSame(oldItem: TableItem, newItem: TableItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: TableItem, newItem: TableItem): Boolean {
            return oldItem == newItem
        }
    }
}