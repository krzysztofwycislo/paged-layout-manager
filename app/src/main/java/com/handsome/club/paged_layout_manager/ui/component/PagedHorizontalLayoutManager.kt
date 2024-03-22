package com.handsome.club.paged_layout_manager.ui.component

import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

private val Pair<Int, Int>.columns get() = this.first
private val Pair<Int, Int>.rows get() = this.second

class PagedHorizontalLayoutManager(
    private val size: Pair<Int, Int>,
) : RecyclerView.LayoutManager() {

    private val itemsInPage = size.columns * size.rows


    override fun onLayoutChildren(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State?
    ) {
        if (itemCount == 0) return

        val itemWidth = width / size.columns
        val itemHeight = height / size.rows

        for (position in 0 until itemCount) {
            val itemPage = position / itemsInPage
            val itemRow = (position % itemsInPage / size.columns)

            Timber.d("onLayoutChildren position = $position page = $itemPage row = $itemRow")

            val view = recycler.getViewForPosition(position)
                .also(::addView)

            view.layoutParams = (view.layoutParams as RecyclerView.LayoutParams)
                .apply {
                    width = itemWidth
                    height = itemHeight
                }

            val left = ((position % size.columns) * itemWidth + itemPage * width)
            val right = left + itemWidth

            val top = itemRow * itemHeight
            val bottom = top + itemHeight

            Timber.d("onLayoutChildren left = $left right = $right top = $top bottom = $bottom")

            measureChild(view, itemWidth, itemHeight)
            layoutDecorated(view, left, top, right, bottom)
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

}