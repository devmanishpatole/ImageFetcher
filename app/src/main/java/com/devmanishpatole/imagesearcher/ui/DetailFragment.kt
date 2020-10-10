package com.devmanishpatole.imagesearcher.ui

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.devmanishpatole.imagesearcher.R
import com.devmanishpatole.imagesearcher.base.BaseFragment
import com.devmanishpatole.imagesearcher.viewmodel.DetailViewModel
import com.devmanishpatole.imagesearcher.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : BaseFragment<DetailViewModel>() {

    private val mainViewModel by activityViewModels<MainViewModel>()

    override val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()

    override fun getLayoutId() = R.layout.fragment_detail

    override fun setupView(view: View) {
        mainViewModel.setAppBarExpanded(true)
        showBackButton()

        val imageData = args.imageData

        activity?.title = imageData.title

        like.text = imageData.ups.toString()
        dislike.text = imageData.downs.toString()
        score.text = imageData.score.toString()

        detailImage.load(imageData.images?.get(0)?.imageLink) {
            placeholder(R.drawable.placeholder)
            error(R.drawable.image_not_available)
            scale(coil.size.Scale.FIT)
        }
    }

}