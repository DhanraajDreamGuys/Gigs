package dreamguys.in.co.gigs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.DetailGigs;
import dreamguys.in.co.gigs.Model.GETHomeGigs;
import dreamguys.in.co.gigs.Model.POSTAddFav;
import dreamguys.in.co.gigs.Model.POSTRemoveFav;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 10/25/2017.
 */

public class HorizontalRecentPopularGigsAdapter extends RecyclerView.Adapter<HorizontalRecentPopularGigsAdapter.MyViewHolder> {
    private final Context mContext;
    private final List<GETHomeGigs.Recent_gigs_list> recent_popular_gigs_list;
    private HashMap<String, String> postDetails;

    public HorizontalRecentPopularGigsAdapter(Context mContext, List<GETHomeGigs.Recent_gigs_list> recent_popular_gigs_list) {
        this.mContext = mContext;
        this.recent_popular_gigs_list = recent_popular_gigs_list;
        postDetails = new HashMap<String, String>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_popular_gigs_item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.gigsTitle.setText(recent_popular_gigs_list.get(position).getTitle());
        holder.gigsAuthor.setText(recent_popular_gigs_list.get(position).getFullname());
        holder.gigsRate.setText(Constants.DOLLAR_SIGN + recent_popular_gigs_list.get(position).getGig_price());
        holder.gigsLocation.setText(recent_popular_gigs_list.get(position).getCountry() + "," + recent_popular_gigs_list.get(position).getState_name());
        Picasso.with(mContext).load(Constants.BASE_URL + recent_popular_gigs_list.get(position).getImage()).placeholder(R.drawable.no_image).into(holder.gigsImages);
        holder.gigsRating.setRating(Float.parseFloat(recent_popular_gigs_list.get(position).getGig_rating()));
        holder.gigsReviewCount.setText("(" + recent_popular_gigs_list.get(position).getGig_usercount() + ")");
        if (SessionHandler.getInstance().get(mContext, Constants.USER_ID) != null) {
            if (recent_popular_gigs_list.get(position).getFavourite().equalsIgnoreCase("1")){
                holder.isSelected = true;
                holder.favGigsIcons.setVisibility(View.VISIBLE);
                holder.favGigsIcons.setImageResource(R.drawable.ic_favorite_filled_24dp);

            }else{
                holder.isSelected = false;
                holder.favGigsIcons.setVisibility(View.VISIBLE);
                holder.favGigsIcons.setImageResource(R.drawable.ic_favorite_border_purple_24dp);
            }


            holder.favGigsIcons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkChangeReceiver.isConnected()) {
                        postDetails.put("gig_id", recent_popular_gigs_list.get(position).getId());
                        postDetails.put("user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
                        if (holder.isSelected) {
                            holder.isSelected = false;
                            removeFavAPI(holder, position);
                        } else {
                            holder.isSelected = true;
                            addFavAPI(holder, position);
                        }
                    } else {
                        Utils.toastMessage(mContext, mContext.getString(R.string.err_internet_connection));
                    }
                }
            });


        } else {
            holder.favGigsIcons.setVisibility(View.GONE);
        }
    }

    private void removeFavAPI(final MyViewHolder holder, int position) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.removeFav(postDetails).enqueue(new Callback<POSTRemoveFav>() {
            @Override
            public void onResponse(Call<POSTRemoveFav> call, Response<POSTRemoveFav> response) {
                if (response.body().getCode().equals(200)) {
                    Utils.toastMessage(mContext, response.body().getMessage());
                    holder.favGigsIcons.setImageResource(R.drawable.ic_favorite_border_purple_24dp);
                } else {
                    Utils.toastMessage(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<POSTRemoveFav> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });
    }

    private void addFavAPI(final MyViewHolder holder, final int position) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.addFav(postDetails).enqueue(new Callback<POSTAddFav>() {
            @Override
            public void onResponse(Call<POSTAddFav> call, Response<POSTAddFav> response) {
                if (response.body().getCode().equals(200)) {
                    Utils.toastMessage(mContext, response.body().getMessage());
                    holder.favGigsIcons.setImageResource(R.drawable.ic_favorite_filled_24dp);
                } else {
                    Utils.toastMessage(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<POSTAddFav> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return recent_popular_gigs_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView gigsTitle;
        final TextView gigsAuthor;
        final TextView gigsLocation;
        final TextView gigsReviewCount;
        final TextView gigsRate;
        final ImageView gigsImages;
        RatingBar gigsRating;
        final ImageView favGigsIcons;
        Boolean isSelected;

        MyViewHolder(View itemView) {
            super(itemView);
            gigsTitle = (TextView) itemView.findViewById(R.id.tv_gigs_title);
            gigsAuthor = (TextView) itemView.findViewById(R.id.author);
            gigsReviewCount = (TextView) itemView.findViewById(R.id.rating_count);
            gigsImages = (ImageView) itemView.findViewById(R.id.iv_gigs_images);
            gigsRate = (TextView) itemView.findViewById(R.id.tv_gigs_rate);
            gigsLocation = (TextView) itemView.findViewById(R.id.tv_loc);
            gigsRating = (RatingBar) itemView.findViewById(R.id.rating_gigs);
            favGigsIcons = (ImageView) itemView.findViewById(R.id.iv_fav_gigs);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callGigsDetails = new Intent(mContext, DetailGigs.class);
                    callGigsDetails.putExtra(Constants.GIGS_ID, recent_popular_gigs_list.get(getAdapterPosition()).getId());
                    callGigsDetails.putExtra(Constants.GIGS_TITLE, recent_popular_gigs_list.get(getAdapterPosition()).getTitle());
                    mContext.startActivity(callGigsDetails);
                }
            });
        }
    }
}
