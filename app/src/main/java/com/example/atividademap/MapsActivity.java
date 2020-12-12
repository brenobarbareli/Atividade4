package com.example.atividademap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng initialPosition = new LatLng(42, 10);

        // Código a ser implementado chamando o Volley para preencher os Markers com a response da API
        String url = "https://restcountries.eu/rest/v2/lang/it";
        StringRequest request = new StringRequest(
            Request.Method.GET,
            url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("--> Response.Sucesso");
                    System.out.println(response);
                    try {
                        JSONArray arr = new JSONArray(response);
                        System.out.println("--> array convertido");
                        System.out.println(arr);
                        for (int i=0; i < arr.length(); i++) {
                            System.out.println("nome obtido");
                            String name = arr.getJSONObject(i).getString("name").toString();
                            System.out.println(name);
                            System.out.println("lista de posições no mapa");
                            JSONArray geo = arr.getJSONObject(i).getJSONArray("latlng");
                            List<Long> listGeo = new ArrayList<Long>();
                            for (int j=0; j < geo.length(); j++) {
                                System.out.println("valor da posicao");
                                System.out.println(geo.get(j).toString());
                                listGeo.add(geo.getLong(j));
                            }
                            System.out.println("Posição e novo mapa");
                            LatLng position = new LatLng(listGeo.get(0), listGeo.get(1));
                            mMap.addMarker(new MarkerOptions().position(position).title(name));
                        }

                    } catch (JSONException e) {
                        System.out.println("erro na conversão: " + e.toString());
                    }
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("--> response.erro");
                    System.err.println(error);
                }
            }
        );
        MySingleton.getInstance(MapsActivity.this).addToRequestQueue(request);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(initialPosition));
    }
}