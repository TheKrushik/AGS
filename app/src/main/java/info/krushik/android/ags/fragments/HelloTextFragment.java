package info.krushik.android.ags.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.krushik.android.ags.R;

public class HelloTextFragment extends Fragment {

    private static final String EXTRA_USER_NAME = "info.krushik.android.ags.USER_NAME";

    private String mUserName;


    public static HelloTextFragment newInstance(String userName) {
        HelloTextFragment fragmentHello = new HelloTextFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_USER_NAME, userName);

        fragmentHello.setArguments(args);

        return fragmentHello;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hello_text, container, false);

        Bundle args = getArguments();
        mUserName = args.getString(EXTRA_USER_NAME);

        TextView textView = (TextView) view.findViewById(R.id.tvHello);
        textView.setText(mUserName + ", выберите действие...");

        return view;
    }

}
