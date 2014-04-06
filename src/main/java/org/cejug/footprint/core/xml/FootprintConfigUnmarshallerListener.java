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
package org.cejug.footprint.core.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.Unmarshaller.Listener;

import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.jaxb.ConfigEmail;
import org.cejug.footprint.jaxb.ConfigPdfTemplate;
import org.cejug.footprint.jaxb.ConfigSecurity;
import org.cejug.footprint.jaxb.ConfigUnmarshalI18N;
import org.cejug.footprint.jaxb.SignatureStamp;

/**
 * This class is used to check the values read from the config XML. Despite JAXB
 * validates the XML against it schema, the contents of the properties file
 * should be verified following the expected ones.
 * 
 * @author Felipe Gaucho
 * 
 */
public class FootprintConfigUnmarshallerListener extends Listener {
    private transient Logger logger = null;
    /**
     * This is used to mark the first error occurrence. Just before the first
     * error we print a big warning message notifying the user about mallformed
     * configuration data. After that, all other errors appears as simples
     * SEVERE log entry.
     */
    private transient boolean firstError = false;

    public FootprintConfigUnmarshallerListener(Logger logger) {
        super();
        this.logger = logger;
    }

    /**
     * This wrapper method is used to allow a more elegant error notification
     * with a big header.
     */
    private void severe(String key, String[] params) {
        if (!firstError) {
            logger.log(Level.SEVERE, ConfigUnmarshalI18N.CONFIG_ERRORS_HEADER.value(), new String[0]);
            firstError = true;
        }
        logger.log(Level.SEVERE, key, params);
    }

    public FootprintConfigUnmarshallerListener() {
        this(FootprintDefaults.getDefaultFootprintLogger());
    }

    @Override
    public void afterUnmarshal(Object target, Object parent) {
        if (target instanceof ConfigEmail) {
            validateEmailData((ConfigEmail) target);
        } else if (target instanceof ConfigPdfTemplate) {
            validateTemplateData((ConfigPdfTemplate) target);
        } else if (target instanceof ConfigSecurity) {
            validateSecurity((ConfigSecurity) target);
        }
    }

    /**
     * Check the following conditions.
     * <ol>
     * <li>If the PDF template exists.</li>
     * </ol>
     * 
     * @param template
     */
    private void validateTemplateData(ConfigPdfTemplate template) {
        loadFile(ConfigUnmarshalI18N.CONFIG_TEMPLATE_FILE_NOT_FOUND.value(), template.getPdfTemplateFilename());
    }

    /**
     * Check the following conditions.
     * <ol>
     * <li>If the msgTo email address is correct.</li>
     * </ol>
     * 
     * @param email
     */
    private void validateEmailData(ConfigEmail email) {
        String msgFrom = email.getMsgFrom();
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(msgFrom);
        boolean matchFound = m.matches();
        if (!matchFound) {
            severe(ConfigUnmarshalI18N.CONFIG_EMAIL_MALLFORMED.value(), new String[] { msgFrom });
        }
    }

    /**
     * 
     * @param security
     */
    private void validateSecurity(ConfigSecurity security) {
        SignatureStamp stamp = security.getSignatureStamp();
        if (stamp != null) {
            // stamp is optional
            loadFile(ConfigUnmarshalI18N.CONFIG_KEYSTORE_STAMP_NOT_FOUND.value(), stamp.getSignatureStampImageFilename());
        }

        File keystoreFile = loadFile(ConfigUnmarshalI18N.CONFIG_KEYSTORE_FILE_NOT_FOUND.value(), security.getKeystoreFilename());
        InputStream keystoreInputStream = null;
        String keystoreProvider = security.getKeystorePkProvider();
        String keystoreType = security.getKeystoreType();
        try {
            KeyStore keystore = null;
            if (keystoreProvider == null) {
                keystore = KeyStore.getInstance(keystoreType);
            } else {
                keystore = KeyStore.getInstance(keystoreType, keystoreProvider);
            }
            keystoreInputStream = new FileInputStream(keystoreFile);
            keystore.load(keystoreInputStream, security.getKeystorePassword().toCharArray());
            keystoreInputStream.close();
            String alias = security.getKeystorePkAlias();
            if (!keystore.containsAlias(alias)) {
                severe(ConfigUnmarshalI18N.CONFIG_KEYSTORE_ALIAS_UNKNOWN.value(), new String[] { alias });
            }
        } catch (GeneralSecurityException ksError) {
            severe(ConfigUnmarshalI18N.CONFIG_KEYSTORE_UNLOADABLE.value(), new String[] { keystoreFile.getAbsolutePath(), keystoreProvider, keystoreType });
        } catch (IOException e) {
            severe(ConfigUnmarshalI18N.CONFIG_KEYSTORE_GENERIC_PROBLEM.value(), new String[] { e.getMessage() });
        } finally {
            if (keystoreInputStream != null) {

                try {
                    keystoreInputStream.close();
                } catch (IOException e) {
                    severe(ConfigUnmarshalI18N.CONFIG_KEYSTORE_GENERIC_PROBLEM.value(), new String[] { e.getMessage() });

                }
            }
        }

    }

    /**
     * Utility method to check if a file exists. If t exists, return a file
     * reference, otherwise log an error.
     * 
     * @param severeKey
     *            the error i18n key.
     * @param filename
     *            the filename.
     */
    private File loadFile(String severeKey, String filename) {
        File file = null;
        URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        if (url != null) {
            file = new File(url.getFile());
        }
        if (file == null) {
            severe(severeKey, new String[] { filename });
        } else if (!file.exists()) {
            severe(severeKey, new String[] { file.getAbsolutePath() });
        }
        return file;
    }
}