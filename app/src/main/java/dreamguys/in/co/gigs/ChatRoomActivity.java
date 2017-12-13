package dreamguys.in.co.gigs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.POSTChatHistory;
import dreamguys.in.co.gigs.Model.POSTUserChat;
import dreamguys.in.co.gigs.adapter.ChatRoomAdapter;
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
 * Created by Prasad on 11/14/2017.
 */

public class ChatRoomActivity extends AppCompatActivity {

    public static RecyclerView recyclerViewChatRoom;
    String user_id = "", chat_id = "", chatter_name = "";
    static CustomProgressDialog mCustomProgressDialog;
    private HashMap<String, String> postChatDetails = new HashMap<String, String>();
    public static ChatRoomAdapter aChatRoomAdapter;
    private EditText sendMessage;
    private TextView messengerName;
    Toolbar toolbar;
    private HashMap<String, String> postChatmessage = new HashMap<String, String>();
    static List<POSTChatHistory.Chat_detail> chatArray = new ArrayList<POSTChatHistory.Chat_detail>();
    String notification = "", message = "";
    String body;
    JSONObject notificationData;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.None);

        recyclerViewChatRoom = (RecyclerView) findViewById(R.id.rv_chat_room);
        mCustomProgressDialog = new CustomProgressDialog(this);
        user_id = getIntent().getStringExtra("user_id");
        chat_id = getIntent().getStringExtra("chat_id");
        chatter_name = getIntent().getStringExtra("chat_name");

        notification = getIntent().getStringExtra("notification");
        body = getIntent().getStringExtra("receivedResult");
        message = getIntent().getStringExtra("message");
      /*  myBroadcastReceiver = new MyBroadcastReceiver();
        try {
            if (body != null) {
                notificationData = new JSONObject(body);
                user_id = notificationData.getString("from_user_id");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        sendMessage = (EditText) findViewById(R.id.et_send_message);
        messengerName = (TextView) findViewById(R.id.tv_chat_msger_name);
        messengerName.setText(chatter_name);

        chatArray.clear();
        getChatResponse();

        /*recyclerViewChatRoom.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //activitiesData is collection used to populate the recycler Adapter
                chatArray.remove(chatArray.size() - 1);
                aChatRoomAdapter.notifyItemRemoved(chatArray.size());

                // Make a networking call to get the data on page scrolled
                getChatResponse();

                //after you get response
                aChatRoomAdapter.notifyDataSetChanged();
            }
        });*/

    }

    private void getChatResponse() {
        if (NetworkChangeReceiver.isConnected()) {
            if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null && user_id != null) {
                postChatDetails.put("from_user_id", SessionHandler.getInstance().get(this, Constants.USER_ID));
                postChatDetails.put("to_user_id", user_id);
                postChatDetails.put("page", "0");
            }
            mCustomProgressDialog.showDialog();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.getChatHistory(postChatDetails).enqueue(new Callback<POSTChatHistory>() {
                @Override
                public void onResponse(Call<POSTChatHistory> call, Response<POSTChatHistory> response) {
                    if (response.body() != null)
                        if (response.body().getCode().equals(200)) {
                            chatArray.addAll(response.body().getData().getChat_details());






                            aChatRoomAdapter = new ChatRoomAdapter(ChatRoomActivity.this, chatArray);
                            LinearLayoutManager popularGigsLayoutManager
                                    = new LinearLayoutManager(ChatRoomActivity.this, LinearLayoutManager.VERTICAL, false);
                            recyclerViewChatRoom.setLayoutManager(popularGigsLayoutManager);
                            recyclerViewChatRoom.setAdapter(aChatRoomAdapter);
                            scrollToBottom();
                        }
                    mCustomProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<POSTChatHistory> call, Throwable t) {
                    Log.i("TAG", t.getMessage());
                    mCustomProgressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(this, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
        }
    }

    public static void scrollToBottom() {
        if (aChatRoomAdapter != null) {
            aChatRoomAdapter.notifyDataSetChanged();
            if (aChatRoomAdapter.getItemCount() > 1){
//                recyclerViewChatRoom.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                recyclerViewChatRoom.getLayoutManager().scrollToPosition(aChatRoomAdapter.getItemCount() - 1);
            }
//                recyclerViewChatRoom.getLayoutManager().smoothScrollToPosition(recyclerViewChatRoom, null, aChatRoomAdapter.getItemCount() - 1;

        }
    }


    public void sendMesageBtn(View view) {
        String message = sendMessage.getText().toString().trim();

        if (message.isEmpty()) {
            return;
        }

        postChatmessage.put("from_user_id", SessionHandler.getInstance().get(this, Constants.USER_ID));
        postChatmessage.put("to_user_id", user_id);
        postChatmessage.put("message", message);

        if (NetworkChangeReceiver.isConnected()) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.postUserChat(postChatmessage).enqueue(new Callback<POSTUserChat>() {
                @Override
                public void onResponse(Call<POSTUserChat> call, Response<POSTUserChat> response) {
                    if (response.body() != null) {
                        if (response.body().getCode().equals(200)) {
                            POSTChatHistory.Chat_detail mChat_detail = new POSTChatHistory.Chat_detail();
                            mChat_detail.setChat_from(response.body().getData().get(0).getChat_from());
                            mChat_detail.setChat_to(response.body().getData().get(0).getChat_to());
                            mChat_detail.setChat_time(response.body().getData().get(0).getChat_time());
                            mChat_detail.setContent(response.body().getData().get(0).getContent());
                            aChatRoomAdapter.addData(mChat_detail);
                            aChatRoomAdapter.notifyDataSetChanged();
                            scrollToBottom();
                            sendMessage.setText("");
                        }
                    }
                }

                @Override
                public void onFailure(Call<POSTUserChat> call, Throwable t) {
                    Log.i("TAG", t.getMessage());
                }
            });

        } else {
            Utils.toastMessage(this, getString(R.string.err_internet_connection));
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(MyApplication.ACTION_MyUpdate);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(MyApplication.EXTRA_KEY_UPDATE);
            String body = intent.getStringExtra(MyApplication.MESSAGE);
            if (!result.isEmpty()) {

                try {
                    JSONObject mJSONObject = new JSONObject(result);
                    POSTChatHistory.Chat_detail mChat_detail = new POSTChatHistory.Chat_detail();
                    mChat_detail.setChat_from(mJSONObject.get("from_user_id").toString());
                    mChat_detail.setChat_to(mJSONObject.get("to_user_id").toString());
                    mChat_detail.setChat_time(mJSONObject.get("chat_utc_time").toString());
                    mChat_detail.setContent(body);
                    aChatRoomAdapter.addData(mChat_detail);
                    aChatRoomAdapter.notifyDataSetChanged();
                    scrollToBottom();
                    Log.i("TAG JSONRESULT ---->", mJSONObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
    }
}
