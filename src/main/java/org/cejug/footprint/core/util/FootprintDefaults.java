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

import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import org.cejug.footprint.jaxb.FootprintConfig;

/**
 * A set of shared resources and functionalities.
 * 
 * @author Felipe Gaucho
 */
public final class FootprintDefaults extends Properties {
    /**
     * Default serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Footprint constants - global values.
     */
    public static enum Constants {
        LOGGER_NAME("footprint.dev.java.net"), FOOTPRINT_CONTEXT(FootprintConfig.class.getPackage().getName()), SCHEMA_FILE("schema/config.xsd"), FILE_ENCODING("UTF-8"), RESOURCE_BUNDLE_NAME(
                "i18n/footprint");
        private String key;

        private Constants(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }

    /**
     * Default logger, with a console handler.
     */
    private static final Logger DEFAULT_FOOTPRINT_LOGGER = new FootprintDefaults.FootprintLogger(Constants.LOGGER_NAME.toString(), Constants.RESOURCE_BUNDLE_NAME.toString());

    /**
     * getter.
     * 
     * @return a logger with a default file handler.
     */
    public static Logger getDefaultFootprintLogger() {
        return DEFAULT_FOOTPRINT_LOGGER;
    }

    /**
     * 
     * @author $Author$
     * @version $Rev$ ($Date: 2008-02-03 18:12:04 +0100 (Sun, 03 Feb 2008) $)
     */
    private static class FootprintLogger extends Logger {
        public FootprintLogger(String name, String i18n) {
            super(name, i18n);
            addHandler(new ConsoleHandler());
        }
    }
}
