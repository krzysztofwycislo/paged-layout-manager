package com.handsome.club.paged_layout_manager.ui

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.handsome.club.paged_layout_manager.databinding.ActivityMainBinding
import com.handsome.club.paged_layout_manager.ui.cats.ExampleAdapter
import com.handsome.club.paged_layout_manager.ui.component.PageSnapHelper
import com.handsome.club.paged_layout_manager.ui.component.PagedHorizontalLayoutManager

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            recyclerView.initialize()

            nextPageBtn.setOnClickListener { recyclerView.smoothScrollToPosition(11) }
            previousPageBtn.setOnClickListener { recyclerView.smoothScrollToPosition(0) }
        }

        setContentView(binding.root)
    }

    private fun RecyclerView.initialize() {
        val list = (0..40).map { it.toString() }.toMutableList()

        layoutManager = PagedHorizontalLayoutManager(
            size = 5 to 2,
        )

        adapter = ExampleAdapter(list)
        PageSnapHelper().attachToRecyclerView(this)
    }


}