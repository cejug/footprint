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
package org.cejug.footprint.core.publisher.security;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import junit.framework.Assert;

import org.cejug.footprint.core.publisher.JdbcPublisherTestCase;
import org.cejug.footprint.core.security.FootprintSignatureVerifier;
import org.cejug.footprint.core.security.pdf.FootprintSecurityFactory;
import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.core.xml.FootprintXmlReader;
import org.cejug.footprint.core.xml.XmlStreamFactory;
import org.cejug.footprint.jaxb.FootprintConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * Test signature and verification classes.
 * 
 * @author Felipe Gaucho
 */
public class SecurityTestCase {
    public static final String configExampleFilename = "configExample.xml";
    private transient FootprintConfig config = null;

    @Before
    public void setUp() throws Exception {
        FootprintXmlReader reader = XmlStreamFactory.getInstance().getReader();
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configExampleFilename);
        InputStreamReader file_reader = new InputStreamReader(stream, FootprintDefaults.Constants.FILE_ENCODING.toString());
        config = reader.read(file_reader).getValue();

        String gen = config.getTemplate().getPdfGeneratedPath();
        config.getTemplate().setPdfGeneratedPath("build/classes/" + gen);

        // Generate a signed certificate for signing and verifying
        JdbcPublisherTestCase publisher = new JdbcPublisherTestCase();
        publisher.setUp();
        publisher.setConfig(config);
        publisher.testJdbcRun();
        config.getTemplate().setPdfGeneratedPath(gen);
    }

    @Test
    public void testDefaultVerifier() {
        // FootprintVerifier is an interface to .
        try {
            FootprintSignatureVerifier verifier = FootprintSecurityFactory.getInstance().getDefaultPdfVerifier(config);

            File certificatefolder = new File(getClass().getClassLoader().getResource(".").getFile());
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".pdf");
                }
            };

            File[] pdfs = certificatefolder.listFiles(filter);
            // URL uu =
            // Thread.currentThread().getContextClassLoader().getResource(".");
            // String ss = uu.getFile();
            KeyStore keystore = FootprintSecurityFactory.getInstance().loadKeystore(config);
            String path = pdfs[0].getCanonicalPath();
            // Verification should fail since we are using a SUN demo
            // certificate.
            Assert.assertFalse("Certificate failed: Cannot be verified against the KeyStore or the certificate chain", verifier.verify(path, keystore));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
