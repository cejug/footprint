/* -
 * This file is part of footprint project. Copyright (C) CEJUG.
 *
 * This application is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This application is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * There is a full copy of the GNU Lesser General Public License along with
 * this library. Look for the file license.txt at the root level. If you do not
 * find it, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 */
package org.cejug.footprint;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 
 * @author eprogramming
 *
 */
public final class FootPrint {

    private PdfReader reader;
    private Map<String, String> values;

    public FootPrint(Map<String, String> values, URL url) {
        this(values);
        try {
            this.reader = new PdfReader(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FootPrint(Map<String, String> values, InputStream inputStream) {
        this(values);
        try {
            this.reader = new PdfReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FootPrint(Map<String, String> values, String templatePDF) {
        this(values);
        try {
            this.reader = new PdfReader(new FileInputStream(templatePDF));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FootPrint(Map<String, String> values) {
        this.values = values;
    }

    public byte[] generate() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfStamper stamper = new PdfStamper(reader, baos);
            AcroFields fields = stamper.getAcroFields();
            Set<String> keys = fields.getFields().keySet();
            for (String k : keys) {
                fields.setField(k, values.get(k));
            }
            stamper.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

}
