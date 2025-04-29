package com.mmjck.movies.ui.screen.favorites

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.mmjck.movies.R
import com.mmjck.movies.ui.screen.favorites.viewmodel.FavoritesViewModel

import androidx.recyclerview.widget.LinearLayoutManager
import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mmjck.movies.ui.screen.details.adapter.RecommendationMoviesAdapter
import com.mmjck.movies.ui.screen.home.adapter.FavoritesMoviesAdapter
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavoritesFragment: Fragment(R.layout.fragment_favorites){
    private val viewModel by viewModels<FavoritesViewModel>()
    private lateinit var noDataTextView: View


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getData()

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.fragment_favorites_movie_swipe_refresh)
        noDataTextView = view.findViewById(R.id.fragment_favorites_empty_state)


        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getData()
            swipeRefreshLayout.isRefreshing = false
        }


        bindObservers()

    }

    private fun loadAdapter(movies: List<Movie>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.fragment_favorites_movie_recycler_view)

        val adapter = FavoritesMoviesAdapter(movies) { id ->
            val detailsFragment = DetailsFragment()

            val bundle = Bundle()
            bundle.putString("movie_id", id)
            detailsFragment.arguments = bundle

            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, detailsFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = adapter
    }


    private fun bindObservers() {
        lifecycleScope.launch {
            viewModel.data.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> {
                        loadProgress(true)
                    }
                    is NetworkResult.Success -> {
                        loadProgress(false)
                        Log.e("ERROR", it.data.toString())
                        val recyclerView = view?.findViewById<RecyclerView>(R.id.fragment_favorites_movie_recycler_view)

                        if(it.data.data.isEmpty()){
                            recyclerView?.let {r ->
                                r.visibility = View.GONE
                            }
                            noDataTextView.visibility = View.VISIBLE
                        }else {
                            loadAdapter(it.data.data)

                        }
                    }
                    is NetworkResult.Failure -> {
                        Log.e("ERROR", it.message)
                        loadProgress(false)
                    }
                }
            }
        }
    }

    private fun loadProgress(isLoading: Boolean) {
        val progressIndicator = requireView().findViewById<ProgressBar>(R.id.fragment_favorites_progress)

        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

}
