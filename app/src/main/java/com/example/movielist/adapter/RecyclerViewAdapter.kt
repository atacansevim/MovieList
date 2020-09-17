package com.example.movielist.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.model.Genre
import com.example.movielist.model.Result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_layout.view.*

class RecyclerViewAdapter(private var MovieList: List<Result>, private val listener: Listener, private val genrelist:ArrayList<Genre>) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.RowHolder {// Determines what adapter  will show?


            var view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
            return RowHolder(view)

    }


    interface Listener{// For Clickable item
        fun onItemClick(movie: Result)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var genres_text: String = ""
        fun bind(movie:Result,listener: Listener,genrelist:ArrayList<Genre>)
        {
            itemView.setOnClickListener { listener.onItemClick(movie) }

            Picasso.get().load( "https://image.tmdb.org/t/p/w185/" + movie.posterPath).into(itemView.posterimageView)
            var year = movie.releaseDate.split("-")
            itemView.yeartextView.text = year[0]
            itemView.titletextView.text = movie.title
            itemView.ratetextView.text = movie.voteAverage.toString()

            genres_text = genreIdtoGenreName(genrelist,movie.genreIds)
            itemView.genretextView.setText(genres_text)

        }

        fun genreIdtoGenreName(GenreList:ArrayList<Genre>,GenreIdList:ArrayList<Int>):String// Obtained Genre Id convert to Genre Name using this function
        {
            var _genre_list = ""
            for (genre in GenreIdList) {

                for(genre_name in GenreList)
                {
                    if(genre == genre_name.id)
                    {
                        if(GenreIdList.indexOf(genre) + 1 != GenreIdList.size)
                            _genre_list += genre_name.name + ", "
                        else
                            _genre_list += genre_name.name
                    }
                }

            }
            return  _genre_list
        }
    }


    override fun getItemCount(): Int {
        return MovieList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.RowHolder, position: Int) {

        holder.bind(MovieList[position],listener,genrelist)

    }

    fun appendMovies(moviesToAppend: List<Result>) {
        MovieList = MovieList + moviesToAppend
        notifyDataSetChanged()
    }


}



