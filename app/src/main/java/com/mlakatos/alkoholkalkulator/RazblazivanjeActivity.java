package com.mlakatos.alkoholkalkulator;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class RazblazivanjeActivity extends AppCompatActivity {

    // Definicija varijabli
    private EditText etPocetnaKolicina, etPocetnaJacina, etKrajnjaJacina;
    private TextView tvRezultat;
    private Button btnIzracunaj;

    ImageButton clearBtn_kolicina, clearBtn_pocetna, clearBtn_zeljena;
    // Originalni niz etanola
    private final double[] etanola = {35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75};
    private final double[] vode = {68.207, 67.207, 66.185, 65.242, 64.295, 63.347, 62.395, 61.439, 60.476, 59.511, 58.542, 57.570, 56.596, 55.617, 54.635, 53.650, 52.662, 51.670, 50.676, 49.679, 48.679, 47.679, 46.670, 45.661, 44.650, 43.637, 42.620, 41.601, 40.579, 39.555, 38.529, 37.500, 36.469, 35.436, 34.399, 33.360, 32.129, 31.180, 30.229, 29.276, 28.319};
    //private final double[] sazimanje = {3.059, 3.124, 3.185, 3.242, 3.295, 3.347, 3.395, 3.439, 3.476, 3.511, 3.542, 3.570, 3.596, 3.617, 3.635, 3.650, 3.662, 3.670, 3.676, 3.679, 3.679, 3.679, 3.670, 3.661, 3.650, 3.637, 3.620, 3.601, 3.579, 3.555, 3.529, 3.500, 3.469, 3.436, 3.399, 3.360, 3.319, 3.276, 3.229, 3.180, 3.129};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.isLocaleEnglish(this)) {
            Utils.setDefaultLanguage(this);
        }
        setContentView(R.layout.activity_razblazivanje);

        // Povezivanje varijabli sa elementima interfejsa
        etPocetnaKolicina = findViewById(R.id.etPocetnaKolicina);
        etPocetnaJacina = findViewById(R.id.etPocetnaJacina);
        etKrajnjaJacina = findViewById(R.id.etKrajnjaJacina);
        tvRezultat = findViewById(R.id.tvRezultat);
        btnIzracunaj = findViewById(R.id.btnIzracunaj);
        clearBtn_pocetna = findViewById(R.id.clearBtn_pocetna);
        clearBtn_kolicina = findViewById(R.id.clearBtn_kolicina);
        clearBtn_zeljena = findViewById(R.id.clearBtn_zeljena);

/*        // Proveri da li je stanje sačuvano
        if (savedInstanceState != null) {

        }*/


        // Postavljanje klik eventa na dugme
        btnIzracunaj.setOnClickListener(v ->{
                /*izracunajRazblazivanje();*/
                izracunaj();
        });

        setupClearButton(etPocetnaJacina, clearBtn_pocetna);
        setupClearButton(etPocetnaKolicina, clearBtn_kolicina);
        setupClearButton(etKrajnjaJacina, clearBtn_zeljena);
    }

    // Metod za izračunavanje
    private void izracunaj() {

        String ctr_pocetnaKolicina = etPocetnaKolicina.getText().toString().trim();
        String ctr_pocetnaJacina = etPocetnaJacina.getText().toString().trim();
        String ctr_krajnjaJacina = etKrajnjaJacina.getText().toString().trim();

        // Provera da li su sva polja prazna
        if (ctr_pocetnaKolicina.isEmpty() || ctr_pocetnaJacina.isEmpty() || ctr_krajnjaJacina.isEmpty()) {
            // Prikaži obaveštenje u TextView ako su prazna
            tvRezultat.setText(getString(R.string.empty_fields_warning));
            return;
        }

        // Čitamo unete vrednosti
        double pocetnaKolicina = Double.parseDouble(etPocetnaKolicina.getText().toString());
        double pocetnaJacina = Double.parseDouble(etPocetnaJacina.getText().toString());
        double krajnjaJacina = Double.parseDouble(etKrajnjaJacina.getText().toString());

        // Računamo konačnu zapreminu korišćenjem formule C1 * V1 = C2 * V2
        double krajnjaKolicina = (pocetnaJacina / krajnjaJacina) * pocetnaKolicina;

        // Pronađi indekse za početnu i krajnju jačinu u tabeli
        int pocetniIndeks = nadjiIndeks(pocetnaJacina);
        int krajnjiIndeks = nadjiIndeks(krajnjaJacina);

        if (pocetniIndeks == -1 || krajnjiIndeks == -1) {
            tvRezultat.setText(getString(R.string.alcohol_strength_not_found));
            return;
        }

        // Izračunavanje vode na osnovu tabele
        double pocetnaVoda = pocetnaKolicina * vode[pocetniIndeks] / 100;
        double krajnjaVoda = krajnjaKolicina * vode[krajnjiIndeks] / 100;

        // Izračunavanje količine vode koja treba da se doda
        double kolicinaVodeZaDodati = krajnjaVoda - pocetnaVoda;

        // Izračunavanje sažimanja smeše
        //double sazimanjeSmesa = krajnjaKolicina * sazimanje[krajnjiIndeks] / 100;

        // Izračunavanje konačne količine uzimajući u obzir sažimanje
        double konacnaKolicina = krajnjaKolicina; // - sazimanjeSmesa;

        // Prikaz rezultata
        tvRezultat.setText(String.format(getString(R.string.add_water), kolicinaVodeZaDodati, konacnaKolicina));
    }

    // Metoda za pronalaženje indeksa jačine iz tabele
    private int nadjiIndeks(double jacina) {
        for (int i = 0; i < etanola.length; i++) {
            if (etanola[i] == jacina) {
                return i;
            }
        }
        return -1; // Ako nije pronađena jačina
    }

    private void setupClearButton(EditText editText, ImageButton clearButton) {
        // Prikaz ili sakrivanje dugmeta na osnovu unosa
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
            public void afterTextChanged(Editable s) {
            }
        });

        // Brisanje teksta kada se pritisne dugme
        clearButton.setOnClickListener(v -> {
                editText.setText("");  // Očisti tekst
                clearButton.setVisibility(View.GONE);  // Sakrij dugme posle brisanja
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Sačuvaj tekst iz EditText polja
        outState.putString("pocetnaKolicina", etPocetnaKolicina.getText().toString());
        outState.putString("pocetnaJacina", etPocetnaJacina.getText().toString());
        outState.putString("krajnjaJacina", etKrajnjaJacina.getText().toString());
        // Sačuvaj tekst iz TextView
        outState.putString("rezultatRazblazivanja", tvRezultat.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        etPocetnaKolicina.setText(savedInstanceState.getString("pocetnaKolicina"));
        etPocetnaJacina.setText(savedInstanceState.getString("pocetnaJacina"));
        etKrajnjaJacina.setText(savedInstanceState.getString("krajnjaJacina"));
        tvRezultat.setText(savedInstanceState.getString("rezultatRazblazivanja"));
    }

}

