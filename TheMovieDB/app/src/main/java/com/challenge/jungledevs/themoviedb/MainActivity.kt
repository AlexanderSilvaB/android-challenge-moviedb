package com.challenge.jungledevs.themoviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.challenge.jungledevs.themoviedb.databinding.ActivityMainBinding
import com.challenge.jungledevs.themoviedb.util.makeStatusBarTransparent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeStatusBarTransparent()

        val binding = DataBindingUtil.setContentView<ActivityMainBinding >(this, R.layout.activity_main)
        binding.handler = this
        binding.manager = supportFragmentManager

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
