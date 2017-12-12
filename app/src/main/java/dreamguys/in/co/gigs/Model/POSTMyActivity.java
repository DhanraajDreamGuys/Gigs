package dreamguys.in.co.gigs.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasad on 11/7/2017.
 */

public class POSTMyActivity {

    public class Data {

        @SerializedName("my_purchases")
        @Expose
        private List<My_purchase> my_purchases = null;
        @SerializedName("my_sale")
        @Expose
        private List<My_sale> my_sale = null;
        @SerializedName("my_payments")
        @Expose
        private List<My_payment> my_payments = null;
        @SerializedName("wallet_balance")
        @Expose
        private String wallet_balance;

        public List<My_purchase> getMy_purchases() {
            return my_purchases;
        }

        public void setMy_purchases(List<My_purchase> my_purchases) {
            this.my_purchases = my_purchases;
        }

        public List<My_sale> getMy_sale() {
            return my_sale;
        }

        public void setMy_sale(List<My_sale> my_sale) {
            this.my_sale = my_sale;
        }

        public List<My_payment> getMy_payments() {
            return my_payments;
        }

        public void setMy_payments(List<My_payment> my_payments) {
            this.my_payments = my_payments;
        }

        public String getWallet_balance() {
            return wallet_balance;
        }

        public void setWallet_balance(String wallet_balance) {
            this.wallet_balance = wallet_balance;
        }

    }

    public class My_payment implements Parcelable {

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
        @SerializedName("delivery_date")
        @Expose
        private String delivery_date;
        @SerializedName("payment_status")
        @Expose
        private String payment_status;
        @SerializedName("decline_accept")
        @Expose
        private String decline_accept;
        @SerializedName("seller_status")
        @Expose
        private String seller_status;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("cancel_accept")
        @Expose
        private String cancel_accept;
        @SerializedName("created_at")
        @Expose
        private String created_at;
        @SerializedName("buyer_status")
        @Expose
        private String buyer_status;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("buyer_name")
        @Expose
        private String buyer_name;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("gig_image_thumb")
        @Expose
        private String gig_image_thumb;
        @SerializedName("withdraw_message")
        @Expose
        private String withdraw_message;

        @SerializedName("withdraw_val")
        @Expose
        private String withdraw_val;


        public String getWithdraw_message() {
            return withdraw_message;
        }

        public void setWithdraw_message(String withdraw_message) {
            this.withdraw_message = withdraw_message;
        }

        public String getWithdraw_val() {
            return withdraw_val;
        }

        public void setWithdraw_val(String withdraw_val) {
            this.withdraw_val = withdraw_val;
        }

        protected My_payment(Parcel in) {
            user_id = in.readString();
            order_id = in.readString();
            created_date = in.readString();
            title = in.readString();
            withdraw_message = in.readString();
            withdraw_val= in.readString();
            gigs_id = in.readString();
            extra_gig_ref = in.readString();
            time_zone = in.readString();
            seller_id = in.readString();
            delivery_date = in.readString();
            payment_status = in.readString();
            decline_accept = in.readString();
            seller_status = in.readString();
            amount = in.readString();
            cancel_accept = in.readString();
            created_at = in.readString();
            buyer_status = in.readString();
            status = in.readString();
            buyer_name = in.readString();
            username = in.readString();
            gig_image_thumb = in.readString();
        }

