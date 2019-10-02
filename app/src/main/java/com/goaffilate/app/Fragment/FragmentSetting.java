package com.goaffilate.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.goaffilate.app.LoginActivity;
import com.goaffilate.app.MainActivity;
import com.goaffilate.app.ProfileActivity;
import com.goaffilate.app.R;
import com.goaffilate.app.TransactionActivity;
import com.goaffilate.app.utils.Session_management;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSetting extends Fragment {
TextView share,logout,transactions,profile;
Session_management session_management;
LinearLayout l1,l2,l3,l4;
    public FragmentSetting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_fragment_setting, container, false);
        share=view.findViewById(R.id.share);
        transactions=view.findViewById(R.id.transactions);

        l1=view.findViewById(R.id.l1);
           l2=view.findViewById(R.id.l2);
           l3=view.findViewById(R.id.l3);
          l4= view.findViewById(R.id.l4);

          logout=view.findViewById(R.id.logout);
        profile=view.findViewById(R.id.myprofile);
        session_management=new Session_management(getContext());
        MainActivity.toolbar.setVisibility(View.GONE);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ProfileActivity.class));
                profile.setEnabled(false);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ProfileActivity.class));
                profile.setEnabled(false);
                l2.setEnabled(false);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             shareApp();
             share.setEnabled(false);
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                shareApp();
                share.setEnabled(false);
                l3.setEnabled(false);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_management.logoutSession();

                Intent intent=new Intent(getContext(), LoginActivity.class);
               startActivity(intent);
                Animatoo.animateSlideLeft(getActivity());
                logout.setEnabled(false);

            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_management.logoutSession();

                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(getActivity());
                logout.setEnabled(false);
                l4.setEnabled(false);
            }
        });
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session_management.logoutSession();

                Intent intent=new Intent(getContext(), TransactionActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(getActivity());
                transactions.setEnabled(false);
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session_management.logoutSession();

                Intent intent=new Intent(getContext(), TransactionActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(getActivity());
                transactions.setEnabled(false);
                l1.setEnabled(false);
            }
        });


        return view;

    }
    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
