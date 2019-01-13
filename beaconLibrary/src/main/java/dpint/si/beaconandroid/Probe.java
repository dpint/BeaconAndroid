package dpint.si.beaconandroid;

import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Probe {
    private final static int PROBE_INTERVAL = 2000;

    private int beaconTimeout;
    private String beaconType;

    private AtomicBoolean isClosed;

    private final DatagramSocket socket;

    private ProbeBroadcaster probeBroadcaster;

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
}
