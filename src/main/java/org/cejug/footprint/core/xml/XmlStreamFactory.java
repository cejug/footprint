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

/**
 * The factory of readers and writer of XML files. The goal of this factory is
 * to expose only the interfaces of the default streamers, leaving an
 * opportunity of third-party implementations to be used by Footprint.
 * 
 * @author Felipe Gaucho
 */
public final class XmlStreamFactory {
    /** reference to the singleton instance. */
    private static XmlStreamFactory instance = new XmlStreamFactory();

    /** singleton constructor. */
    private XmlStreamFactory() {
    }

    /** point of access to the singleton instance. */
    public static XmlStreamFactory getInstance() {
        return instance;
    }

    /** @return new JaxbCurriculumReader(). */
    public FootprintXmlReader getReader() {
        return new JaxbFootprintXmlReader();
    }

    /** @return new JaxbCurriculumWriter(). */
    public FootprintXmlWriter getWriter() {
        return new JaxbFootprintXmlWriter();
    }
}
