package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasad on 10/25/2017.
 */

public class GETHomeGigs {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("primary")
    @Expose
    private List<Primary> primary = null;

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

    public List<Primary> getPrimary() {
        return primary;
    }

    public void setPrimary(List<Primary> primary) {
        this.primary = primary;
    }

    public class Primary {

        @SerializedName("base_url")
        @Expose
        private String base_url;
        @SerializedName("popular_gigs_image")
        @Expose
        private List<Popular_gigs_image> popular_gigs_image = null;
        @SerializedName("categories")
        @Expose
        private List<Category> categories = null;
        @SerializedName("popular_gigs_list")
        @Expose
        private List<Popular_gigs_list> popular_gigs_list = null;
        @SerializedName("recent_gigs_list")
        @Expose
        private List<Recent_gigs_list> recent_gigs_list = null;

        public String getBase_url() {
            return base_url;
        }

        public void setBase_url(String base_url) {
            this.base_url = base_url;
        }

        public List<Popular_gigs_image> getPopular_gigs_image() {
            return popular_gigs_image;
        }

        public void setPopular_gigs_image(List<Popular_gigs_image> popular_gigs_image) {
            this.popular_gigs_image = popular_gigs_image;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<Popular_gigs_list> getPopular_gigs_list() {
            return popular_gigs_list;
        }

        public void setPopular_gigs_list(List<Popular_gigs_list> popular_gigs_list) {
            this.popular_gigs_list = popular_gigs_list;
        }

        public List<Recent_gigs_list> getRecent_gigs_list() {
            return recent_gigs_list;
        }

        public void setRecent_gigs_list(List<Recent_gigs_list> recent_gigs_list) {
            this.recent_gigs_list = recent_gigs_list;
        }

    }

    public class Popular_gigs_list {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String user_id;
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

    public class Popular_gigs_image {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image")
        @Expose
        private String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public class Category {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("subcategory")
        @Expose
        private String subcategory;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(String subcategory) {
            this.subcategory = subcategory;
        }

    }

    public class Recent_gigs_list {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String user_id;
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


}
