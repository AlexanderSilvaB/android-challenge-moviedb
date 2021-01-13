package com.challenge.jungledevs.themoviedb.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.challenge.jungledevs.themoviedb.MainActivity
import com.challenge.jungledevs.themoviedb.R
import com.challenge.jungledevs.themoviedb.adapters.MoviesListAdapter
import com.challenge.jungledevs.themoviedb.databinding.MovieDetailsFragmentBinding
import com.challenge.jungledevs.themoviedb.databinding.SearchFragmentBinding
import com.challenge.jungledevs.themoviedb.viewmodel.MoviesListViewModel
import com.challenge.jungledevs.themoviedb.viewmodel.NavigationInterface

class SearchFragment : Fragment() {

    lateinit var viewModel: MoviesListViewModel
    lateinit var nav: NavigationInterface

    companion object {
        fun newInstance(viewModel: MoviesListViewModel, nav: NavigationInterface): SearchFragment {
            val fragment = SearchFragment()
            fragment.viewModel = viewModel
            fragment.nav = nav
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : SearchFragmentBinding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.nav = nav
        binding.recyclerView.adapter =
            MoviesListAdapter(
                emptyList(),
                nav
            )
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if(linearLayoutManager != null &&
                    linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.searchResults.size - 1){
                    viewModel.searchMore()
                }
            }
        })

        viewModel.search("")

        (requireActivity() as MainActivity).findViewById<SearchView>(R.id.search_view).requestFocus()

        return binding.root
    }
}