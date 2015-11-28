package rail.mini_project.projectrail;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Destination extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int count =0;
    LatLng dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 1) {

                    Intent intentMain = new Intent(Destination.this,
                            Schedule.class);
                    LatLng source = (LatLng)getIntent().getExtras().get("Source");
                    intentMain.putExtra("Source",source);
                    intentMain.putExtra("Destination", dest);
                    Destination.this.startActivity(intentMain);
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Select Destination", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(21, 78);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng destination) {
                    if (count == 0) {
                        MarkerOptions marker = new MarkerOptions().position
                                (destination).title("Destination");
                        mMap.addMarker(marker);
                        dest = destination;
                        count++;
                    }
                }
            });
        }
}
