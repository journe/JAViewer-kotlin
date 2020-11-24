package io.github.javiewer.activity

import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog.Builder
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.button.MaterialButton
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.AbstractBadgeableDrawerItem
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialize.util.UIUtils
import io.github.javiewer.JAViewer
import io.github.javiewer.R
import io.github.javiewer.R.drawable
import io.github.javiewer.R.id
import io.github.javiewer.R.layout
import io.github.javiewer.fragment.ActressesFragment
import io.github.javiewer.fragment.HomeFragment
import io.github.javiewer.fragment.PopularFragment
import io.github.javiewer.fragment.ReleasedFragment
import io.github.javiewer.fragment.genre.GenreTabsFragment
import io.github.javiewer.network.BasicService
import io.github.javiewer.view.SimpleSearchView
import io.github.javiewer.view.SimpleSearchView.OnQueryTextListener
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.android.synthetic.main.app_bar_main.app_bar
import kotlinx.android.synthetic.main.app_bar_main.search_view
import kotlinx.android.synthetic.main.app_bar_main.toolbar
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

class MainActivity : SecureActivity() {
  var currentFragment: Fragment? = null

  var idOfDrawerItem = ID_HOME
  private var fragmentManager: FragmentManager? = null
  private var mDrawer: Drawer? = null

  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    setSupportActionBar(toolbar)

    ButterKnife.bind(this)
    if (JAViewer.CONFIGURATIONS == null) {
      startActivity(Intent(this, StartActivity::class.java))
      finish()
      return
    }
    JAViewer.recreateService()

    val host: NavHostFragment = supportFragmentManager
        .findFragmentById(R.id.content) as NavHostFragment? ?: return
    val navController = host.navController

