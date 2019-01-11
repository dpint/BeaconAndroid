package dpint.si.app;

import java.util.List;

import dpint.si.beaconandroid.BeaconLocation;

interface BeaconService{
    void startProbe(String beaconType);
    void stopProbe();
    boolean isProbeRunning();
    List<BeaconLocation> getBeacons();

    void startAdvertise(String beaconType);
    void stopAdvertise(String beaconType);
    List<String> getRunningAdvertisements();
}
