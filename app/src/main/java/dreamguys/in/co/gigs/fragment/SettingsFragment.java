package dreamguys.in.co.gigs.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import dreamguys.in.co.gigs.ChangePassword;
import dreamguys.in.co.gigs.Login;
import dreamguys.in.co.gigs.MainActivity;
import dreamguys.in.co.gigs.PaypalSettings;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.UpdateProfile;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.SessionHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View settingsView;
    private ListView listSettings;
    private SettingsAdapter aSettingsAdapter;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        String[] settings = getResources().getStringArray(R.array.settings_data);
        listSettings = (ListView) settingsView.findViewById(R.id.lv_settings);

        if (SessionHandler.getInstance().get(getActivity(), Constants.USER_ID) == null) {
            settings[5] = "Login";
            aSettingsAdapter = new SettingsAdapter(getActivity(), settings);
        } else {
            settings[5] = "Logout";
            aSettingsAdapter = new SettingsAdapter(getActivity(), settings);
        }
        listSettings.setAdapter(aSettingsAdapter);


        return settingsView;
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

    private class SettingsAdapter extends BaseAdapter {
        Activity activity;
        final Context mContext;
        final String[] settings;
        final LayoutInflater mInflater;

        public SettingsAdapter(Context mContext, String[] settings) {
            this.mContext = mContext;
            this.settings = settings;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return settings.length;
        }

        @Override
        public String getItem(int position) {
            return settings[position];
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
                mHolder.txtSettingsItems = (TextView) convertView.findViewById(R.id.tv_settings_items);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            mHolder.txtSettingsItems.setText(getItem(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            if (SessionHandler.getInstance().get(mContext, Constants.USER_ID) != null) {
                                mContext.startActivity(new Intent(mContext, ChangePassword.class));
                            } else {
                                startActivity(new Intent(mContext, Login.class));
                                ((MainActivity) mContext).finish();
                            }

                            break;
                        case 1:
                            if (SessionHandler.getInstance().get(mContext, Constants.USER_ID) != null) {
                                mContext.startActivity(new Intent(mContext, UpdateProfile.class));
                            } else {
                                startActivity(new Intent(mContext, Login.class));
                                ((MainActivity) mContext).finish();
                            }

                            break;
                        case 2:
                            if (SessionHandler.getInstance().get(mContext, Constants.USER_ID) != null) {
                                mContext.startActivity(new Intent(mContext, PaypalSettings.class));
                            } else {
                                startActivity(new Intent(mContext, Login.class));
                                ((MainActivity) mContext).finish();
                            }
                            break;
                        case 5:
                            if (SessionHandler.getInstance().get(mContext, Constants.USER_ID) != null) {
                                removePrefs(mContext);
                                notifyDataSetChanged();
                                mContext.startActivity(new Intent(mContext, Login.class));
                                ((MainActivity) mContext).finish();
                            } else {
                                mContext.startActivity(new Intent(mContext, Login.class));
                                ((MainActivity) mContext).finish();
                            }
                            break;
                    }
                }
            });
            return convertView;
        }

        private void removePrefs(Context mContext) {
            SessionHandler.getInstance().remove(mContext, Constants.USER_ID);
            SessionHandler.getInstance().remove(mContext, Constants.EMAIL_ID);
            SessionHandler.getInstance().remove(mContext, Constants.USER_NAME);
        }

        class ViewHolder {
            TextView txtSettingsItems;
        }
    }
}
