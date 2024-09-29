package com.mlakatos.alkoholkalkulator;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class KalkulatorActivity extends AppCompatActivity {

    private EditText etOcitanaVrednost, etTemperatura/*, etReferentnaTemperatura*/;
    private Spinner spinnerReferentnaTemperatura;
    private TextView tvRezultat, tvRezultatGradi;
    private Button btnIzracunaj2;

    ImageButton clearBtn_ocitana_vrednost, clearBtn_temepratura;

    private final double[] korekcijskeVrednosti = {
            0.169, 0.177, 0.192, 0.213, 0.207, 0.217, 0.227, 0.233, 0.243, 0.250,
            0.257, 0.260, 0.267, 0.273, 0.277, 0.283, 0.287, 0.293, 0.293, 0.297,
            0.300, 0.303, 0.303, 0.310, 0.310, 0.310, 0.317, 0.317, 0.320, 0.323,
            0.323, 0.327, 0.327, 0.327, 0.330, 0.330, 0.337, 0.337, 0.340, 0.343,
            0.343, 0.350, 0.350, 0.350, 0.357, 0.357, 0.360, 0.367, 0.367, 0.370,
            0.373, 0.373, 0.380, 0.380, 0.383, 0.387, 0.390, 0.390, 0.393, 0.397,
            0.397, 0.400, 0.400, 0.400, 0.397, 0.400, 0.400, 0.397, 0.397, 0.393,
            0.390, 0.383, 0.377, 0.373, 0.363, 0.353, 0.343, 0.333, 0.317, 0.307,
            0.287, 0.267, 0.247, 0.230, 0.210, 0.190, 0.173, 0.157, 0.140, 0.130,
            0.113, 0.103, 0.097, 0.087, 0.083, 0.077, 0.077, 0.073, 0.072, 0.048
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.isLocaleEnglish(this)) {
            Utils.setDefaultLanguage(this);
        }

        setContentView(R.layout.activity_kalkulator);

        // Povezivanje elemenata sa XML-a
        etOcitanaVrednost = findViewById(R.id.etOcitanaVrednost);
        etTemperatura = findViewById(R.id.etTemperatura);
        //etReferentnaTemperatura = findViewById(R.id.etReferentnaTemperatura);
        tvRezultat = findViewById(R.id.tvRezultat);
        tvRezultatGradi = findViewById(R.id.tvRezultatGradi);
        btnIzracunaj2 = findViewById(R.id.btnIzracunaj);

        clearBtn_ocitana_vrednost = findViewById(R.id.clearBtn_ocitana_vrednost);
        clearBtn_temepratura= findViewById(R.id.clearBtn_temepratura);
        spinnerReferentnaTemperatura = findViewById(R.id.spinnerReferentnaTemperatura);

        // Kreiraj adapter za spinner koristeći niz vrednosti iz strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.referentne_vrednosti, android.R.layout.simple_spinner_item);

        // Podesi izgled padajuće liste
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Poveži adapter sa spinner-om
        spinnerReferentnaTemperatura.setAdapter(adapter);

        // Postavi default vrednost (ako je potrebno)
        spinnerReferentnaTemperatura.setSelection(1); // Postavi na "20" kao podrazumevano

        // Postavljanje listenera za dugme
        btnIzracunaj2.setOnClickListener(v ->
                izracunajPravuJacinu()
        );

        // Poveži svaki EditText sa odgovarajućim dugmetom
        setupClearButton(etOcitanaVrednost, clearBtn_ocitana_vrednost);
        setupClearButton(etTemperatura, clearBtn_temepratura);
    }
    // Funkcija koja postavlja listener-e za EditText i ClearButton
    private void setupClearButton(EditText editText, ImageButton clearButton) {
        // Prikaz ili sakrivanje dugmeta na osnovu unosa
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Ako postoji tekst, prikaži dugme za brisanje
                if (s.length() > 0) {
                    clearButton.setVisibility(View.VISIBLE);
                } else {
                    clearButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Brisanje teksta kada se pritisne dugme
        clearButton.setOnClickListener(v -> {
                editText.setText("");  // Očisti tekst
                clearButton.setVisibility(View.GONE);  // Sakrij dugme posle brisanja
        });
    }
    // Metoda za izračunavanje korekcije alkohola
    @SuppressLint("StringFormatMatches")
    private void izracunajPravuJacinu() {
        try {

            // Uzimanje tekstualnih vrednosti iz polja
            String ctr_ocitanaVrednost = etOcitanaVrednost.getText().toString().trim();
            String ctr_temperatura = etTemperatura.getText().toString().trim();

            // Provera da li su polja prazna
            if (ctr_ocitanaVrednost.isEmpty() || ctr_temperatura.isEmpty()) {
                // Prikaži obaveštenje u TextView ako su prazna
                tvRezultat.setText(getString(R.string.empty_fields_warning));
                return;
            }

            // Dobijanje unetih vrednosti i konverzija u integer
            int ocitanaVrednost = Integer.parseInt(etOcitanaVrednost.getText().toString());
            int temperatura = Integer.parseInt(etTemperatura.getText().toString());

            if(temperatura > 30 || temperatura < 1){
                tvRezultat.setText(getString(R.string.invalid_temperature_range));
                tvRezultatGradi.setText("");
                return;
            }
            //int referentnaTemperatura = Integer.parseInt(etReferentnaTemperatura.getText().toString());

            String referentnaTemperaturaString = spinnerReferentnaTemperatura.getSelectedItem().toString();
            int referentnaTemperatura = Integer.parseInt(referentnaTemperaturaString);

            // Poziv metode za izračunavanje korekcije
            double pravaJacina = izracunajKorekciju(ocitanaVrednost, temperatura, referentnaTemperatura);

            // Prikaz rezultata u procentima
            tvRezultat.setText(String.format(getString(R.string.alcohol_strength), pravaJacina));

            // Konverzija u grade (1 grad = 2.46% alkohola)
            double jacinaUGradima = pravaJacina / 2.46;
            String jacinaUGradimaZaokruzena = String.format("%.2f", jacinaUGradima);  // Zaokruženo na 2 decimale

            // Prikaz rezultata u gradima
            tvRezultatGradi.setText(String.format(getString(R.string.alcohol_strength_grad), jacinaUGradimaZaokruzena));


        } catch (NumberFormatException e) {
            tvRezultat.setText(getString(R.string.invalid_value));
            tvRezultatGradi.setText("");
        } catch (IllegalArgumentException e) {
            tvRezultat.setText(getString(R.string.invalid_alcohol_range));
            tvRezultatGradi.setText("");
        }
    }

    // Funkcija za izračunavanje korekcije sa dinamičkom vrednošću korekcije
// Funkcija za izračunavanje korekcije na osnovu očitane vrednosti, temperature i referentne temperature
    private double izracunajKorekciju(int ocitanaVrednost, int temperatura, int referentnaTemperatura) {
        // Očitana vrednost mora biti između 1 i 100
        if (ocitanaVrednost < 1 || ocitanaVrednost > 100) {
            throw new IllegalArgumentException(getString(R.string.alcohol_value_range_error));

        }

        // Uzimanje korekcijske vrednosti za datu očitanu vrednost
        double korekcijaPoStepeni = korekcijskeVrednosti[100 - ocitanaVrednost];  // Indeksiranje od 0

        double korekcija;
        int razlikaUTemperaturi = temperatura - referentnaTemperatura;

        // Za svaki stepen preko referentne temperature oduzmi korekciju
        if (razlikaUTemperaturi > 0) {
            korekcija = ocitanaVrednost - (razlikaUTemperaturi * korekcijaPoStepeni);
        }
        // Za svaki stepen ispod referentne temperature dodaj korekciju
        else {
            korekcija = ocitanaVrednost + (Math.abs(razlikaUTemperaturi) * korekcijaPoStepeni);
        }
        if(korekcija > 100){
            korekcija = 100;
        }
        if(korekcija < 0){
            korekcija = 0;
        }
        return korekcija;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Čuvanje vrednosti iz EditText polja
        outState.putString("ocitanaVrednost", etOcitanaVrednost.getText().toString());
        outState.putString("temperatura", etTemperatura.getText().toString());
        //outState.putString("referentnaTemperatura", etReferentnaTemperatura.getText().toString());
        outState.putString("pravaJacina", tvRezultat.getText().toString());
        outState.putString("pravaJacinaGradi", tvRezultatGradi.getText().toString());

        // Čuvanje pozicije Spinner-a
        int spinnerPosition = spinnerReferentnaTemperatura.getSelectedItemPosition();
        outState.putInt("spinnerReferentnaTemperaturaPosition", spinnerPosition);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Vraćanje vrednosti u polja nakon rotacije
        etOcitanaVrednost.setText(savedInstanceState.getString("ocitanaVrednost"));
        etTemperatura.setText(savedInstanceState.getString("temperatura"));
        //etReferentnaTemperatura.setText(savedInstanceState.getString("referentnaTemperatura"));
        tvRezultat.setText(savedInstanceState.getString("pravaJacina"));
        tvRezultatGradi.setText(savedInstanceState.getString("pravaJacinaGradi"));
        // Vraćanje pozicije Spinner-a
        int spinnerPosition = savedInstanceState.getInt("spinnerReferentnaTemperaturaPosition");
        spinnerReferentnaTemperatura.setSelection(spinnerPosition);

    }
}