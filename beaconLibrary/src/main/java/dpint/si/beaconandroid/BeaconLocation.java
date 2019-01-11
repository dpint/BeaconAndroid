package dpint.si.beaconandroid;

import java.net.InetAddress;
import java.util.Date;

public class BeaconLocation {
    private InetAddress address;
    private String data;
    private Date lastAdvertised;

    public BeaconLocation(InetAddress address, String data, Date lastAdvertised){
        this.address = address;
        this.data = data;
        this.lastAdvertised = lastAdvertised;
    }

    public InetAddress getAddress() {
        return address;
    }

    public String getData() {
        return data;
    }

    public Date getLastAdvertised() {
        return lastAdvertised;
    }
}