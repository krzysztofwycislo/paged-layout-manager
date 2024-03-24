package com.handsome.club.paged_layout_manager.ui

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.handsome.club.paged_layout_manager.data.exampleCats
import com.handsome.club.paged_layout_manager.databinding.ActivityMainBinding
import com.handsome.club.paged_layout_manager.ui.cats.CatsRecyclerAdapter
import com.handsome.club.paged_layout_manager.ui.component.PageSnapHelper
import com.handsome.club.paged_layout_manager.ui.component.PagedHorizontalLayoutManager

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            catsList.initializeCatsList()

            nextPageBtn.setOnClickListener { catsList.smoothScrollToPosition(20) }
            previousPageBtn.setOnClickListener { catsList.smoothScrollToPosition(0) }
        }

        setContentView(binding.root)
    }

    private fun RecyclerView.initializeCatsList() {
        layoutManager = PagedHorizontalLayoutManager(
            size = 2 to 5,
        )

        adapter = CatsRecyclerAdapter(exampleCats.toMutableList())
        PageSnapHelper().attachToRecyclerView(this)
    }


}