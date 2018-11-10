package br.com.valber.movie.utuls;

import android.arch.lifecycle.LiveData;

import java.util.List;

public interface Dao<T> {

    void save(T t);

    void update(T t);

    void deleteAll();

    LiveData<List<T>> getAll();
}
