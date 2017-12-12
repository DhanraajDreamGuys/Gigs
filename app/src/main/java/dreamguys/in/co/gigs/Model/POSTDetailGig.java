package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Prasad on 11/1/2017.
 */

public class POSTDetailGig {

    public class Datum implements Serializable {

        @SerializedName("gigs_details")
        @Expose
        private Gigs_details gigs_details;
        @SerializedName("similar_gigs")
        @Expose
        private List<Similar_gig> similar_gigs = null;
        @SerializedName("reviews")
        @Expose
        private List<Review> reviews = null;

        public Gigs_details getGigs_details() {
            return gigs_details;
        }

        public void setGigs_details(Gigs_details gigs_details) {
            this.gigs_details = gigs_details;
        }

        public List<Similar_gig> getSimilar_gigs() {
            return similar_gigs;
        }

        public void setSimilar_gigs(List<Similar_gig> similar_gigs) {
            this.similar_gigs = similar_gigs;
        }

        public List<Review> getReviews() {
            return reviews;
        }

        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }

    }

    public static class Extra_gig implements Serializable{

        @SerializedName("gigs_id")
        @Expose
        private String gigs_id;
        @SerializedName("extra_gigs")
        @Expose
        private String extra_gigs;
        @SerializedName("extra_gigs_amount")
        @Expose
        private String extra_gigs_amount;
        @SerializedName("extra_gigs_delivery")
        @Expose
        private String extra_gigs_delivery;

        public String getGigs_id() {
            return gigs_id;
        }

        public void setGigs_id(String gigs_id) {
            this.gigs_id = gigs_id;
        }

        public String getExtra_gigs() {
            return extra_gigs;
        }

        public void setExtra_gigs(String extra_gigs) {
            this.extra_gigs = extra_gigs;
        }

        public String getExtra_gigs_amount() {
            return extra_gigs_amount;
        }

        public void setExtra_gigs_amount(String extra_gigs_amount) {
            this.extra_gigs_amount = extra_gigs_amount;
        }

        public String getExtra_gigs_delivery() {
            return extra_gigs_delivery;
        }

        public void setExtra_gigs_delivery(String extra_gigs_delivery) {
            this.extra_gigs_delivery = extra_gigs_delivery;
        }

    }

    public class Gigs_details implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("delivering_days")
        @Expose
        private String delivering_days;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("gig_price")
        @Expose
        private String gig_price;
        @SerializedName("category_id")
        @Expose
        private String category_id;
        @SerializedName("gig_details")
        @Expose
        private String gig_details;
        @SerializedName("super_fast_charges")
        @Expose
        private String super_fast_charges;
        @SerializedName("super_fast_delivery_desc")
        @Expose
        private String super_fast_delivery_desc;

        @SerializedName("super_fast_delivery_checked")
        @Expose
        private Boolean super_fast_delivery_checked;

        @SerializedName("super_fast_days")
        @Expose
        private String super_fast_days;
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
        @SerializedName("user_thumb_image")
        @Expose
        private String user_thumb_image;
        @SerializedName("user_profile_image")
        @Expose
        private String user_profile_image;
        @SerializedName("favourite")
        @Expose
        private String favourite;
        @SerializedName("extra_gigs")
        @Expose
        private List<Extra_gig> extra_gigs = null;

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

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getGig_details() {
            return gig_details;
        }

        public void setGig_details(String gig_details) {
            this.gig_details = gig_details;
        }

        public String getSuper_fast_charges() {
            return super_fast_charges;
        }

        public void setSuper_fast_charges(String super_fast_charges) {
            this.super_fast_charges = super_fast_charges;
        }

        public String getSuper_fast_delivery_desc() {
            return super_fast_delivery_desc;
        }

        public void setSuper_fast_delivery_desc(String super_fast_delivery_desc) {
            this.super_fast_delivery_desc = super_fast_delivery_desc;
        }

        public Boolean getSuper_fast_delivery_checked() {
            return super_fast_delivery_checked;
        }

        public void setSuper_fast_delivery_checked(Boolean super_fast_delivery_checked) {
            this.super_fast_delivery_checked = super_fast_delivery_checked;
        }

        public String getSuper_fast_days() {
            return super_fast_days;
        }

        public void setSuper_fast_days(String super_fast_days) {
            this.super_fast_days = super_fast_days;
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

        public String getUser_thumb_image() {
            return user_thumb_image;
        }

        public void setUser_thumb_image(String user_thumb_image) {
            this.user_thumb_image = user_thumb_image;
        }

        public String getFavourite() {
            return favourite;
        }

        public void setFavourite(String favourite) {
            this.favourite = favourite;
        }

        public String getUser_profile_image() {
            return user_profile_image;
        }

        public void setUser_profile_image(String user_profile_image) {
            this.user_profile_image = user_profile_image;
        }

        public List<Extra_gig> getExtra_gigs() {
            return extra_gigs;
        }

        public void setExtra_gigs(List<Extra_gig> extra_gigs) {
            this.extra_gigs = extra_gigs;
        }

    }

    public class Review {

        @SerializedName("gig_id")
        @Expose
        private String gig_id;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("time_zone")
        @Expose
        private String time_zone;
        @SerializedName("created_date")
        @Expose
        private String created_date;
        @SerializedName("to_user_id")
        @Expose
        private String to_user_id;
        @SerializedName("from_user_id")
        @Expose
        private String from_user_id;
        @SerializedName("sellername")
        @Expose
        private String sellername;
        @SerializedName("buyername")
        @Expose
        private String buyername;

        public String getGig_id() {
            return gig_id;
        }

        public void setGig_id(String gig_id) {
            this.gig_id = gig_id;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getTime_zone() {
            return time_zone;
        }

        public void setTime_zone(String time_zone) {
            this.time_zone = time_zone;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getTo_user_id() {
            return to_user_id;
        }

        public void setTo_user_id(String to_user_id) {
            this.to_user_id = to_user_id;
        }

        public String getFrom_user_id() {
            return from_user_id;
        }

        public void setFrom_user_id(String from_user_id) {
            this.from_user_id = from_user_id;
        }

        public String getSellername() {
            return sellername;
        }

        public void setSellername(String sellername) {
            this.sellername = sellername;
        }

        public String getBuyername() {
            return buyername;
        }

        public void setBuyername(String buyername) {
            this.buyername = buyername;
        }

    }

    public class Similar_gig {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("delivering_days")
        @Expose
        private String delivering_days;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("gig_price")
        @Expose
        private String gig_price;
        @SerializedName("category_id")
        @Expose
        private String category_id;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("gig_usercount")
        @Expose
        private String gig_usercount;
        @SerializedName("gig_rating")
        @Expose
        private String gig_rating;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state_name")
        @Expose
        private String state_name;
        @SerializedName("fullname")
        @Expose
        private String fullname;
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

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
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

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
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
