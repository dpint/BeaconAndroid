package dpint.si.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import dpint.si.beaconandroid.Beacon;

public class AdvertiseFragment extends Fragment {

    private Button createButton;
    private EditText portEditText;
    private EditText beaconTypeEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advertise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        createButton = view.findViewById(R.id.createButton);
        portEditText = view.findViewById(R.id.beaconPort);
        beaconTypeEditText = view.findViewById(R.id.beaconType);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String portText = portEditText.getText().toString();
                String beaconType = beaconTypeEditText.getText().toString();
                Integer port = Utils.tryParseInt(portText);

                // TODO: 31.1.2019 Check for a beacon type length limit in the protocol
                if(beaconType.equals("") || portText.equals("")){
                    Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_LONG).show();
                }else if(port == null) {
                    Toast.makeText(getActivity(), R.string.port_not_number, Toast.LENGTH_LONG).show();
                }else if(port < 0 || port > 65535){
                    Toast.makeText(getActivity(), R.string.port_invalid_number, Toast.LENGTH_LONG).show();
                }else{
                    try {
                        new Beacon(getContext().getApplicationContext(), beaconType, port);
                        Toast.makeText(getActivity(), getString(R.string.beacon_created), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), getString(R.string.beacon_creation_fail), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
