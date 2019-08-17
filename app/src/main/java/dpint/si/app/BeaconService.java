package dpint.si.app;

import java.util.List;

import dpint.si.beaconandroid.Beacon;
import dpint.si.beaconandroid.BeaconLocation;
import dpint.si.beaconandroid.Probe;

interface BeaconService{
    void startProbe(String beaconType);
    void stopProbe(String beaconType);
    boolean isProbeRunning();
    Probe getRunningProbe();

    List<BeaconLocation> getFoundBeacons();

    void startAdvertise(String beaconType, int port);
    void stopAdvertise(String beaconType, int port);
    boolean isAdvertisementRunning(String beaconType, int port);
    List<Beacon> getRunningAdvertisements();
}
