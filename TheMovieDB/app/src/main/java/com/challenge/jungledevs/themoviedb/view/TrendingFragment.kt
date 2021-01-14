package com.challenge.jungledevs.themoviedb.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.challenge.jungledevs.themoviedb.MainActivity
import com.challenge.jungledevs.themoviedb.R
import com.challenge.jungledevs.themoviedb.extensions.hideKeyboard
import com.challenge.jungledevs.themoviedb.extensions.showKeyboard
import com.challenge.jungledevs.themoviedb.model.Movie
import com.challenge.jungledevs.themoviedb.viewmodel.NavigationInterface
import com.challenge.jungledevs.themoviedb.viewmodel.MoviesListViewModel
import com.google.android.material.appbar.AppBarLayout


class TrendingFragment : Fragment(), NavigationInterface {

    lateinit var viewModel: MoviesListViewModel
    var backToSearch : Boolean = false

    companion object {
        fun newInstance(viewModel: MoviesListViewModel): TrendingFragment {
            val fragment = TrendingFragment()
            fragment.viewModel = viewModel
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true /** true means that the callback is enabled */) {
            override fun handleOnBackPressed() {
                if(backToSearch)
                {
                    loadFragment(SearchFragment.newInstance(viewModel, this@TrendingFragment), "search")
                    backToSearch = false
                }
                else {
                    val fragment =
                        requireActivity().supportFragmentManager.findFragmentByTag("trending")
                    if (fragment == null || !fragment.isVisible) {
                        loadFragment(
                            MoviesListFragment.newInstance(
                                viewModel,
                                this@TrendingFragment
                            ), "trending"
                        )
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
        })

        (requireActivity() as MainActivity).findViewById<SearchView>(R.id.search_view).setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(qString: String): Boolean {
                hideKeyboard()
                viewModel.search(qString)
                return true
            }
        })
    }

    fun showKeyboard(){
        requireActivity().showKeyboard((requireActivity() as MainActivity).findViewById<SearchView>(R.id.search_view))
    }

    fun hideKeyboard(){
        requireActivity().hideKeyboard((requireActivity() as MainActivity).findViewById<SearchView>(R.id.search_view))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trending_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFragment(MoviesListFragment.newInstance(viewModel, this), "trending")
    }

    override fun onStart() {
        super.onStart()
        viewModel.load()
    }

    override fun onMovieClick(movie: Movie) {
        val fragment = requireActivity().supportFragmentManager.findFragmentByTag("search")
        if (fragment != null && fragment.isVisible)
            backToSearch = true


        viewModel.select(movie)
        loadFragment(MovieDetailsFragment.newInstance(viewModel, this), "movie")
    }

    override fun onSearchClick() {
        loadFragment(SearchFragment.newInstance(viewModel, this), "search")
    }

    override fun tryAgain(page: String) {
        if(page == "trending")
            viewModel.load()
        else if(page == "search") {
            val query : String = viewModel.searchQuery.get()!!
            val pageNum : Int = viewModel.searchPage.get()!!
            viewModel.search(query, pageNum)
        }
        else if(page == "movie")
            viewModel.loadSelected {}
    }

    private fun loadFragment(fragment: Fragment, tag: String){
        viewModel.reset()
        if(tag == "trending")
            (requireActivity() as MainActivity).supportActionBar?.hide()
        else
            (requireActivity() as MainActivity).supportActionBar?.show()

        if(tag == "search") {
            (requireActivity() as MainActivity).findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.trending))
            (requireActivity() as MainActivity).findViewById<SearchView>(R.id.search_view).visibility =
                View.VISIBLE
            (requireActivity() as MainActivity).findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true, false)
            showKeyboard()
        }
        else {
            (requireActivity() as MainActivity).findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(Color.TRANSPARENT)
            (requireActivity() as MainActivity).findViewById<SearchView>(R.id.search_view).visibility =
                View.GONE
        }

        val ft = requireActivity().supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.content_view, fragment, tag)
        ft.commit()
    }
}
