package io.github.javiewer.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import io.github.javiewer.adapter.MovieAdapter
import io.github.javiewer.adapter.item.Movie
import io.github.javiewer.network.provider.AVMOProvider
import io.github.javiewer.view.decoration.MovieItemDecoration
import io.github.javiewer.view.listener.EndlessOnScrollListener
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_recycler.recycler_view
import kotlinx.android.synthetic.main.fragment_recycler.refresh_layout
import okhttp3.ResponseBody
import retrofit2.Call
import java.util.ArrayList

abstract class MovieFragment : RecyclerFragment<Movie, LinearLayoutManager?>() {
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    setLayoutManager(LinearLayoutManager(this.context))
    recycler_view.addItemDecoration(MovieItemDecoration())
    adapter = SlideInBottomAnimationAdapter(MovieAdapter(items, requireActivity()))
    val animator: ItemAnimator = SlideInUpAnimator()
    animator.addDuration = 300
    recycler_view.itemAnimator = animator
    setOnRefreshListener(OnRefreshListener { onScrollListener!!.refresh() })
    addOnScrollListener(object : EndlessOnScrollListener<Movie?>() {
      override fun newCall(page: Int): Call<ResponseBody> {
        return this@MovieFragment.newCall(page)
      }

      override fun getLayoutManager(): LayoutManager {
        return this@MovieFragment.layoutManager!!
      }

      override fun getRefreshLayout(): SwipeRefreshLayout {
        return this@MovieFragment.refresh_layout
      }

      override fun getItems(): ArrayList<Movie> {
        return this@MovieFragment.items
      }

      override fun getAdapter(): Adapter<*>? {
        return this@MovieFragment.adapter
      }

      @Throws(
          Exception::class
      ) override fun onResult(response: ResponseBody) {
        super.onResult(response)
        val wrappers = AVMOProvider.parseMovies(response.string())
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

  abstract fun newCall(page: Int): Call<ResponseBody>
}