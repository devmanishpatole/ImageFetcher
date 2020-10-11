package com.devmanishpatole.imagesearcher.gallery.list.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import com.devmanishpatole.imagesearcher.R
import com.devmanishpatole.imagesearcher.gallery.list.adapter.GalleryAdapter
import com.devmanishpatole.imagesearcher.gallery.list.adapter.GalleryLoadStateAdapter
import com.devmanishpatole.imagesearcher.base.BaseFragment
import com.devmanishpatole.imagesearcher.model.ViralSelection
import com.devmanishpatole.imagesearcher.exception.NetworkException
import com.devmanishpatole.imagesearcher.util.ViewUtil
import com.devmanishpatole.imagesearcher.util.hide
import com.devmanishpatole.imagesearcher.util.navigateWithAnim
import com.devmanishpatole.imagesearcher.util.show
import com.devmanishpatole.imagesearcher.gallery.list.viewmodel.GalleryViewModel
import com.devmanishpatole.imagesearcher.gallery.home.viewmodel.MainViewModel
import com.devmanishpatole.imagesearcher.model.PhotoRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * Shows the list of images and filter options to apply filter on result set.
 */
@AndroidEntryPoint
class GalleryFragment : BaseFragment<GalleryViewModel>() {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val args by navArgs<GalleryFragmentArgs>()
    private var viralSelection = ViralSelection.ALL
    private lateinit var galleryAdapter: GalleryAdapter

    override val viewModel by viewModels<GalleryViewModel>()

    override fun getLayoutId() = R.layout.fragment_gallery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (null == savedInstanceState) {
            // Resets filter to all
            mainViewModel.resetViralSelection()

            //Starts searching for images
            args.request.run {
                viewModel.searchImages(
                    PhotoRequest(
                        query,
                        section,
                        includeViral
                    )
                )
            }
        }
    }

    override fun setupView(view: View) {
        initObservers()
        setupList()
        addLoadStateChangeListener()
    }

    private fun initObservers() {
        // Observes the searched photo result and apply selected filter before submitting data to recycler view adapter.
        viewModel.photos.observe(viewLifecycleOwner) {
            when (viralSelection) {
                ViralSelection.ALL -> galleryAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                ViralSelection.VIRAL -> galleryAdapter.submitData(viewLifecycleOwner.lifecycle,
                    it.filter { imageData ->
                        imageData.isViral
                    })
                ViralSelection.NON_VIRAL -> galleryAdapter.submitData(viewLifecycleOwner.lifecycle,
                    it.filter { imageData ->
                        !imageData.isViral
                    })
            }
        }

        // Observes the filter selection updates and refreshes underlying dataset to apply filters.
        mainViewModel.viral.observe(viewLifecycleOwner) {
            // Updates the filter selection only if it updated from previous selection.
            if (it != viralSelection) {
                viralSelection = it
                galleryAdapter.refresh()
            }
        }
    }

    /**
     * Initialises the layout-manager, adapter and item click actions.
     */
    private fun setupList() {
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

        galleryAdapter.onItemClick = { imageData ->
            findNavController().navigateWithAnim(
                GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(
                    imageData.title, imageData
                )
            )
        }
    }

    /**
     * Listens to load state changes.
     */
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
                        mainViewModel.showCategories(true)
                        imageList.show()
                        ViewUtil.hideView(noImages, internetError)
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

    override fun onDestroyView() {
        imageList.adapter = null
        super.onDestroyView()
        //Hide filter categories
        mainViewModel.showCategories(false)
    }
}
