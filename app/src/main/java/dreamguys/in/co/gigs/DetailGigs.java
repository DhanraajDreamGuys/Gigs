package dreamguys.in.co.gigs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import dreamguys.in.co.gigs.Model.GETProfession;
import dreamguys.in.co.gigs.Model.POSTAddFav;
import dreamguys.in.co.gigs.Model.POSTDetailGig;
import dreamguys.in.co.gigs.Model.POSTRemoveFav;
import dreamguys.in.co.gigs.Model.POSTVisitGig;
import dreamguys.in.co.gigs.adapter.DetailGigsReviewAdapter;
import dreamguys.in.co.gigs.adapter.HorizontalRecommendedGigsAdapter;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 10/31/2017.
 */

public class DetailGigs extends AppCompatActivity implements View.OnClickListener {

    TextView toolTitle, username, gigsTitle, userprofession, gigsDesc, gigsNos, gigsCountry, gigsUserCount, gigsSpeaks, gigsReviews;
    Toolbar toolbar;
    String gigs_title = "", gigs_id = "";
    ImageView mGigsImages, profileUserImage, inputDescShow, inputDescHide, inputGigsFav;
    Button orderNow;
    CustomProgressDialog mCustomProgressDialog;
    HashMap<String, String> postGigDetails = new HashMap<String, String>();
    HashMap<String, String> postVisitGigs = new HashMap<String, String>();
    Gson gson;
    private GETProfession[] getProfession;
    private RecyclerView horizontalRecommendedGigs;
    DetailGigsReviewAdapter detailGigsReviewAdapter;
    private RatingBar gigsRating;
    private int sum;
    private ListView mDetailGigsReview;
    private RelativeLayout GigsReviews, GigsSimilar, GigsExtras;
    private LinearLayout inputGigsExtras, inputGigsSuperFastExtras, inputGigsUserCount, inputGigsTotalViews, inputGigsUserCountry, inputGigsUserSpeaks, inputGigsUserProfile;
    private ArrayList<POSTDetailGig.Extra_gig> OrderGigs = new ArrayList<>();
    private String seller_gigs_user_id = "";
    private POSTDetailGig.Gigs_details mGigs_details;
    private String SUPERFAST_DESC, SUPERFAST_DAYS, SUPERFAST_CHARGES;
    private HashMap<String, String> postDetails = new HashMap<String, String>();
    //Dynamic Extras view
    CheckBox inputCheckBox, inputSuperFastCheckBox;
    TextView inputCost, inputSuperFastCost, inputSimilarGigs;
    String gigsUserId = "", deliveryDays = "";

