package com.example.astroweather;

import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class SunFragment extends Fragment {
    Thread thread;

    TextView wschod ;
    TextView wschodaz;

    TextView zachod;
    TextView zachodaz;

    TextView zmierzch;
    TextView swit;

    TextView wsporzedne;

    Boolean isCreated = false;



    public void doThis(){
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        if(getActivity() == null)
                            return;

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        String time = sharedPreferences.getString("pref_sync", "5");
                        SunFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                                String latitudeS = sharedPreferences.getString("latitude", "51.746960");
                                String longitudeS = sharedPreferences.getString("longitude", "19.455891");

                                double latitude = parseDouble(latitudeS);

                                ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
                                ActivityManager.getMyMemoryState(myProcess);

                                Boolean isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;

                                if(isInBackground) {

                                }else {

                                }


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

                                AstroCalculator astro = new AstroCalculator(new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), (int) TimeUnit.HOURS.convert(calendar.getTimeZone().getRawOffset(), TimeUnit.MILLISECONDS), true) ,new AstroCalculator.Location(latitude, longitude));

                                wschod.setText(astro.getSunInfo().getSunrise().toString().substring(10,16));
                                wschodaz.setText(astro.getSunInfo().getAzimuthRise() + "");

                                zachod.setText(astro.getSunInfo().getSunset().toString().substring(10,16));
                                zachodaz.setText(astro.getSunInfo().getAzimuthSet() + "");

                                zmierzch.setText(astro.getSunInfo().getTwilightEvening().toString().substring(10,16));
                                swit.setText(astro.getSunInfo().getTwilightMorning().toString().substring(10,16));
                                wsporzedne.setText((latitude + "").substring(0,4) + ", " + (longitude + "").substring(0,4));


                                Toast.makeText(getActivity().getApplicationContext(), "Synch", Toast.LENGTH_SHORT).show();

                                Log.println(1,"", "Odswieżam");



                            }
                        });
                        Thread.sleep(1000 * parseInt(time));
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.sun_layout, container, false);
        wschod = rootView.findViewById(R.id.wschodCzasInput);
        wschodaz = rootView.findViewById(R.id.wschodAzymutInput);

        zachod = rootView.findViewById(R.id.zachodCzasInput);
        zachodaz = rootView.findViewById(R.id.zachodAzymutInput);

        zmierzch = rootView.findViewById(R.id.zmierzchInput);
        swit = rootView.findViewById(R.id.switInput);

        wsporzedne = rootView.findViewById(R.id.wspolrzedne);


        doThis();

        return rootView;
    }

    @Override
    public void onStop() {
        thread.interrupt();
        isCreated = true;
        super.onStop();
    }

    @Override
    public void onStart() {
        if(isCreated == true)
            doThis();


        super.onStart();
    }
}