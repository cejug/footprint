/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (C) 2007 Felipe Gaúcho
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 
 This file is part of the FOOTPRINT Project (a generator of signed PDF
 documents, originally used as JUG events certificates), hosted at
 https://github.com/cejug/footprint
 
 
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
package org.cejug.footprint.core.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Singleton resource bundle, used to share localized properties.
 * 
 * @author Felipe Gaucho
 */
public final class FootprintResourceBundle extends ResourceBundle {
    private static String baseName = "i18n/footprint";

    /** Footprint default resource bundle. */
    private static FootprintResourceBundle instance = new FootprintResourceBundle();

    /** Singleton constructor. */
    private FootprintResourceBundle() {
        super();
        setParent(ResourceBundle.getBundle(baseName, Locale.getDefault(), Thread.currentThread().getContextClassLoader()));
    }

    /**
     * 
     * @return The singleton instance of this resource bundle.
     */
    public static FootprintResourceBundle getInstance() {
        return instance;
    }

    /**
     * Load a value from the bundle and then applies replacement using regular
     * expression. This method uses the default regular expression delimiter.
     * 
     * @param key
     *            the sentence key.
     * @param replacement
     *            the set of tokens to be included in the placeholders. The
     *            index of each placeholder if synchronized with the replacement
     *            array positions. Can be null.
     * @return a replaced string.
     * @throws java.util.MissingResourceException
     *             if the key is not available.
     * @see FootprintResourceBundle#DEFAULT_REGEX_DELIMITER
     */
    public String getString(String key, Object[] replacement) throws MissingResourceException {
        String sentence = getString(key);
        return MessageFormat.format(sentence, replacement == null ? new Object[0] : replacement);
    }

    /** {@inheritdoc} */
    protected Object handleGetObject(String key) {
        try {
            return parent.getObject(key);
        } catch (MissingResourceException noValue) {
            return key;
        }
    }

    /** {@inheritdoc} */
    public Enumeration<String> getKeys() {
        return parent.getKeys();
    }
}
