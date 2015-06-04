package com.example.sasha.osmdroid.views.navdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sasha.osmdroid.R;
import com.google.android.gms.common.SignInButton;

/**
 * Created by sasha on 3/6/15.
 */
public class RegisrtrationFragment extends Fragment {
    View.OnClickListener onClickListener;
    SignInButton signIn;
    Button signOut;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, null);
        signIn = (SignInButton) view.findViewById(R.id.button6);
        signOut = (Button) view.findViewById(R.id.button7);
        signIn.setOnClickListener(onClickListener);
        signOut.setOnClickListener(onClickListener);
        return view;
    }
}
