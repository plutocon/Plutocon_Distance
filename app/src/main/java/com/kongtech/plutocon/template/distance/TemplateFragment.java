package com.kongtech.plutocon.template.distance;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kongtech.plutocon.sdk.Plutocon;
import com.kongtech.plutocon.sdk.PlutoconManager;
import com.kongtech.plutocon.template.PlutoconListActivity;
import com.kongtech.plutocon.template.view.AttrItemView;
import com.kongtech.view.MultipleSeekBar;

import java.util.List;

public class TemplateFragment extends Fragment implements View.OnClickListener {

    private AttrItemView aivTargetName;
    private AttrItemView aivTargetAddress;
    private AttrItemView aivDistance;

    private MultipleSeekBar multipleSeekBar;

    private PlutoconManager plutoconManager;
    private Plutocon targetPlutocon;

    public static Fragment newInstance(Context context) {
        TemplateFragment f = new TemplateFragment();
        return f;
    }

    public void startMonitoring(){

        plutoconManager.startMonitoring(PlutoconManager.MONITORING_FOREGROUND, new PlutoconManager.OnMonitoringPlutoconListener() {
            @Override
            public void onPlutoconDiscovered(final Plutocon plutocon, List<Plutocon> plutocons) {
                if(plutocon.getMacAddress().equals(targetPlutocon.getMacAddress())) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int rssi = plutocon.getRssi() * -1;
                            multipleSeekBar.setIndicator(rssi);

                            int level1 = multipleSeekBar.getMinValue();
                            int level2 = multipleSeekBar.getMaxValue();

                            if(level1 > rssi) {
                                aivDistance.setAttrValueColor(0xff00ff00);
                                aivDistance.setValue("Near");
                            }
                            else if(level2 > rssi) {
                                aivDistance.setAttrValueColor(0xff0000ff);
                                aivDistance.setValue("Far");
                            }
                            else {
                                aivDistance.setAttrValueColor(0xffff0000);
                                aivDistance.setValue("Too Far");
                            }

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(targetPlutocon != null){
            this.startMonitoring();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        plutoconManager.stopMonitoring();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        plutoconManager.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_template, null);

        aivTargetName = (AttrItemView)view.findViewById(R.id.aivTargetName);
        aivTargetAddress = (AttrItemView)view.findViewById(R.id.aivTargetAddress);
        aivDistance = (AttrItemView)view.findViewById(R.id.aivDistance);

        aivTargetName.setOnClickListener(this);
        multipleSeekBar = (MultipleSeekBar)view.findViewById(R.id.multipleSeekBar);
        return view;
    }

    @Override
    public void onStart() {
        plutoconManager = new PlutoconManager(this.getContext());
        plutoconManager.connectService(null);
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if(checkPermission())
            startActivityForResult(new Intent(getActivity(), PlutoconListActivity.class), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if(resultCode == 1) {
                    targetPlutocon = (Plutocon)data.getParcelableExtra("PLUTOCON");
                    aivTargetName.setValue(targetPlutocon.getName());
                    aivTargetAddress.setValue(targetPlutocon.getMacAddress());

                    this.startMonitoring();
                }
            break;
        }
    }

    private boolean checkPermission(){
        BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        if((mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())){
            startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            return false;
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }

            LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            if(!gps_enabled){
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                return false;
            }
        }
        return true;
    }
}
