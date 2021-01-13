package com.challenge.jungledevs.themoviedb.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.jungledevs.themoviedb.adapters.MoviesListAdapter
import com.challenge.jungledevs.themoviedb.databinding.MoviesListFragmentBinding
import com.challenge.jungledevs.themoviedb.viewmodel.NavigationInterface
import com.challenge.jungledevs.themoviedb.viewmodel.MoviesListViewModel


class MoviesListFragment : Fragment() {
    lateinit var viewModel: MoviesListViewModel
    lateinit var nav: NavigationInterface

    companion object {
        fun newInstance(viewModel: MoviesListViewModel, nav: NavigationInterface): MoviesListFragment {
            val fragment = MoviesListFragment()
            fragment.viewModel = viewModel
            fragment.nav = nav
            return fragment
        }
    }

    override fun onCreateView(@NonNull inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : MoviesListFragmentBinding = MoviesListFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.nav = nav
        binding.recyclerView.adapter =
            MoviesListAdapter(
                emptyList(),
                nav
            )
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }
}