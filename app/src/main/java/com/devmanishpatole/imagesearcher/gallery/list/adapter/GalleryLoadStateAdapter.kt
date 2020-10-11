package com.devmanishpatole.imagesearcher.gallery.list.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.devmanishpatole.imagesearcher.gallery.list.viewholder.GalleryLoadStateViewHolder

/**
 * Adapter to handle lazy loading buffering and error.
 */
class GalleryLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<GalleryLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: GalleryLoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = GalleryLoadStateViewHolder.create(parent, retry)
}