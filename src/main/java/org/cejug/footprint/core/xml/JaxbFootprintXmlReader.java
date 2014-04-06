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

import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.jaxb.FootprintConfig;
import org.xml.sax.SAXException;

/**
 * Convert XML configuration files in Configuration Object.
 * 
 * @author Felipe Gaucho
 */
class JaxbFootprintXmlReader implements FootprintXmlReader {

    /** A listener of unmarshaling events. */
    private transient Unmarshaller.Listener listener;

    /**
     * New XML reader.
     * 
     * @param listener
     *            the listener of unmarshaling events.
     */
    public JaxbFootprintXmlReader(Unmarshaller.Listener listener) {
        this.listener = listener;
    }

    /**
     * New XML reader, that uses a default unmarshaling listener.
     * 
     * @see FootprintConfigUnmarshallerListener
     */
    public JaxbFootprintXmlReader() {
        this(new FootprintConfigUnmarshallerListener());
    }

    /**
     * Load a footprint project from a XML input stream.
     * 
     * @throws JAXBException
     * @throws SAXException
     */
    @SuppressWarnings("unchecked")
    public JAXBElement<FootprintConfig> read(InputStreamReader inputStream) throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(FootprintDefaults.Constants.FOOTPRINT_CONTEXT.toString(), Thread.currentThread().getContextClassLoader());

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setEventHandler(new FootprintConfigValidationHandler());
        unmarshaller.setListener(listener);
        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.XML_NS_URI);
        Schema schema = sf.newSchema(Thread.currentThread().getContextClassLoader().getResource(FootprintDefaults.Constants.SCHEMA_FILE.toString()));
        unmarshaller.setSchema(schema);

        JAXBElement<FootprintConfig> FootprintConfig = (JAXBElement<FootprintConfig>) unmarshaller.unmarshal(inputStream);
        return FootprintConfig;
    }
}
