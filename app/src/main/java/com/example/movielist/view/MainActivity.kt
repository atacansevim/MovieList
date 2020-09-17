package com.example.movielist.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.movielist.R
import com.example.movielist.adapter.RecyclerViewAdapter
import com.example.movielist.model.Result
import com.example.movielist.service.MovieApi
import com.example.movielist.service.Genre
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener {
    private var api: MovieApi? = null
    private var genreapi: Genre? = null
    private var recyclerViewAdapter : RecyclerViewAdapter? = null
    private var genrelist: ArrayList<com.example.movielist.model.Genre> = kotlin.collections.ArrayList()
    private var resultlist:List<Result> = kotlin.collections.ArrayList()
    var page:Int = 1
    private var swipeContainer: SwipeRefreshLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeContainer = findViewById(R.id.itemsswipetorefresh);
        textView2.text = page.toString()
        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        getSupportActionBar()?.hide()

        swipeContainer?.setOnRefreshListener(OnRefreshListener {//Pull Refresh
            resultlist = emptyList()
            page = 1
            loadData(page=page)
            swipeContainer!!.setRefreshing(false)
            textView2.text = page.toString()
        })

        swipeContainer?.setColorSchemeResources(//Pull Refresh Loading Circle Color Set
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {//Endless Scroolling Request Function
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {

                    if(page + 1 <501 ) {

                        loadData(page=page)
                        page = page + 1
                        textView2.text = page.toString()
                    }
                    else
                    {
                        Toast.makeText(this@MainActivity,"You are already viewing all pages",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        loadGenreData()// GenreList fill function note: Genre ID to Genre Name
        loadData(page)// Load Popular Movie Page by Page

    }

    fun loadGenreData(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        genreapi = retrofit.create(Genre::class.java)// initialize Genre List API

        genreapi?.getGenreName()
            ?.enqueue(object : Callback<com.example.movielist.model.Genres> {
                override fun onResponse(
                    call: Call<com.example.movielist.model.Genres>,
                    response: Response<com.example.movielist.model.Genres>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                           for(genre in responseBody.genres)
                           {
                               val _genre = com.example.movielist.model.Genre(genre.id,genre.name)// get genre list item fill genrelist
                               genrelist.add(_genre)
                           }
                        } else {
                            Log.d("Repository", "Failed to get response")
                        }
                    }
                }

                override fun onFailure(call: Call<com.example.movielist.model.Genres>, t: Throwable) {
                    Log.e("Repository", "onFailure", t)
                }
            })

    }

    fun loadData(page:Int){

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(MovieApi::class.java)// initialize Popular Movie List API

        getPopularMovies(page = page)

    }

    fun getPopularMovies(page: Int) {
        api?.getPopularMovies(page = page)
        ?.enqueue(object : Callback<com.example.movielist.model.Movie> {
                override fun onResponse(
                    call: Call<com.example.movielist.model.Movie>,
                    response: Response<com.example.movielist.model.Movie>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null)
                        {

                            if(page == 1)//initialize
                            {
                                resultlist = responseBody.results
                                recyclerViewAdapter = RecyclerViewAdapter(resultlist,this@MainActivity,genrelist)
                                recyclerView.adapter = recyclerViewAdapter
                            }
                            else if(page == 1 && recyclerViewAdapter != null)// Pull Refresh
                            {
                                resultlist = responseBody.results
                                recyclerViewAdapter?.notifyDataSetChanged()
                            }
                            else//Endless Scrolling
                            {
                                recyclerViewAdapter?.appendMovies(responseBody.results)
                            }

                        }
                        else
                        {
                            Log.d("Repository", "Failed to get response")
                        }
                    }
                }
                override fun onFailure(call: Call<com.example.movielist.model.Movie>, t: Throwable) {
                    Log.e("Repository", "onFailure", t)
                }
            })
    }

    override fun onItemClick(movie: Result) {//Recycler View OnClick Method
        val intent = Intent(this, MovieDetailsActivity::class.java)

        intent.putExtra("poster", movie.backdropPath)
        intent.putExtra("title", movie.title)
        intent.putExtra("year", movie.releaseDate)
        intent.putExtra("overview", movie.overview)
        intent.putExtra("GenreList", genrelist)
        intent.putExtra("GenreIdList",movie.genreIds)

        startActivity(intent)
    }

}





