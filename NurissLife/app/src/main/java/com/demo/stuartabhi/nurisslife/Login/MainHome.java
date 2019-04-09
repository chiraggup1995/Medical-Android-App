package com.demo.stuartabhi.nurisslife.Login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.stuartabhi.nurisslife.Fragment.ShareFragment;
import com.demo.stuartabhi.nurisslife.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainHome extends Fragment {
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;

    public MainHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActionBar().setTitle("Nuriss Home");
        getActionBar().setSubtitle("M.R.");
        ScrollView sv2=(ScrollView)inflater.inflate(R.layout.fragment_main_home,container,false);
        txtName=(TextView)sv2.findViewById(R.id.name);
        txtEmail=(TextView)sv2.findViewById(R.id.email);
        btnLogout=(Button)sv2.findViewById(R.id.btnLogout);
        db=new SQLiteHandler(getActivity());
        session=new SessionManager(getActivity());
        if(!session.isLoggedIn())
        {
            logoutUser();
        }
        HashMap<String,String> user=db.getUserDetails();
        String name=user.get("name");
        String email=user.get("email");

        txtName.setText(name);
        txtEmail.setText(email);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        return sv2;
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame,new ShareFragment()).addToBackStack(null).commit();
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity)getActivity()).getSupportActionBar();
    }

}
