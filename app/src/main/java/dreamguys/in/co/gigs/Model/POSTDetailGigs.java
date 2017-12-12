package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasad on 10/31/2017.
 */

public class POSTDetailGigs {

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

    public class Datum {

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
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("user_timezone")
        @Expose
        private String user_timezone;
        @SerializedName("verified")
        @Expose
        private String verified;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("zipcode")
        @Expose
        private String zipcode;
        @SerializedName("lang_speaks")
        @Expose
        private String lang_speaks;
        @SerializedName("country_id")
        @Expose
        private String country_id;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("profession")
        @Expose
        private String profession;
        @SerializedName("contact")
        @Expose
        private String contact;
        @SerializedName("description")
        @Expose
        private String description;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getUser_timezone() {
            return user_timezone;
        }

        public void setUser_timezone(String user_timezone) {
            this.user_timezone = user_timezone;
        }

        public String getVerified() {
            return verified;
        }

        public void setVerified(String verified) {
            this.verified = verified;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getLang_speaks() {
            return lang_speaks;
        }

        public void setLang_speaks(String lang_speaks) {
            this.lang_speaks = lang_speaks;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }
}
