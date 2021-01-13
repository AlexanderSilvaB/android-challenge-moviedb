package com.challenge.jungledevs.themoviedb.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.jungledevs.themoviedb.adapters.MoviesListAdapter
import com.challenge.jungledevs.themoviedb.databinding.MovieDetailsFragmentBinding
import com.challenge.jungledevs.themoviedb.databinding.MoviesListFragmentBinding
import com.challenge.jungledevs.themoviedb.viewmodel.NavigationInterface
import com.challenge.jungledevs.themoviedb.viewmodel.MoviesListViewModel

class MovieDetailsFragment : Fragment() {

    lateinit var viewModel: MoviesListViewModel
    lateinit var nav: NavigationInterface

    companion object {
        fun newInstance(viewModel: MoviesListViewModel, nav: NavigationInterface): MovieDetailsFragment {
            val fragment = MovieDetailsFragment()
            fragment.viewModel = viewModel
            fragment.nav = nav
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : MovieDetailsFragmentBinding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.nav = nav
        binding.recyclerView.adapter =
            MoviesListAdapter(
                emptyList(),
                nav
            )
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        viewModel.loadSelected()
        return binding.root
    }

}