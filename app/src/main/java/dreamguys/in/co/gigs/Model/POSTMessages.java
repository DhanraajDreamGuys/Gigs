package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasad on 11/14/2017.
 */

public class POSTMessages {

    public class Data {

        @SerializedName("details")
        @Expose
        private List<Detail> details = null;

        public List<Detail> getDetails() {
            return details;
        }

        public void setDetails(List<Detail> details) {
            this.details = details;
        }

    }

    public class Detail {

        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("profile_image")
        @Expose
        private String profile_image;
        @SerializedName("chat_id")
        @Expose
        private String chat_id;
        @SerializedName("timezone")
        @Expose
        private String timezone;
        @SerializedName("last_message")
        @Expose
        private String last_message;
        @SerializedName("utc_time")
        @Expose
        private String chat_time;
        @SerializedName("chat_timezone")
        @Expose
        private String chat_timezone;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getChat_id() {
            return chat_id;
        }

        public void setChat_id(String chat_id) {
            this.chat_id = chat_id;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getLast_message() {
            return last_message;
        }

        public void setLast_message(String last_message) {
            this.last_message = last_message;
        }

        public String getChat_time() {
            return chat_time;
        }

        public void setChat_time(String chat_time) {
            this.chat_time = chat_time;
        }

        public String getChat_timezone() {
            return chat_timezone;
        }

        public void setChat_timezone(String chat_timezone) {
            this.chat_timezone = chat_timezone;
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

}
