package rail.mini_project.projectrail;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Route extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Number_list> route = new ArrayList<Number_list>();
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        number = getIntent().getExtras().getString("Train_Number");

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(21, 78);
        PolylineOptions path = new PolylineOptions();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        try {
            route = new Trainnumber(number).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(route.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Connection Error, Please try again", Toast.LENGTH_SHORT).show();
            Intent intentMain = new Intent(Route.this,Input.class);

            Route.this.startActivity(intentMain);
            finish();
        }
        for (Number_list tlist: route){
            LatLng a;
            if(!tlist.getResp().equals("200")){
                Toast.makeText(getApplicationContext(),
                        "Invalid Train Number", Toast.LENGTH_SHORT).show();
                Intent intentMain = new Intent(Route.this,Input.class);
                Route.this.startActivity(intentMain);
                finish();
            }
            else {
                a = new LatLng(tlist.getLatitude(),
                        tlist.getLongitude());
                if (tlist.getArrival().equals("Source")) {
                    MarkerOptions marker_destination = new MarkerOptions().
                            position(a).title("Source Position " + tlist.getName() + ", " + tlist.getState()).
                            snippet("Departure Time " + tlist.getDeparture() + "\nDay " + tlist.getDay());
                    mMap.addMarker(marker_destination).setIcon(BitmapDescriptorFactory.defaultMarker
                            (BitmapDescriptorFactory.HUE_RED));
                }
                if (tlist.getDeparture().equals("Destination")) {
                    MarkerOptions marker_destination = new MarkerOptions().
                            position(a).title("Destination Position " + tlist.getName() + ", " + tlist.getState()).
                            snippet("Arrival Time " + tlist.getArrival() + "\nDay " + tlist.getDay());
                    mMap.addMarker(marker_destination).setIcon(BitmapDescriptorFactory.defaultMarker
                            (BitmapDescriptorFactory.HUE_RED));
                }
                MarkerOptions marker_destination = new MarkerOptions().
                        position(a);
                mMap.addMarker(marker_destination).setIcon(BitmapDescriptorFactory.defaultMarker
                        (BitmapDescriptorFactory.HUE_RED));
                path.add(a);
            }
        }
        Polyline line = mMap.addPolyline(path);
        line.setWidth(5);
        line.setColor(Color.BLUE);

        line.setGeodesic(true);

    }
}
