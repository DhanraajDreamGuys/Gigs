package dreamguys.in.co.gigs.Model.myactivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class My_purchase implements Parcelable {

        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("order_id")
        @Expose
        private String order_id;
        @SerializedName("created_date")
        @Expose
        private String created_date;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("delivery")
        @Expose
        private String delivery;
        @SerializedName("seller_name")
        @Expose
        private String seller_name;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("pay_status")
        @Expose
        private String pay_status;
        @SerializedName("delivery_date")
        @Expose
        private String delivery_date;
        @SerializedName("gigs_id")
        @Expose
        private String gigs_id;
        @SerializedName("extra_gig_ref")
        @Expose
        private String extra_gig_ref;
        @SerializedName("time_zone")
        @Expose
        private String time_zone;
        @SerializedName("seller_id")
        @Expose
        private String seller_id;
        @SerializedName("payment_status")
        @Expose
        private String payment_status;
        @SerializedName("decline_accept")
        @Expose
        private String decline_accept;
        @SerializedName("seller_status")
        @Expose
        private String seller_status;
        @SerializedName("cancel_accept")
        @Expose
        private String cancel_accept;
        @SerializedName("buyer_status")
        @Expose
        private String buyer_status;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String created_at;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("gig_image_thumb")
        @Expose
        private String gig_image_thumb;
        @SerializedName("order_status")
        @Expose
        private String order_status;
        @SerializedName("feedback")
        @Expose
        private String feedback;
        @SerializedName("order_cancel")
        @Expose
        private String order_cancel;

        public My_purchase(Parcel in) {
            user_id = in.readString();
            order_id = in.readString();
            created_date = in.readString();
            title = in.readString();
            delivery = in.readString();
            seller_name = in.readString();
            amount = in.readString();
            pay_status = in.readString();
            delivery_date = in.readString();
            gigs_id = in.readString();
            extra_gig_ref = in.readString();
            time_zone = in.readString();
            seller_id = in.readString();
            payment_status = in.readString();
            decline_accept = in.readString();
            seller_status = in.readString();
            cancel_accept = in.readString();
            buyer_status = in.readString();
            status = in.readString();
            created_at = in.readString();
            username = in.readString();
            gig_image_thumb = in.readString();
            order_status = in.readString();
            feedback = in.readString();
            order_cancel = in.readString();
        }



        public final Creator<My_purchase> CREATOR = new Creator<My_purchase>() {
            @Override
            public My_purchase createFromParcel(Parcel in) {
                return new My_purchase(in);
            }

            @Override
            public My_purchase[] newArray(int size) {
                return new My_purchase[size];
            }
        };

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getDelivery_date() {
            return delivery_date;
        }

        public void setDelivery_date(String delivery_date) {
            this.delivery_date = delivery_date;
        }

        public String getGigs_id() {
            return gigs_id;
        }

        public void setGigs_id(String gigs_id) {
            this.gigs_id = gigs_id;
        }

        public String getExtra_gig_ref() {
            return extra_gig_ref;
        }

        public void setExtra_gig_ref(String extra_gig_ref) {
            this.extra_gig_ref = extra_gig_ref;
        }

        public String getTime_zone() {
            return time_zone;
        }

        public void setTime_zone(String time_zone) {
            this.time_zone = time_zone;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getDecline_accept() {
            return decline_accept;
        }

        public void setDecline_accept(String decline_accept) {
            this.decline_accept = decline_accept;
        }

        public String getSeller_status() {
            return seller_status;
        }

        public void setSeller_status(String seller_status) {
            this.seller_status = seller_status;
        }

        public String getCancel_accept() {
            return cancel_accept;
        }

        public void setCancel_accept(String cancel_accept) {
            this.cancel_accept = cancel_accept;
        }

        public String getBuyer_status() {
            return buyer_status;
        }

        public void setBuyer_status(String buyer_status) {
            this.buyer_status = buyer_status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getGig_image_thumb() {
            return gig_image_thumb;
        }

        public void setGig_image_thumb(String gig_image_thumb) {
            this.gig_image_thumb = gig_image_thumb;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getOrder_cancel() {
            return order_cancel;
        }

        public void setOrder_cancel(String order_cancel) {
            this.order_cancel = order_cancel;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(user_id);
            dest.writeString(order_id);
            dest.writeString(created_date);
            dest.writeString(title);
            dest.writeString(delivery);

            dest.writeString(seller_name);
            dest.writeString(amount);
            dest.writeString(pay_status);
            dest.writeString(delivery_date);
            dest.writeString(gigs_id);

            dest.writeString(extra_gig_ref);
            dest.writeString(time_zone);
            dest.writeString(seller_id);
            dest.writeString(payment_status);
            dest.writeString(decline_accept);

            dest.writeString(seller_status);
            dest.writeString(cancel_accept);
            dest.writeString(buyer_status);
            dest.writeString(status);
            dest.writeString(created_at);

            dest.writeString(username);
            dest.writeString(gig_image_thumb);
            dest.writeString(order_status);
            dest.writeString(feedback);
            dest.writeString(order_cancel);
        }
    }
