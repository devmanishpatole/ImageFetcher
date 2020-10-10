package com.devmanishpatole.imagesearcher

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
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

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}