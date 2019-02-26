package br.com.valber.movie.database.movie;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import java.util.List;

import br.com.valber.movie.entity.Movie;
import br.com.valber.movie.utils.Dao;

public class MovieRepository implements Dao<Movie> {

    private MovieDAO movieDAO;
    private LiveData<List<Movie>> getAll;
    private LiveData<Movie> getMovie;

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
    public void delete(Movie movie) {
        new Delete(movieDAO).execute(movie.getId());
    }

    @Override
    public LiveData<Movie> select(final Movie movie) {
        MutableLiveData<Integer> id = new MutableLiveData<>();
        return Transformations.switchMap(id, userId -> movieDAO.getMovie(movie.getId()));
    }

    @Override
    public void update(Movie movie) {
        new UpdateAsync(movieDAO).execute(movie);
    }

    @Override
    public void deleteAll() {
        new DeleteAlltAsync(movieDAO).execute();
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

    public static class DeleteAlltAsync extends AsyncTask<Void, Void, Void> {

        private MovieDAO movieDAO;

        public DeleteAlltAsync(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(Void... movies) {
            movieDAO.deleteAll();
            return null;
        }
    }

    public static class Delete extends AsyncTask<Integer, Void, Void> {
        private  MovieDAO movieDAO;

        public Delete(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }


        @Override
        protected Void doInBackground(Integer... integers) {
            movieDAO.delete(integers[0]);
            return null;
        }
    }

    public static class GetId extends AsyncTask<Integer, Void, Void> {
        private  MovieDAO movieDAO;

        public GetId(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }


        @Override
        protected Void doInBackground(Integer... integers) {
            movieDAO.getMovie(integers[0]);
            return null;
        }
    }


}
