package dreamguys.in.co.gigs.network;

import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.GETAllGigs;
import dreamguys.in.co.gigs.Model.GETCategory;
import dreamguys.in.co.gigs.Model.GETCountry;
import dreamguys.in.co.gigs.Model.GETHomeGigs;
import dreamguys.in.co.gigs.Model.GETMyGigs;
import dreamguys.in.co.gigs.Model.GETProfession;
import dreamguys.in.co.gigs.Model.GETState;
import dreamguys.in.co.gigs.Model.POSTAcceptBuyRequest;
import dreamguys.in.co.gigs.Model.POSTAddFav;
import dreamguys.in.co.gigs.Model.POSTBuyNow;
import dreamguys.in.co.gigs.Model.POSTCancelGigs;
import dreamguys.in.co.gigs.Model.POSTChangePassword;
import dreamguys.in.co.gigs.Model.POSTChatHistory;
import dreamguys.in.co.gigs.Model.POSTCreategigs;
import dreamguys.in.co.gigs.Model.POSTDetailGig;
import dreamguys.in.co.gigs.Model.POSTEditGigs;
import dreamguys.in.co.gigs.Model.POSTFavGigs;
import dreamguys.in.co.gigs.Model.POSTForgotPassword;
import dreamguys.in.co.gigs.Model.POSTGigsReview;
import dreamguys.in.co.gigs.Model.POSTLastVisitedGigs;
import dreamguys.in.co.gigs.Model.POSTLeaveFeedback;
import dreamguys.in.co.gigs.Model.POSTLogin;
import dreamguys.in.co.gigs.Model.POSTMessages;
import dreamguys.in.co.gigs.Model.POSTMyActivity;
import dreamguys.in.co.gigs.Model.POSTPaymentSuccess;
import dreamguys.in.co.gigs.Model.POSTPaypalSettings;
import dreamguys.in.co.gigs.Model.POSTPurchaseSeeFedBck;
import dreamguys.in.co.gigs.Model.POSTRegister;
import dreamguys.in.co.gigs.Model.POSTRemoveFav;
import dreamguys.in.co.gigs.Model.POSTSearchGigs;
import dreamguys.in.co.gigs.Model.POSTSellerReviews;
import dreamguys.in.co.gigs.Model.POSTSubCategory;
import dreamguys.in.co.gigs.Model.POSTUserChat;
import dreamguys.in.co.gigs.Model.POSTViewProfile;
import dreamguys.in.co.gigs.Model.POSTVisitGig;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("user/country")
    Call<List<GETCountry>> getCountry();

    @GET("user/state/{stateid}")
    Call<List<GETState>> getState(@Path(value = "stateid", encoded = true) String stateid);

    @Multipart
    @POST("user/registration")
    Call<POSTRegister> postRegister(@Part("email") RequestBody email,
                                    @Part("username") RequestBody username,
                                    @Part("password") RequestBody password,
                                    @Part("state") RequestBody state,
                                    @Part("country") RequestBody country,
                                    @Part("fullname") RequestBody fullname,
                                    @Part("user_timezone") RequestBody user_timezone,
                                    @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<POSTLogin> postLogin(@FieldMap HashMap<String, String> loginData);

    @FormUrlEncoded
    @POST("user/forgot_password")
    Call<POSTForgotPassword> postForgot(@FieldMap HashMap<String, String> forgotPassword);

    @FormUrlEncoded
    @POST("user/change_password")
    Call<POSTChangePassword> postChangePassword(@FieldMap HashMap<String, String> changePassword);

    @GET("user/profession")
    Call<List<GETProfession>> getProfession();

    @GET("gigs/{user_id}")
    Call<GETHomeGigs> getHomeGigs(@Path(value = "user_id", encoded = true) String user_id);

    @FormUrlEncoded
    @POST("user/paypal_setting")
    Call<POSTPaypalSettings> postPaypalSettings(@FieldMap HashMap<String, String> paypalsettings);

    @Multipart
    @POST("user/profile")
    Call<POSTPaypalSettings> postUpdateProfile(@Part("user_id") RequestBody user_id,
                                               @Part("user_contact") RequestBody user_contact,
                                               @Part("user_zip") RequestBody user_zip,
                                               @Part("user_city") RequestBody user_city,
                                               @Part("user_addr") RequestBody user_addr,
                                               @Part("user_desc") RequestBody user_desc,
                                               @Part("country_id") RequestBody country_id,
                                               @Part("state_id") RequestBody state_id,
                                               @Part("profession") RequestBody profession,
                                               @Part("user_name") RequestBody user_name,
                                               @Part("language_tags") RequestBody language_tags,
                                               @Part MultipartBody.Part image);

    @GET("gigs/categories")
    Call<GETCategory> getCategories();

    @GET("gigs/gigs_list/{user_id}")
    Call<GETAllGigs> getAllGigs(@Path(value = "user_id", encoded = true) String stateid);

    @FormUrlEncoded
    @POST("gigs/categories")
    Call<GETAllGigs> getUserGigs(@FieldMap HashMap<String, String> updateProfile);

    @GET("gigs/my_gigs/{user_id}")
    Call<GETMyGigs> getMyGigs(@Path(value = "user_id", encoded = true) String userid);

    @FormUrlEncoded
    @POST("gigs/seller_reviews")
    Call<POSTSellerReviews> getSellerReviews(@FieldMap HashMap<String, String> sellerReviews);

    @FormUrlEncoded
    @POST("gigs/search_gig")
    Call<POSTSearchGigs> postSearchGigs(@FieldMap HashMap<String, String> searchGigs);


    @FormUrlEncoded
    @POST("gigs/seller_buyer_review")
    Call<POSTGigsReview> getGigsReviews(@FieldMap HashMap<String, String> sellerReviews);

    @FormUrlEncoded
    @POST("user/profile_details")
    Call<POSTViewProfile> postViewProfile(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/categories")
    Call<POSTSubCategory> getSubCategory(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/gigs_details")
    Call<POSTDetailGig> getDetailGigs(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/add_favourites")
    Call<POSTAddFav> addFav(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/remove_favourites")
    Call<POSTRemoveFav> removeFav(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/favourites_gigs")
    Call<POSTFavGigs> getFavLists(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/create_gigs")
    Call<POSTCreategigs> createGigs(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/update_gigs")
    Call<POSTCreategigs> updateGigs(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/last_visited_gigs")
    Call<POSTLastVisitedGigs> getLastVisitedGigs(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/last_visit")
    Call<POSTVisitGig> postVisitedGigs(@FieldMap HashMap<String, String> visitedGigs);

    @FormUrlEncoded
    @POST("gigs/edit_gigs")
    Call<POSTEditGigs> editGigs(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/my_gig_activity")
    Call<POSTMyActivity> getMyActivity(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("gigs/buyer_cancel")
    Call<POSTCancelGigs> purchaseCancelGigs(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/purchases_feedback")
    Call<POSTLeaveFeedback> postLeaveFeedback(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/seefeedback")
    Call<POSTPurchaseSeeFedBck> postpurchsseFedBck(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/accept_buyer_request")
    Call<POSTAcceptBuyRequest> postacceptbuyrequest(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/sale_order_status")
    Call<POSTAcceptBuyRequest> postorderStatus(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/withdram_payment_request")
    Call<POSTAcceptBuyRequest> postWithdrawRequest(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/messages")
    Call<POSTMessages> postmessages(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("gigs/chat_details")
    Call<POSTChatHistory> getChatHistory(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/buyer_chat")
    Call<POSTAcceptBuyRequest> postMessage(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/user_chat")
    Call<POSTUserChat> postUserChat(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/save_device_id")
    Call<POSTAcceptBuyRequest> postPushDetails(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/buy_now")
    Call<POSTBuyNow> postBuyingGigsdetails(@FieldMap HashMap<String, String> updateProfile);

    @FormUrlEncoded
    @POST("gigs/paypal_success")
    Call<POSTPaymentSuccess> postPaymentSuccess(@FieldMap HashMap<String, String> updateProfile);
}