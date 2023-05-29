package com.mariaokon.aptekiszukaj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.DownloadManager;
import android.content.ClipData;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String name [] = {};
    Button btn1, btn2;
    ArrayAdapter<String> arrayAdapter;
    SearchView searchView;
    Menu menu;
    String searchText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        listView = findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,name);
        listView.setAdapter(arrayAdapter);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "https://rejestrymedyczne.ezdrowie.gov.pl/api/pharmacies/search?page=0&size=10&sortField=dateOfChanged&sortDirection=DESC&pharmacyCity=Tychy";

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //List<String> nazwyAptek = new ArrayList<>();
                        String nazwaApteki = "";
                        try {

                                JSONObject info = response.getJSONObject(0);
                                nazwaApteki = info.getString("name");
                                //nazwyAptek.add(nazwaApteki);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this,"Nazwa apteki" + nazwaApteki, Toast.LENGTH_SHORT).show();

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Obsługa błędu
                    }
                });
                queue.add(request);

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
                Toast.makeText(MainActivity.this,"Clicked me 2" +searchText, Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        this.menu = menu;
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