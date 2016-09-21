/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Utils {
    private static Locale currentLocale = new Locale("de", "DE");

    private static final ResourceBundle msg = getBundle();
    private static final NumberFormat nf = NumberFormat.getInstance(currentLocale);

    public static ResourceBundle getBundle() {
        try {
            // @todo add internationalization for other languages
            ResourceBundle rb = ResourceBundle.getBundle("de.seibushin.bodyArchitect.i18n.MessagesBundle", currentLocale);
            return rb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(String key) {
        return msg.getString(key);
    }

    // @todo move to service
    public static NumberFormat getNumberFormat() {
        return nf;
    }
}
