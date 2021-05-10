package io.github.javiewer.fragment.actress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import io.github.javiewer.model.repository.ActressPagingSource

/**
 * Created by journey on 2020/12/18.
 */
class ActressesViewModel : ViewModel() {

  val actress = Pager(
      // Configure how data is loaded by passing additional properties to
      // PagingConfig, such as prefetchDistance.
      PagingConfig(pageSize = 20)
  ) {
    ActressPagingSource()
  }.flow.cachedIn(viewModelScope)
}