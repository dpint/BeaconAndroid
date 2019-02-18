package dpint.si.beaconandroid;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class Beacon {
    final static int DISCOVERY_PORT = 35891;

    private String beaconType;
    private byte[] response;

    private int port;
    private AtomicBoolean isClosed;

    private final DatagramSocket socket;

    private ProbeReceiver probeReceiver;

    public Beacon(Context context, String beaconType, int port) throws IOException{
        this.beaconType = beaconType;
        this.port = port;

        socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.setBroadcast(true);
        socket.bind(new InetSocketAddress(DISCOVERY_PORT));

        isClosed = new AtomicBoolean(false);

        probeReceiver = new ProbeReceiver();
        probeReceiver.start();
    }

    public void close(){
        if(!isClosed.getAndSet(true)){
            socket.close();
        }
    }

    private class ProbeReceiver extends Thread {
        byte[] buffer = new byte[256];

        public void run() {
            while(true){
                DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(recv);
                } catch (IOException e) {
                    if(!socket.isClosed()){
                        socket.close();
                    }
                    isClosed.set(true);
                }

                byte[] message = encode(beaconType);
                if (hasPrefix(message, recv.getData(), recv.getLength())) {
                    DatagramPacket packet = new DatagramPacket(message, message.length,
                            recv.getAddress(), recv.getPort());
                    try {
                        socket.send(packet);
                    } catch (IOException e) {
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

    static boolean hasPrefix(byte[] prefix, byte[] message, int messageLength){
        if(messageLength >= prefix.length){
            for(int i = 0; i < messageLength; i++){
                if(prefix[i] != message[i]){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    static String decode(byte[] message){
        return new String(message, Charset.forName("UTF-8"));
    }

    static byte[] encode(String message){
        return message.getBytes(Charset.forName("UTF-8"));
    }
}
