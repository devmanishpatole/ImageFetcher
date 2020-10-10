package com.devmanishpatole.imagesearcher.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry
import coil.load
import com.devmanishpatole.imagesearcher.R
import com.devmanishpatole.imagesearcher.base.BaseItemViewHolder
import com.devmanishpatole.imagesearcher.data.ImageData
import com.devmanishpatole.imagesearcher.viewmodel.GalleryItemViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.android.synthetic.main.grid_element_layout.view.*

class GalleryViewHolder(parent: ViewGroup) :
    BaseItemViewHolder<ImageData, GalleryItemViewModel>(R.layout.grid_element_layout, parent) {

    override lateinit var lifecycleRegistry: LifecycleRegistry

    override lateinit var viewModel: GalleryItemViewModel

    override fun setupView(view: View) {

    }

    override fun bind(data: ImageData) {
        super.bind(data)

        with(itemView) {
            imageView.load(data.images?.get(0)?.imageLink) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.image_not_available)
                scale(coil.size.Scale.FIT)
            }

            imageTitle.text = data.title
        }

    }

    @InstallIn(FragmentComponent::class)
    @EntryPoint
    interface ProviderGalleryItemViewModel {
        fun galleryItemViewModel(): GalleryItemViewModel
    }

    private fun getComicItemViewModel(fragment: Fragment): GalleryItemViewModel {
        val hiltEntryPoint = EntryPointAccessors.fromFragment(
            fragment, ProviderGalleryItemViewModel::class.java
        )
        return hiltEntryPoint.galleryItemViewModel()
    }

    override fun injectDependency() {
        lifecycleRegistry = LifecycleRegistry(this)
        viewModel =
            getComicItemViewModel((itemView.context as ViewComponentManager.FragmentContextWrapper).fragment)
    }

}