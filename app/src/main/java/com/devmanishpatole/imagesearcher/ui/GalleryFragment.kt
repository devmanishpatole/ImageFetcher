package com.devmanishpatole.imagesearcher.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import com.devmanishpatole.imagesearcher.R
import com.devmanishpatole.imagesearcher.adapter.GalleryAdapter
import com.devmanishpatole.imagesearcher.adapter.GalleryLoadStateAdapter
import com.devmanishpatole.imagesearcher.base.BaseFragment
import com.devmanishpatole.imagesearcher.data.PhotoRequest
import com.devmanishpatole.imagesearcher.data.Section
import com.devmanishpatole.imagesearcher.data.ViralSelection
import com.devmanishpatole.imagesearcher.exception.NetworkException
import com.devmanishpatole.imagesearcher.util.hide
import com.devmanishpatole.imagesearcher.util.show
import com.devmanishpatole.imagesearcher.viewmodel.GalleryViewModel
import com.devmanishpatole.imagesearcher.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GalleryFragment : BaseFragment<GalleryViewModel>() {

    private val mainViewModel by activityViewModels<MainViewModel>()

    private val args by navArgs<GalleryFragmentArgs>()

    private var query = ""
    private var viralSelection = ViralSelection.ALL

    private lateinit var galleryAdapter: GalleryAdapter

    companion object {
        private const val QUERY = "QUERY"
    }

    override val viewModel by viewModels<GalleryViewModel>()

    override fun getLayoutId() = R.layout.fragment_gallery

    override fun setupView(view: View) {
        mainViewModel.showCategories(true)
        observeFilterSelection()
        setupList()
        addLoadStateChangeListener()
        showBackButton()

        val request = args.request
        setupTitle(request)
        searchImages(request.query, request.section, request.includeViral)
    }

    private fun setupList(){
        galleryAdapter = GalleryAdapter(viewLifecycleOwner.lifecycle)

        imageList.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.column_size))

        imageList.apply {
            galleryAdapter.apply {
                adapter = this
                adapter = withLoadStateHeaderAndFooter(
                    header = GalleryLoadStateAdapter { retry() },
                    footer = GalleryLoadStateAdapter { retry() }
                )
            }
        }
    }

    private fun observeFilterSelection() {
        mainViewModel.viral.observe(viewLifecycleOwner, {
            if (it != viralSelection) {
                viralSelection = it
                galleryAdapter.refresh()
            }
        })
    }

    private fun setupTitle(request: PhotoRequest) {
        activity?.title = if (request.query.isNotEmpty()) {
            request.query
        } else {
            when (request.section) {
                Section.HOT -> getString(R.string.hot_images)
                Section.TOP -> getString(R.string.top_images)
                Section.USER -> getString(R.string.user_images)
                else -> getString(R.string.hot_images)
            }
        }
    }

    private fun showBackButton() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun addLoadStateChangeListener() {
        galleryAdapter.addLoadStateListener { loadState ->

            when (loadState.source.refresh) {
                // Showing list when success
                is LoadState.NotLoading -> {
                    hideProgressbarOrError()
                    if (galleryAdapter.itemCount == 0) {
                        noImages.show()
                        imageList.hide()
                    } else {
                        imageList.show()
                        noImages.hide()
                        internetError.hide()
                    }
                }
                // Showing progress for load
                is LoadState.Loading -> showProgressbar()
                // Showing no comics in case of error.
                is LoadState.Error -> {
                    hideProgressbarOrError()
                    noImages.show()
                    imageList.hide()
                    if ((loadState.source.refresh as LoadState.Error).error is NetworkException) {
                        internetError.show()
                    }
                }
            }

            // Popping error
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let { showMessage("\uD83D\uDE28 Error ${it.error}") }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainViewModel.showCategories(false)
        outState.putString(QUERY, query)
    }

    private fun searchImages(query: String, section: Section, viral: Boolean) {
        imageList.scrollToPosition(0)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchImages(query, section, viral).collectLatest {
                when (viralSelection) {
                    ViralSelection.ALL -> galleryAdapter.submitData(it)
                    ViralSelection.VIRAL -> galleryAdapter.submitData(
                        it.filter { imageData ->
                            imageData.isViral
                        })
                    ViralSelection.NON_VIRAL -> galleryAdapter.submitData(
                        it.filter { imageData ->
                            !imageData.isViral
                        })
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Hide filter categories
        mainViewModel.showCategories(false)
    }
}
