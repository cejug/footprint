/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (C) 2008-2009 Felipe Gaúcho
 
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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Locale;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.Assert;

import org.cejug.footprint.core.publisher.JdbcPublisherTestCase;
import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.jaxb.FootprintConfig;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Felipe Gaucho
 */
public class XmlStreamTestCase {
    /**
     * fields that will be changed for testing - you can add more fields, or
     * even create a list to provide a better coverage. The first code
     * considered few fields as enough proof of robustness to read/write XML
     * files. For the data verification, we suggest another test case.
     */
    private transient XMLGregorianCalendar expectedDate;

    public transient String expectedIdiom = null;

    public transient String expectedColumnName = null;

    @Test
    public void writeReadAndCompare() {
        InputStream modifiedStream = null;
        InputStream defaultConfigStream = null;

        try {
            defaultConfigStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JdbcPublisherTestCase.CONFIG_EXAMPLE_FILENAME);
            JAXBElement<FootprintConfig> config;

            config = readConfigurationObject(defaultConfigStream);
            setTestData(config);
            File tempFile = writeModifiedConfiguration(config);
            modifiedStream = new FileInputStream(tempFile.getPath());
            config = readConfigurationObject(modifiedStream);
            compareData(config.getValue());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        } finally {
            if (modifiedStream != null) {
                try {
                    modifiedStream.close();
                } catch (IOException e) {
                    Assert.fail(e.getMessage());
                }
            }
            if (defaultConfigStream != null) {
                try {
                    defaultConfigStream.close();
                } catch (IOException e) {
                    Assert.fail(e.getMessage());
                }
            }
        }

    }

    /**
     * Writes the modified configuration to a temporary file.
     * 
     * @throws IOException
     * @throws JAXBException
     */
    private File writeModifiedConfiguration(JAXBElement<FootprintConfig> config) throws JAXBException {
        OutputStream stream = null;
        try {
            File temp = File.createTempFile("test", ".xml");
            temp.deleteOnExit();
            Writer streamWriter = new OutputStreamWriter(new FileOutputStream(temp), FootprintDefaults.Constants.FILE_ENCODING.toString());
            FootprintXmlWriter writer = XmlStreamFactory.getInstance().getWriter();
            writer.write(config, streamWriter);
            streamWriter.close();
            return temp;
        } catch (IOException e) {
            throw new JAXBException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new JAXBException(e);
                }
            }

        }

    }

    /**
     * The test data should be dynamic enough to avoid repetition of failure.
     * 
     * @throws DatatypeConfigurationException
     */
    private void setTestData(JAXBElement<FootprintConfig> config) throws DatatypeConfigurationException {
        expectedIdiom = Locale.getDefault().getLanguage();
        config.getValue().setIdiom(expectedIdiom);
        expectedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar("2007-11-27");
        config.getValue().setDate(expectedDate.toXMLFormat());
        expectedColumnName = Locale.getDefault().getDisplayCountry();
        config.getValue().getDatabase().setEmailColumnName(expectedColumnName);
    }

    private void compareData(FootprintConfig footprint) {
        assertEquals(footprint.getDate(), expectedDate.toXMLFormat());
        assertEquals(footprint.getDatabase().getEmailColumnName(), expectedColumnName);
        assertEquals(footprint.getIdiom(), expectedIdiom);
    }

    private JAXBElement<FootprintConfig> readConfigurationObject(InputStream stream) throws UnsupportedEncodingException, JAXBException, SAXException {
        FootprintXmlReader reader = XmlStreamFactory.getInstance().getReader();
        InputStreamReader file_reader = new InputStreamReader(stream, FootprintDefaults.Constants.FILE_ENCODING.toString());
        return reader.read(file_reader);
    }
}
