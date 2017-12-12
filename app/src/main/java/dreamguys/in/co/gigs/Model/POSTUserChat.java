package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasad on 11/15/2017.
 */

public class POSTUserChat {

    public class Datum {

        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("chat_from")
        @Expose
        private String chat_from;
        @SerializedName("chat_to")
        @Expose
        private String chat_to;
        @SerializedName("chat_time")
        @Expose
        private String chat_time;
        @SerializedName("from_user_name")
        @Expose
        private String from_user_name;
        @SerializedName("to_user_name")
        @Expose
        private String to_user_name;
        @SerializedName("profile_image")
        @Expose
        private String profile_image;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getChat_from() {
            return chat_from;
        }

        public void setChat_from(String chat_from) {
            this.chat_from = chat_from;
        }

        public String getChat_to() {
            return chat_to;
        }

        public void setChat_to(String chat_to) {
            this.chat_to = chat_to;
        }

        public String getChat_time() {
            return chat_time;
        }

        public void setChat_time(String chat_time) {
            this.chat_time = chat_time;
        }

        public String getFrom_user_name() {
            return from_user_name;
        }

        public void setFrom_user_name(String from_user_name) {
            this.from_user_name = from_user_name;
        }

        public String getTo_user_name() {
            return to_user_name;
        }

        public void setTo_user_name(String to_user_name) {
            this.to_user_name = to_user_name;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
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
