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
import dreamguys.in.co.gigs.Favourites;
import dreamguys.in.co.gigs.Model.POSTFavGigs;
import dreamguys.in.co.gigs.Model.POSTRemoveFav;
import dreamguys.in.co.gigs.R;
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
 * Created by Prasad on 10/25/2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {
    private final Context mContext;
    private List<POSTFavGigs.Datum> fav_gigs;
//    private List<String> gigsRemovedIds = new ArrayList<String>();
    private CustomProgressDialog mCustomProgressDialog;
    private HashMap<String, String> postDetails;

    public FavouriteAdapter(Context mContext, List<POSTFavGigs.Datum> fav_gigs) {
        this.mContext = mContext;
        this.fav_gigs = fav_gigs;
        postDetails = new HashMap<String, String>();
        mCustomProgressDialog = new CustomProgressDialog(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_gigs_item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.gigsTitle.setText(fav_gigs.get(position).getTitle());
        holder.gigsAuthor.setText(fav_gigs.get(position).getFullname());
        holder.gigsRate.setText(Constants.DOLLAR_SIGN + fav_gigs.get(position).getGig_price());
        holder.gigsLocation.setText(fav_gigs.get(position).getCountry() + "," + fav_gigs.get(position).getState_name());
        Picasso.with(mContext).load(Constants.BASE_URL + fav_gigs.get(position).getImage()).into(holder.gigsImages);
        holder.gigsRating.setRating(Float.parseFloat(fav_gigs.get(position).getGig_rating()));
        holder.gigsReviewCount.setText("(" + fav_gigs.get(position).getGig_usercount() + ")");

        if (fav_gigs.get(position).getFavourite().equalsIgnoreCase("1")) {
            holder.favGigsIcons.setImageResource(R.drawable.ic_favorite_filled_24dp);
        }

        holder.favGigsIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkChangeReceiver.isConnected()) {
                    mCustomProgressDialog.showDialog();
                    postDetails.put("gig_id", fav_gigs.get(position).getId());
                    postDetails.put("user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
                    removeFavAPI(holder, position);
                } else {
                    Utils.toastMessage(mContext, mContext.getString(R.string.err_internet_connection));
                }
            }
        });


    }

    private void removeFavAPI(final MyViewHolder holder, final int position) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.removeFav(postDetails).enqueue(new Callback<POSTRemoveFav>() {
            @Override
            public void onResponse(Call<POSTRemoveFav> call, Response<POSTRemoveFav> response) {
                if (response.body().getCode().equals(200)) {
                    Utils.toastMessage(mContext, response.body().getMessage());
                    ((Favourites) mContext).getFavRemGigIds(true);
                    fav_gigs.remove(position);
                    notifyDataSetChanged();
                    if (fav_gigs.size() == 0) {
                        ((Favourites) mContext).finish();

                    }
                } else {
                    Utils.toastMessage(mContext, response.body().getMessage());
                }

                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTRemoveFav> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {
        return fav_gigs.size();
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
                    callGigsDetails.putExtra(Constants.GIGS_ID, fav_gigs.get(getAdapterPosition()).getId());
                    callGigsDetails.putExtra(Constants.GIGS_TITLE, fav_gigs.get(getAdapterPosition()).getTitle());
                    mContext.startActivity(callGigsDetails);
                }
            });
        }
    }
}
