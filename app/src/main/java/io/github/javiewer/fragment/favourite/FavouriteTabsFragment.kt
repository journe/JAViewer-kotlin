package io.github.javiewer.fragment.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import io.github.javiewer.R
import io.github.javiewer.R.layout
import io.github.javiewer.adapter.ViewPagerAdapter
import io.github.javiewer.fragment.ExtendedAppBarFragment

class FavouriteTabsFragment : ExtendedAppBarFragment() {
  @BindView(R.id.favourite_tabs)
  var mTabLayout: TabLayout? = null

  @BindView(R.id.favourite_view_pager)
  var mViewPager: ViewPager? = null
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    mAdapter = ViewPagerAdapter(
        activity!!.supportFragmentManager
    )
    mViewPager!!.adapter = mAdapter
    mTabLayout!!.setupWithViewPager(mViewPager)
    var fragment: FavouriteFragment = FavouriteMovieFragment()
    mAdapter!!.addFragment(fragment, "作品")
    fragment = FavouriteActressFragment()
    mAdapter!!.addFragment(fragment, "女优")
    mAdapter!!.notifyDataSetChanged()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(layout.fragment_favourite, container, false)
    ButterKnife.bind(this, view)
    return view
  }

  companion object {
    var mAdapter: ViewPagerAdapter? = null
    fun update() {
      if (mAdapter != null) {
        for (i in 0 until mAdapter!!.count) {
          (mAdapter!!.getItem(i) as FavouriteFragment).update()
        }
      }
    }
  }
}