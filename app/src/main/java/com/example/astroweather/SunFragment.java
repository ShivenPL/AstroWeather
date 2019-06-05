package com.example.astroweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.TimeZone;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class SunFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.sun_layout, container, false);
        final TextView wschod = rootView.findViewById(R.id.wschodCzasInput);
        final TextView wschodaz = rootView.findViewById(R.id.wschodAzymutInput);

        final TextView zachod = rootView.findViewById(R.id.zachodCzasInput);
        final TextView zachodaz = rootView.findViewById(R.id.zachodAzymutInput);

        final TextView zmierzch = rootView.findViewById(R.id.zmierzchInput);
        final TextView swit = rootView.findViewById(R.id.switInput);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        if(getActivity() == null)
                            return;

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        String time = sharedPreferences.getString("pref_sync", "30");
                        SunFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                                String latitudeS = sharedPreferences.getString("latitude", "51.746960");
                                String longitudeS = sharedPreferences.getString("longitude", "19.455891");

                                double latitude = parseDouble(latitudeS);


                                if(latitude > 78.999999)
                                {
                                    latitude = 78.999999;
                                    sharedPreferences.edit().putString("latitude", latitude + "").commit();
                                }

                                else if(latitude < -78.999999)
                                {
                                    latitude = -78.999999;
                                    sharedPreferences.edit().putString("latitude", latitude + "").commit();
                                }
                                else{}

                                double longitude = parseDouble(longitudeS);
                                if(longitude > 180)
                                {
                                    longitude = 180;
                                    sharedPreferences.edit().putString("longitude", longitude + "").commit();
                                }

                                else if(longitude < -180)
                                {
                                    longitude = -180;
                                    sharedPreferences.edit().putString("longitude", longitude + "").commit();
                                }
                                else{}

                                Calendar calendar =  Calendar.getInstance(TimeZone.getDefault());

                                AstroCalculator astro = new AstroCalculator(new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), 2, true) ,new AstroCalculator.Location(latitude, longitude));

                                wschod.setText(astro.getSunInfo().getSunrise().toString().substring(10,16));
                                wschodaz.setText(astro.getSunInfo().getAzimuthRise() + "");

                                zachod.setText(astro.getSunInfo().getSunset().toString().substring(10,16));
                                zachodaz.setText(astro.getSunInfo().getAzimuthSet() + "");

                                zmierzch.setText(astro.getSunInfo().getTwilightEvening().toString().substring(10,16));
                                swit.setText(astro.getSunInfo().getTwilightMorning().toString().substring(10,16));
                                Toast.makeText(getActivity().getApplicationContext(), "Synch", Toast.LENGTH_LONG).show();
                            }
                        });
                        Thread.sleep(1000 * parseInt(time));
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        return rootView;
    }
}