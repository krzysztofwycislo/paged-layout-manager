package com.handsome.club.paged_layout_manager.ui.component

import androidx.recyclerview.widget.RecyclerView

private val Pair<Int, Int>.columns get() = this.first
private val Pair<Int, Int>.rows get() = this.second

class PagedHorizontalLayoutManager(
    private val gridSize: Pair<Int, Int>,
): RecyclerView.LayoutManager() {



    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

}