package com.mmjck.movies.ui.screen.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mmjck.movies.R
import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.ui.screen.favorites.DetailsFragment
import com.mmjck.movies.ui.screen.home.adapter.HomeAdapter
import com.mmjck.movies.ui.screen.home.viewmodel.HomeViewModel
import com.mmjck.movies.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var noDataTextView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getData()

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.fragment_home_swipe_refresh)
        noDataTextView = view.findViewById(R.id.fragment_home_empty_state)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getData()
            swipeRefreshLayout.isRefreshing = false
        }

        bindObservers()
    }

    private fun loadAdapter(movies: List<Movie>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.fragment_home_movie_recycler_view)
         val adapter = HomeAdapter(movies) { id ->
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
                        if(it.data.data.isEmpty()){
                            val recyclerView = view?.findViewById<RecyclerView>(R.id.fragment_home_movie_recycler_view)
                            if (recyclerView != null) {
                                recyclerView.visibility = View.GONE
                            }
                            noDataTextView.visibility = View.VISIBLE

                        }else {
                            loadAdapter(it.data.data)
                        }
                    }
                    is NetworkResult.Failure -> {
                        loadProgress(false)
                    }
                }
            }
        }
    }

    private fun loadProgress(isLoading: Boolean) {
        val progressIndicator = requireView().findViewById<ProgressBar>(R.id.fragment_home_progress)
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}