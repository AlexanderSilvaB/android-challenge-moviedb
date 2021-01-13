package com.challenge.jungledevs.themoviedb.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.challenge.jungledevs.themoviedb.databinding.MovieItemBinding
import com.challenge.jungledevs.themoviedb.model.Movie
import com.challenge.jungledevs.themoviedb.viewmodel.NavigationInterface

class MoviesListAdapter(var items: List<Movie>, var nav: NavigationInterface) : RecyclerView.Adapter<MoviesListAdapter.ViewHolder>(),
    AdapterItemsContract {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: MovieItemBinding = MovieItemBinding.inflate(inflater, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun replaceItems(items: List<*>) {
        this.items = items as List<Movie>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], nav)
    }

    class ViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, nav: NavigationInterface) {
            binding.movie = movie
            binding.nav = nav
            binding.executePendingBindings()
        }
    }
}