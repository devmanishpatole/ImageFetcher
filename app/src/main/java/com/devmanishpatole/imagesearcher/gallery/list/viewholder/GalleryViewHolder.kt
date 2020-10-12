package com.devmanishpatole.imagesearcher.gallery.list.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devmanishpatole.imagesearcher.R
import com.devmanishpatole.imagesearcher.base.BaseItemViewHolder
import com.devmanishpatole.imagesearcher.gallery.list.viewmodel.GalleryItemViewModel
import com.devmanishpatole.imagesearcher.model.ImageData
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.android.synthetic.main.grid_element_layout.view.*

class GalleryViewHolder(parent: ViewGroup, private val onItemClick: (Int) -> Unit) :
    BaseItemViewHolder<ImageData, GalleryItemViewModel>(R.layout.grid_element_layout, parent) {

    override lateinit var lifecycleRegistry: LifecycleRegistry

    override lateinit var viewModel: GalleryItemViewModel

    override fun setupView(view: View) {
        // No Implementation
    }

    override fun bind(data: ImageData) {
        super.bind(data)

        with(itemView) {
            if (data.images?.isNotEmpty() == true) {
                imageView.load(data.images[0].imageLink) {
                    placeholder(R.drawable.placeholder)
                    error(R.drawable.image_not_available)
                    scale(coil.size.Scale.FIT)
                }
            } else {
                imageView.load(R.drawable.image_not_available)
            }
            imageTitle.text = data.title

            itemView.setOnClickListener {
                if (layoutPosition != RecyclerView.NO_POSITION) {
                    onItemClick(layoutPosition)
                }
            }
        }
    }

    @InstallIn(FragmentComponent::class)
    @EntryPoint
    interface ProviderGalleryItemViewModel {
        fun galleryItemViewModel(): GalleryItemViewModel
    }

    private fun getImageItemViewModel(fragment: Fragment): GalleryItemViewModel {
        val hiltEntryPoint = EntryPointAccessors.fromFragment(
            fragment, ProviderGalleryItemViewModel::class.java
        )
        return hiltEntryPoint.galleryItemViewModel()
    }

    override fun injectDependency() {
        lifecycleRegistry = LifecycleRegistry(this)
        viewModel =
            getImageItemViewModel((itemView.context as ViewComponentManager.FragmentContextWrapper).fragment)
    }

}