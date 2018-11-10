package br.com.valber.movie.database.movie;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import br.com.valber.movie.entity.Movie;
import br.com.valber.movie.utuls.Dao;

public class MovieRepository implements Dao<Movie> {

    private MovieDAO movieDAO;
    private LiveData<List<Movie>> getAll;

    public MovieRepository(Application application) {
        MovieDatabase movieDatabase = MovieDatabase.getInstance(application);
        this.movieDAO = movieDatabase.movieDAO();
        this.getAll = movieDAO.getAll();
    }

    @Override
    public void save(Movie movie) {
        new InsertAsync(movieDAO).execute(movie);
    }

    @Override
    public void update(Movie movie) {
        new UpdateAsync(movieDAO).execute(movie);
    }

    @Override
    public void deleteAll() {
        new DelteAlltAsync(movieDAO).execute();
    }

    @Override
    public LiveData<List<Movie>> getAll() {
        return this.getAll;
    }

    public static class InsertAsync extends AsyncTask<Movie, Void, Void> {

        private MovieDAO movieDAO;

        public InsertAsync(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDAO.inset(movies[0]);
            return null;
        }
    }

    public static class UpdateAsync extends AsyncTask<Movie, Void, Void> {

        private MovieDAO movieDAO;

        public UpdateAsync(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDAO.update(movies[0]);
            return null;
        }
    }

    public static class DelteAlltAsync extends AsyncTask<Void, Void, Void> {

        private MovieDAO movieDAO;

        public DelteAlltAsync(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(Void... movies) {
            movieDAO.deleteAll();
            return null;
        }
    }

}
