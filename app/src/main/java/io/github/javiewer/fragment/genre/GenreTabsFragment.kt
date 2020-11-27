package io.github.javiewer.fragment.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.github.javiewer.JAViewer
import io.github.javiewer.R.layout
import io.github.javiewer.adapter.ViewPagerAdapter
import io.github.javiewer.fragment.ExtendedAppBarFragment
import io.github.javiewer.model.network.provider.AVMOProvider.parseGenres
import kotlinx.android.synthetic.main.fragment_genre.genre_progress_bar
import kotlinx.android.synthetic.main.fragment_genre.genre_tabs
import kotlinx.android.synthetic.main.fragment_genre.genre_view_pager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreTabsFragment : ExtendedAppBarFragment() {
  var mTabLayout: TabLayout? = null
  var mViewPager: ViewPager? = null
  var mProgressBar: ProgressBar? = null
  var mAdapter: ViewPagerAdapter? = null

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    mAdapter = ViewPagerAdapter(activity?.supportFragmentManager)
    mViewPager!!.adapter = mAdapter
    mTabLayout!!.setupWithViewPager(mViewPager)
    val call = JAViewer.SERVICE.getGenres()
    call.enqueue(object : Callback<ResponseBody> {
      override fun onResponse(
        call: Call<ResponseBody>,
        response: Response<ResponseBody>
      ) {
        mProgressBar!!.visibility = View.GONE
        try {
          val genres = parseGenres(
              response.body()!!
                  .string()
          )
          var fragment: GenreFragment
          for (title in genres.keys) {
            fragment = GenreFragment()
            genres[title]?.let { fragment.genres.addAll(it.toList()) }
            mAdapter!!.addFragment(fragment, title)
          }
          mAdapter!!.notifyDataSetChanged()
          mTabLayout!!.visibility = View.VISIBLE
        } catch (e: Throwable) {
          onFailure(call, e)
        }
      }

      override fun onFailure(
        call: Call<ResponseBody>,
        t: Throwable
      ) {
        t.printStackTrace()
      }
    })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(layout.fragment_genre, container, false)
    mTabLayout = genre_tabs
    mViewPager = genre_view_pager
    mProgressBar = genre_progress_bar
    return view
  }
}