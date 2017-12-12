package dreamguys.in.co.gigs.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dreamguys.in.co.gigs.CategoryList;
import dreamguys.in.co.gigs.DetailGigs;
import dreamguys.in.co.gigs.GigsLists;
import dreamguys.in.co.gigs.Model.GETHomeGigs;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.adapter.HorizontalPopularGigsAdapter;
import dreamguys.in.co.gigs.adapter.HorizontalRecentPopularGigsAdapter;
import dreamguys.in.co.gigs.adapter.HorizontalTopCategoryAdapter;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ViewPager mViewPager;
    private View mView;
    private CustomProgressDialog mCustomProgressDialog;
    private final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    private int currentPage = 0;
    private Timer timer;
    private PopularGigsImageAdapter aPopularGigsImageAdapter;
    private RecyclerView horizontalCategories;
    private RecyclerView horizontalPopularGigs;
    private RecyclerView horizontalRecentGigs;
    private TextView categoryMore;
    private TextView popularGigsMore;
    private TextView recentGigsMore;
    private String user_id = "";
    HorizontalPopularGigsAdapter aHorizontalPopularGigsAdapter;
    HorizontalRecentPopularGigsAdapter aHorizontalRecentPopularGigsAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        initLayouts(mView);
        mCustomProgressDialog = new CustomProgressDialog(getActivity());
        if (SessionHandler.getInstance().get(getActivity(), Constants.USER_ID) != null) {
            user_id = SessionHandler.getInstance().get(getActivity(), Constants.USER_ID);
        } else {
            user_id = "";
        }


        categoryMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CategoryList.class));
            }
        });

        popularGigsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GigsLists.class));
            }
        });

        recentGigsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GigsLists.class));
            }
        });

        return mView;
    }

    private void initLayouts(View mView) {
        mViewPager = (ViewPager) mView.findViewById(R.id.vp_popular_gigs);
        horizontalCategories = (RecyclerView) mView.findViewById(R.id.horizontal_categories);
        horizontalPopularGigs = (RecyclerView) mView.findViewById(R.id.horizontal_popular_gigs);
        horizontalRecentGigs = (RecyclerView) mView.findViewById(R.id.horizontal_recent_popular_gigs);
        categoryMore = (TextView) mView.findViewById(R.id.tv_category_more);
        popularGigsMore = (TextView) mView.findViewById(R.id.tv_popular_gigs_more);
        recentGigsMore = (TextView) mView.findViewById(R.id.tv_recent_gigs_more);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalCategories.setLayoutManager(horizontalLayoutManagaer);
        LinearLayoutManager popularGigsLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalPopularGigs.setLayoutManager(popularGigsLayoutManager);
        LinearLayoutManager recentPopularGigsLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecentGigs.setLayoutManager(recentPopularGigsLayoutManager);
    }

    private void setViewpagerAutoSlide() {
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == (aPopularGigsImageAdapter.getCount() + 1) - 1) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }


    private void GETHomegigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getHomeGigs(user_id).enqueue(new Callback<GETHomeGigs>() {
            @Override
            public void onResponse(Call<GETHomeGigs> call, Response<GETHomeGigs> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        if (response.body().getPrimary().size() > 0) {
                            for (GETHomeGigs.Primary primary : response.body().getPrimary()) {
                                Constants.BASE_URL = primary.getBase_url();
                                if (primary.getPopular_gigs_image().size() > 0) {
                                    if (getActivity() != null) {
                                        aPopularGigsImageAdapter = new PopularGigsImageAdapter(getActivity(), primary.getPopular_gigs_image());
                                        mViewPager.setAdapter(aPopularGigsImageAdapter);
                                        setViewpagerAutoSlide();
                                        if (primary.getCategories().size() > 0) {
                                            HorizontalTopCategoryAdapter aHorizontalTopCategoryAdapter = new HorizontalTopCategoryAdapter(getActivity(), primary.getCategories());
                                            horizontalCategories.setAdapter(aHorizontalTopCategoryAdapter);
                                        }

                                        if (primary.getPopular_gigs_list().size() > 0) {
                                            aHorizontalPopularGigsAdapter = new HorizontalPopularGigsAdapter(getActivity(), primary.getPopular_gigs_list());
                                            horizontalPopularGigs.setAdapter(aHorizontalPopularGigsAdapter);
                                        }
                                        if (primary.getRecent_gigs_list().size() > 0) {
                                            aHorizontalRecentPopularGigsAdapter = new HorizontalRecentPopularGigsAdapter(getActivity(), primary.getRecent_gigs_list());
                                            horizontalRecentGigs.setAdapter(aHorizontalRecentPopularGigsAdapter);
                                        }
                                    }
                                } else {
                                    setViewpagerVisibliity();
                                }
                            }

                        } else {
                            Toast.makeText(getActivity(), "No primary data found...", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                    }
                } else {
                    Log.i("ERROR_HOME_FRAGMENT", response.errorBody().toString());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GETHomeGigs> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            GETHomegigs();
        } else {
            showToast();
        }

    }

    private void setViewpagerVisibliity() {
        mViewPager.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interfaces must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class PopularGigsImageAdapter extends PagerAdapter {
        final Context mContext;
        final List<GETHomeGigs.Popular_gigs_image> popular_gigs_image;
        final LayoutInflater mInflater;

        PopularGigsImageAdapter(Context context, List<GETHomeGigs.Popular_gigs_image> popular_gigs_image) {
            this.mContext = context;
            this.popular_gigs_image = popular_gigs_image;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return popular_gigs_image.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View innerImageLayer = mInflater.inflate(R.layout.adapter_popular_gigs_slider, null);
            final GETHomeGigs.Popular_gigs_image popular_gigs_imagelist = popular_gigs_image.get(position);

            ImageView gigsImages = (ImageView) innerImageLayer.findViewById(R.id.iv_popular_gigs_image);
            TextView textCount = (TextView) innerImageLayer.findViewById(R.id.tv_page_count);
            Picasso.with(mContext).load(Constants.BASE_URL + popular_gigs_imagelist.getImage()).placeholder(R.drawable.no_image).into(gigsImages);
            textCount.setText((position + 1) + "/" + (getCount()));
            innerImageLayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callGigsDetails = new Intent(mContext, DetailGigs.class);
                    callGigsDetails.putExtra(Constants.GIGS_ID, popular_gigs_imagelist.getId());
                    callGigsDetails.putExtra(Constants.GIGS_TITLE, popular_gigs_imagelist.getTitle());
                    mContext.startActivity(callGigsDetails);
                }
            });
            container.addView(innerImageLayer);
            return innerImageLayer;
        }

    }

    private void showToast() {
        Toast.makeText(getActivity(), getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }

}