    appBarConfiguration = AppBarConfiguration(
        setOf(
            id.nav_home, id.nav_favourite, id.nav_released, id.nav_actresses, id.nav_fav_movie,
            id.nav_popular, id.nav_genre
        ),//顶层导航设置
        drawer_layout
    )
    setupActionBarWithNavController(navController, appBarConfiguration)
    nav_view?.setupWithNavController(navController)

//      if (fragment is ExtendedAppBarFragment) {
//        app_bar!!.elevation = 0f
//      } else {
        app_bar!!.elevation = 4 * resources.displayMetrics.density
//      }

//    mNavigationView.getHeaderView(0)
//        .setOnClickListener {
//          findNavController(R.id.nav_host_fragment).navigate(R.id.login_dest)
//          drawer_layout.closeDrawers()
//        }

//    initFragments()
//    buildDrawer()
  }

  override fun onSupportNavigateUp(): Boolean {
    return findNavController(R.id.content).navigateUp(appBarConfiguration)
  }

  private fun buildDrawer() {
    val result = DrawerBuilder()
        .withActivity(this)
        .withToolbar(toolbar!!)
        .withHeader(layout.drawer_header)
        .addDrawerItems(
            PrimaryDrawerItem().withIdentifier(ID_HOME.toLong())
                .withName("主页")
                .withIcon(drawable.ic_menu_home)
                .withIconTintingEnabled(true),
            PrimaryDrawerItem().withIdentifier(ID_FAV.toLong())
                .withName("收藏夹")
                .withTag("Fav")
                .withIcon(drawable.ic_menu_star)
                .withIconTintingEnabled(true)
                .withSelectable(false),
            DividerDrawerItem(),
            PrimaryDrawerItem().withIdentifier(ID_RELEASED.toLong())
                .withName("已发布")
                .withIcon(drawable.ic_menu_released)
                .withIconTintingEnabled(true),
            PrimaryDrawerItem().withIdentifier(ID_POPULAR.toLong())
                .withName("热门")
                .withIcon(drawable.ic_menu_popular)
                .withIconTintingEnabled(true),
            PrimaryDrawerItem().withIdentifier(ID_ACTRESSES.toLong())
                .withName("女优")
                .withIcon(drawable.ic_menu_actresses)
                .withIconTintingEnabled(true),
            PrimaryDrawerItem().withIdentifier(ID_GENRE.toLong())
                .withName("类别")
                .withIcon(drawable.ic_menu_genre)
                .withIconTintingEnabled(true),
            DividerDrawerItem(),
            PrimaryDrawerItem().withIdentifier(ID_GITHUB.toLong())
                .withName("GitHub")
                .withTag("Github")
                .withIcon(drawable.ic_menu_github)
                .withIconTintingEnabled(true)
                .withSelectable(false)
        )
        .withSelectedItem(ID_HOME.toLong())
        .withOnDrawerItemClickListener { view, position, drawerItem ->
          idOfDrawerItem = drawerItem.identifier.toInt()
          when (idOfDrawerItem) {
            ID_GITHUB -> {
              val intent = Intent(
                  Intent.ACTION_VIEW,
                  Uri.parse("https://github.com/SplashCodes/JAViewer/releases")
              )
              intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
              startActivity(intent)
            }
            ID_FAV -> {
              val intent = Intent(this@MainActivity, FavouriteActivity::class.java)
              startActivity(intent)
            }
            else -> if (drawerItem is AbstractBadgeableDrawerItem<*>) {
              setFragment(idOfDrawerItem, drawerItem.name.text)
            }
          }
          false
        }
        .build()
    val guideline: Guideline = result.header
        .findViewById(id.guideline_status_bar)
    guideline.setGuidelineBegin(UIUtils.getStatusBarHeight(this, true))
    mDrawer = result
    val mTextSource = mDrawer!!.header
        .findViewById<TextView>(id.text_view_source)
    mTextSource.text = JAViewer.CONFIGURATIONS.dataSource
        .toString()
    val mButtonSwitch: MaterialButton = mDrawer!!.header
        .findViewById(id.btn_switch_source)
    mButtonSwitch.setOnClickListener { view: View? -> onSwitchSource() }
    mDrawer!!.setSelection(ID_HOME.toLong())
  }

  private fun initFragments() {
    fragmentManager = supportFragmentManager
    val transaction =
      fragmentManager!!.beginTransaction()
    for (fragmentClass in FRAGMENTS.values) {
      try {
        val fragment =
          fragmentClass!!.getConstructor(*arrayOfNulls(0))
              .newInstance()!!
        transaction.add(id.content, fragment!!, fragmentClass!!.simpleName)
            .hide(fragment)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
    transaction.commit()
    fragmentManager!!.executePendingTransactions()
  }

  private fun setFragment(
    fragment: Fragment?,
    title: CharSequence
  ) {
    supportActionBar!!.title = title
    val old = currentFragment
    if (old === fragment) {
      return
    }
    val transaction = fragmentManager!!.beginTransaction()
    if (old != null) {
      transaction.hide(old)
    }
    transaction.show(fragment!!)
    transaction.commit()
    currentFragment = fragment

  }

  private fun setFragment(
    id: Int,
    title: CharSequence
  ) {
    this.setFragment(
        fragmentManager!!.findFragmentByTag(FRAGMENTS[id]!!.simpleName), title
    )
  }

//  override fun onSaveInstanceState(outState: Bundle) {
//    outState.putString("CurrentFragment", currentFragment::class.java.getSimpleName())
//    outState.putInt("SelectedDrawerItemId", idOfDrawerItem)
//    super.onSaveInstanceState(outState)
//  }

//  override fun onBackPressed() {
//    if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
//      mDrawerLayout!!.closeDrawer(GravityCompat.START)
//      return
//    }
//    if (mSearchView!!.isSearchOpen) {
//      mSearchView!!.closeSearch()
//      return
//    }
//    moveTaskToBack(false)
//  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    val item = menu.findItem(id.action_search)
    search_view!!.setMenuItem(item)
    search_view!!.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        try {
          startActivity(
              MovieListActivity.newIntent(
                  this@MainActivity, "$query 的搜索结果",
                  JAViewer.getDataSource()
                      .getLink()
                      + BasicService.LANGUAGE_NODE
                      + "/search/"
                      + URLEncoder.encode(query, "UTF-8")
              )
          )
        } catch (e: UnsupportedEncodingException) {
          return false
        }
        return true
      }

      override fun onQueryTextChange(newText: String): Boolean {
        return false
      }
    })
    return true
  }

  private fun restart() {
    val intent = intent
    finish()
    startActivity(intent)
  }

  private fun onSwitchSource() {
    val ds =
      JAViewer.DATA_SOURCES.toTypedArray()
    val items = arrayOfNulls<String>(ds.size)
    for (i in ds.indices) {
      items[i] = ds[i]
          .toString()
    }
    val dialog =
      Builder(this)
          .setTitle("选择数据源")
          .setItems(items, OnClickListener { dialog, which ->
            val newSource =
              JAViewer.DATA_SOURCES[which]
            if (newSource == JAViewer.getDataSource()) {
              return@OnClickListener
            }
            JAViewer.CONFIGURATIONS.dataSource = newSource
            JAViewer.CONFIGURATIONS.save()
            restart()
          })
          .create()
    dialog.show()
  }

  companion object {
    const val ID_HOME = 1
    const val ID_FAV = 2
    const val ID_POPULAR = 3
    const val ID_RELEASED = 4
    const val ID_ACTRESSES = 5
    const val ID_GENRE = 6
    const val ID_GITHUB = 7
    val FRAGMENTS: HashMap<Int?, Class<out Fragment?>?> =
      object : HashMap<Int?, Class<out Fragment?>?>() {
        init {
          put(ID_HOME, HomeFragment::class.java)
          put(ID_POPULAR, PopularFragment::class.java)
          put(ID_RELEASED, ReleasedFragment::class.java)
          put(ID_ACTRESSES, ActressesFragment::class.java)
          put(ID_GENRE, GenreTabsFragment::class.java)
        }
      }
  }
}