package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prasad on 11/30/2017.
 */

public class POSTPaymentSuccess {

    public class Data {

        @SerializedName("item_amount")
        @Expose
        private String item_amount;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("buyer_id")
        @Expose
        private String buyer_id;
        @SerializedName("currency_type")
        @Expose
        private String currency_type;
        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("gig_image_thumb")
        @Expose
        private String gig_image_thumb;
        @SerializedName("buyername")
        @Expose
        private String buyername;
        @SerializedName("buyerusername")
        @Expose
        private String buyerusername;
        @SerializedName("sellername")
        @Expose
        private String sellername;
        @SerializedName("sellerusername")
        @Expose
        private String sellerusername;
        @SerializedName("gig_price")
        @Expose
        private String gig_price;
        @SerializedName("extra_gig_ref")
        @Expose
        private String extra_gig_ref;
        @SerializedName("extra_gig_dollar")
        @Expose
        private String extra_gig_dollar;

        public String getItem_amount() {
            return item_amount;
        }

        public void setItem_amount(String item_amount) {
            this.item_amount = item_amount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBuyer_id() {
            return buyer_id;
        }

        public void setBuyer_id(String buyer_id) {
            this.buyer_id = buyer_id;
        }

        public String getCurrency_type() {
            return currency_type;
        }

        public void setCurrency_type(String currency_type) {
            this.currency_type = currency_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGig_image_thumb() {
            return gig_image_thumb;
        }

        public void setGig_image_thumb(String gig_image_thumb) {
            this.gig_image_thumb = gig_image_thumb;
        }

        public String getBuyername() {
            return buyername;
        }

        public void setBuyername(String buyername) {
            this.buyername = buyername;
        }

        public String getBuyerusername() {
            return buyerusername;
        }

        public void setBuyerusername(String buyerusername) {
            this.buyerusername = buyerusername;
        }

        public String getSellername() {
            return sellername;
        }

        public void setSellername(String sellername) {
            this.sellername = sellername;
        }

        public String getSellerusername() {
            return sellerusername;
        }

        public void setSellerusername(String sellerusername) {
            this.sellerusername = sellerusername;
        }

        public String getGig_price() {
            return gig_price;
        }

        public void setGig_price(String gig_price) {
            this.gig_price = gig_price;
        }

        public String getExtra_gig_ref() {
            return extra_gig_ref;
        }

        public void setExtra_gig_ref(String extra_gig_ref) {
            this.extra_gig_ref = extra_gig_ref;
        }

        public String getExtra_gig_dollar() {
            return extra_gig_dollar;
        }

        public void setExtra_gig_dollar(String extra_gig_dollar) {
            this.extra_gig_dollar = extra_gig_dollar;
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
