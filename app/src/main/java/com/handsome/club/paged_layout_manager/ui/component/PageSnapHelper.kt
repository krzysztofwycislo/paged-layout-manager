package com.handsome.club.paged_layout_manager.ui.component

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.SnapHelper
import kotlin.math.abs

class PageSnapHelper : SnapHelper() {

    private var horizontalHelper: OrientationHelper? = null

    private fun getHorizontalHelper(
        layoutManager: LayoutManager
    ): OrientationHelper {
        if (horizontalHelper == null || horizontalHelper?.layoutManager !== layoutManager) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }

        return horizontalHelper!!
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: LayoutManager,
        targetView: View
    ): IntArray {
        val out = if (layoutManager.canScrollHorizontally()) {
            val helper = getHorizontalHelper(layoutManager)
            helper.getDecoratedStart(targetView) - helper.startAfterPadding
        } else 0
        return intArrayOf(out, 0)
    }

    override fun findSnapView(layoutManager: LayoutManager): View? {
        if (layoutManager !is PagedHorizontalLayoutManager) return null

        return indexOfChildClosestToCenter(layoutManager)
            .run(layoutManager::getChildAt)
            ?.run(layoutManager::getPosition)
            ?.run(layoutManager::getPageFirstPosition)
            ?.run(layoutManager::findViewByPosition)
    }

    override fun findTargetSnapPosition(
        layoutManager: LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        if (layoutManager !is PagedHorizontalLayoutManager) return 0

        return indexOfChildClosestToCenter(layoutManager)
            .run(layoutManager::getChildAt)
            ?.run(layoutManager::getPosition)
            ?.run(layoutManager::getNextAndPreviousPagePositions)
            ?.run { if (velocityX > 0) second else first }
            ?: 0
    }

    private fun indexOfChildClosestToCenter(layoutManager: LayoutManager): Int {
        if (layoutManager.childCount == 0) return -1

        val helper = getHorizontalHelper(layoutManager)
        val center = helper.totalSpace / 2

        return (0 until layoutManager.childCount).minBy {
            val child = layoutManager.getChildAt(it)
            val start = helper.getDecoratedStart(child)
            val end = helper.getDecoratedEnd(child)

            abs((start + end) / 2 - center)
        }
    }
}