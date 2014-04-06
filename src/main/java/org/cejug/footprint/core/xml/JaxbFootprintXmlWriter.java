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

import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.jaxb.FootprintConfig;

/**
 * Serialize footprint configuration objects in XML format.
 * 
 * @author Felipe Gaucho
 */
class JaxbFootprintXmlWriter implements FootprintXmlWriter {

    /**
     * Serializes a footprint configuration object in a XML file.
     * 
     * @param config
     *            the footprint configuration object.
     * @param writer
     *            the XML writer.
     * 
     */
    public void write(JAXBElement<FootprintConfig> config, Writer writer) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(FootprintDefaults.Constants.FOOTPRINT_CONTEXT.toString(), Thread.currentThread().getContextClassLoader());
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_ENCODING, FootprintDefaults.Constants.FILE_ENCODING.toString());
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setEventHandler(new FootprintConfigValidationHandler());
        // REVIEW: passwords should be written in plain XML ?
        // properties.getValue().getSecurity().setKeystorePassword(null);
        // properties.getValue().getSecurity().setKeystorePkPassword(null);
        // properties.getValue().getEmail().setSmtpPassword(null);
        m.marshal(config, writer);
    }
}
