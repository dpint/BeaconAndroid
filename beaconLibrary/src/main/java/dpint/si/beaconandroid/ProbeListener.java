package dpint.si.beaconandroid;

public interface ProbeListener {
    void onBeaconFound(BeaconLocation beaconLocation);
    void onBeaconLost(BeaconLocation beaconLocation);
}
