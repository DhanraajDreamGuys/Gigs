package dreamguys.in.co.gigs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dreamguys.in.co.gigs.ChatRoomActivity;
import dreamguys.in.co.gigs.Model.POSTMessages;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.utils.CircleTransform;

/**
 * Created by Prasad on 11/14/2017.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.MyViewHolder> {
    Context mContext;
    List<POSTMessages.Detail> details;


    public MessageChatAdapter(Context mContext, List<POSTMessages.Detail> details) {
        this.mContext = mContext;
        this.details = details;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_message_chat, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (!details.get(position).getProfile_image().isEmpty())
            Picasso.with(mContext).load(details.get(position).getProfile_image()).transform(new CircleTransform()).placeholder(R.drawable.no_image).into(holder.imageMessagerPf);

        holder.messangerName.setText(details.get(position).getFirstname());
        holder.messangerMsg.setText(details.get(position).getLast_message());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(details.get(position).getChat_time());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            String hrMins = fmtOut.format(date);
            holder.messangerTime.setText(hrMins);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageMessagerPf;
        private TextView messangerName, messangerMsg, messangerTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageMessagerPf = (ImageView) itemView.findViewById(R.id.iv_message_profile_img);
            messangerName = (TextView) itemView.findViewById(R.id.tv_messager_name);
            messangerMsg = (TextView) itemView.findViewById(R.id.tv_messager_last_message);
            messangerTime = (TextView) itemView.findViewById(R.id.tv_messager_last_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callChatRoom = new Intent(mContext, ChatRoomActivity.class);
                    callChatRoom.putExtra("user_id", details.get(getAdapterPosition()).getUser_id());
                    callChatRoom.putExtra("chat_id", details.get(getAdapterPosition()).getChat_id());
                    callChatRoom.putExtra("chat_name", details.get(getAdapterPosition()).getFirstname());
                    mContext.startActivity(callChatRoom);


                }
            });
        }
    }
}
