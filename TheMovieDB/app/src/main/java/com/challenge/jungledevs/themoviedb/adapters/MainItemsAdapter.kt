package com.challenge.jungledevs.themoviedb.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.challenge.jungledevs.themoviedb.services.ServiceBuilder
import com.challenge.jungledevs.themoviedb.services.TheMovieDBDataSource
import com.challenge.jungledevs.themoviedb.services.TheMovieDBApi
import com.challenge.jungledevs.themoviedb.view.TrendingFragment
import com.challenge.jungledevs.themoviedb.viewmodel.MoviesListViewModel

class MainItemsAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val HOME = 0
    val TOP = 1
    val SEARCH = 2
    val TRENDING = 3

    val TABS = listOf(HOME, TOP, SEARCH, TRENDING)

    val appContext = context.applicationContext
    val viewModel: MoviesListViewModel = createViewModel()

    fun createViewModel(): MoviesListViewModel {
        val api = ServiceBuilder.buildService(TheMovieDBApi::class.java)
        val dataSource = TheMovieDBDataSource(api)
        return MoviesListViewModel(dataSource, appContext)
    }

    override fun getItem(position: Int): Fragment {
        val fragment = when(position){
            HOME -> TrendingFragment.newInstance(
                viewModel
            )
            TOP -> TrendingFragment.newInstance(
                viewModel
            )
            SEARCH -> TrendingFragment.newInstance(
                viewModel
            )
            TRENDING -> TrendingFragment.newInstance(
                viewModel
            )
            else -> TrendingFragment.newInstance(
                viewModel
            )
        }
        return fragment
    }

    override fun getCount(): Int {
        return  TABS.size
    }

}