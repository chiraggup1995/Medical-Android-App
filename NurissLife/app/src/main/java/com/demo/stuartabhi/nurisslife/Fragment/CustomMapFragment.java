package com.demo.stuartabhi.nurisslife.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by stuartabhi on 6/1/2016.
 */
public class CustomMapFragment extends SupportMapFragment {

    private static final LatLng NurissLife = new LatLng(28.602012, 77.098750);
    private GoogleMap googleMap;



    @Override
    public void onActivityCreated(Bundle bundle) {
        googleMap=getMap();
        googleMap.addMarker(new MarkerOptions().position(NurissLife).title("Nuriss LifeCare"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NurissLife,15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);
        super.onActivityCreated(bundle);
    }
}
