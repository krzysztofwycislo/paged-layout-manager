package com.handsome.club.paged_layout_manager.ui.component

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min


private val Pair<Int, Int>.columns get() = this.first
private val Pair<Int, Int>.rows get() = this.second

class PagedHorizontalLayoutManager(
    private val size: Pair<Int, Int>,
) : RecyclerView.LayoutManager() {

    private val itemsInPage = size.columns * size.rows

    private var scrollOffset = 0

    private var itemRects = arrayListOf<Rect>()

    private val itemWidth get() = width / size.columns
    private val itemHeight get() = height / size.rows

    private val recyclerViewRect
        get() = Rect().apply {
            left = scrollOffset
            top = 0
            right = left + width
            bottom = height
        }

    override fun onLayoutChildren(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) {
        if (itemCount == 0) return

        val removedCache = mutableListOf<Int>()
        if (state.isPreLayout) {
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val layoutParams = view!!.layoutParams as RecyclerView.LayoutParams
                if (layoutParams.isItemRemoved) {
                    removedCache.add(layoutParams.viewLayoutPosition)
                }
            }
        }

        itemRects.clear()

        (0 until itemCount).map { position ->
            calculateAllItemPosition(position)
        }.also(itemRects::addAll)

        fillGrid(recycler, state, removedCache)
    }

    private fun calculateAllItemPosition(position: Int): Rect {
        val itemPage = position / itemsInPage
        val itemRow = (position % itemsInPage / size.columns)

        val (left, right) = if (isLayoutRTL()) {
            val right = ((position % size.columns) * -itemWidth + itemPage * -width) + width
            val left = right - itemWidth

            left to right
        } else {
            val left = ((position % size.columns) * itemWidth + itemPage * width)
            val right = left + itemWidth

            left to right
        }

        val top = itemRow * itemHeight
        val bottom = top + itemHeight

        return Rect(left, top, right, bottom)
    }

    private fun fillGrid(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        removedCache: List<Int> = emptyList()
    ) {
        detachAndScrapAttachedViews(recycler)

        getVisibleViews(recycler)
            .forEach { (view, itemRect) ->
                view.layoutParams = (view.layoutParams as RecyclerView.LayoutParams)
                    .apply {
                        width = itemWidth
                        height = itemHeight
                    }

                addView(view)
                measureChildWithMargins(view, itemWidth, itemHeight)
                layoutDecoratedWithMargins(
                    view,
                    itemRect.left - scrollOffset,
                    itemRect.top,
                    itemRect.right - scrollOffset,
                    itemRect.bottom
                )
            }

        if (state.isPreLayout && removedCache.isNotEmpty()) {
            removedCache.forEach { index ->
                layoutAppearingView(recycler, index)
            }
        }

        val scrapListCopy = recycler.scrapList.toList()
        scrapListCopy.forEach {
            recycler.recycleView(it.itemView)
        }
    }

    private fun layoutAppearingView(recycler: RecyclerView.Recycler, appearingViewIndex: Int) {
        val extraPosition = itemRects.size - itemsInPage + appearingViewIndex

        if (extraPosition > itemRects.size) return

        val appearingView = recycler.getViewForPosition(extraPosition)
        addView(appearingView)
        measureChildWithMargins(appearingView, itemWidth, itemHeight)

        val lastRect = itemRects.last()
        layoutDecoratedWithMargins(
            appearingView,
            lastRect.left,
            lastRect.top,
            lastRect.right,
            lastRect.bottom
        )
    }

    private fun getVisibleViews(recycler: RecyclerView.Recycler): List<Pair<View, Rect>> {
        return itemRects.mapIndexed { index, itemRect ->
            if (Rect.intersects(recyclerViewRect, itemRect))
                recycler.getViewForPosition(index) to itemRect
            else
                null
        }.filterNotNull()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        if (itemCount == 0 && state.isPreLayout) return 0

        val pages = itemCount / itemsInPage

        val fullSize = pages * width
        val lastOffset = scrollOffset

        scrollOffset = if (isLayoutRTL())
            min(max(-fullSize, scrollOffset + dx), 0)
        else
            min(max(0, scrollOffset + dx), fullSize)

        fillGrid(recycler, state)
        return lastOffset - scrollOffset
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    private fun isLayoutRTL(): Boolean {
        return layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return true
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

}