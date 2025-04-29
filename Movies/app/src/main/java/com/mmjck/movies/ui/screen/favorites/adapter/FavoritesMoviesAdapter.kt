package com.mmjck.movies.ui.screen.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmjck.movies.R
import com.mmjck.movies.data.model.Movie
import com.mmjck.movies.utils.Functions

class FavoritesMoviesAdapter(
    private val list: List<Movie>,
    var onClicked: (String) -> Unit
) : RecyclerView.Adapter<FavoritesMoviesAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewPoster: ImageView = view.findViewById(R.id.fragment_favorites_image)
        val textViewTitle: TextView = view.findViewById(R.id.fragment_favorites_title)
        val textViewReleaseDate: TextView = view.findViewById(R.id.fragment_favorites_date)
        val textViewRating: TextView = view.findViewById(R.id.fragment_favorites_text_rating)
        val textViewTotalVotes: TextView = view.findViewById(R.id.fragment_favorites_total_votes)
        val cardViewMovieDetails: CardView = view.findViewById(R.id.cardViewMovieDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.fragment_favorites_movies_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = list[position]

        holder.textViewTitle.text = movie.title
        holder.textViewReleaseDate.text = Functions.getYearFromDate(movie.releaseDate).toString()
        holder.textViewRating.text = movie.voteAverage.toString()
        holder.textViewTotalVotes.text = movie.voteCount.toString()

        val posterImageCompletePath = "https://image.tmdb.org/t/p/w500${movie.path}"

        Glide.with(context)
            .load(posterImageCompletePath)
            .centerCrop()
            .placeholder(R.drawable.no_image_view_holder)
            .into(holder.imageViewPoster)

        holder.cardViewMovieDetails.setOnClickListener {
            movie.movieId?.let { id -> onClicked(id) }
        }
    }

    override fun getItemCount(): Int = list.size
}
