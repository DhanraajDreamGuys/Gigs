package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasad on 11/9/2017.
 */

public class POSTPurchaseSeeFedBck {
    public class Data {

        @SerializedName("user_content")
        @Expose
        private User_content user_content;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("user_feed")
        @Expose
        private List<User_feed> user_feed = null;
        @SerializedName("f_id")
        @Expose
        private String f_id;
        @SerializedName("t_id")
        @Expose
        private String t_id;
        @SerializedName("g_id")
        @Expose
        private String g_id;
        @SerializedName("order_id")
        @Expose
        private String order_id;
        @SerializedName("s_image")
        @Expose
        private String s_image;

        public User_content getUser_content() {
            return user_content;
        }

        public void setUser_content(User_content user_content) {
            this.user_content = user_content;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<User_feed> getUser_feed() {
            return user_feed;
        }

        public void setUser_feed(List<User_feed> user_feed) {
            this.user_feed = user_feed;
        }

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getT_id() {
            return t_id;
        }

        public void setT_id(String t_id) {
            this.t_id = t_id;
        }

        public String getG_id() {
            return g_id;
        }

        public void setG_id(String g_id) {
            this.g_id = g_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getS_image() {
            return s_image;
        }

        public void setS_image(String s_image) {
            this.s_image = s_image;
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
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class User_content {

        @SerializedName("profile_name")
        @Expose
        private String profile_name;
        @SerializedName("profile_image")
        @Expose
        private String profile_image;
        @SerializedName("profile_url")
        @Expose
        private String profile_url;
        @SerializedName("rating")
        @Expose
        private Integer rating;
        @SerializedName("country")
        @Expose
        private String country;

        public String getProfile_name() {
            return profile_name;
        }

        public void setProfile_name(String profile_name) {
            this.profile_name = profile_name;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getProfile_url() {
            return profile_url;
        }

        public void setProfile_url(String profile_url) {
            this.profile_url = profile_url;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

    }

    public class User_feed {

        @SerializedName("fb_user_name")
        @Expose
        private String fb_user_name;
        @SerializedName("fb_user_url")
        @Expose
        private String fb_user_url;
        @SerializedName("fb_user_img")
        @Expose
        private String fb_user_img;
        @SerializedName("fb_user_time")
        @Expose
        private String fb_user_time;
        @SerializedName("fb_user_comment")
        @Expose
        private String fb_user_comment;
        @SerializedName("fb_user_rating")
        @Expose
        private String fb_user_rating;

        public String getFb_user_name() {
            return fb_user_name;
        }

        public void setFb_user_name(String fb_user_name) {
            this.fb_user_name = fb_user_name;
        }

        public String getFb_user_url() {
            return fb_user_url;
        }

        public void setFb_user_url(String fb_user_url) {
            this.fb_user_url = fb_user_url;
        }

        public String getFb_user_img() {
            return fb_user_img;
        }

        public void setFb_user_img(String fb_user_img) {
            this.fb_user_img = fb_user_img;
        }

        public String getFb_user_time() {
            return fb_user_time;
        }

        public void setFb_user_time(String fb_user_time) {
            this.fb_user_time = fb_user_time;
        }

        public String getFb_user_comment() {
            return fb_user_comment;
        }

        public void setFb_user_comment(String fb_user_comment) {
            this.fb_user_comment = fb_user_comment;
        }

        public String getFb_user_rating() {
            return fb_user_rating;
        }

        public void setFb_user_rating(String fb_user_rating) {
            this.fb_user_rating = fb_user_rating;
        }

    }
}
