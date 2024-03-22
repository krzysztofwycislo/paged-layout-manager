package com.handsome.club.paged_layout_manager.ui

import android.app.Activity
import android.os.Bundle
import com.handsome.club.paged_layout_manager.data.exampleCats
import com.handsome.club.paged_layout_manager.databinding.ActivityMainBinding
import com.handsome.club.paged_layout_manager.ui.cats.CatsRecyclerAdapter

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            catsList.adapter = CatsRecyclerAdapter(exampleCats)
        }

        setContentView(binding.root)
    }

}