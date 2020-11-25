package io.github.javiewer.fragment.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import butterknife.BindView
import butterknife.ButterKnife
import io.github.javiewer.JAViewer
import io.github.javiewer.R
import io.github.javiewer.R.layout
import io.github.javiewer.adapter.GenreAdapter
import io.github.javiewer.adapter.item.Genre
import io.github.javiewer.view.ViewUtil
import io.github.javiewer.view.decoration.GridSpacingItemDecoration
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import okhttp3.ResponseBody
import retrofit2.Call

class GenreFragment : Fragment() {
  @BindView(R.id.genre_recycler_view)
  var recycler_view: RecyclerView? = null
  var genres: MutableList<Genre> = mutableListOf()
    protected set
  var adapter: Adapter<*>? = null
    private set
  private var mLayoutManager: StaggeredGridLayoutManager? = null
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(layout.fragment_genre_list, container, false)
    ButterKnife.bind(this, view)
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    recycler_view!!.layoutManager =
      StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
        mLayoutManager = it
      }
    recycler_view!!.adapter = GenreAdapter(genres, this.activity).also { adapter = it }
    recycler_view!!.addItemDecoration(GridSpacingItemDecoration(2, ViewUtil.dpToPx(8), true))
    val animator: ItemAnimator = SlideInUpAnimator()
    animator.addDuration = 300
    recycler_view!!.itemAnimator = animator
    adapter!!.notifyItemRangeInserted(0, adapter!!.itemCount)
  }

  fun getCall(page: Int): Call<ResponseBody> {
    return JAViewer.SERVICE.getActresses(page)
  }
}