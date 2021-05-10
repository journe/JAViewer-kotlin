package io.github.javiewer.activity

import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog.Builder
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import io.github.javiewer.JAViewer
import io.github.javiewer.R
import io.github.javiewer.base.SecureActivity
import io.github.javiewer.fragment.actress.ActressesFragment
import io.github.javiewer.fragment.HomeFragment
import io.github.javiewer.fragment.ActressesFragment
import io.github.javiewer.fragment.AllFragment
import io.github.javiewer.fragment.PopularFragment
import io.github.javiewer.fragment.ReleasedFragment
import io.github.javiewer.fragment.genre.GenreTabsFragment
import io.github.javiewer.model.network.BasicService
import io.github.javiewer.view.SimpleSearchView.OnQueryTextListener
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.android.synthetic.main.app_bar_main.search_view
import kotlinx.android.synthetic.main.app_bar_main.toolbar
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

class MainActivity : SecureActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    JAViewer.recreateService()

    val host: NavHostFragment = supportFragmentManager
        .findFragmentById(R.id.content) as NavHostFragment? ?: return
    val navController = host.navController

    appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.nav_home, R.id.nav_favourite, R.id.nav_released, R.id.nav_actresses,
            R.id.nav_fav_movie,
            R.id.nav_popular, R.id.nav_genre
        ),//顶层导航设置
        drawer_layout
    )
    setupActionBarWithNavController(navController, appBarConfiguration)
    nav_view?.setupWithNavController(navController)
    val headerView = nav_view.getHeaderView(0)

    headerView.findViewById<TextView>(R.id.text_view_source).text =
      JAViewer.CONFIGURATIONS.dataSource
          .toString()
    headerView
        .findViewById<TextView>(R.id.btn_switch_source)
        .setOnClickListener { onSwitchSource() }

//    if (fragment is ExtendedAppBarFragment) {
//      app_bar!!.elevation = 0f
//    } else {
//      app_bar!!.elevation = 4 * resources.displayMetrics.density
//    }

//    when (idOfDrawerItem) {
//      ID_GITHUB -> {
//        val intent = Intent(
//            Intent.ACTION_VIEW,
//            Uri.parse("https://github.com/SplashCodes/JAViewer/releases")
//        )
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)
//      }
//      ID_FAV -> {
//        val intent = Intent(this@MainActivity, FavouriteActivity::class.java)
//        startActivity(intent)
//      }
//      else -> if (drawerItem is AbstractBadgeableDrawerItem<*>) {
//        setFragment(idOfDrawerItem, drawerItem.name.text)
//      }
//    }
  }

  override fun onSupportNavigateUp(): Boolean {
    return findNavController(R.id.content).navigateUp(appBarConfiguration)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    val item = menu.findItem(R.id.action_search)
    search_view!!.setMenuItem(item)
    search_view!!.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        try {
          startActivity(
              MovieListActivity.newIntent(
                  this@MainActivity, "$query 的搜索结果",
                  JAViewer.dataSource
                      .link
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
            if (newSource == JAViewer.dataSource) {
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
          put(ID_HOME, AllFragment::class.java)
          put(ID_POPULAR, PopularFragment::class.java)
          put(ID_RELEASED, ReleasedFragment::class.java)
          put(ID_ACTRESSES, ActressesFragment::class.java)
          put(ID_GENRE, GenreTabsFragment::class.java)
        }
      }
  }
}