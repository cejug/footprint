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
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 
 * @author eprogramming
 *
 */
public class PdfTest {

    private static final String SAMPLE = "example.pdf";

    @Test
    public void should_print_the_values_of_pdf_sample() throws Exception {

        URL url = this.getClass().getResource(SAMPLE);
        PdfReader reader = new PdfReader(url);
        AcroFields fields = reader.getAcroFields();
        Set<String> keys = fields.getFields().keySet();
        for (String k : keys) {
            System.out.println(k + ": " + fields.getField(k));
        }

    }

    @Test
    public void should_write_the_values_of_pdf_sample() throws Exception {

        URL url = this.getClass().getResource(SAMPLE);
        PdfReader reader = new PdfReader(url);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("result.pdf"));
        AcroFields form = stamper.getAcroFields();
        form.setField("member", "12345");
        form.setField("userGroup", "6789");
        form.setField("eventName", "101112");
        form.setField("eventTime", "101112");
        form.setField("venue", "101112");
        stamper.close();

    }

    @Test
    public void should_write_the_values_of_pdf_sample_on_bytearray() throws Exception {

        URL url = this.getClass().getResource(SAMPLE);
        PdfReader reader = new PdfReader(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, baos);
        AcroFields form = stamper.getAcroFields();
        form.setField("member", "eeee");
        form.setField("userGroup", "dddd");
        form.setField("eventName", "cccc");
        form.setField("eventTime", "bbbb");
        form.setField("venue", "aaaaa");
        stamper.close();

        try (FileOutputStream fos = new FileOutputStream("result.pdf")) {
            fos.write(baos.toByteArray());
        }

    }

    @Test
    public void shouldCheckDifferentConstructors() throws Exception {

        Map<String, String> values = new HashMap<>();
        values.put("member", "abcde");
        values.put("userGroup", "okokok");
        values.put("eventName", "xyz");
        values.put("eventTime", "987654321");
        values.put("venue", "123456789");

        FootPrint footPrint = new FootPrint(values, this.getClass().getResource(SAMPLE));
        System.out.println(footPrint.generate().length);

        footPrint = new FootPrint(values, "example.pdf");
        System.out.println(footPrint.generate().length);

        footPrint = new FootPrint(values, this.getClass().getResourceAsStream(SAMPLE));
        System.out.println(footPrint.generate().length);

    }

}
