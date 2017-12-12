package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasad on 11/14/2017.
 */

public class POSTChatHistory {
    public static class Chat_detail {

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

    }

    public class Data {

        @SerializedName("total_records")
        @Expose
        private Integer total_records;
        @SerializedName("chat_details")
        @Expose
        private List<Chat_detail> chat_details = null;

        public Integer getTotal_records() {
            return total_records;
        }

        public void setTotal_records(Integer total_records) {
            this.total_records = total_records;
        }

        public List<Chat_detail> getChat_details() {
            return chat_details;
        }

        public void setChat_details(List<Chat_detail> chat_details) {
            this.chat_details = chat_details;
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
