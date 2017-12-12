package dreamguys.in.co.gigs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.POSTSubCategory;
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
 * Created by Prasad on 10/26/2017.
 */

public class SubCategoryList extends AppCompatActivity {

    private ListView categoryLists;
    private Toolbar toolbar;
    private CategoryListAdapter aCategoryListAdapter;
    private CustomProgressDialog mCustomProgressDialog;
    private String category_id = "", category_name = "", user_id = "";
    private HashMap<String, String> postCategoryDetails = new HashMap<String, String>();
    private TextView toolTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        category_id = getIntent().getStringExtra(Constants.CAT_ID);
        category_name = getIntent().getStringExtra(Constants.CAT_NAME);

        mCustomProgressDialog = new CustomProgressDialog(this);
        initLayouts();

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            if (!category_id.isEmpty()) {
                postCategoryDetails.put("category_id", category_id);
            }
            if (SessionHandler.getInstance().get(SubCategoryList.this, Constants.USER_ID) != null) {
                postCategoryDetails.put("user_id", user_id);
            }
            postCategoryDetails.put("services", "All");
            getCategoryLists();
        } else {
            showToast();
        }
    }

    private void getCategoryLists() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getSubCategory(postCategoryDetails).enqueue(new Callback<POSTSubCategory>() {
            @Override
            public void onResponse(Call<POSTSubCategory> call, Response<POSTSubCategory> response) {
                if (response.body().getCode().equals(200)) {
                    if (response.body().getData().size() > 0) {
                        aCategoryListAdapter = new CategoryListAdapter(SubCategoryList.this, response.body().getData());
                        categoryLists.setAdapter(aCategoryListAdapter);
                    } else {
                        Utils.toastMessage(SubCategoryList.this, response.body().getMessage());
                    }

                } else {
                    Utils.toastMessage(SubCategoryList.this, response.body().getMessage());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTSubCategory> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });
    }


    private void initLayouts() {
        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        categoryLists = (ListView) findViewById(R.id.lv_category_list);
        toolTitle = (TextView) findViewById(R.id.tool_title);
        toolTitle.setText(category_name);

    }

    private class CategoryListAdapter extends BaseAdapter {
        final Context mContext;
        final List<POSTSubCategory.Datum> primary;
        final LayoutInflater mInflater;

        CategoryListAdapter(Context mContext, List<POSTSubCategory.Datum> primary) {
            this.mContext = mContext;
            this.primary = primary;
            mInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return primary.size();
        }

        @Override
        public POSTSubCategory.Datum getItem(int position) {
            return primary.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;

            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.adapter_settings, null);
                mHolder.categoryNames = (TextView) convertView.findViewById(R.id.tv_settings_items);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            mHolder.categoryNames.setText(getItem(position).getName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callSubCategory = new Intent(mContext, GigsLists.class);
                    callSubCategory.putExtra(Constants.CAT_ID, category_id);
                    callSubCategory.putExtra(Constants.SUB_CAT_ID, getItem(position).getCid());
                    startActivity(callSubCategory);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView categoryNames;
        }
    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
