package dreamguys.in.co.gigs.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dreamguys.in.co.gigs.Model.POSTDetailGig;
import dreamguys.in.co.gigs.R;

/**
 * Created by user5 on 03-11-2017.
 */

public class DetailGigsReviewAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Context mContext;
    private List<POSTDetailGig.Review> ReviewGigs;
    String commentTime = "";
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");


    public DetailGigsReviewAdapter(Context mContext, List<POSTDetailGig.Review> reviewGigs) {
        this.mContext = mContext;
        ReviewGigs = reviewGigs;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ReviewGigs.size();
    }

    @Override
    public POSTDetailGig.Review getItem(int position) {
        return ReviewGigs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_review_gigs, null);
            mHolder.inputGigsBuyername = (TextView) convertView.findViewById(R.id.tv_gigs_username);
            mHolder.inputGigscomment = (TextView) convertView.findViewById(R.id.tv_gigs_comment);
            mHolder.inputGigsReviewRatingBar = (RatingBar) convertView.findViewById(R.id.rating_gigs);
            try {
                Date oldDate = dateFormat.parse(ReviewGigs.get(position).getCreated_date());
                Date currentDate = new Date();
                long diff = currentDate.getTime() - oldDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                if (oldDate.before(currentDate)) {

                    if (seconds < 60) {
                        mHolder.inputGigsBuyername.setText(ReviewGigs.get(position).getBuyername() + " ("
                                + seconds + " seconds ago)");
                    } else if (minutes < 60) {
                        mHolder.inputGigsBuyername.setText(ReviewGigs.get(position).getBuyername() + " ("
                                + minutes + " minutes ago)");
                    }else if (hours < 24) {
                        mHolder.inputGigsBuyername.setText(ReviewGigs.get(position).getBuyername() + " ("
                                + hours + " hours ago)");
                    }else {
                        mHolder.inputGigsBuyername.setText(ReviewGigs.get(position).getBuyername() + " ("
                                + days + " days ago)");
                    }

                    Log.e("oldDate", "is previous date");
                    Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
                            + " hours: " + hours + " days: " + days);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }


        mHolder.inputGigscomment.setText(ReviewGigs.get(position).getComment());
        mHolder.inputGigsReviewRatingBar.setRating(Float.parseFloat(ReviewGigs.get(position).getRating()));

        return convertView;
    }


    private class ViewHolder {
        TextView inputGigsBuyername, inputGigscomment;
        RatingBar inputGigsReviewRatingBar;
    }

}
