package com.example.kevinramirez.openweatcher;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kevinramirez.openweatcher.Misc.Connection;
import com.example.kevinramirez.openweatcher.Misc.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatcherFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public String cityValidate = "";

    Typeface weatherFont;
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    Connection conex;
    Handler handler;

    public WeatcherFragment() {}

    public static WeatcherFragment newInstance(String param1, String param2) {
        WeatcherFragment fragment = new WeatcherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        conex = new Connection();
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weatcher, container, false);
        cityField = (TextView)view.findViewById(R.id.city_field);
        updatedField = (TextView)view.findViewById(R.id.updated_field);
        detailsField = (TextView)view.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)view.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)view.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        validarCity();
        return view;
    }

    private void validarCity(){
        if(cityValidate.equals("")){
            changeCity("Cali");
        }
    }

    public void onButtonPressed(Uri uri)  {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void renderWeather(String jsonString){
        try {
            JSONObject json = new JSONObject(jsonString);
            cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " +json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humedad: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Ultima Actualización: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void  setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    public void changeCity(String city) {
        String url = Constants.urlOpenWeatcher+city+"&appid="+Constants.apiKey;
        try {
            conex.endPoint(url, null, getContext(), false, new Connection.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    renderWeather(result);
                    cityValidate = result;
                }
                @Override
                public void onError(String result) {
                    android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getContext());
                    dialogBuilder.setTitle("Buscar Ciudad");
                    View DialogView =  LayoutInflater.from(getContext()).inflate(R.layout.layout_error_conexion, null);
                    dialogBuilder.setView(DialogView);
                    final android.app.AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    renderWeather(cityValidate);
                }
            });

        }catch(Exception e){
            Log.e("Exception",e.getMessage());
        }
    }
}
