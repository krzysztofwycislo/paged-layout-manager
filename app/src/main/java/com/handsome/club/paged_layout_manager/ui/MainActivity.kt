package com.handsome.club.paged_layout_manager.ui

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.handsome.club.paged_layout_manager.data.exampleCats
import com.handsome.club.paged_layout_manager.databinding.ActivityMainBinding
import com.handsome.club.paged_layout_manager.ui.cats.CatsRecyclerAdapter
import com.handsome.club.paged_layout_manager.ui.component.PagedHorizontalLayoutManager

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            catsList.initializeCatsList()
        }

        setContentView(binding.root)
    }

    private fun RecyclerView.initializeCatsList() {
        adapter = CatsRecyclerAdapter(exampleCats)

        layoutManager = PagedHorizontalLayoutManager(
            gridSize = 3 to 5,
        )
    }

}