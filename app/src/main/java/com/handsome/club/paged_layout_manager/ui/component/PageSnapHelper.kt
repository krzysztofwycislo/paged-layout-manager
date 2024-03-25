package com.handsome.club.paged_layout_manager.ui.component

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
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
        if (layoutManager !is PagedHorizontalLayoutManager) return intArrayOf(0, 0)

        val out = if (layoutManager.canScrollHorizontally()) {
            layoutManager.getPosition(targetView)
                .run(layoutManager::calculateDistanceToPageStart)
        } else 0

        return intArrayOf(out, 0)
    }

    override fun findSnapView(layoutManager: LayoutManager): View? {
        if (layoutManager !is PagedHorizontalLayoutManager) return null

        return indexOfChildClosestToCenter(layoutManager)
            .run(layoutManager::getChildAt)
            ?.run(layoutManager::getPosition)
            ?.run(layoutManager::getPageLeftMostPosition)
            ?.run(layoutManager::findViewByPosition)
    }

    override fun findTargetSnapPosition(
        layoutManager: LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        if (layoutManager !is PagedHorizontalLayoutManager) return RecyclerView.NO_POSITION

        val childIndex = if(velocityX < 0){
            indexOfChildClosestToLeft(layoutManager)
        } else {
            indexOfChildClosestToRight(layoutManager)
        }

        return layoutManager.getChildAt(childIndex)
            ?.run(layoutManager::getPosition)
            ?.run(layoutManager::getPageLeftMostPosition)
            ?: 0
    }

    private fun indexOfChildClosestToCenter(layoutManager: LayoutManager): Int {
        if (layoutManager.childCount == 0) return RecyclerView.NO_POSITION

        val helper = getHorizontalHelper(layoutManager)
        val center = helper.totalSpace / 2

        return (0 until layoutManager.childCount).minBy {
            val child = layoutManager.getChildAt(it)
            val start = helper.getDecoratedStart(child)
            val end = helper.getDecoratedEnd(child)

            abs((start + end) / 2 - center)
        }
    }

    private fun indexOfChildClosestToLeft(layoutManager: LayoutManager): Int {
        if (layoutManager.childCount == 0) return RecyclerView.NO_POSITION

        val helper = getHorizontalHelper(layoutManager)
        return (0 until layoutManager.childCount).minBy {
            layoutManager.getChildAt(it)
                .run(helper::getDecoratedStart)
        }
    }

    private fun indexOfChildClosestToRight(layoutManager: LayoutManager): Int {
        if (layoutManager.childCount == 0) return RecyclerView.NO_POSITION

        val helper = getHorizontalHelper(layoutManager)
        return (0 until layoutManager.childCount).maxBy {
            layoutManager.getChildAt(it)
                .run(helper::getDecoratedEnd)
        }
    }
}