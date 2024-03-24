package com.handsome.club.paged_layout_manager.ui.component

import android.graphics.PointF
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min


private val Pair<Int, Int>.columns get() = this.first
private val Pair<Int, Int>.rows get() = this.second
typealias Page = Int

class PagedHorizontalLayoutManager(
    private val size: Pair<Int, Int>,
) : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    private val itemsInPage = size.columns * size.rows

    private var scrollOffset = 0

    private var pagedItems = mutableListOf<PagedItem>()

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
        Timber.i("onLayoutChildren isPreLayout = ${state.isPreLayout}, itemCount = $itemCount")

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

        pagedItems.clear()

        (0 until itemCount).map { position ->
            calculatePagedItems(position)
        }.also(pagedItems::addAll)

        fillGrid(recycler, state, removedCache)
    }

    private fun calculatePagedItems(position: Int): PagedItem {
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

        return PagedItem(
            Rect(left, top, right, bottom),
            itemPage
        )
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
        val extraPosition = pagedItems.size - itemsInPage + appearingViewIndex

        if (extraPosition > pagedItems.size) return

        val appearingView = recycler.getViewForPosition(extraPosition)
        addView(appearingView)
        measureChildWithMargins(appearingView, itemWidth, itemHeight)

        val lastRect = pagedItems.last().rect
        layoutDecoratedWithMargins(
            appearingView,
            lastRect.left,
            lastRect.top,
            lastRect.right,
            lastRect.bottom
        )
    }

    private fun getVisibleViews(recycler: RecyclerView.Recycler): List<Pair<View, Rect>> {
        return pagedItems.mapIndexed { index, pagedItem ->
            if (Rect.intersects(recyclerViewRect, pagedItem.rect))
                recycler.getViewForPosition(index) to pagedItem.rect
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

        val pages = (itemCount - 1) / itemsInPage
        val fullSize = pages * width
        val lastOffset = scrollOffset

        scrollOffset = if (isLayoutRTL())
            (scrollOffset + dx).coerceIn(-fullSize..0)
        else
            (scrollOffset + dx).coerceIn(0..fullSize)

        fillGrid(recycler, state)

        return scrollOffset - lastOffset
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

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) {
            return null
        }

        val firstChildPos = getPosition(getChildAt(0)!!)
        val direction = if (targetPosition < firstChildPos != isLayoutRTL()) -1f else 1f

        return pagedItems.getOrNull(targetPosition)?.run { PointF(direction, 0f) }
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val linearSmoothScroller = LinearSmoothScroller(recyclerView.context)
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

    data class PagedItem(
        val rect: Rect,
        val page: Page
    )

}