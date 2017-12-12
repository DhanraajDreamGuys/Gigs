package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prasad on 11/27/2017.
 */

public class POSTBuyNow {
    public class Data {

        @SerializedName("gig_order_id")
        @Expose
        private Integer gig_order_id;
        @SerializedName("gig_amount")
        @Expose
        private Integer gig_amount;
        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("gig_name")
        @Expose
        private String gig_name;

        public Integer getGig_order_id() {
            return gig_order_id;
        }

        public void setGig_order_id(Integer gig_order_id) {
            this.gig_order_id = gig_order_id;
        }

        public Integer getGig_amount() {
            return gig_amount;
        }

        public void setGig_amount(Integer gig_amount) {
            this.gig_amount = gig_amount;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getGig_name() {
            return gig_name;
        }

        public void setGig_name(String gig_name) {
            this.gig_name = gig_name;
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
