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

class HomeAdapter(
    private val list: List<Movie>,
    var onClicked: (String) -> Unit,
    ): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var context: Context

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageViewPoster: ImageView = itemView.findViewById(R.id.imageViewPoster)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewReleaseDate: TextView = itemView.findViewById(R.id.textViewReleaseDate)
        val textViewRating: TextView = itemView.findViewById(R.id.textViewRating)
        val textViewTotalVotes: TextView = itemView.findViewById(R.id.textViewTotalVotes)
        val cardViewMovieDetails: CardView = itemView.findViewById(R.id.cardViewMovieDetails)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie: Movie = list[position]

        holder.textViewTitle.text = movie.title
        holder.textViewReleaseDate.text = Functions.getYearFromDate(movie.releaseDate).toString()
        holder.textViewRating.text = movie.voteAverage.toString()
        holder.textViewTotalVotes.text = movie.voteCount.toString()

        val posterImageCompletePath = "https://image.tmdb.org/t/p/w500" + movie.path


        Glide
            .with(context)
            .load(posterImageCompletePath)
            .centerCrop()
            .placeholder(R.drawable.no_image_view_holder)
            .into(holder.imageViewPoster)

        holder.cardViewMovieDetails.setOnClickListener {
            onClicked(movie.id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_home_movies_card_view, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}