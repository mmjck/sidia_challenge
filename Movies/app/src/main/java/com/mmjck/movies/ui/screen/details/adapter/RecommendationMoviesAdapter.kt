package com.mmjck.movies.ui.screen.details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmjck.movies.R
import com.mmjck.movies.data.model.Movie

class RecommendationMoviesAdapter(
    private val list: List<Movie>,
    var onClicked: (String) -> Unit,
): RecyclerView.Adapter<RecommendationMoviesAdapter.ViewHolder>() {
    private lateinit var context: Context

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageViewPoster: ImageView = itemView.findViewById(R.id.list_recommendation_movie_image_view)
        val cardViewMovieDetails: CardView = itemView.findViewById(R.id.list_recommendation_movie_card_view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie: Movie = list[position]

        val posterImageCompletePath = "https://image.tmdb.org/t/p/w500" + movie.path

        Glide
            .with(context)
            .load(posterImageCompletePath)
            .centerCrop()
            .placeholder(R.drawable.no_image_view_holder)
            .into(holder.imageViewPoster)

        holder.cardViewMovieDetails.setOnClickListener {
            movie.movieId?.let {
                onClicked(it)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_recommendation, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}