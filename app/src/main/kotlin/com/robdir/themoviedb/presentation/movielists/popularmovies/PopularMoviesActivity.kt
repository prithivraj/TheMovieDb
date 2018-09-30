package com.robdir.themoviedb.presentation.movielists.popularmovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.robdir.themoviedb.R
import com.robdir.themoviedb.presentation.base.BaseActivity
import com.robdir.themoviedb.presentation.common.TheMovieDbError
import com.robdir.themoviedb.presentation.common.gone
import com.robdir.themoviedb.presentation.common.visible
import com.robdir.themoviedb.presentation.common.visibleIf
import com.robdir.themoviedb.presentation.moviedetails.MovieDetailActivity
import com.robdir.themoviedb.presentation.movielists.common.MovieAdapter
import com.robdir.themoviedb.presentation.movielists.common.MovieModel
import com.robdir.themoviedb.presentation.movielists.searchmovies.SearchMoviesActivity
import kotlinx.android.synthetic.main.activity_popular_movies.*
import kotlinx.android.synthetic.main.layout_no_movies.*
import javax.inject.Inject

class PopularMoviesActivity :
    BaseActivity(),
    MovieAdapter.Callback {

    companion object {
        val TAG: String = PopularMoviesActivity::class.java.simpleName
    }

    // region Injected properties
    @Inject
    lateinit var movieAdapter: MovieAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    // endregion

    private lateinit var popularMoviesViewModel: PopularMoviesViewModel

    // region Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_movies)

        setSupportActionBar(toolbarPopularMovies)
        setupRecyclerViewMovies()

        popularMoviesViewModel =
            ViewModelProviders.of(this, viewModelFactory)[PopularMoviesViewModel::class.java]
                .apply {
                    movies.observe(this@PopularMoviesActivity,
                        Observer<List<MovieModel>> { movies -> displayMovies(movies.orEmpty()) }
                    )

                    isLoading.observe(this@PopularMoviesActivity,
                        Observer<Boolean> { isLoading -> if (isLoading == false) hideProgress() else showProgress() }
                    )

                    error.observe(this@PopularMoviesActivity,
                        Observer<TheMovieDbError> { manageError(R.string.no_popular_movies_error_message) }
                    )

                    networkError.observe(this@PopularMoviesActivity,
                        Observer<TheMovieDbError> { manageError(R.string.network_error_message) }
                    )
                }

        textViewNoMoviesAction.setOnClickListener { loadPopularMovies() }

        loadPopularMovies()
    }
    // endregion

    // region Override methods
    override fun onMovieSelected(movie: MovieModel, imageViewMoviePoster: ImageView) {
        startActivityWithTransitionAnimation(
            MovieDetailActivity.intent(this, movie),
            imageViewMoviePoster
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item?.itemId) {
            R.id.menuItemSearchMovies -> {
                startActivity(SearchMoviesActivity.intent(this@PopularMoviesActivity))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    // endregion

    // region Private methods
    private fun setupRecyclerViewMovies() {
        swipeRefreshLayoutMovies.apply {
            setOnRefreshListener {
                loadPopularMovies(fromSwipeToRefresh = true)
            }
            setColorSchemeResources(R.color.colorAccent)
        }

        recyclerViewMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context)
        }
        movieAdapter.callback = this
    }

    private fun displayMovies(movies: List<MovieModel>) {
        swipeRefreshLayoutMovies.visible()
        swipeRefreshLayoutMovies.isRefreshing = false

        recyclerViewMovies.visible()
        layoutNoPopularMovies.gone()

        movieAdapter.movies = movies
    }

    private fun loadPopularMovies(fromSwipeToRefresh: Boolean = false) {
        popularMoviesViewModel.getPopularMovies(fromSwipeToRefresh)
    }

    private fun showProgress() {
        layoutPopularMoviesProgress.visibleIf(!swipeRefreshLayoutMovies.isRefreshing)
    }

    private fun hideProgress() {
        layoutPopularMoviesProgress.gone()
    }

    private fun manageError(@StringRes messageId: Int) {
        movieAdapter.movies = emptyList()

        swipeRefreshLayoutMovies.gone()
        swipeRefreshLayoutMovies.isRefreshing = false

        layoutNoPopularMovies.visible()
        textViewNoMoviesMessage.setText(messageId)
    }
    // endregion
}
