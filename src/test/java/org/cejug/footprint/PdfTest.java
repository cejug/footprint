package org.cejug.footprint;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Set;

import org.junit.Test;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfTest {

    private static final String SAMPLE = "samples.pdf";
    
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
        AcroFields fields = reader.getAcroFields();
        Set<String> keys = fields.getFields().keySet();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, baos);
        
        for (String k : keys) {
          fields.setField(fields.getField(k),fields.getField(k));
        }
        
        stamper.close();
        
    }
    
}
