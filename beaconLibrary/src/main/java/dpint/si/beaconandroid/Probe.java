package dpint.si.beaconandroid;

import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class Probe {
    private final static int PROBE_INTERVAL = 2000;

    private int beaconTimeout;
    private String beaconType;

    private AtomicBoolean isClosed;

    private DatagramSocket socket;

    private ProbeBroadcaster probeBroadcaster;
    private ProbeReceiver probeReceiver;

    private ProbeListener probeListener;

    public Probe(Context context, String beaconType) throws IOException{
        this(context, beaconType, 5000);
    }

    public Probe(Context context, String beaconType, int beaconTimeout) throws IOException{
        this.beaconTimeout = beaconTimeout;
        this.beaconType = beaconType;

        socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.setBroadcast(true);
        socket.bind(new InetSocketAddress(0));

        isClosed = new AtomicBoolean(false);

        probeBroadcaster = new ProbeBroadcaster();
        probeBroadcaster.start();

        probeReceiver = new ProbeReceiver();
        probeReceiver.start();
    }

    public String getBeaconType() {
        return beaconType;
    }

    public void registerProbeListener(ProbeListener probeListener) {
        this.probeListener = probeListener;
    }

    public void close(){
        if(!isClosed.getAndSet(true)){
            socket.close();
            probeBroadcaster.interrupt();
        }
    }

    private class ProbeBroadcaster extends Thread {
        public void run() {
            byte[] message = Beacon.encode(beaconType);
            DatagramPacket packet = new DatagramPacket(message, message.length,
                    Utils.getBroadcastIpAddress(), Beacon.DISCOVERY_PORT);

            while(true){
                try {
                    socket.send(packet);
                    Thread.sleep(PROBE_INTERVAL);
                } catch (IOException|InterruptedException e) {
                    if(!socket.isClosed()){
                        socket.close();
                    }
                    isClosed.set(true);
                    break;
                }
            }
        }
    }

    private class ProbeReceiver extends Thread {
        byte[] buffer = new byte[256];

        public void run() {
            while(true) {
                DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(recv);
                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                    isClosed.set(true);
                    break;
                }

                byte[] message = Beacon.encode(beaconType);
                if (Beacon.hasPrefix(message, recv.getData(), recv.getLength())) {
                    InetAddress inetAddress = recv.getAddress();
                    String data = Beacon.decode(recv.getData());

                    newBeacon(new BeaconLocation(inetAddress, data, new Date()));
                }
            }
        }
    }

    private void newBeacon(BeaconLocation newBeacon) {
        probeListener.onBeaconFound(newBeacon);
    }
}
