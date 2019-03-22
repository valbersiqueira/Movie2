
package br.com.valber.movie.json;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultReviewsJSON implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("results")
    @Expose
    private List<ReviewsJSON> reviewsJSONS = new ArrayList<ReviewsJSON>();
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;
    @SerializedName("total_results")
    @Expose
    private Long totalResults;
    public final static Creator<ResultReviewsJSON> CREATOR = new Creator<ResultReviewsJSON>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ResultReviewsJSON createFromParcel(Parcel in) {
            return new ResultReviewsJSON(in);
        }

        public ResultReviewsJSON[] newArray(int size) {
            return (new ResultReviewsJSON[size]);
        }

    }
    ;

    protected ResultReviewsJSON(Parcel in) {
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.page = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.reviewsJSONS, (ReviewsJSON.class.getClassLoader()));
        this.totalPages = ((Long) in.readValue((Long.class.getClassLoader())));
        this.totalResults = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public ResultReviewsJSON() {
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

    public List<ReviewsJSON> getReviewsJSONS() {
        return reviewsJSONS;
    }

    public void setReviewsJSONS(List<ReviewsJSON> reviewsJSONS) {
        this.reviewsJSONS = reviewsJSONS;
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
        dest.writeList(reviewsJSONS);
        dest.writeValue(totalPages);
        dest.writeValue(totalResults);
    }

    public int describeContents() {
        return  0;
    }

}
