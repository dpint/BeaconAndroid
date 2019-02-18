package dpint.si.app;

import java.util.List;

import dpint.si.beaconandroid.BeaconLocation;
import dpint.si.beaconandroid.Probe;

interface BeaconService{
    void startProbe(String beaconType);
    void stopProbe();
    boolean isProbeRunning();
    List<BeaconLocation> getBeacons();

    void startAdvertise(String beaconType, int port);
    void stopAdvertise(String beaconType);
    List<Probe> getRunningAdvertisements();
}
