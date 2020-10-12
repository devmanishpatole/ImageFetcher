package com.devmanishpatole.imagesearcher.gallery.list.adapter

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import com.devmanishpatole.imagesearcher.base.BasePagingAdapter
import com.devmanishpatole.imagesearcher.gallery.list.viewholder.GalleryViewHolder
import com.devmanishpatole.imagesearcher.model.ImageData

class GalleryAdapter(parentLifecycle: Lifecycle) :
    BasePagingAdapter<ImageData, GalleryViewHolder>(parentLifecycle, IMAGE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GalleryViewHolder(parent, ::onClick)

    lateinit var onItemClick: (ImageData) -> Unit

    private fun onClick(position: Int) {
        if (::onItemClick.isInitialized) {
            val imageData = getItem(position)
            imageData?.let { onItemClick(it) }
        }
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<ImageData>() {
            override fun areItemsTheSame(
                oldItem: ImageData,
                newItem: ImageData
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ImageData,
                newItem: ImageData
            ) = oldItem == newItem
        }
    }
}