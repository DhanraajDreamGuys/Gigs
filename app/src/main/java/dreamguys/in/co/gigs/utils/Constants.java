package dreamguys.in.co.gigs.utils;

import java.util.ArrayList;
import java.util.List;

import dreamguys.in.co.gigs.Model.POSTDetailGig;
import dreamguys.in.co.gigs.Model.POSTMyActivity;

/**
 * Created by Prasad on 10/24/2017.
 */

public class Constants {

    public static final String DOLLAR_SIGN = "$";
    public static final String COUNTRY_JSON = "COUNTRY_JSON";
    public static final String TIMEZONE_ID = "TIMEZONE_ID";
    public static final String IS_WELCOME_FIRST_TIME = "IS_WELCOME_FIRST_TIME";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_ID = "USER_ID";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static final String PROFESSION = "PROFESSION";
    public static String BASE_URL;
    public static String CAT_ID = "CAT_ID";
    public static String GIGS_ID = "GIGS_ID";
    public static String SUB_ID = "SUB_ID";
    public static String GIGS_TITLE = "GIGS_TITLE";
    public static String CAT_NAME = "CAT_NAME";
    public static String SUB_CAT_ID = "SUB_CAT_ID";
    public static List<POSTMyActivity.My_purchase> My_Purchase_Array = new ArrayList<POSTMyActivity.My_purchase>();
    public static List<POSTMyActivity.My_sale> My_Sales_Array;
    public static List<POSTMyActivity.My_payment> My_Payment_Array;
    public static List<POSTDetailGig.Review> reviewList;
    public static String MY_PURARRAY_KEY = "MY_PURARRAY_KEY";
    public static String MY_SALEARRAY_KEY = "MY_SALEARRAY_KEY";
    public static String MY_PAYMENTARRAY_KEY = "MY_PAYMENTARRAY_KEY";
    public static String WALLET_BALANCE = "WALLET_BALANCE";
    public static final String NOTIFICATION_IDS = "NOTIFICATION_IDS";
    public static String SUPERFAST_CHARGES = "SUPERFAST_CHARGES";
    public static String SUPERFAST_DAYS = "SUPERFAST_DAYS";
    public static String SUPERFAST_DELIVERY_DESC = "SUPERFAST_DELIVERY_DESC";
    public static String passwordMatch = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])[^\\s]{8,15}$";
    public static String cityMatch = "[a-zA-Z]+";
    public static String numberMatch = "^[0-9]+$";
}
