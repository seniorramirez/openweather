package com.example.kevinramirez.openweatcher;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.kevinramirez.openweatcher.Adapter.CityAdapter;
import com.example.kevinramirez.openweatcher.Model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public ImageView search;
    public Button searchCityButton;
    public AutoCompleteTextView autoCompleteTextView_city;
    public CityAdapter cityAdapter;
    public EditText cityField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new WeatcherFragment())
                    .commit();
        }
        search = (ImageView)findViewById(R.id.Search);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        //cityAdapter = new CityAdapter(this, android.R.layout.simple_dropdown_item_1line, getArrCity());
    }

    private ArrayList<City> getArrCity(){
        ArrayList<City> returnArray = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("city.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("listCity");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return returnArray;
    }



    private void showInputDialog(){
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        dialogBuilder.setTitle("Buscar Ciudad");
        View DialogView =  LayoutInflater.from(this).inflate(R.layout.layout_search_city, null);
        dialogBuilder.setView(DialogView);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();
        /*autoCompleteTextView_city = (AutoCompleteTextView) DialogView.findViewById(R.id.autocompleteCity);
        autoCompleteTextView_city.setAdapter(cityAdapter);
        autoCompleteTextView_city.setThreshold(3);*/
        cityField = (EditText) DialogView.findViewById(R.id.city_field);
        searchCityButton = (Button) DialogView.findViewById(R.id.accepterSearch);
        searchCityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                changeCity(cityField.getText().toString());
            }
        });
        alertDialog.show();
    }

    public void changeCity(String city){
        WeatcherFragment wf = (WeatcherFragment)getSupportFragmentManager()
                .findFragmentById(R.id.activity_main);
        wf.changeCity(capitalizarTexto(city));
    }

    public String capitalizarTexto(String text){
        String []palabras = text.split("\\s+");
        StringBuilder textoFormateado = new StringBuilder();

        for(String palabra : palabras){
            textoFormateado.append(palabra.substring(0,1).toUpperCase()
                    .concat( palabra.substring(1,palabra.length())
                            .toLowerCase()).concat(" "));
        }

        return textoFormateado.toString();
    }
}
