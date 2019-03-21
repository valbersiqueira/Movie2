
package br.com.valber.movie.json;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewsJSON implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;
    @SerializedName("total_results")
    @Expose
    private Long totalResults;
    public final static Creator<ReviewsJSON> CREATOR = new Creator<ReviewsJSON>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ReviewsJSON createFromParcel(Parcel in) {
            return new ReviewsJSON(in);
        }

        public ReviewsJSON[] newArray(int size) {
            return (new ReviewsJSON[size]);
        }

    }
    ;

    protected ReviewsJSON(Parcel in) {
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.page = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.results, (br.com.valber.movie.json.Result.class.getClassLoader()));
        this.totalPages = ((Long) in.readValue((Long.class.getClassLoader())));
        this.totalResults = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public ReviewsJSON() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(page);
        dest.writeList(results);
        dest.writeValue(totalPages);
        dest.writeValue(totalResults);
    }

    public int describeContents() {
        return  0;
    }

}
