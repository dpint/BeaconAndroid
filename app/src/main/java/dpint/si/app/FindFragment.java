package dpint.si.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import dpint.si.beaconandroid.Probe;

public class FindFragment extends Fragment{

    interface ProbeListener{
        void beaconFound(Bundle data);
        void beaconLost(Bundle data);
    }

    private RecyclerView beaconListRecyclerView;
    private EditText beaconTypeEditText;
    private Button findButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        beaconListRecyclerView = view.findViewById(R.id.beaconList);
        beaconTypeEditText = view.findViewById(R.id.beaconType);
        findButton = view.findViewById(R.id.findButton);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beaconType = beaconTypeEditText.getText().toString();
                if(beaconType.equals("")){
                    Toast.makeText(getActivity(), getString(R.string.empty_beacon_type),
                            Toast.LENGTH_LONG).show();
                }else if(findButton.getText().toString().equals(getString(R.string.find))){
                    findButton.setText(R.string.stop);
                    beaconTypeEditText.setEnabled(false);

                    try {
                        Probe p = new Probe(getActivity(), beaconType);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    findButton.setText(R.string.find);
                    beaconTypeEditText.setEnabled(true);
                }
            }
        });
    }
}
