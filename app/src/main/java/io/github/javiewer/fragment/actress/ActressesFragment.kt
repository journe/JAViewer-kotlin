package io.github.javiewer.fragment.actress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.github.javiewer.R
import io.github.javiewer.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_actresses.recycler_view
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ActressesFragment : BaseFragment() {
  val viewModel: ActressesViewModel by viewModels()

  private val itemPagingAdapter by lazy { ActressItemPagingAdapter() }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_actresses, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    recycler_view.adapter = itemPagingAdapter
    lifecycleScope.launch {
      viewModel.actress.collectLatest {
        itemPagingAdapter.submitData(it)
      }
    }

  }
}