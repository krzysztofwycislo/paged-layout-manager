package androidx.recyclerview.widget

import android.annotation.SuppressLint
import com.handsome.club.paged_layout_manager.ui.component.PagedHorizontalLayoutManager
import timber.log.Timber


@SuppressLint("VisibleForTests")
fun RecyclerView.smoothScrollToPage(page: Int) {
    if (mLayoutSuppressed) {
        return
    }

    if (mLayout !is PagedHorizontalLayoutManager) {
        Timber.tag(RecyclerView.TAG).e("Smooth scroll to page can be only used with PagedHorizontalLayoutManager")
        return
    }

    (mLayout as PagedHorizontalLayoutManager).smoothScrollToPage(this, mState, page)
}