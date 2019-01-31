package dpint.si.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import java.util.List;

import dpint.si.beaconandroid.BeaconLocation;

public class MainActivity extends AppCompatActivity implements BeaconService {
    Fragment findFragment;
    Fragment advertiseFragment;

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
        return null;
    }

    @Override
    public void startAdvertise(String beaconType) {

    }

    @Override
    public void stopAdvertise(String beaconType) {

    }

    @Override
    public List<String> getRunningAdvertisements() {
        return null;
    }
}
