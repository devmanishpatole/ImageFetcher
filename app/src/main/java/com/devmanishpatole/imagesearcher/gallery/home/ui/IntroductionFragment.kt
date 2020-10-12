package com.devmanishpatole.imagesearcher.gallery.home.ui

import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmanishpatole.imagesearcher.R
import com.devmanishpatole.imagesearcher.base.BaseFragment
import com.devmanishpatole.imagesearcher.gallery.home.viewmodel.IntroductionViewModel
import com.devmanishpatole.imagesearcher.model.PhotoRequest
import com.devmanishpatole.imagesearcher.model.Section
import com.devmanishpatole.imagesearcher.util.navigateWithAnim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_intoduction.*

/**
 * Launch fragment display welcome message and provides various options to user to search
 * or find images under various categories
 */
@AndroidEntryPoint
class IntroductionFragment : BaseFragment<IntroductionViewModel>(), View.OnClickListener {

    override val viewModel by viewModels<IntroductionViewModel>()

    override fun getLayoutId() = R.layout.fragment_intoduction

    override fun setupView(view: View) {
        hotImages.setOnClickListener(this)
        topImages.setOnClickListener(this)
        userImages.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchView.clearFocus()
                    launchSearchImage(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onClick(view: View?) {
        val action = when (view?.id) {
            R.id.hotImages -> {
                IntroductionFragmentDirections.actionIntroductionFragmentToGalleryFragment(
                    getString(R.string.hot_images), PhotoRequest(section = Section.HOT)
                )
            }
            R.id.topImages -> {
                IntroductionFragmentDirections.actionIntroductionFragmentToGalleryFragment(
                    getString(R.string.top_images), PhotoRequest(section = Section.TOP)
                )
            }
            R.id.userImages -> {
                IntroductionFragmentDirections.actionIntroductionFragmentToGalleryFragment(
                    getString(R.string.user_images),
                    PhotoRequest(section = Section.USER, includeViral = checkViral.isChecked)
                )
            }
            else -> null
        }
        action?.let { findNavController().navigateWithAnim(it) }
    }

    private fun launchSearchImage(query: String) {
        val action = IntroductionFragmentDirections.actionIntroductionFragmentToGalleryFragment(
            query, PhotoRequest(query)
        )
        findNavController().navigateWithAnim(action)
    }
}