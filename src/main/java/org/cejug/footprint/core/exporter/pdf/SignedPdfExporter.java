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
package org.cejug.footprint.core.exporter.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.cejug.footprint.core.exporter.FootprintExporterException;
import org.cejug.footprint.core.security.FootprintSigner;
import org.cejug.footprint.core.security.pdf.FootprintSecurityFactory;
import org.cejug.footprint.jaxb.ConfigPdfTemplate;
import org.cejug.footprint.jaxb.FootprintConfig;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Create signed PDF documents.
 * 
 * @author Felipe Gaucho
 */
class SignedPdfExporter extends AbstractPdfExporter {
    private static final String TEXTFONT_FIELD_PROPERTY = "textfont";

    /**
     * Creates a new exporter.
     * 
     * @param config
     *            the configuration object.
     */
    public SignedPdfExporter(FootprintConfig config) {
        super(config);
    }

    @Override
    public void export(final OutputStream output, final Charset charset, final Map<String, String> fields, final Map<String, BaseFont> trueTypeMapping) throws FootprintExporterException {

        // try {
        ConfigPdfTemplate templateConfig = config.getTemplate();
        URL url = Thread.currentThread().getContextClassLoader().getResource(templateConfig.getPdfTemplateFilename());
        PdfReader templateReader;

        try {
            templateReader = new PdfReader(url);
            ByteArrayOutputStream bytesWriter = new ByteArrayOutputStream(templateReader.getFileLength());
            PdfStamper stamper = new PdfStamper(templateReader, bytesWriter);

            AcroFields l_acrofields = stamper.getAcroFields();

            if (fields != null) {
                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    String fieldName = entry.getKey();
                    BaseFont unicode = trueTypeMapping.get(fieldName);
                    if (unicode != null) {
                        l_acrofields.setFieldProperty(fieldName, TEXTFONT_FIELD_PROPERTY, unicode, null);
                    }
                    l_acrofields.setField(fieldName, entry.getValue());
                }
            }

            stamper.close();
            byte[] b = bytesWriter.toByteArray();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            FootprintSigner signer = FootprintSecurityFactory.getInstance().getDefaultPdfSigner(config);
            signer.sign(bi, output);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO log...
            throw new FootprintExporterException(e);
        } catch (IllegalArgumentException e) {
            throw new FootprintExporterException(e);
        } catch (GeneralSecurityException e) {
            // TODO log...
            throw new FootprintExporterException(e);
        } catch (ClassNotFoundException e) {
            // TODO log...
            throw new FootprintExporterException(e);
        } catch (InstantiationException e) {
            // TODO log...
            throw new FootprintExporterException(e);
        } catch (IllegalAccessException e) {
            // TODO log...
            throw new FootprintExporterException(e);
        } catch (InvocationTargetException e) {
            // TODO log...
            throw new FootprintExporterException(e);
        } catch (NoSuchMethodException e) {
            // TODO log...
            throw new FootprintExporterException(e);
        }
    }
}
