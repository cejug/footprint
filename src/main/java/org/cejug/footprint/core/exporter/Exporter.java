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
package org.cejug.footprint.core.exporter;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.itextpdf.text.pdf.BaseFont;

/**
 * Exports a file containing a certificate (usually a signed PDF document).
 * 
 * @author Felipe Gaucho
 */
public interface Exporter {
    /**
     * This constant allow programmers to expose the name of the default signed
     * pdf exporter. Value = {@value} .
     */
    String DEFAULT_SIGNED_PDF_EXPORTER = "net.java.dev.footprint.exporter.pdf.SignedPdfExporter";

    /**
     * Generates a document filling the form fields. Important: be careful about
     * the charset of output files. If the charset of the template file and the
     * charset of the writer are different, a
     * <code>java.nio.charset.CharacterEncode</code> will be created to handle
     * the situation.
     * 
     * @param fields
     *            a mapping between database fields and template fields - can be
     *            null. Cannot be null.
     * @param writer
     *            The stream to which the document output will be addressed.
     * @param trueTypesMapping
     * @throws Exception
     *             if the document generation failed.
     */
    void export(OutputStream writer, Charset charset, Map<String, String> fields, Map<String, BaseFont> trueTypesMapping) throws FootprintExporterException;
}
