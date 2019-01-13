package dpint.si.beaconandroid;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Utils {

    @Nullable
    public static InetAddress getIpAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if(wifiManager == null){
            return null;
        }

        int ipInt = wifiManager.getConnectionInfo().getIpAddress();
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)){
            ipInt = Integer.reverseBytes(ipInt);
        }
        byte[] ipByte = BigInteger.valueOf(ipInt).toByteArray();

        InetAddress address = null;
        try{
            address = InetAddress.getByAddress(ipByte);
        }catch (UnknownHostException ignored){}

        return address;
    }

    @Nullable
    static InetAddress getBroadcastIpAddress(){
        InetAddress broadcastAddress = null;
        Enumeration<NetworkInterface> interfaces;

        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return null;
        }

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            try {
                if (!networkInterface.isLoopback()) {
                    for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                        if (address.getBroadcast() != null) {
                            broadcastAddress = address.getBroadcast();
                        }
                    }
                }
            } catch (SocketException ignored){}
        }

        return broadcastAddress;
    }
}