    private Boolean isFav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gigs);
        mCustomProgressDialog = new CustomProgressDialog(this);

        gson = new Gson();
        gigs_id = getIntent().getStringExtra(Constants.GIGS_ID);
        gigs_title = getIntent().getStringExtra(Constants.GIGS_TITLE);
        initLayouts();
        getProfession = gson.fromJson(SessionHandler.getInstance().get(DetailGigs.this, Constants.PROFESSION), GETProfession[].class);
        postGigDetails.put("gig_id", gigs_id);
        postVisitGigs.put("gig_id", gigs_id);
        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID) != null) {
                postVisitGigs.put("user_id",SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID));
                postGigDetails.put("userid", SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID));
                postVisitGigs();
            } else {
                postGigDetails.put("userid", "");
            }
            getDetailGig();

        } else {
            Utils.toastMessage(DetailGigs.this, getString(R.string.err_internet_connection));
        }


        inputGigsFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDetails.put("user_id", SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID));
                if (isFav) {
                    removeFavAPI();
                    isFav = false;
                } else {
                    isFav = true;
                    addFavAPI();
                }
            }
        });
    }

    private void postVisitGigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postVisitedGigs(postVisitGigs).enqueue(new Callback<POSTVisitGig>() {
            @Override
            public void onResponse(Call<POSTVisitGig> call, Response<POSTVisitGig> response) {
                if (response.body().getCode().equals(200)) {
                    Log.d("TAG", response.body().getMessage());
                } else {
                    Log.d("TAG", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<POSTVisitGig> call, Throwable t) {

            }
        });
    }


    private void getDetailGig() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getDetailGigs(postGigDetails).enqueue(new Callback<POSTDetailGig>() {
            @Override
            public void onResponse(Call<POSTDetailGig> call, final Response<POSTDetailGig> response) {
                if (response.body().getCode().equals(200)) {
                    if (response.body().getData().size() > 0) {
                        POSTDetailGig.Gigs_details gigs_details = response.body().getData().get(0).getGigs_details();

                        sellerLogin(response);

                        Picasso.with(DetailGigs.this).load(Utils.BASEURL + gigs_details.getImage()).placeholder(R.drawable.no_image).into(mGigsImages);
                        Picasso.with(DetailGigs.this).load(Utils.BASEURL + gigs_details.getUser_thumb_image()).placeholder(R.drawable.no_image).into(profileUserImage);
                        username.setText(gigs_details.getFullname());
                        for (int i = 0; i < getProfession.length; i++) {
                            if (getProfession[i].getId().equalsIgnoreCase(gigs_details.getProfession())) {
                                userprofession.setText(getProfession[i].getProfession_name());
                            }
                        }
                        if (!gigs_details.getCountry().isEmpty()) {
                            gigsCountry.setText(gigs_details.getCountry());
                        } else {
                            inputGigsUserCountry.setVisibility(View.GONE);
                        }
                        if (!gigs_details.getTotal_views().isEmpty()) {
                            gigsNos.setText(gigs_details.getTotal_views());
                        } else {
                            inputGigsTotalViews.setVisibility(View.GONE);
                        }
                        if (!gigs_details.getGig_usercount().isEmpty()) {
                            gigsUserCount.setText(gigs_details.getGig_usercount());
                        } else {
                            inputGigsUserCount.setVisibility(View.GONE);
                        }
                        if (!gigs_details.getLang_speaks().isEmpty()) {
                            gigsSpeaks.setText(gigs_details.getLang_speaks());
                        } else {
                            inputGigsUserSpeaks.setVisibility(View.GONE);
                        }

                        String description = Html.fromHtml(gigs_details.getGig_details()).toString();
                        gigsDesc.setText(description.trim());
                        int lineCount = gigsDesc.getLineCount();
                        if (lineCount > 3) {
                            inputDescShow.setVisibility(View.VISIBLE);
                            inputDescHide.setVisibility(View.GONE);
                            gigsDesc.setMaxLines(4);
                        }
                        if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID) != null) {
                            if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID).equalsIgnoreCase(gigs_details.getUser_id())) {
                                orderNow.setText("Edit Gigs");
                                inputGigsFav.setVisibility(View.GONE);
                            }
                        }

                        gigsRating.setRating(Float.parseFloat(gigs_details.getGig_rating()));


                    } else {
                        Utils.toastMessage(DetailGigs.this, response.body().getMessage());
                    }
                } else {
                    Utils.toastMessage(DetailGigs.this, response.body().getMessage());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTDetailGig> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });
    }


    @SuppressLint("ResourceType")
    private void sellerLogin(final Response<POSTDetailGig> response) {
        for (int i = 0; i < response.body().getData().size(); i++) {
            mGigs_details = response.body().getData().get(i).getGigs_details();
            gigsUserId = response.body().getData().get(i).getGigs_details().getUser_id();

            final int position = i;

            if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID) != null) {
                if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID).equalsIgnoreCase(gigsUserId)) {
                    inputGigsFav.setVisibility(View.GONE);
                } else {
                    inputGigsFav.setVisibility(View.VISIBLE);
                    if (response.body().getData().get(i).getGigs_details().getFavourite().equalsIgnoreCase("1")) {
                        isFav = true;
                        inputGigsFav.setImageResource(R.drawable.ic_favorite_filled_24dp);
                        postDetails.put("gig_id", response.body().getData().get(position).getGigs_details().getId());

                    } else {
                        isFav = false;
                        postDetails.put("gig_id", response.body().getData().get(position).getGigs_details().getId());
                        inputGigsFav.setImageResource(R.drawable.ic_favorite_border_purple_24dp);

                    }
                }
            } else {
                inputGigsFav.setVisibility(View.GONE);
            }

           /* if (response.body().getData().get(i).getGigs_details().getExtra_gigs().size() < 0 && response.body().getData().get(i).getGigs_details().getSuper_fast_delivery_desc().isEmpty()) {
                inputGigsSuperFastExtras.setVisibility(View.GONE);
                inputGigsExtras.setVisibility(View.GONE);
                GigsExtras.setVisibility(View.GONE);
            }
*/
            /*if (!response.body().getData().get(i).getGigs_details().getSuper_fast_delivery_desc().isEmpty()) {

            } else {
                inputGigsSuperFastExtras.setVisibility(View.GONE);
            }*/

            sum = Integer.parseInt(response.body().getData().get(i).getGigs_details().getGig_price());
            orderNow.setText("Order for $" + sum);
            deliveryDays = response.body().getData().get(i).getGigs_details().getDelivering_days();

            if (response.body().getData().get(i).getSimilar_gigs().size() > 0) {
                HorizontalRecommendedGigsAdapter horizontalRecommendedGigsAdapter = new HorizontalRecommendedGigsAdapter(DetailGigs.this, response.body().getData().get(i).getSimilar_gigs());
                horizontalRecommendedGigs.setAdapter(horizontalRecommendedGigsAdapter);
            } else {
                horizontalRecommendedGigs.setVisibility(View.GONE);
                GigsSimilar.setVisibility(View.GONE);
            }


            if (response.body().getData().get(i).getGigs_details().getExtra_gigs().size() > 0) {
                GigsExtras.setVisibility(View.VISIBLE);


                for (int j = 0; j < response.body().getData().get(i).getGigs_details().getExtra_gigs().size(); j++) {
                    View checkBoxView = getLayoutInflater().inflate(R.layout.checkbox_view_extras, null);
                    inputGigsExtras.addView(checkBoxView);
                    inputCheckBox = (CheckBox) checkBoxView.findViewById(R.id.cb_extras);
                    inputCost = (TextView) checkBoxView.findViewById(R.id.cost);

                    if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID) != null) {
                        if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID).equalsIgnoreCase(gigsUserId)) {
                            inputCheckBox.setButtonDrawable(null);
                        }
                    }

                    inputCheckBox.setText(response.body().getData().get(i).getGigs_details().getExtra_gigs().get(j).getExtra_gigs());
                    inputCost.setText("For $" + response.body().getData().get(i).getGigs_details().getExtra_gigs().get(j).getExtra_gigs_amount() + " in " +
                            response.body().getData().get(i).getGigs_details().getExtra_gigs().get(j).getExtra_gigs_delivery() + " day");

                    final int finalI = i;
                    final int finalJ = j;

                    inputCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                            if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID) != null) {
                                if (checked) {
                                    sum = sum + Integer.parseInt(response.body().getData().get(finalI).getGigs_details().getExtra_gigs().get(finalJ).getExtra_gigs_amount());
                                    orderNow.setText("Order for $" + sum);
                                    OrderGigs.add(response.body().getData().get(finalI).getGigs_details().getExtra_gigs().get(finalJ));
                                } else {
                                    sum = sum - Integer.parseInt(response.body().getData().get(finalI).getGigs_details().getExtra_gigs().get(finalJ).getExtra_gigs_amount());
                                    orderNow.setText("Order for $" + sum);
                                    if (OrderGigs.contains(response.body().getData().get(finalI).getGigs_details().getExtra_gigs().get(finalJ))) {
                                        OrderGigs.remove(response.body().getData().get(finalI).getGigs_details().getExtra_gigs().get(finalJ));
                                    }
                                }
                            } else {
                                Toast.makeText(DetailGigs.this, "You need to login to add extras for the gigs....", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });


                }
            } else {
                inputGigsExtras.setVisibility(View.GONE);
                GigsExtras.setVisibility(View.GONE);
            }

            if (response.body().getData().get(i).getGigs_details().getSuper_fast_charges().equalsIgnoreCase("0")
                    && response.body().getData().get(i).getGigs_details().getSuper_fast_delivery_desc().isEmpty() &&
                    response.body().getData().get(i).getGigs_details().getSuper_fast_days().isEmpty()) {
                inputGigsSuperFastExtras.setVisibility(View.GONE);
                GigsExtras.setVisibility(View.GONE);
                SUPERFAST_CHARGES = "";
                SUPERFAST_DAYS = "";
                SUPERFAST_DESC = "";
            } else {
                inputGigsSuperFastExtras.setVisibility(View.VISIBLE);
                GigsExtras.setVisibility(View.VISIBLE);

                if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID) != null) {
                    if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID).equalsIgnoreCase(gigsUserId)) {
                        inputSuperFastCheckBox.setButtonDrawable(null);
                    }
                }

                inputSuperFastCheckBox.setText(response.body().getData().get(i).getGigs_details().getSuper_fast_delivery_desc());
                final int finalI1 = i;
                inputSuperFastCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID) != null) {
                            if (!SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID).equalsIgnoreCase(gigsUserId)) {
                                if (checked) {
                                    sum = sum + Integer.parseInt(response.body().getData().get(finalI1).getGigs_details().getSuper_fast_charges());
                                    SUPERFAST_CHARGES = response.body().getData().get(finalI1).getGigs_details().getSuper_fast_charges();
                                    SUPERFAST_DAYS = response.body().getData().get(finalI1).getGigs_details().getSuper_fast_days();
                                    SUPERFAST_DESC = response.body().getData().get(finalI1).getGigs_details().getSuper_fast_delivery_desc();
                                    orderNow.setText("Order for $" + sum);
                                } else {
                                    sum = sum - Integer.parseInt(response.body().getData().get(finalI1).getGigs_details().getSuper_fast_charges());
                                    SUPERFAST_CHARGES = "";
                                    SUPERFAST_DAYS = "";
                                    SUPERFAST_DESC = "";

                                    orderNow.setText("Order for $" + sum);

                                }
                            }

                        } else {
                            Toast.makeText(DetailGigs.this, "You need to login to add extras for the gigs....", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                inputSuperFastCost.setText("For $" + response.body().getData().get(i).getGigs_details().getSuper_fast_charges() + " in "
                        + response.body().getData().get(i).getGigs_details().getSuper_fast_charges() + " day");
            }

//            OrderGigs.addAll(response.body().getData().get(i).getGigs_details().getExtra_gigs());

            /*if (response.body().getData().get(i).getGigs_details().getSuper_fast_delivery_checked() != null) {
                SUPERFAST_CHARGES = response.body().getData().get(i).getGigs_details().getSuper_fast_charges();
                SUPERFAST_DAYS = response.body().getData().get(i).getGigs_details().getSuper_fast_days();
                SUPERFAST_DESC = response.body().getData().get(i).getGigs_details().getSuper_fast_delivery_desc();
            }
*/
            if (response.body().getData().get(i).getReviews().size() > 0) {
                Constants.reviewList = response.body().getData().get(i).getReviews();
                detailGigsReviewAdapter = new DetailGigsReviewAdapter(DetailGigs.this, response.body().getData().get(i).getReviews());
                mDetailGigsReview.setAdapter(detailGigsReviewAdapter);
                Utils.getListViewSize(mDetailGigsReview);
            } else {
                GigsReviews.setVisibility(View.GONE);
                mDetailGigsReview.setVisibility(View.GONE);
            }
        }
    }

    private void addFavAPI() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.addFav(postDetails).enqueue(new Callback<POSTAddFav>() {
            @Override
            public void onResponse(Call<POSTAddFav> call, Response<POSTAddFav> response) {
                if (response.body().getCode().equals(200)) {
                    Utils.toastMessage(DetailGigs.this, response.body().getMessage());
                    inputGigsFav.setImageResource(R.drawable.ic_favorite_filled_24dp);
                } else {
                    Utils.toastMessage(DetailGigs.this, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<POSTAddFav> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });

    }

    private void initLayouts() {
        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        toolTitle = (TextView) findViewById(R.id.tv_gigs_title);
        profileUserImage = (ImageView) findViewById(R.id.iv_profile_image);
        mGigsImages = (ImageView) findViewById(R.id.iv_gigs_images);
        username = (TextView) findViewById(R.id.tv_gigs_username);
        userprofession = (TextView) findViewById(R.id.tv_gigs_profession);
        orderNow = (Button) findViewById(R.id.btn_order_now);
        gigsDesc = (TextView) findViewById(R.id.tv_gigs_desc);
        gigsNos = (TextView) findViewById(R.id.gigs_nos_views);
        gigsCountry = (TextView) findViewById(R.id.country);
        gigsUserCount = (TextView) findViewById(R.id.gigs_user_count);
        gigsSpeaks = (TextView) findViewById(R.id.tv_user_speaks);
        gigsRating = (RatingBar) findViewById(R.id.rating_gigs);
        horizontalRecommendedGigs = (RecyclerView) findViewById(R.id.horizontal_recent_popular_gigs);
        inputSuperFastCost = (TextView) findViewById(R.id.superfastcost);
        inputSuperFastCheckBox = (CheckBox) findViewById(R.id.superfastdesc);
        mDetailGigsReview = (ListView) findViewById(R.id.lv_review_gigs);
        inputDescShow = (ImageView) findViewById(R.id.input_show);
        inputDescHide = (ImageView) findViewById(R.id.input_hide);
        GigsReviews = (RelativeLayout) findViewById(R.id.rl_reviews);
        GigsSimilar = (RelativeLayout) findViewById(R.id.rl_similar_gigs);
        inputSimilarGigs = (TextView) findViewById(R.id.tv_recent_gigs_more);
        inputGigsExtras = (LinearLayout) findViewById(R.id.ll_gigs_extras);
        inputGigsSuperFastExtras = (LinearLayout) findViewById(R.id.ll_gigs_superfast_extras);
        GigsExtras = (RelativeLayout) findViewById(R.id.rl_gigs_extras);
        inputGigsUserCount = (LinearLayout) findViewById(R.id.ll_gigs_user_count);
        inputGigsTotalViews = (LinearLayout) findViewById(R.id.ll_gigs_total_views);
        inputGigsUserCountry = (LinearLayout) findViewById(R.id.ll_gigs_user_country);
        inputGigsUserSpeaks = (LinearLayout) findViewById(R.id.ll_gigs_user_speaks);
        inputGigsUserProfile = (LinearLayout) findViewById(R.id.ll_gigs_user_profile);
        inputGigsFav = (ImageView) findViewById(R.id.AD_iv_fav);
        gigsReviews = (TextView) findViewById(R.id.tv_review_gigs_more);


        LinearLayoutManager recentPopularGigsLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalRecommendedGigs.setLayoutManager(recentPopularGigsLayoutManager);
        toolTitle.setText(gigs_title);
        inputDescHide.setOnClickListener(this);
        inputDescShow.setOnClickListener(this);
        inputSimilarGigs.setOnClickListener(this);
        inputGigsUserProfile.setOnClickListener(this);
        gigsReviews.setOnClickListener(this);
    }

    public void goToBuy(View view) {

        if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
            if (SessionHandler.getInstance().get(this, Constants.USER_ID).equalsIgnoreCase(gigsUserId)) {
                Intent CallEditGigs = new Intent(DetailGigs.this, EditGigs.class);
                CallEditGigs.putExtra(Constants.GIGS_ID, gigs_id);
                startActivity(CallEditGigs);
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("order_gigs", OrderGigs);
                Intent callOrderDetails = new Intent(DetailGigs.this, CartModule.class);
                callOrderDetails.putExtra("Gigs_Detail", mGigs_details);
                callOrderDetails.putExtra("total_cost", sum);
                callOrderDetails.putExtra("delivery_days", deliveryDays);
                callOrderDetails.putExtra(Constants.GIGS_ID, gigs_id);
                callOrderDetails.putExtra(Constants.SUPERFAST_CHARGES, SUPERFAST_CHARGES);
                callOrderDetails.putExtra(Constants.SUPERFAST_DAYS, SUPERFAST_DAYS);
                callOrderDetails.putExtra(Constants.SUPERFAST_DELIVERY_DESC, SUPERFAST_DESC);
                callOrderDetails.putExtras(bundle);
                startActivity(callOrderDetails);
            }
        } else {
            Intent CallEditGigs = new Intent(DetailGigs.this, Login.class);
            CallEditGigs.putExtra(Constants.GIGS_ID, gigs_id);
            startActivity(CallEditGigs);

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.input_show) {
            inputDescShow.setVisibility(View.GONE);
            inputDescHide.setVisibility(View.VISIBLE);
            gigsDesc.setMaxLines(Integer.MAX_VALUE);
        } else if (view.getId() == R.id.input_hide) {
            inputDescShow.setVisibility(View.VISIBLE);
            inputDescHide.setVisibility(View.GONE);
            gigsDesc.setMaxLines(4);
        } else if (view.getId() == R.id.tv_recent_gigs_more) {
            Intent CallGigsList = new Intent(DetailGigs.this, GigsLists.class);
            startActivity(CallGigsList);
        } else if (view.getId() == R.id.ll_gigs_user_profile) {

            if (SessionHandler.getInstance().get(DetailGigs.this, Constants.USER_ID) != null) {
                Intent callUserProfile = new Intent(DetailGigs.this, DetailGigsUserInformation.class);
                callUserProfile.putExtra(Constants.GIGS_ID, gigs_title);
                callUserProfile.putExtra(Constants.GIGS_ID, gigs_id);
                startActivity(callUserProfile);
            } else {
                Intent CallEditGigs = new Intent(DetailGigs.this, Login.class);
                startActivity(CallEditGigs);
            }


        } else if (view.getId() == R.id.tv_review_gigs_more) {
            Intent callUserProfile = new Intent(DetailGigs.this, UserReviews.class);
            callUserProfile.putExtra(Constants.GIGS_ID, gigs_id);
            startActivity(callUserProfile);
        }
    }


    private void removeFavAPI() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.removeFav(postDetails).enqueue(new Callback<POSTRemoveFav>() {
            @Override
            public void onResponse(Call<POSTRemoveFav> call, Response<POSTRemoveFav> response) {
                if (response.body().getCode().equals(200)) {
                    Utils.toastMessage(DetailGigs.this, response.body().getMessage());
                    inputGigsFav.setImageResource(R.drawable.ic_favorite_border_purple_24dp);
                } else {
                    Utils.toastMessage(DetailGigs.this, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<POSTRemoveFav> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });
    }
}
