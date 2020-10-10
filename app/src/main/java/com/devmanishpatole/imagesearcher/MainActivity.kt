package com.devmanishpatole.imagesearcher

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.devmanishpatole.imagesearcher.base.BaseActivity
import com.devmanishpatole.imagesearcher.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {

    override val viewModel by viewModels<MainViewModel>()

    override fun getLayoutId() = R.layout.activity_main

    override fun setupView(savedInstanceState: Bundle?) {
        //NO IMPLEMENTATION
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}