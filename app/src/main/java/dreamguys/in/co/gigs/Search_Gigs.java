package dreamguys.in.co.gigs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dreamguys.in.co.gigs.Model.GETCategory;
import dreamguys.in.co.gigs.Model.GETCountry;
import dreamguys.in.co.gigs.Model.GETState;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user5 on 27-12-2017.
 */

public class Search_Gigs extends AppCompatActivity {


    private Spinner spinCountry, spinState, spinCategory;
    private GETCountry[] getCountryLists;
    private Gson gson;

    private final List<String> addCountryLists = new ArrayList<String>();
    private final List<GETCategory.Primary> addCategoryLists = new ArrayList<GETCategory.Primary>();
    private final List<String> addStateLists = new ArrayList<String>();
    private final List<String> addCatLists = new ArrayList<String>();
    private String country = "";
    private String state = "";
    private Button searchGigs;
    private List<GETState> stateData;
    private EditText inputTitle;
    private Toolbar mToolbar;
    String cat_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_gigs);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
        spinCountry = (Spinner) findViewById(R.id.spinner_counrty);
        spinState = (Spinner) findViewById(R.id.spinner_state);
        spinCategory = (Spinner) findViewById(R.id.spinner_category);
        searchGigs = (Button) findViewById(R.id.button_search);
        inputTitle = (EditText) findViewById(R.id.et_title);
        gson = new Gson();
        getCategoryLists();

        if (SessionHandler.getInstance().get(Search_Gigs.this, Constants.COUNTRY_JSON) != null) {

            getCountryLists = gson.fromJson(SessionHandler.getInstance().get(Search_Gigs.this, Constants.COUNTRY_JSON), GETCountry[].class);

            for (GETCountry getCountryList : getCountryLists) {
                addCountryLists.add(getCountryList.getCountry());
            }
            if (addCountryLists.size() > 0) {
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Search_Gigs.this,
                        android.R.layout.simple_spinner_item, addCountryLists);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCountry.setAdapter(adapter);


                spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (addCountryLists.get(position).equalsIgnoreCase(getCountryLists[position].getCountry())) {
                            country = getCountryLists[position].getId();
                            if (!country.isEmpty())
                                getStateAPI();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        }


    }


    private void getCategoryLists() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getCategories().enqueue(new Callback<GETCategory>() {
            @Override
            public void onResponse(Call<GETCategory> call, Response<GETCategory> response) {

                if (response.body().getCode().equals(200)) {
                    if (response.body().getPrimary().size() > 0) {

                        for (GETCategory.Primary getCategory : response.body().getPrimary()) {
                            addCatLists.add(getCategory.getName());
                            addCategoryLists.add(getCategory);
                        }
                        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(Search_Gigs.this,
                                android.R.layout.simple_spinner_item, addCatLists);
                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinCategory.setAdapter(categoryAdapter);

                        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cat_id = addCategoryLists.get(position).getCid();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });



                        /*aCategoryListAdapter = new CategoryList.CategoryListAdapter(CategoryList.this, response.body().getPrimary());
                        categoryLists.setAdapter(aCategoryListAdapter);*/
                    } else {
                        Utils.toastMessage(Search_Gigs.this, response.body().getMessage());
                    }

                } else {
                    Utils.toastMessage(Search_Gigs.this, response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<GETCategory> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });
    }


    private void getStateAPI() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getState(country).enqueue(new Callback<List<GETState>>() {
            @Override
            public void onResponse(Call<List<GETState>> call, final Response<List<GETState>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        stateData = response.body();
                        addStateLists.clear();
                        for (GETState getState : response.body()) {
                            addStateLists.add(getState.getState_name());
                        }
                        if (addStateLists.size() > 0 && stateData.size() > 0) {
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Search_Gigs.this,
                                    android.R.layout.simple_spinner_item, addStateLists);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinState.setAdapter(adapter);

                            spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (addStateLists.get(position).equalsIgnoreCase(stateData.get(position).getState_name())) {
                                        state = stateData.get(position).getState_id();
//                                        Toast.makeText(Register.this, "ID: " + state, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<GETState>> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });
    }

    public void SearchGigs(View view) {
        Intent callSearchActivity = new Intent(Search_Gigs.this, SearchGigsList.class);
        callSearchActivity.putExtra("cat_id", cat_id);
        callSearchActivity.putExtra("state", state);
        callSearchActivity.putExtra("country", country);
        callSearchActivity.putExtra("title", inputTitle.getText().toString());
        startActivity(callSearchActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
