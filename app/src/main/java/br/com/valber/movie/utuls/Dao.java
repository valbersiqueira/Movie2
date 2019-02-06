package br.com.valber.movie.utuls;

import android.arch.lifecycle.LiveData;

import java.util.List;

import br.com.valber.movie.entity.Movie;

public interface Dao<T> {

    void save(T t);

    void delete(T t);

    T select(T t);

    void update(T t);

    void deleteAll();

    LiveData<List<T>> getAll();
}
