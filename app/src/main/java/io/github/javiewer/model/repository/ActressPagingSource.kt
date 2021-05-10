package io.github.javiewer.model.repository

import androidx.paging.PagingSource
import io.github.javiewer.JAViewer
import io.github.javiewer.model.entity.MovieDetail.Actress
import io.github.javiewer.model.network.provider.AVMOProvider

/**
 * Created by journey on 2020/12/18.
 */
class ActressPagingSource : PagingSource<Int, Actress>() {
  override suspend fun load(
    params: LoadParams<Int>
  ): LoadResult<Int, Actress> {
    try {
      // Start refresh at page 1 if undefined.
      val nextPageNumber = params.key ?: 1
      val response = JAViewer.SERVICE.getActresses(nextPageNumber)
      return LoadResult.Page(
          data = AVMOProvider.parseActresses(response.string()),
          prevKey = null, // Only paging forward.
          nextKey = nextPageNumber + 1
      )
    } catch (e: Exception) {
      // Handle errors in this block and return LoadResult.Error if it is an
      // expected error (such as a network failure).
      return LoadResult.Error(e)
    }
  }
}