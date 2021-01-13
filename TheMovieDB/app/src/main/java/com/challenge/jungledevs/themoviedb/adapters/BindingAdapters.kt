package com.challenge.jungledevs.themoviedb.adapters

import android.graphics.*
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.challenge.jungledevs.themoviedb.MainActivity
import com.challenge.jungledevs.themoviedb.R
import com.challenge.jungledevs.themoviedb.model.Movie
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso

class BindingAdapters {
    companion object {
        @BindingAdapter("items")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: MutableList<Movie>) {

            recyclerView.adapter.let {
                if (it is AdapterItemsContract) {
                    it.replaceItems(items)
                }
            }
        }

        @BindingAdapter("imageUrl")
        @JvmStatic
        fun setImageUrl(view: ImageView, imageUrl: String?)
        {
            if(imageUrl != null) {
                Picasso
                    .with(view.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.movie_cover)
                    .into(view)
            }
        }

        @BindingAdapter("pager")
        @JvmStatic
        fun setPager(view: TabLayout, pagerView: ViewPager)
        {
            view.setupWithViewPager(pagerView, true)
            disableTabAt(
                view,
                0
            )
            disableTabAt(
                view,
                3
            )
            disableTabAt(
                view,
                2
            )

            var ICONS = listOf(R.drawable.ic_home, R.drawable.ic_trophy, R.drawable.ic_movie, R.drawable.ic_trending)
            for (i in 0..view.tabCount)
            {
                view.getTabAt(i)?.setIcon(ICONS[i])
                if(view.selectedTabPosition == i)
                    view.getTabAt(i)?.icon?.setColorFilter(view.context.resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
                else
                    view.getTabAt(i)?.icon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            }

            view.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.icon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.icon?.setColorFilter(view.context.resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
                }

            })
        }

        @BindingAdapter("handler")
        @JvmStatic
        fun setHandler(view: ViewPager, activity: MainActivity)
        {
            val adapter =
                MainItemsAdapter(
                    view.context,
                    activity.supportFragmentManager
                )
            view.adapter = adapter
            view.currentItem = 1
        }

        fun disableTabAt(tablayout: TabLayout?, index: Int) {
            (tablayout?.getChildAt(0) as? ViewGroup)?.getChildAt(index)?.isEnabled = false
        }
    }
}