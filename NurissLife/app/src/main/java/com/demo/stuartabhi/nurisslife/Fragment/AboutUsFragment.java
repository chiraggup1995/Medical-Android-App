package com.demo.stuartabhi.nurisslife.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.demo.stuartabhi.nurisslife.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment  {

    private ViewFlipper iv1;
    private TextView tv1,tv2;

    public AboutUsFragment() {
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
        View root=inflater.inflate(R.layout.fragment_about_us,container,false);
        getActionBar().setTitle("About Us");
        getActionBar().setSubtitle("Read more");
        iv1=(ViewFlipper) root.findViewById(R.id.viewflip);
        iv1.startFlipping();
        iv1.setFlipInterval(7000);
        iv1.setInAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fadeout));
        tv1=(TextView)root.findViewById(R.id.abouttextlong);
        tv2=(TextView)root.findViewById(R.id.abouttextsmall);
        return root;
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity)getActivity()).getSupportActionBar();
    }
}
