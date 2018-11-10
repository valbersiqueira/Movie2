package br.com.valber.movie.database.movie;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.valber.movie.entity.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance;

    public abstract MovieDAO movieDAO();

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class,
                    "movie_databese")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
