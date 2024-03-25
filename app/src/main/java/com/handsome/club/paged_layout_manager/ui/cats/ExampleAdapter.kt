package com.handsome.club.paged_layout_manager.ui.cats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.handsome.club.paged_layout_manager.databinding.ListItemBinding


class ExampleAdapter(
    private var values: MutableList<String>
) : RecyclerView.Adapter<ExampleAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemBinding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return ItemViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(values[position])
    }

    inner class ItemViewHolder(
        private val binding: ListItemBinding
    ) : ViewHolder(binding.root) {

        fun bind(value: String) = with(binding) {
            text.text = value

            itemContainer.setOnClickListener {
                val realIndex = values.indexOf(value)
                values.removeAt(realIndex)
                notifyItemRemoved(realIndex)
            }
        }
    }
}