        public final Creator<My_payment> CREATOR = new Creator<My_payment>() {
            @Override
            public My_payment createFromParcel(Parcel in) {
                return new My_payment(in);
            }

            @Override
            public My_payment[] newArray(int size) {
                return new My_payment[size];
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

        public String getDelivery_date() {
            return delivery_date;
        }

        public void setDelivery_date(String delivery_date) {
            this.delivery_date = delivery_date;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCancel_accept() {
            return cancel_accept;
        }

        public void setCancel_accept(String cancel_accept) {
            this.cancel_accept = cancel_accept;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        public String getBuyer_name() {
            return buyer_name;
        }

        public void setBuyer_name(String buyer_name) {
            this.buyer_name = buyer_name;
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
            dest.writeString(gigs_id);
            dest.writeString(extra_gig_ref);
            dest.writeString(time_zone);
            dest.writeString(seller_id);
            dest.writeString(delivery_date);
            dest.writeString(payment_status);
            dest.writeString(decline_accept);
            dest.writeString(withdraw_val);
            dest.writeString(withdraw_message);
            dest.writeString(seller_status);
            dest.writeString(amount);
            dest.writeString(cancel_accept);
            dest.writeString(created_at);
            dest.writeString(buyer_status);
            dest.writeString(status);
            dest.writeString(buyer_name);
            dest.writeString(username);
            dest.writeString(gig_image_thumb);
        }
    }

    public class My_purchase implements Parcelable {

        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("to_user_id")
        @Expose
        private String to_user_id;
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
        @SerializedName("status_msg_val")
        @Expose
        private Integer status_msg_val;
        @SerializedName("feedback")
        @Expose
        private String feedback;
        @SerializedName("feedback_val")
        @Expose
        private Integer feedback_val;
        @SerializedName("order_cancel")
        @Expose
        private String order_cancel;
        @SerializedName("order_cancel_val")
        @Expose
        private Integer order_cancel_val;

        protected My_purchase(Parcel in) {
            user_id = in.readString();
            to_user_id = in.readString();
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

        public String getTo_user_id() {
            return to_user_id;
        }

        public void setTo_user_id(String to_user_id) {
            this.to_user_id = to_user_id;
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

        public Integer getStatus_msg_val() {
            return status_msg_val;
        }

        public void setStatus_msg_val(Integer status_msg_val) {
            this.status_msg_val = status_msg_val;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public Integer getFeedback_val() {
            return feedback_val;
        }

        public void setFeedback_val(Integer feedback_val) {
            this.feedback_val = feedback_val;
        }

        public String getOrder_cancel() {
            return order_cancel;
        }

        public void setOrder_cancel(String order_cancel) {
            this.order_cancel = order_cancel;
        }

        public Integer getOrder_cancel_val() {
            return order_cancel_val;
        }

        public void setOrder_cancel_val(Integer order_cancel_val) {
            this.order_cancel_val = order_cancel_val;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(user_id);
            dest.writeString(to_user_id);
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

    public class My_sale implements Parcelable {

        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("from_user_id")
        @Expose
        private String from_user_id;
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
        @SerializedName("buyer_name")
        @Expose
        private String buyer_name;
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
        @SerializedName("cancel_reason")
        @Expose
        private String cancel_reason;
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
        @SerializedName("order_cancel_val")
        @Expose
        private String order_cancel_val;
        @SerializedName("feedback_val")
        @Expose
        private String feedback_val;
        @SerializedName("status_msg_val")
        @Expose
        private String status_msg_val;

        public String getOrder_cancel_val() {
            return order_cancel_val;
        }

        public void setOrder_cancel_val(String order_cancel_val) {
            this.order_cancel_val = order_cancel_val;
        }

        public String getFeedback_val() {
            return feedback_val;
        }

        public void setFeedback_val(String feedback_val) {
            this.feedback_val = feedback_val;
        }

        public String getStatus_msg_val() {
            return status_msg_val;
        }

        public void setStatus_msg_val(String status_msg_val) {
            this.status_msg_val = status_msg_val;
        }

        public String getFrom_user_id() {
            return from_user_id;
        }

        public void setFrom_user_id(String from_user_id) {
            this.from_user_id = from_user_id;
        }

        public String getCancel_reason() {
            return cancel_reason;
        }

        public void setCancel_reason(String cancel_reason) {
            this.cancel_reason = cancel_reason;
        }

        protected My_sale(Parcel in) {
            user_id = in.readString();
            from_user_id = in.readString();
            order_id = in.readString();
            cancel_reason = in.readString();
            created_date = in.readString();
            title = in.readString();
            delivery = in.readString();
            buyer_name = in.readString();
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

        public final Creator<My_sale> CREATOR = new Creator<My_sale>() {
            @Override
            public My_sale createFromParcel(Parcel in) {
                return new My_sale(in);
            }

            @Override
            public My_sale[] newArray(int size) {
                return new My_sale[size];
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

        public String getBuyer_name() {
            return buyer_name;
        }

        public void setBuyer_name(String buyer_name) {
            this.buyer_name = buyer_name;
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
            dest.writeString(from_user_id);
            dest.writeString(order_id);
            dest.writeString(created_date);
            dest.writeString(title);
            dest.writeString(cancel_reason);
            dest.writeString(delivery);
            dest.writeString(buyer_name);
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
