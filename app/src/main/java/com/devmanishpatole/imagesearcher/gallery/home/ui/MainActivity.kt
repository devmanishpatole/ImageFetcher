package com.devmanishpatole.imagesearcher.gallery.home.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.devmanishpatole.imagesearcher.R
import com.devmanishpatole.imagesearcher.base.BaseActivity
import com.devmanishpatole.imagesearcher.model.ViralSelection
import com.devmanishpatole.imagesearcher.util.hide
import com.devmanishpatole.imagesearcher.util.show
import com.devmanishpatole.imagesearcher.gallery.home.viewmodel.MainViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.categories.*

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {

    private lateinit var navController: NavController

    override val viewModel by viewModels<MainViewModel>()

    override fun getLayoutId() = R.layout.activity_main

    override fun setupView(savedInstanceState: Bundle?) {
        initObservers()
        setupNavigationComponent()
        initFilterClickListener()
    }

    private fun initObservers() {
        // Displays search filter categories
        viewModel.showCategories.observe(this, { showCategories ->
            if (showCategories) {
                categories.show()
            } else {
                categories.hide()
            }
        })

        // Expand toolbar - Needed when displaying detail image.
        viewModel.setAppBarExpanded.observe(this, { expanded ->
            appBar.setExpanded(expanded, true)
        })

        // Resets filter selection to All
        viewModel.resetViral.observe(this, {
            all.isChecked = true
        })
    }

    private fun initFilterClickListener() {
        // Listens to category selections and sends category change update.
        categoryGroup.setOnCheckedChangeListener { group, id ->
            val chip = group.findViewById<Chip>(id)
            if (null != chip) {
                when (chip.text.toString()) {
                    getString(R.string.all) -> viewModel.setViral(ViralSelection.ALL)
                    getString(R.string.viral) -> viewModel.setViral(ViralSelection.VIRAL)
                    getString(R.string.non_viral) -> viewModel.setViral(ViralSelection.NON_VIRAL)
                }
            } else {
                all.isChecked = true
            }
        }
    }

    private fun setupNavigationComponent() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Support back arrow and navigation
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}