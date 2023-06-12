package com.mariaokon.aptekiszukaj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String name [] = {};
    Button btn1, btn2;
    ArrayAdapter<String> arrayAdapter;
    SearchView searchView;
    String searchText = "";
    TextView mTextviewresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        listView = findViewById(R.id.listview);
        List<String> data = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        listView.setAdapter(arrayAdapter);





        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Dzialam", Toast.LENGTH_SHORT).show();
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "https://rejestrymedyczne.ezdrowie.gov.pl/api/pharmacies/search?page=0&size=10&sortField=dateOfChanged&sortDirection=DESC&pharmacyCity=" + searchText;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    arrayAdapter.clear();
                                    arrayAdapter.add("By znaleźć aptekę na mapie, wystarczy wpisać jej adres w powyższe pole wyszukiwania oraz użyć przycisku MAPA");

                                    //zmienny indeks array!
                                    String rr = response.toString();
                                    JSONObject jo = new JSONObject(rr);
                                    Iterator<String> keys = response.keys();
                                    String pierwszyKlucz = keys.next();
                                    JSONArray jsonArray = response.getJSONArray(pierwszyKlucz);
                                    for (int i = 0; i <jsonArray.length(); i++){
                                        JSONObject apteki = jsonArray.getJSONObject(i);
                                        String nazwaApteki = apteki.getString("name");
                                        String nrTel = null;
                                        String kod = null;
                                        String ulica = null;
                                        String numer = null;
                                        String miasto = null;
                                        String czyOtwartaWNN = null;
                                        if (nazwaApteki != "" || kod == "NIEAKTYWNA"){
                                            nrTel = apteki.getString("phoneNumber");
                                            //JSONObject adres = response.getJSONObject("address");
                                            JSONObject kodwstep = apteki.getJSONObject("pharmacyStatus");
                                            kod = kodwstep.getString("code");
                                            //probowalam petla - wystarczy rozpakowac jsonobject by dostac sie do nastepnego
                                            JSONObject adres = apteki.getJSONObject("address");
                                            ulica = adres.getString("street");
                                            numer = adres.getString("homeNumber");
                                            miasto = adres.getString("city");
                                            czyOtwartaWNN = apteki.getString("openOnSundaysNonTrade");
                                            if (czyOtwartaWNN == "true"){
                                                czyOtwartaWNN = "Apteka jest otwarta w niedziele niehandlowe";
                                            }
                                            else{
                                                czyOtwartaWNN = "Apteka jest zamknięta w niedziele niehandlowe";
                                            }
                                            //Toast.makeText(MainActivity.this, nazwaApteki, Toast.LENGTH_SHORT).show();
                                            arrayAdapter.add("Nazwa apteki: "+nazwaApteki + ", " + "\n" +
                                                    "Numer kontaktowy: " + nrTel + "\n" + "Status: " + kod + "\n" + "Lokalizacja: " + "\n" +
                                                    ulica + " " + numer + ", " + miasto + "\n" + czyOtwartaWNN);
                                        }
                                        else{
                                            nazwaApteki = "Brak danych";
                                        }



                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                queue.add(request);




//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(MainActivity.this,"Responce: " + response.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });


//                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        //List<String> nazwyAptek = new ArrayList<>();
//                        String nazwaApteki = "";
//                        try {
//
//                                JSONObject info = response.getJSONObject(0);
//                                nazwaApteki = info.getString("name");
//                                //nazwyAptek.add(nazwaApteki);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Toast.makeText(MainActivity.this,"Nazwa apteki" + nazwaApteki, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Obsługa błędu
//                    }
//                });
//
//                queue.add(request);

                //StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                  //  @Override
                    //public void onResponse(String response) {
                     //   Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

               //     }
               // }, new Response.ErrorListener() {
                //    @Override
                //    public void onErrorResponse(VolleyError error) {
                //        Toast.makeText(MainActivity.this,"Błąd", Toast.LENGTH_SHORT).show();
                //    }
                //});
                //queue.add(stringRequest);

                //Toast.makeText(MainActivity.this, "Typed: " + searchText
                  //      , Toast.LENGTH_SHORT).show();


            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Clicked me 2" +searchText, Toast.LENGTH_SHORT).show();
                if (searchText.equals("")){
                    Toast.makeText(MainActivity.this,"Wpisz adres by otworzyć mapę",Toast.LENGTH_SHORT).show();
                }
                else{

                    Uri uri = Uri.parse("https://www.google.com/maps/dir/"+  "/"+  searchText);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Wpisz nazwę miasta");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                searchText = newText;
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);

    }
}