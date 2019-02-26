package br.com.valber.movie.database.movie;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import br.com.valber.movie.entity.Movie;
import br.com.valber.movie.utils.Dao;

public class MovieViewModel extends AndroidViewModel{

    private Dao dao;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        dao = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getAll() {
        return dao.getAll();
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public void save(Movie movie){
        dao.save(movie);
    }

    public LiveData<Movie> getMovie(Movie movie) {
        return dao.select(movie);
    }

}
