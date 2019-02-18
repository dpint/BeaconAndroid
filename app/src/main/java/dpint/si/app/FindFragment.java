package dpint.si.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindFragment extends Fragment {

    private RecyclerView beaconListRecyclerView;
    private EditText beaconTypeEditText;
    private Button findButton;

    private LinearLayoutManager layoutManager;
    public static BeaconLocationsAdapter beaconLocationsAdapter;

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

        layoutManager = new LinearLayoutManager(getActivity());
        beaconListRecyclerView.setLayoutManager(layoutManager);
        beaconListRecyclerView.setHasFixedSize(true);
        beaconLocationsAdapter = new BeaconLocationsAdapter(MainActivity.currentBeacons);
        beaconListRecyclerView.setAdapter(beaconLocationsAdapter);

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
                    ((MainActivity)getActivity()).startProbe(beaconTypeEditText.getText().toString());
                }else{
                    findButton.setText(R.string.find);
                    beaconTypeEditText.setEnabled(true);
                }
            }
        });
    }
}
