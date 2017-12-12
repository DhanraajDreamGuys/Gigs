package dreamguys.in.co.gigs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import dreamguys.in.co.gigs.Model.POSTChatHistory;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.SessionHandler;

/**
 * Created by Prasad on 11/14/2017.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {

    Context mContext;
    private static List<POSTChatHistory.Chat_detail> chat_details;

    private int SELF = 1;

    public ChatRoomAdapter(Context mContext, List<POSTChatHistory.Chat_detail> chat_details) {
        this.mContext = mContext;
        this.chat_details = chat_details;
    }

    public void addData(POSTChatHistory.Chat_detail addedMessage) {
        chat_details.add(addedMessage);
    }

    @Override
    public ChatRoomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == SELF) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_from_msg, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_to_msg, parent, false);
        }
        return new ChatRoomAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatRoomAdapter.MyViewHolder holder, int position) {
        if (!chat_details.get(position).getChat_time().isEmpty()) {
            holder.fromMsgDate.setVisibility(View.VISIBLE);

            String aDate = chat_details.get(position).getChat_time();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
//                date = simpleDateFormat.parse(aDate);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date dates = simpleDateFormat.parse(aDate);
                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                String formattedDate = simpleDateFormat2.format(dates);
//                String formattedDate = simpleDateFormat2.format(date);
                holder.fromMsgDate.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } else {
            holder.fromMsgDate.setVisibility(View.GONE);
        }
        holder.fromMsg.setText(chat_details.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return chat_details.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (Objects.equals(chat_details.get(position).getChat_from(), SessionHandler.getInstance().get(mContext, Constants.USER_ID))) {
            return SELF;
        }
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView fromMsg, fromMsgDate;
        private ImageView profileImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            fromMsg = (TextView) itemView.findViewById(R.id.tv_from_msg);
            fromMsgDate = (TextView) itemView.findViewById(R.id.tv_from_msg_time);
        }
    }



}
