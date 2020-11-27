package io.github.javiewer.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import io.github.javiewer.adapter.DownloadLinkAdapter
import io.github.javiewer.model.entity.DownloadLink
import io.github.javiewer.model.network.provider.DownloadLinkProvider
import io.github.javiewer.view.decoration.DownloadItemDecoration
import io.github.javiewer.view.listener.BasicOnScrollListener
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_recycler.recycler_view
import kotlinx.android.synthetic.main.fragment_recycler.refresh_layout
import okhttp3.ResponseBody
import retrofit2.Call

class DownloadFragment : RecyclerFragment<DownloadLink?, LinearLayoutManager?>() {
  var provider: DownloadLinkProvider? = null
  var keyword: String? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val bundle = arguments
    provider = DownloadLinkProvider.getProvider(bundle!!.getString("provider"))
    keyword = bundle.getString("keyword")
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    /*if (JAViewer.CONFIGURATIONS.showAds()) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("52546C5153814CA9A9714647F5960AFE")
                    .build();
            mAdView.loadAd(adRequest);
        }*/
    setLayoutManager(LinearLayoutManager(this.context))
    adapter = ScaleInAnimationAdapter(DownloadLinkAdapter(this.items, this.activity, provider))
    recycler_view.addItemDecoration(DownloadItemDecoration())
    val animator: ItemAnimator = SlideInUpAnimator()
    animator.addDuration = 300
    recycler_view.setItemAnimator(animator)
    setOnRefreshListener(
        OnRefreshListener { onScrollListener!!.refresh() })
    addOnScrollListener(object : BasicOnScrollListener<DownloadLink?>() {
      override fun newCall(page: Int): Call<ResponseBody?>? {
        return this@DownloadFragment.newCall(page)
      }

      override fun getLayoutManager(): LinearLayoutManager? {
        return this@DownloadFragment.layoutManager
      }

      override fun getRefreshLayout(): SwipeRefreshLayout {
        return this@DownloadFragment.refresh_layout
      }

      override fun getAdapter(): Adapter<*>? {
        return this@DownloadFragment.adapter
      }

      override fun getItems(): MutableList<DownloadLink?> {
        return this@DownloadFragment.items
      }

      @Throws(
          Exception::class
      ) override fun onResult(response: ResponseBody) {
        super.onResult(response)
        val downloads = provider!!.parseDownloadLinks(response.string())
        val pos = items.size
        if (downloads.isEmpty()) {
          isEnd = true
        } else {
          items.addAll(downloads)
          adapter!!.notifyItemRangeInserted(pos, downloads.size)
        }
      }
    })
    refresh_layout.post(Runnable {
      refresh_layout.setRefreshing(true)
      onRefreshListener!!.onRefresh()
    })
    super.onActivityCreated(savedInstanceState)
  }

  fun newCall(page: Int): Call<ResponseBody?>? {
    return provider!!.search(keyword, page)
  }
}