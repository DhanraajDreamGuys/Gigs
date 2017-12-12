package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.GETProfession;
import dreamguys.in.co.gigs.Model.POSTDetailGig;
import dreamguys.in.co.gigs.dialog.ChatCommentBox;
import dreamguys.in.co.gigs.fragment.DetailReviewGigsFragment;
import dreamguys.in.co.gigs.fragment.DetailsSellerGigsFragment;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomViewPager;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user5 on 06-11-2017.
 */

public class DetailGigsUserInformation extends AppCompatActivity {


    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private LinearLayout inputGigsUserCount, inputGigsTotalViews, inputGigsUserCountry, inputGigsUserSpeaks;
    TextView toolTitle, username, gigsTitle, userprofession, gigsDesc, gigsNos, gigsCountry, gigsUserCount, gigsSpeaks;
    HashMap<String, String> postGigDetails = new HashMap<String, String>();
    private Gson gson;
    private GETProfession[] getProfession;
    private RatingBar gigsRating;
    private Button inputContact;
    private ImageView profileUserImage;
    private String gigs_id;
    Toolbar toolbar;
    ChatCommentBox mChatCommentBox;
    String seller_user_id = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_gigs_user_information);
       /* NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.UI_nested_scrollview);
        scrollView.setFillViewport(true);*/
        initLayouts();
        gson = new Gson();
        getProfession = gson.fromJson(SessionHandler.getInstance().get(DetailGigsUserInformation.this, Constants.PROFESSION), GETProfession[].class);
        gigs_id = SessionHandler.getInstance().get(DetailGigsUserInformation.this, Constants.GIGS_ID);
        viewPager = (CustomViewPager) findViewById(R.id.CustomviewPager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.reMeasureCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (NetworkChangeReceiver.isConnected()) {
            if (SessionHandler.getInstance().get(DetailGigsUserInformation.this, Constants.USER_ID) != null) {
                postGigDetails.put("userid", SessionHandler.getInstance().get(DetailGigsUserInformation.this, Constants.USER_ID));
            } else {
                postGigDetails.put("userid", "");
            }
            postGigDetails.put("gig_id", gigs_id);
            getUserDetails();
        } else {
            Utils.toastMessage(DetailGigsUserInformation.this, getString(R.string.err_internet_connection));
        }
    }


    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DetailReviewGigsFragment(DetailGigsUserInformation.this), "Reviews");
        adapter.addFragment(new DetailsSellerGigsFragment(DetailGigsUserInformation.this), "My Gigs");
        viewPager.setAdapter(adapter);

    }


    private void getUserDetails() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getDetailGigs(postGigDetails).enqueue(new Callback<POSTDetailGig>() {
            @Override
            public void onResponse(Call<POSTDetailGig> call, Response<POSTDetailGig> response) {
                if (response.body().getCode().equals(200)) {
                    if (response.body().getData().size() > 0) {

                        seller_user_id = response.body().getData().get(0).getGigs_details().getUser_id();

                        POSTDetailGig.Gigs_details gigs_details = response.body().getData().get(0).getGigs_details();
                        Picasso.with(DetailGigsUserInformation.this).load(Utils.BASEURL + gigs_details.getUser_thumb_image()).placeholder(R.drawable.ic_app_icons).into(profileUserImage);
                        username.setText(gigs_details.getFullname());
                        toolTitle.setText(gigs_details.getFullname());
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
                        gigsRating.setRating(Float.parseFloat(gigs_details.getGig_rating()));
                    } else {
                        Utils.toastMessage(DetailGigsUserInformation.this, response.body().getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<POSTDetailGig> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });
    }

    public void openChatCommentBox(View view) {
        mChatCommentBox = new ChatCommentBox(this, seller_user_id);
        mChatCommentBox.show(getSupportFragmentManager(), "show_comment_box");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void initLayouts() {
        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        toolTitle = (TextView) findViewById(R.id.tv_gigs_title);
        username = (TextView) findViewById(R.id.tv_gigs_username);
        userprofession = (TextView) findViewById(R.id.tv_gigs_profession);
        inputContact = (Button) findViewById(R.id.btn_order_now);
        gigsNos = (TextView) findViewById(R.id.gigs_nos_views);
        gigsCountry = (TextView) findViewById(R.id.country);
        gigsUserCount = (TextView) findViewById(R.id.gigs_user_count);
        gigsSpeaks = (TextView) findViewById(R.id.tv_user_speaks);
        gigsRating = (RatingBar) findViewById(R.id.rating_gigs);
        inputGigsUserCount = (LinearLayout) findViewById(R.id.ll_gigs_user_count);
        inputGigsTotalViews = (LinearLayout) findViewById(R.id.ll_gigs_total_views);
        inputGigsUserCountry = (LinearLayout) findViewById(R.id.ll_gigs_user_country);
        inputGigsUserSpeaks = (LinearLayout) findViewById(R.id.ll_gigs_user_speaks);
        profileUserImage = (ImageView) findViewById(R.id.iv_profile_image);
        LinearLayoutManager recentPopularGigsLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


    }

}
