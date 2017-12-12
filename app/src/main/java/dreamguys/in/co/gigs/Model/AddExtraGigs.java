package dreamguys.in.co.gigs.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prasad on 11/3/2017.
 */

public class AddExtraGigs {

    @SerializedName("extra_gigs")
    @Expose
    private String extra_gigs;
    @SerializedName("extra_gigs_amount")
    @Expose
    private String extra_gigs_amount;
    @SerializedName("extra_gigs_delivery")
    @Expose
    private String extra_gigs_delivery;


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
