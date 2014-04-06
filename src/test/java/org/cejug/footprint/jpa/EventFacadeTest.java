package org.cejug.footprint.jpa;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.cejug.footprint.jpa.entity.FpAbstractEvent;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * 
 * @author Felipe Gaucho
 * 
 */
public class EventFacadeTest {
    // private FootprintEntityFacade<FpEvent> eventFacade = new EventFacade();

    @Ignore
    @Test
    public void eventCrudTest() {
        // InputStream stream =
        // Thread.currentThread().getContextClassLoader().getResourceAsStream("mockup/Event.xml");
        try {
            // FpEvent event = (FpEvent) read(stream, FpEvent.class).getValue();
            System.out.println(read(null, FpAbstractEvent.class));
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public JAXBElement<?> read(InputStream inputStream, Class type) throws JAXBException, SAXException, IOException {
        JAXBContext jc = JAXBContext.newInstance(type.getPackage().getName(), Thread.currentThread().getContextClassLoader());
        // JAXBContext jc =
        // JAXBContext.newInstance(type.getPackage().getName());

        Unmarshaller unmarshaller = jc.createUnmarshaller();

        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.XML_NS_URI);
        URL schemaFile = Thread.currentThread().getContextClassLoader().getResource("schema1.xsd");
        Schema schema = sf.newSchema(schemaFile);
        // unmarshaller.setSchema(null);
        // unmarshaller.setEventHandler(new FootprintConfigValidationHandler());
        // unmarshaller.setListener(listener);
        // SchemaFactory sf =
        // SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // Schema schema =
        // sf.newSchema(Thread.currentThread().getContextClassLoader().getResource(FootprintDefaults.Constants.SCHEMA_FILE.toString()));
        unmarshaller.setSchema(schema);
        URL url = new URL("http://fgaucho.dyndns.org:8080/footprint-service/certificate/test");

        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "application/xml");

        // InputStream stream = connection.getInputStream();
        // BufferedInputStream reader = new BufferedInputStream(stream);
        /*
         * System.out.println(reader.available()); byte[] ava = new
         * byte[reader.available()]; reader.read(ava); System.out.println(new
         * String(ava)); // return null;
         */
        return (JAXBElement<?>) unmarshaller.unmarshal(connection.getInputStream());
    }

}
