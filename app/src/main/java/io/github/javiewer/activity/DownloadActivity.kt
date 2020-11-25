package io.github.javiewer.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import io.github.javiewer.JAViewer
import io.github.javiewer.R.id
import io.github.javiewer.R.layout
import io.github.javiewer.adapter.ViewPagerAdapter
import io.github.javiewer.fragment.DownloadFragment

class DownloadActivity : SecureActivity() {
  @BindView(id.download_toolbar)
  var mToolbar: Toolbar? = null

  @BindView(id.download_tabs)
  var mTabLayout: TabLayout? = null

  @BindView(id.download_view_pager)
  var mViewPager: ViewPager? = null
  var keyword: String? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_download)
    ButterKnife.bind(this)
    var bundle = this.intent.extras
    keyword = this.intent.extras.getString("keyword")
    setSupportActionBar(mToolbar)
    supportActionBar!!.title = keyword
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    val adapter = ViewPagerAdapter(supportFragmentManager)
    var fragment: Fragment
    fragment = DownloadFragment()
    bundle = bundle.clone() as Bundle
    bundle.putString("provider", "btso")
    fragment.setArguments(bundle)
    adapter.addFragment(fragment, "BTSO")
    fragment = DownloadFragment()
    bundle = bundle.clone() as Bundle
    bundle.putString("provider", "torrentkitty")
    fragment.setArguments(bundle)
    adapter.addFragment(fragment, "Torrent Kitty")
    mViewPager!!.adapter = adapter
    mTabLayout!!.setupWithViewPager(mViewPager)
    var downloadCounter = JAViewer.CONFIGURATIONS.downloadCounter
    if (downloadCounter == -1L) {
      return
    }
    downloadCounter++
    JAViewer.CONFIGURATIONS.downloadCounter = downloadCounter
    JAViewer.CONFIGURATIONS.save()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }
}