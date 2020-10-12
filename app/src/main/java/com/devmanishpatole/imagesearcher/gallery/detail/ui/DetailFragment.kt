package com.devmanishpatole.imagesearcher.gallery.detail.ui

import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.BlurTransformation
import com.devmanishpatole.imagesearcher.R
import com.devmanishpatole.imagesearcher.base.BaseFragment
import com.devmanishpatole.imagesearcher.gallery.detail.viewmodel.DetailViewModel
import com.devmanishpatole.imagesearcher.gallery.home.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * Shows full image with all meta information about the image.
 */
class DetailFragment : BaseFragment<DetailViewModel>() {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val args by navArgs<DetailFragmentArgs>()

    override val viewModel by viewModels<DetailViewModel>()

    override fun getLayoutId() = R.layout.fragment_detail

    override fun setupView(view: View) {
        // Shows the toolbar expanded
        mainViewModel.setAppBarExpanded(true)

        args.imageData.run {
            like.text = ups.toString()
            dislike.text = downs.toString()
            scoreText.text = score.toString()

            descriptionText.text = description ?: run {
                if (images?.isNotEmpty() == true) {
                    images[0].description
                } else ""
            }

            if (images?.isNotEmpty() == true) {
                detailImage.load(images[0].imageLink) {
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

}