package com.devmanishpatole.imagesearcher.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
            val request = args.request
            viewModel.searchImages(
                PhotoRequest(
                    request.query,
                    request.section,
                    request.includeViral
                )
            )
        }
    }

    override fun setupView(view: View) {
        mainViewModel.showCategories(true)
        observeFilterSelection()
        setupList()
        addLoadStateChangeListener()
        showBackButton()
        setupTitle(args.request)
        observePhotos()
    }

    private fun observePhotos() {
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
    }

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
            findNavController().navigate(
                GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(
                    imageData
                )
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        //Hide filter categories
        mainViewModel.showCategories(false)
    }
}
