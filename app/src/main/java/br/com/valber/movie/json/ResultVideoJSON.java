
package br.com.valber.movie.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultVideoJSON implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieVideoJSON> movieVideoJSONS = null;
    private final static long serialVersionUID = -7433522148958862330L;

    protected ResultVideoJSON(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        movieVideoJSONS = in.createTypedArrayList(MovieVideoJSON.CREATOR);
    }

    public static final Creator<ResultVideoJSON> CREATOR = new Creator<ResultVideoJSON>() {
        @Override
        public ResultVideoJSON createFromParcel(Parcel in) {
            return new ResultVideoJSON(in);
        }

        @Override
        public ResultVideoJSON[] newArray(int size) {
            return new ResultVideoJSON[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieVideoJSON> getMovieVideoJSONS() {
        return movieVideoJSONS;
    }

    public void setMovieVideoJSONS(List<MovieVideoJSON> movieVideoJSONS) {
        this.movieVideoJSONS = movieVideoJSONS;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeTypedList(movieVideoJSONS);
    }
}
