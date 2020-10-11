package com.devmanishpatole.imagesearcher

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.devmanishpatole.imagesearcher.base.BaseActivity
import com.devmanishpatole.imagesearcher.data.ViralSelection
import com.devmanishpatole.imagesearcher.util.hide
import com.devmanishpatole.imagesearcher.util.show
import com.devmanishpatole.imagesearcher.viewmodel.MainViewModel
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
        viewModel.showCategories.observe(this, { showCategories ->
            if (showCategories) {
                categories.show()
            } else {
                categories.hide()
            }
        })

        viewModel.setAppBarExpanded.observe(this, { expanded ->
            appBar.setExpanded(expanded, true)
        })

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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}