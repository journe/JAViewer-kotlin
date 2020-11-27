package io.github.javiewer.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import io.github.javiewer.JAViewer
import io.github.javiewer.adapter.ActressAdapter
import io.github.javiewer.model.entity.Actress
import io.github.javiewer.model.network.provider.AVMOProvider
import io.github.javiewer.view.decoration.ActressItemDecoration
import io.github.javiewer.view.listener.EndlessOnScrollListener
import kotlinx.android.synthetic.main.fragment_recycler.recycler_view
import kotlinx.android.synthetic.main.fragment_recycler.refresh_layout
import okhttp3.ResponseBody
import retrofit2.Call

class ActressesFragment : RecyclerFragment<Actress?, LinearLayoutManager?>() {
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    setLayoutManager(LinearLayoutManager(this.context))
    //this.setAdapter(new SlideInBottomAnimationAdapter(new ActressAdapter(getItems(), this.getActivity())));
    adapter = ActressAdapter(items, this.activity)
    recycler_view.addItemDecoration(ActressItemDecoration())

    /*RecyclerView.ItemAnimator animator = new SlideInUpAnimator();
        animator.setAddDuration(300);
        recycler_view.setItemAnimator(animator);*/setOnRefreshListener(
        OnRefreshListener { onScrollListener!!.refresh() })
    addOnScrollListener(object : EndlessOnScrollListener<Actress?>() {
      override fun newCall(page: Int): Call<ResponseBody> {
        return this@ActressesFragment.newCall(page)
      }

      override fun getLayoutManager(): LayoutManager {
        return layoutManager
      }

      override fun getRefreshLayout(): SwipeRefreshLayout {
        return this@ActressesFragment.refresh_layout
      }

      override fun getAdapter(): Adapter<*>? {
        return adapter
      }

      override fun getItems(): MutableList<Actress> {
        return items
      }

      @Throws(
          Exception::class
      ) override fun onResult(response: ResponseBody) {
        super.onResult(response)
        val wrappers = AVMOProvider.parseActresses(response.string())
        val pos = items.size
        items.addAll(wrappers)
        adapter!!.notifyItemRangeInserted(pos, wrappers.size)
      }
    })
    refresh_layout.post {
      refresh_layout.isRefreshing = true
      onRefreshListener!!.onRefresh()
    }
    super.onActivityCreated(savedInstanceState)
  }

  fun newCall(page: Int): Call<ResponseBody> {
    return JAViewer.SERVICE.getActresses(page)
  }
}