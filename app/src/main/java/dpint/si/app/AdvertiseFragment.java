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

enum Action {
    START,
    STOP
}

public class AdvertiseFragment extends Fragment {

    private Button advertiseButton;
    private Button stopButton;
    private EditText portEditText;
    private EditText beaconTypeEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advertise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        advertiseButton = view.findViewById(R.id.advertiseButton);
        stopButton = view.findViewById(R.id.stopButton);
        portEditText = view.findViewById(R.id.beaconPort);
        beaconTypeEditText = view.findViewById(R.id.beaconType);

        advertiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertise(Action.START);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertise(Action.STOP);
            }
        });
    }

    private void advertise(Action action) {
        String portText = portEditText.getText().toString();
        String beaconType = beaconTypeEditText.getText().toString();
        Integer port = Utils.tryParseInt(portText);

        beaconTypeEditText.setText(R.string.empty);
        portEditText.setText(R.string.empty);

        if(checkInput(portText, beaconType, port)) {
            if(((MainActivity)getActivity()).isAdvertisementRunning(beaconType, port)){
                if(action == Action.START){
                    Toast.makeText(getActivity(), R.string.beacon_exists, Toast.LENGTH_LONG).show();
                }else{
                    ((MainActivity)getActivity()).stopAdvertise(beaconType, port);
                    Toast.makeText(getActivity(), getString(R.string.beacon_stopped),
                            Toast.LENGTH_LONG).show();
                }
            }else{
                if(action == Action.START){
                    ((MainActivity)getActivity()).startAdvertise(beaconType, port);
                    Toast.makeText(getActivity(), getString(R.string.beacon_created),
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), R.string.beacon_not_exist,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean checkInput(String portText, String beaconType, Integer port) {
        // TODO: 31.1.2019 Check for a beacon type length limit in the protocol
        if(beaconType.equals("") || portText.equals("")){
            Toast.makeText(getActivity(), R.string.empty_fields, Toast.LENGTH_LONG).show();
            return false;
        }else if(port == null) {
            Toast.makeText(getActivity(), R.string.port_not_number, Toast.LENGTH_LONG).show();
            return false;
        }else if(port < 0 || port > 65535){
            Toast.makeText(getActivity(), R.string.port_invalid_number, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
