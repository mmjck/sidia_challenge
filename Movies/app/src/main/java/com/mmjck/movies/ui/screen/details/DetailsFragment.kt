package com.mmjck.movies.ui.screen.favorites

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mmjck.movies.R

import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmjck.movies.ui.screen.favorites.viewmodel.DetailsViewModel
import com.mmjck.movies.ui.screen.details.adapter.RecommendationMoviesAdapter
import com.mmjck.movies.utils.Functions
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailsFragment: Fragment(R.layout.fragment_movie_details){
    private val viewModel by viewModels<DetailsViewModel>()

    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var favoriteIcon: ImageView
    private lateinit var durationTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.fragment_detail_image)
        titleTextView = view.findViewById(R.id.fragment_detail_title)
        favoriteIcon = view.findViewById(R.id.fragment_detail_ic_favorite)
        durationTextView = view.findViewById(R.id.fragment_detail_duration)
        descriptionTextView = view.findViewById(R.id.fragment_detail_description)
        progressBar = view.findViewById(R.id.fragment_detail_progress)


        favoriteIcon.setOnClickListener {
            toggleFavoriteIcon()
        }


        arguments?.let {
            val movieId = it.getString("movie_id") ?: return@let
            viewModel.getData(movieId)

            bindObservers()
        }

    }

    private fun toggleFavoriteIcon() {
        val movieId = arguments?.getString("movie_id") ?: return

        lifecycleScope.launch {
            viewModel.updateFavorites(movieId).collect { result ->
                Log.e("DATA", result.toString())
                when (result) {
                    is NetworkResult.Loading -> {
                        loadProgress(true)
                    }
                    is NetworkResult.Success -> {
                        loadProgress(false)
                        Toast.makeText(requireContext(), "Adicionado !", Toast.LENGTH_SHORT).show()
                        viewModel.getData(movieId)
                    }
                    is NetworkResult.Failure -> {
                        loadProgress(false)
                        Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun loadData(movie: Movie) {
        titleTextView.text = movie.title
        durationTextView.text = Functions.formatRuntime(movie.runtime)
        descriptionTextView.text = movie.overview

        val posterImageCompletePath = "https://image.tmdb.org/t/p/w500" + movie.path


        Glide
            .with(requireContext())
            .load(posterImageCompletePath)
            .centerCrop()
            .placeholder(R.drawable.no_image_view_holder)
            .into(imageView)

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
                        loadData(it.data)
                        Log.e("DATA", it.data.toString())
                        if (it.data.isFavorite) {
                            favoriteIcon.setImageResource(R.drawable.ic_favorite)
                        } else {
                            favoriteIcon.setImageResource(R.drawable.ic_favorite_border)
                        }

                    }
                    is NetworkResult.Failure -> {
                        loadProgress(false)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.moviesRecommendation.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> {
                    }
                    is NetworkResult.Success -> {
                        loadProgress(false)
                        loadAdapter(it.data.data)
                    }
                    is NetworkResult.Failure -> {
                        loadProgress(false)
                    }
                }
            }
        }
    }

    private fun loadProgress(isLoading: Boolean) {
        val progressIndicator = requireView().findViewById<ProgressBar>(R.id.fragment_detail_progress)
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE

    }


    private fun loadAdapter(movies: List<Movie>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.fragment_home_recommendations_recycler_view)

        val adapter = RecommendationMoviesAdapter(movies) { id ->
            val detailsFragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putString("movie_id", id)
            detailsFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, detailsFragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.adapter = adapter
    }
}
