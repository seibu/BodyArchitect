/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import java.util.Locale;
import java.util.ResourceBundle;

public class MsgUtil {
    private static final ResourceBundle msg = getBundle();

    private static ResourceBundle getBundle() {
        try {
            // @todo add internationalization for other languages
            Locale currentLocale = new Locale("de", "DE");
            ResourceBundle rb = ResourceBundle.getBundle("i18n.MessagesBundle", currentLocale);
            return rb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(String key) {
        return msg.getString(key);
    }
}
