package com.handsome.club.paged_layout_manager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.handsome.club.paged_layout_manager.databinding.CatListItemBinding
import com.handsome.club.paged_layout_manager.model.Cat


class CatsRecyclerAdapter(
    private val cats: List<Cat>
) : RecyclerView.Adapter<CatsRecyclerAdapter.CatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatsViewHolder {
        val itemBinding = CatListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return CatsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: CatsViewHolder, position: Int) {
        holder.bind(cats[position])
    }

    inner class CatsViewHolder(
        private val binding: CatListItemBinding
    ) : ViewHolder(binding.root) {

        fun bind(cat: Cat) {
            binding.catName.text = cat.name
        }

    }
}