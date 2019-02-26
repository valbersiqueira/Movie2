package br.com.valber.movie.utils;

import android.arch.lifecycle.LiveData;

import java.util.List;


public interface Dao<T> {

    void save(T t);

    void delete(T t);

    LiveData<T> select(T t);

    void update(T t);

    void deleteAll();

    LiveData<List<T>> getAll();
}
