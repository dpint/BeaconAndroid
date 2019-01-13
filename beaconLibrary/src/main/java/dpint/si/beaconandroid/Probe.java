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
    private int beaconTimeout;
    private String beaconType;

    private AtomicBoolean isClosed;

    private final DatagramChannel channel;
    private final DatagramSocket socket;

    private ProbeBroadcaster probeBroadcaster;

    public Probe(Context context, String beaconType) throws IOException{
        this(context, beaconType, 5000);
    }

    public Probe(Context context, String beaconType, int beaconTimeout) throws IOException{
        this.beaconTimeout = beaconTimeout;
        this.beaconType = beaconType;

        channel = DatagramChannel.open();
        socket = channel.socket();
        socket.setReuseAddress(true);
        socket.setBroadcast(true);
        socket.bind(new InetSocketAddress(Beacon.DISCOVERY_PORT));

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
                    Utils.getBroadcastIpAddress(), socket.getLocalPort());

            while(true){
                try {
                    socket.send(packet);
                    Thread.sleep(2000);
                } catch (IOException|InterruptedException e) {
                    if(!socket.isClosed()){
                        socket.close();
                    }
                    isClosed.set(true);
                }
            }
        }
    }
}
