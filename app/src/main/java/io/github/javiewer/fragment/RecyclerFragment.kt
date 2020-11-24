package io.github.javiewer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import io.github.javiewer.R.color
import io.github.javiewer.R.layout
import io.github.javiewer.view.ViewUtil
import io.github.javiewer.view.listener.BasicOnScrollListener
import kotlinx.android.synthetic.main.fragment_recycler.recycler_view
import kotlinx.android.synthetic.main.fragment_recycler.refresh_layout
import java.util.ArrayList

/**
 * Project: JAViewer
 */
abstract class RecyclerFragment<I, LM : LayoutManager?> : Fragment() {

  var onRefreshListener: OnRefreshListener? = null
    private set
  var onScrollListener: BasicOnScrollListener<*>? = null
    private set
  var items = ArrayList<I>()
    set(items) {
      val size = items.size
      if (size > 0) {
        items.clear()
        adapter!!.notifyDataSetChanged()
      }
      items.addAll(items)
      adapter!!.notifyDataSetChanged()
    }

  protected fun setRecyclerViewPadding(dp: Int) {
    recycler_view!!.setPadding(
        ViewUtil.dpToPx(dp),
        ViewUtil.dpToPx(dp),
        ViewUtil.dpToPx(dp),
        ViewUtil.dpToPx(dp)
    )
  }

  val layoutManager: LM?
    get() = recycler_view!!.layoutManager as LM?

  fun setLayoutManager(mLayoutManager: LM) {
    recycler_view!!.layoutManager = mLayoutManager
  }

  var adapter: Adapter<*>?
    get() = if (recycler_view == null) {
      null
    } else recycler_view!!.adapter
    set(mAdapter) {
      recycler_view!!.adapter = mAdapter
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(layout.fragment_recycler, container, false)
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    refresh_layout!!.setColorSchemeColors(
        ContextCompat.getColor(this.context!!, color.googleBlue),
        ContextCompat.getColor(this.context!!, color.googleGreen),
        ContextCompat.getColor(this.context!!, color.googleRed),
        ContextCompat.getColor(this.context!!, color.googleYellow)
    )
    if (savedInstanceState != null) {
      layoutManager!!.onRestoreInstanceState(savedInstanceState.getParcelable("LayoutManagerState"))
      items = (savedInstanceState.getSerializable("Items") as ArrayList<I>)
      if (onScrollListener != null) {
        onScrollListener!!.restoreState(savedInstanceState.getBundle("ScrollListenerState"))
      }
    }
  }

  fun addOnScrollListener(listener: BasicOnScrollListener<*>) {
    recycler_view!!.addOnScrollListener(listener.also { onScrollListener = it })
  }

  fun setOnRefreshListener(listener: OnRefreshListener) {
    refresh_layout!!.setOnRefreshListener(listener.also { onRefreshListener = it })
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putSerializable("Items", items)
    outState.putParcelable("LayoutManagerState", layoutManager!!.onSaveInstanceState())
    if (onScrollListener != null) {
      outState.putBundle("ScrollListenerState", onScrollListener!!.saveState())
    }
    super.onSaveInstanceState(outState)
  }
}