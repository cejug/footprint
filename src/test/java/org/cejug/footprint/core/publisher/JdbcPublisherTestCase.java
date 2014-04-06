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
package org.cejug.footprint.core.publisher;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.cejug.footprint.core.exporter.Exporter;
import org.cejug.footprint.core.exporter.pdf.PdfExporterFactory;
import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.core.xml.FootprintXmlReader;
import org.cejug.footprint.core.xml.XmlStreamFactory;
import org.cejug.footprint.jaxb.FootprintConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of PDF generation and signature.
 * 
 * @author Felipe Gaucho
 */
public class JdbcPublisherTestCase {
    public static final String CONFIG_EXAMPLE_FILENAME = "configExample.xml";
    private FootprintConfig config = null;

    @Before
    public void setUp() throws Exception {
        FootprintXmlReader reader = XmlStreamFactory.getInstance().getReader();
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_EXAMPLE_FILENAME);
        InputStreamReader file_reader = new InputStreamReader(stream, FootprintDefaults.Constants.FILE_ENCODING.toString());
        config = reader.read(file_reader).getValue();
    }

    /**
     * TODO: decide to delete or not the output folder after the test. tries to
     * delete the generated files.
     * 
     * @Override
     * @After public void tearDown() throws Exception { if (config != null) {
     *        String filename = config.getTemplate().getPdfGeneratedPath(); if
     *        (filename == null) { filename =
     *        AbstractFootprintPublisher.DEFAULT_PDF_GENERATED_PATH; } File
     *        generatedPath = new File(filename); //
     *        assertTrue(deleteDir(generatedPath)); } }
     */

    @Test
    public void testJdbcRun() {
        if (config != null) {
            try {
                // Exporter is a class the generate and sign a PDF document.
                Exporter jdbcExporter = PdfExporterFactory.getPdfExporter(Exporter.DEFAULT_SIGNED_PDF_EXPORTER, new Object[] { config });

                // Publisher is a thread the reads a data source and generates 1
                // certificate for each line.
                FootprintPublisher publisher = new JdbcPublisher(jdbcExporter, config, FootprintDefaults.getDefaultFootprintLogger());
                Thread service = new Thread(publisher);
                service.start();
                // long timestamp = System.currentTimeMillis();
                while (service.isAlive()) {
                    /*
                     * if (System.currentTimeMillis() > timestamp + 30000) {
                     * fail("more than 30 seconds is too much! something
                     * wrong."); } else {
                     */
                    Thread.sleep(500);
                    // }
                }
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }
    }

    public FootprintConfig getConfig() {
        return config;
    }

    public void setConfig(FootprintConfig config) {
        this.config = config;
    }
}
