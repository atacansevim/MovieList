package com.example.movielist.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.example.movielist.R
import com.example.movielist.model.Genre
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_details2.*
import kotlinx.android.synthetic.main.activity_movie_details2.titletextView
import kotlinx.android.synthetic.main.activity_movie_details2.yeartextView


class MovieDetailsActivity : AppCompatActivity() {
    var title: String? = null
    var year: String? ? = null
    var overview: String? = null
    var poster_path: String? ? = null
    var GenreIdList:ArrayList<Int>? = null
    private var genrelist: ArrayList<Genre> = kotlin.collections.ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details2)
        getSupportActionBar()?.hide()

        ///////////Get Intent\\\\\\\\\\\\\\\\\\\\\\
        title = intent.getStringExtra("title")
        year = intent.getStringExtra("year")
        overview = intent.getStringExtra("overview")
        GenreIdList = intent.getIntegerArrayListExtra("GenreIdList")
        genrelist = intent.getSerializableExtra("GenreList") as ArrayList<Genre>
        poster_path = intent.getStringExtra("poster")
        ///////////////Get Intent Finish\\\\\\\\\\\

        ///////////Set Value\\\\\\\\\\\\\\\\\\\\\\
        textView.setText("Summary")
        titletextView.setText(title)
        yeartextView.setText(year)
        overviewTextView.setText(overview)
        Picasso.get().load( "https://image.tmdb.org/t/p/w185/" +poster_path).into(coverImageView)
        genrestextView.setText(genreIdtoGenreName(genrelist!!,GenreIdList!!))
        ///////////////Set Value Finish\\\\\\\\\\\

        textView.movementMethod = ScrollingMovementMethod()

    }
    fun ActionButton(view:View)// back button's Onclik where is top of left side on page
    {
        super.onBackPressed();
    }

    fun genreIdtoGenreName(GenreList:ArrayList<Genre>,GenreIdList:ArrayList<Int>):String
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