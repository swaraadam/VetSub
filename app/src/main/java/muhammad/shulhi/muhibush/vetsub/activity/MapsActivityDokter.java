package muhammad.shulhi.muhibush.vetsub.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import muhammad.shulhi.muhibush.vetsub.R;
import muhammad.shulhi.muhibush.vetsub.model.Dokter;
import muhammad.shulhi.muhibush.vetsub.services.ApiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivityDokter extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION = 500;
    private List<Dokter> mTokoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_dokter);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(MapsActivityDokter.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapsActivityDokter.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivityDokter.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        ApiServices.service_get.dokter_getall().enqueue(new Callback<ArrayList<Dokter>>() {
            @Override
            public void onResponse(Call<ArrayList<Dokter>> call, Response<ArrayList<Dokter>> response) {
                mTokoList = response.body();
                MarkerOptions markerOptions;
                Dokter toko;

                for (int i=0;i<mTokoList.size();i++){
                    toko = mTokoList.get(i);

                    markerOptions = new MarkerOptions()
                            .position(new LatLng(toko.getPosisi().getLat(),toko.getPosisi().getLon()))
                            .title(toko.getNama())
                            .snippet(toko.getJam().getBuka()+"-"+toko.getJam().getTutup());
                    mMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Dokter>> call, Throwable t) {
                Toast.makeText(MapsActivityDokter.this,"Mohon maaf terjadi gangguan dengan jaringan Anda", Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnInfoWindowClickListener(this);

        LatLng surabaya = new LatLng(-7.255530, 112.752102);
        final CameraPosition cameraPosition = new CameraPosition.Builder().target(surabaya).zoom((float) 11.5).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
//        searchEvent("Healthy Lifestyle Talkshow with Favourite Beauty Care");
        for (int i = 0;i<mTokoList.size();i++){
            if (mTokoList.get(i).getNama().equals(marker.getTitle())){
//                Toast.makeText(MapsActivityDokter.this,mTokoList.get(i).getNama(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MapsActivityDokter.this, ProfileActivityToko.class);
                intent.putExtra(ProfileActivityToko.EXTRA_ID_PROFILE,mTokoList.get(i).get_id());
                intent.putExtra(ProfileActivityToko.EXTRA_JENIS_LAYANAN,"dokter");
                startActivity(intent);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    onResume();
                }
                break;
        }
    }
}
