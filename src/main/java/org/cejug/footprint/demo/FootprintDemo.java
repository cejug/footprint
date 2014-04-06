package org.cejug.footprint.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import javax.mail.MessagingException;

import org.cejug.footprint.core.exporter.Exporter;
import org.cejug.footprint.core.exporter.pdf.PdfExporterFactory;
import org.cejug.footprint.core.mailer.DefaultMailerStrategy;
import org.cejug.footprint.core.mailer.Mailer;
import org.cejug.footprint.core.publisher.FootprintPublisher;
import org.cejug.footprint.core.publisher.JdbcPublisher;
import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.core.xml.FootprintXmlReader;
import org.cejug.footprint.core.xml.XmlStreamFactory;
import org.cejug.footprint.jaxb.FootprintConfig;

/**
 * Footprint demo class.
 * 
 * @author Felipe Gaucho
 */
public class FootprintDemo {
    public static final String CONFIG_EXAMPLE_FILENAME = "configExample.xml";
    private FootprintConfig config = null;

    public static void main(String[] args) throws Exception {
        new FootprintDemo();
    }

    /**
     * Basic usage of Footprint library. We are working in a fancy GUI but
     * without any release milestone. You can find the draft Swing application
     * that is being used as proof-of-concept in order to evaluate a good
     * usability for our GUI at SVN repository.
     */
    public FootprintDemo() throws Exception {
        super();
        // Load the XML confguration file and creates an Object representation
        // through JAXB.
        loadConfig();

        // optional, just to show how to change the Config attributes by code.
        setCustomMsgBody();

        // here the system generates and sign all certificates - 1 for each line
        // of the database.
        generateCertificates();

        // IMPORTANT: remember to edit the CSV file or update your database
        // values before to trigger this line,
        // otherwise it will fail. Remember also to update the SMTP values, like
        // password, user and host :)
        // sendByEmail();
    }

    /**
     * Dispatch each generated PDF to its filename email address.
     * 
     * @throws MessagingException
     */
    private void sendByEmail() throws MessagingException {
        Mailer mailer = new DefaultMailerStrategy(config);
        mailer.send();
    }

    /**
     * Creates an Object representation of the XML configuration file.
     * 
     * @throws Exception
     *             IOException or any problem in the configuration XML file.
     */
    private void loadConfig() throws Exception {
        FootprintXmlReader reader = XmlStreamFactory.getInstance().getReader();
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_EXAMPLE_FILENAME);
        InputStreamReader file_reader = new InputStreamReader(stream, FootprintDefaults.Constants.FILE_ENCODING.toString());
        config = reader.read(file_reader).getValue();
    }

    /**
     * Method that writes the signed PDF files to the destination folder, using
     * the following classes:
     * <ul>
     * <li><strong>Exporter</strong> is a class the generate and sign a PDF
     * document.</li>
     * <li><strong>Publisher</strong> is a thread the reads a data source and
     * generates 1 certificate for each line.</li>
     * </ul>
     */
    private void generateCertificates() {
        if (config != null) {

            // Exporter is a class the generate and sign a PDF document.
            Exporter jdbcExporter;
            try {
                jdbcExporter = PdfExporterFactory.getPdfExporter(Exporter.DEFAULT_SIGNED_PDF_EXPORTER, new Object[] { config });
                // Publisher is a thread the reads a data source and generates 1
                // certificate for each line.
                FootprintPublisher publisher = new JdbcPublisher(jdbcExporter, config, FootprintDefaults.getDefaultFootprintLogger());
                Thread service = new Thread(publisher);
                service.start();
                // long timestamp = System.currentTimeMillis();
                while (service.isAlive()) {
                    /*
                     * if (System.currentTimeMillis() > timestamp + 30000) {
                     * fail("more than 30 seconds is too much! something
                     * wrong."); } else {
                     */
                    Thread.sleep(1000);
                    // }
                }
            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            } catch (SecurityException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private void setCustomMsgBody() {
        config.getEmail().setMsgBody(
                "Dear TestJUG member,\n\n" + "You are receiving your certificate of participation in the " + "Footprint Demo Conference, held in Zürich on 13th December 1969."
                        + "\n\nThe atached document was signed with a digital certificate so " + "its contents can be verified against a public key available at the address "
                        + "https://cejug.dev.java.net/servlets/ProjectDocumentView?documentID=62858&noNav=true" + "\n\nQuestion and suggestion are very welcome on the email fgaucho@gmail.com"
                        + " \n\n\nWe appreciate your presence and we hope to see you in our next event" + "\n\n\tFelipe Gaúcho\n" + "\tFootprint Owner\n" + "\n\n" + "___________\n"
                        + "Find the open-source code used to produce this certificate at footprint.dev.java.net.");
    }
}
