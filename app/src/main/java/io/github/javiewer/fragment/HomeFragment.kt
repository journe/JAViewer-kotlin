package io.github.javiewer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.LoadState
import io.github.javiewer.JAViewer
import io.github.javiewer.R
import io.github.javiewer.adapter.MovieListAdapter
import io.github.javiewer.adapter.footer.FooterAdapter
import io.github.javiewer.model.repository.PokemonFactory
import kotlinx.android.synthetic.main.fragment_home.recycler_view
import kotlinx.android.synthetic.main.fragment_home.refresh_layout
import kotlinx.coroutines.flow.collectLatest

/**
 * Project: JAViewer
 */
class HomeFragment : Fragment() {
  private val viewModel: HomeViewModel by viewModels {
    object : ViewModelProvider.Factory {
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(
            PokemonFactory.makePokemonRepository(JAViewer.SERVICE, JAViewer.DB)
        ) as T
      }
    }
  }
  private val mPokemonAdapter by lazy { MovieListAdapter() }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_home, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    recycler_view.adapter = mPokemonAdapter.withLoadStateFooter(FooterAdapter(mPokemonAdapter))

    lifecycleScope.launchWhenCreated {
      mPokemonAdapter.loadStateFlow.collectLatest { state ->
        refresh_layout.isRefreshing = state.refresh is LoadState.Loading
      }
    }

    viewModel.postOfData()
        .observe(viewLifecycleOwner) {
          mPokemonAdapter.submitData(lifecycle, it)
        }
  }
//  override fun newCall(page: Int): Call<ResponseBody> {
//    return JAViewer.SERVICE.getHomePage(page)
//  }
}