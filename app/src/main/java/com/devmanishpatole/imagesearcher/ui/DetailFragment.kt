package com.devmanishpatole.imagesearcher.ui

import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.BlurTransformation
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

        like.text = imageData.ups.toString()
        dislike.text = imageData.downs.toString()
        score.text = imageData.score.toString()

        description.text = imageData.description?.let {
            it
        } ?: run {
            if (imageData.images?.isNotEmpty() == true) {
                imageData.images?.get(0)?.description
            } else ""
        }

        if (imageData.images?.isNotEmpty() == true) {
            detailImage.load(imageData.images?.get(0)?.imageLink) {
                context?.let { BlurTransformation(it) }
                scale(coil.size.Scale.FIT)
                target(object : coil.target.Target {
                    override fun onSuccess(result: Drawable) {
                        super.onSuccess(result)
                        // use the drawable maybe extract color from it etc.
                        hideProgressbarOrError()
                        detailImage.setImageDrawable(result)
                    }

                    override fun onStart(placeholder: Drawable?) {
                        super.onStart(placeholder)
                        showProgressbar()
                    }

                    override fun onError(error: Drawable?) {
                        super.onError(error)
                        hideProgressbarOrError()
                        detailImage.load(R.drawable.image_not_available)
                    }
                })
            }
        } else {
            detailImage.load(R.drawable.image_not_available)
        }
    }

}