package br.com.valber.movie.database.movie;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.valber.movie.entity.Movie;

@Dao
public interface MovieDAO {

    @Insert
    void inset(Movie movie);

    @Update
    void update(Movie movie);

    @Query("DELETE FROM movie")
    void deleteAll();

    @Query("SELECT * FROM movie ORDER BY releaseDate")
    LiveData<List<Movie>> getAll();
}
