package com.kongtech.plutocon.template;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kongtech.plutocon.sdk.Plutocon;
import com.kongtech.plutocon.sdk.PlutoconManager;
import com.kongtech.plutocon.template.distance.R;

import java.util.ArrayList;
import java.util.List;

public class PlutoconListActivity extends AppCompatActivity {

    private List<Plutocon> plutoconList;
    private PlutoconAdpater plutoconAdpater;
    private PlutoconManager plutoconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plutocon_list);

        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.getIndeterminateDrawable().setColorFilter(
                0xffffffff,
                android.graphics.PorterDuff.Mode.SRC_IN);

        plutoconAdpater = new PlutoconAdpater();
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(plutoconAdpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("PLUTOCON", plutoconList.get(position));
                PlutoconListActivity.this.setResult(1, intent);
                PlutoconListActivity.this.finish();
            }
        });
        plutoconList = new ArrayList<>();
        plutoconManager = new PlutoconManager(this);
        this.setResult(0, null);
    }

    private void startMonitoring() {
        plutoconManager.startMonitoring(PlutoconManager.MONITORING_FOREGROUND, new PlutoconManager.OnMonitoringPlutoconListener() {
            @Override
            public void onPlutoconDiscovered(Plutocon plutocon, final List<Plutocon> plutocons) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        plutoconList.clear();
                        plutoconList.addAll(plutocons);
                        plutoconAdpater.refresh();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        plutoconManager.connectService(new PlutoconManager.OnReadyServiceListener() {
            @Override
            public void onReady() {
                PlutoconListActivity.this.startMonitoring();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        plutoconManager.close();
    }

    private class PlutoconAdpater extends BaseAdapter {

        @Override
        public int getCount() {
            return plutoconList == null ? 0 : plutoconList.size();
        }

        @Override
        public Object getItem(int position) {
            return plutoconList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(PlutoconListActivity.this);
                convertView = inflater.inflate(R.layout.item_plutocon, parent, false);
            }

            Plutocon plutocon = plutoconList.get(position);
            TextView tvName = (TextView) convertView.findViewById(R.id.deviceName);
            TextView tvAddress = (TextView) convertView.findViewById(R.id.deviceAddress);
            TextView tvRSSI = (TextView) convertView.findViewById(R.id.deviceRSSI);
            TextView tvInterval = (TextView) convertView.findViewById(R.id.deviceInterval);

            tvName.setText(plutocon.getName());
            tvAddress.setText(plutocon.getMacAddress());
            tvRSSI.setText(plutocon.getRssi() + "dBm");
            tvInterval.setText(plutocon.getInterval() + "ms");

            return convertView;
        }

        private void refresh(){
            notifyDataSetChanged();
        }
    }
}
