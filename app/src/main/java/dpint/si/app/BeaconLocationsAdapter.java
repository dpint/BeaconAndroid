package dpint.si.app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dpint.si.beaconandroid.BeaconLocation;

public class BeaconLocationsAdapter extends RecyclerView.Adapter<BeaconLocationsAdapter.ViewHolder> {
    private List<BeaconLocation> beaconLocations;

    BeaconLocationsAdapter(List<BeaconLocation> beaconLocations) {
        this.beaconLocations = beaconLocations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BeaconLocation beaconLocation = beaconLocations.get(i);
        viewHolder.address.setText(beaconLocation.getAddress().getHostAddress());
        viewHolder.hostname.setText(beaconLocation.getData());
    }

    @Override
    public int getItemCount() {
        return beaconLocations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView address;
        TextView hostname;

        ViewHolder(View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.address);
            hostname = itemView.findViewById(R.id.hostname);
        }
    }
}
