package dreamguys.in.co.gigs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dreamguys.in.co.gigs.DetailGigs;
import dreamguys.in.co.gigs.Model.POSTDetailGig;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.utils.Constants;

/**
 * Created by user5 on 03-11-2017.
 */

public class HorizontalRecommendedGigsAdapter extends RecyclerView.Adapter<HorizontalRecommendedGigsAdapter.MyViewHolder> {
    private final Context mContext;
    private final List<POSTDetailGig.Similar_gig> recent_popular_gigs_list;


    public HorizontalRecommendedGigsAdapter(Context mContext, List<POSTDetailGig.Similar_gig> recent_popular_gigs_list) {
        this.mContext = mContext;
        this.recent_popular_gigs_list = recent_popular_gigs_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_popular_gigs_item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.gigsTitle.setText(recent_popular_gigs_list.get(position).getTitle());
        holder.gigsAuthor.setText(recent_popular_gigs_list.get(position).getFullname());
        holder.gigsRate.setText(Constants.DOLLAR_SIGN + recent_popular_gigs_list.get(position).getGig_price());
        Picasso.with(mContext).load(Constants.BASE_URL + recent_popular_gigs_list.get(position).getImage()).placeholder(R.drawable.no_image).into(holder.gigsImages);
        holder.gigsRating.setRating(Float.parseFloat(recent_popular_gigs_list.get(position).getGig_rating()));
        holder.gigsLocation.setText(recent_popular_gigs_list.get(position).getCountry() + "," + recent_popular_gigs_list.get(position).getState_name());
        holder.gigsReviewCount.setText("(" + recent_popular_gigs_list.get(position).getGig_usercount() + ")");

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
        RelativeLayout gigslayoutLocation;

        MyViewHolder(View itemView) {
            super(itemView);
            gigsTitle = (TextView) itemView.findViewById(R.id.tv_gigs_title);
            gigsAuthor = (TextView) itemView.findViewById(R.id.author);
            gigsReviewCount = (TextView) itemView.findViewById(R.id.rating_count);
            gigsImages = (ImageView) itemView.findViewById(R.id.iv_gigs_images);
            gigsRate = (TextView) itemView.findViewById(R.id.tv_gigs_rate);
            gigsLocation = (TextView) itemView.findViewById(R.id.tv_loc);
            gigsRating = (RatingBar) itemView.findViewById(R.id.rating_gigs);
            gigslayoutLocation = (RelativeLayout) itemView.findViewById(R.id.rl_location);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callGigsDetails = new Intent(mContext, DetailGigs.class);
                    callGigsDetails.putExtra(Constants.GIGS_TITLE, recent_popular_gigs_list.get(getAdapterPosition()).getTitle());
                    callGigsDetails.putExtra(Constants.GIGS_ID, recent_popular_gigs_list.get(getAdapterPosition()).getId());
                    mContext.startActivity(callGigsDetails);
                }
            });
        }
    }
}
