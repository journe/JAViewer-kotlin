package io.github.javiewer.adapter.footer

import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycie_item_network_state.view.errorMsg
import kotlinx.android.synthetic.main.recycie_item_network_state.view.progress
import kotlinx.android.synthetic.main.recycie_item_network_state.view.retryButton

class NetworkStateItemViewHolder(
  val view: View,
  private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(view) {

  fun bindData(
    data: LoadState,
    position: Int
  ) {
    // 正在加载，显示进度条
    view.progress.isVisible = data is LoadState.Loading
    // 加载失败，显示并点击重试按钮
    view.retryButton.isVisible = data is LoadState.Error
    view.retryButton.setOnClickListener { retryCallback() }
    // 加载失败显示错误原因
    view.errorMsg.isVisible = !(data as? LoadState.Error)?.error?.message.isNullOrBlank()
    view.errorMsg.text = (data as? LoadState.Error)?.error?.message
  }
}

inline var View.isVisible: Boolean
  get() = visibility == View.VISIBLE
  set(value) {
    visibility = if (value) View.VISIBLE else View.GONE
  }