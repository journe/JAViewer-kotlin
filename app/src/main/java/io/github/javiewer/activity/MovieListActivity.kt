package io.github.javiewer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import io.github.javiewer.R.id
import io.github.javiewer.R.layout
import io.github.javiewer.base.SecureActivity
import io.github.javiewer.fragment.MovieListFragment
import io.github.javiewer.view.ViewUtil

class MovieListActivity constructor() : SecureActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_movie_list)
    val bundle: Bundle = getIntent().getExtras()
    setSupportActionBar(findViewById<View>(id.toolbar) as Toolbar?)
    getSupportActionBar()!!.setTitle(bundle.getString("title"))
    getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
    getSupportActionBar()!!.setElevation(
        ViewUtil.dpToPx(4)
            .toFloat()
    )
    if (savedInstanceState == null) {
      val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
      val fragment: MovieListFragment = MovieListFragment()
      fragment.setArguments(bundle)
      transaction.replace(id.content_query, fragment)
      transaction.commit()
    }
  }

  public override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  companion object {
    fun newIntent(
      context: Context?,
      title: String?,
      link: String?
    ): Intent {
      val intent: Intent = Intent(context, MovieListActivity::class.java)
      val bundle: Bundle = Bundle()
      bundle.putString("title", title)
      bundle.putString("link", link)
      intent.putExtras(bundle)
      return intent
    }
  }
}