package com.mlakatos.alkoholkalkulator;

import static com.mlakatos.alkoholkalkulator.Utils.isLocaleEnglish;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLocaleEnglish(this)) {
          Utils.setDefaultLanguage(this);
        }
/*        else {
            // Jezik nije engleski
        }*/


        setContentView(R.layout.activity_start);



        // Prikaz za kalkulator alkohola (postojeći prikaz)
        findViewById(R.id.btnKalkAlk).setOnClickListener(v -> {
                // Otvori KalkulatorActivity (postojeći view)
                Intent intent = new Intent(StartActivity.this, KalkulatorActivity.class);
                startActivity(intent);

        });

        // Prikaz za novi ekran
        findViewById(R.id.btnRazblAlk).setOnClickListener(v -> {
                // Ovde dodaj novi prikaz ili drugi activity po potrebi
                Intent intent = new Intent(StartActivity.this, RazblazivanjeActivity.class); // RazblazivanjeActivity predstavlja novi prikaz
                startActivity(intent);
        });


    }

}