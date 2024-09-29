package com.mlakatos.alkoholkalkulator;





import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class Utils {
    public static boolean isLocaleEnglish(Context context) {
        Locale currentLocale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            currentLocale = context.getResources().getConfiguration().locale;
        }

        return "en".equalsIgnoreCase(currentLocale.getLanguage());
    }
    public static void setDefaultLanguage(Context context) {
        String defaultLang = "def";

        Locale locale = new Locale(defaultLang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            config.setLocales(new LocaleList(locale));
        } else {
            config.locale = locale;
        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
