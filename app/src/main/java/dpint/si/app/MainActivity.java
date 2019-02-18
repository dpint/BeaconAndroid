package dpint.si.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dpint.si.beaconandroid.Beacon;
import dpint.si.beaconandroid.BeaconLocation;
import dpint.si.beaconandroid.Probe;
import dpint.si.beaconandroid.ProbeListener;

public class MainActivity extends AppCompatActivity implements BeaconService, ProbeListener {
    Fragment findFragment;
    Fragment advertiseFragment;

    public static List<BeaconLocation> currentBeacons = new ArrayList<>();
    private List<Probe> runningAdvertisements = new ArrayList<>();

    private Handler mHandler;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    if(findFragment == null){
                        findFragment = new FindFragment();
                    }
                    showFragment(new FindFragment());
                    return true;
                case R.id.navigation_advertise:
                    if(advertiseFragment == null){
                        advertiseFragment = new AdvertiseFragment();
                    }
                    showFragment(new AdvertiseFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        showFragment(new FindFragment());
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void startProbe(String beaconType) {
        try {
            Probe p = new Probe(MainActivity.this.getApplicationContext(), beaconType);
            runningAdvertisements.add(p);
            registerListener(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopProbe() {

    }

    @Override
    public boolean isProbeRunning() {
        return false;
    }

    @Override
    public List<BeaconLocation> getBeacons() {
        return currentBeacons;
    }

    @Override
    public void startAdvertise(String beaconType, int port) {
        try {
            new Beacon(MainActivity.this.getApplicationContext(), beaconType, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopAdvertise(String beaconType) {

    }

    @Override
    public List<Probe> getRunningAdvertisements() {
        return runningAdvertisements;
    }

    private void registerListener(Probe p) {
        p.registerProbeListener(this);
    }

    @Override
    public void onBeaconFound(final BeaconLocation beaconLocation) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                currentBeacons.add(beaconLocation);
                FindFragment.beaconLocationsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBeaconLost(BeaconLocation beaconLocation) {

    }
}
