package io.github.javiewer.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import butterknife.BindColor
import butterknife.BindView
import butterknife.ButterKnife
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation.TitleState.ALWAYS_SHOW
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager
import com.google.android.material.appbar.AppBarLayout
import io.github.javiewer.R.color
import io.github.javiewer.R.id
import io.github.javiewer.R.layout
import io.github.javiewer.R.menu
import io.github.javiewer.adapter.ViewPagerAdapter
import io.github.javiewer.base.SecureActivity
import io.github.javiewer.fragment.favourite.FavouriteActressFragment
import io.github.javiewer.fragment.favourite.FavouriteFragment
import io.github.javiewer.fragment.favourite.FavouriteMovieFragment

class FavouriteActivity : SecureActivity() {
  @BindView(id.favourite_view_pager)
  var mViewPager: AHBottomNavigationViewPager? = null

  @BindView(id.app_bar_fav)
  var mAppBarLayout: AppBarLayout? = null

  @BindView(id.bottom_navigation)
  var mBottomNav: AHBottomNavigation? = null

  @BindView(id.toolbar_fav)
  var mToolbar: Toolbar? = null

  @BindColor(color.colorPrimary)
  var mColorPrimary = 0
  private val mOnPageChangeListener: OnPageChangeListener = object : SimpleOnPageChangeListener() {
    override fun onPageSelected(position: Int) {
      mBottomNav!!.currentItem = position
      mBottomNav!!.restoreBottomNavigation()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_favourite)
    ButterKnife.bind(this)
    setSupportActionBar(mToolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    mAdapter = ViewPagerAdapter(supportFragmentManager)
    mViewPager!!.adapter = mAdapter
    mViewPager!!.setPagingEnabled(true)
    mViewPager!!.addOnPageChangeListener(mOnPageChangeListener)
    var fragment: FavouriteFragment = FavouriteMovieFragment()
    mAdapter!!.addFragment(fragment, "作品")
    fragment = FavouriteActressFragment()
    mAdapter!!.addFragment(fragment, "女优")
    mAdapter!!.notifyDataSetChanged()
    val navigationAdapter = AHBottomNavigationAdapter(this, menu.nav_favourite)
    navigationAdapter.setupWithBottomNavigation(mBottomNav)
    mBottomNav!!.isTranslucentNavigationEnabled = true
    mBottomNav!!.accentColor = mColorPrimary
    mBottomNav!!.titleState = ALWAYS_SHOW
    mBottomNav!!.setOnTabSelectedListener(
        AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
          if (!wasSelected) {
            mViewPager!!.currentItem = position
            return@OnTabSelectedListener true
          }
          false
        })
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  companion object {
    var mAdapter: ViewPagerAdapter? = null
    @JvmStatic fun update() {
      if (mAdapter != null) {
        for (i in 0 until mAdapter!!.count) {
          (mAdapter!!.getItem(i) as FavouriteFragment).update()
        }
      }
    }
  }
}