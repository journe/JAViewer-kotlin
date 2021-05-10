package io.github.javiewer.view.listener

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import io.github.javiewer.activity.MovieListActivity
import io.github.javiewer.model.entity.MovieDetail

/**
 * Project: JAViewer
 */
class ActressClickListener(
  val actress: MovieDetail.Actress,
  private val mActivity: Activity
) : OnClickListener {
  override fun onClick(v: View) {
    if (actress.detailUrl != null) {
      val intent = Intent(mActivity, MovieListActivity::class.java)
      val bundle = Bundle()
      bundle.putString("title", actress.name + " 的作品")
      bundle.putString("link", actress.detailUrl)
      intent.putExtras(bundle)
      mActivity.startActivity(intent)
    }
  }

}