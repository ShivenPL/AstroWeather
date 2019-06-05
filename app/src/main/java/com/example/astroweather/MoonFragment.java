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

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class MoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.moon_layout, container, false);
        final TextView wschod = rootView.findViewById(R.id.wschodCzasInput);

        final TextView zachod = rootView.findViewById(R.id.zachodCzasInput);

        final TextView now = rootView.findViewById(R.id.nowInput);
        final TextView pelnia = rootView.findViewById(R.id.pelniaInput);

        final TextView faza = rootView.findViewById(R.id.fazaInput);
        final TextView syn = rootView.findViewById(R.id.synoInput);




        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        if(getActivity() == null)
                            return;

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        String time = sharedPreferences.getString("pref_sync", "30");
                        MoonFragment.this.getActivity().runOnUiThread(new Runnable() {
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

                                wschod.setText(astro.getMoonInfo().getMoonrise().toString().substring(10,16));

                                zachod.setText(astro.getMoonInfo().getMoonset().toString().substring(10,16));

                                now.setText(astro.getMoonInfo().getNextNewMoon().toString().substring(0,16));

                                pelnia.setText(astro.getMoonInfo().getNextFullMoon().toString().substring(0,16));

                                faza.setText((astro.getMoonInfo().getIllumination() + "").substring(0,4) + "%");


                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                                try {
                                    Date first = simpleDateFormat.parse(calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
                                    Date second = simpleDateFormat.parse(astro.getMoonInfo().getNextNewMoon().toString().substring(0,18));
                                    long diffInMillies = Math.abs(first.getTime() - second.getTime());
                                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                                    syn.setText(diff + "");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

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