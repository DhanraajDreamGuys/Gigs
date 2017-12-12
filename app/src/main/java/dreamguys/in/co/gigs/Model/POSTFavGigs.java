package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasad on 11/4/2017.
 */

public class POSTFavGigs {
    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("delivering_days")
        @Expose
        private String delivering_days;
        @SerializedName("category_id")
        @Expose
        private String category_id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("gig_price")
        @Expose
        private String gig_price;
        @SerializedName("total_views")
        @Expose
        private String total_views;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state_name")
        @Expose
        private String state_name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("gig_usercount")
        @Expose
        private String gig_usercount;
        @SerializedName("gig_rating")
        @Expose
        private String gig_rating;
        @SerializedName("favourite")
        @Expose
        private String favourite;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDelivering_days() {
            return delivering_days;
        }

        public void setDelivering_days(String delivering_days) {
            this.delivering_days = delivering_days;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGig_price() {
            return gig_price;
        }

        public void setGig_price(String gig_price) {
            this.gig_price = gig_price;
        }

        public String getTotal_views() {
            return total_views;
        }

        public void setTotal_views(String total_views) {
            this.total_views = total_views;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getGig_usercount() {
            return gig_usercount;
        }

        public void setGig_usercount(String gig_usercount) {
            this.gig_usercount = gig_usercount;
        }

        public String getGig_rating() {
            return gig_rating;
        }

        public void setGig_rating(String gig_rating) {
            this.gig_rating = gig_rating;
        }

        public String getFavourite() {
            return favourite;
        }

        public void setFavourite(String favourite) {
            this.favourite = favourite;
        }

    }

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
