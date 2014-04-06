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
package org.cejug.footprint.core.publisher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cejug.footprint.core.exporter.Exporter;
import org.cejug.footprint.core.exporter.pdf.PdfExporterFactory;
import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.jaxb.CommonI18N;
import org.cejug.footprint.jaxb.ConfigJdbc;
import org.cejug.footprint.jaxb.ConfigMapping;
import org.cejug.footprint.jaxb.ConfigPdfTemplate;
import org.cejug.footprint.jaxb.DriverProperties;
import org.cejug.footprint.jaxb.FontMapping;
import org.cejug.footprint.jaxb.FootprintConfig;
import org.cejug.footprint.jaxb.JdbcDriverProperties;
import org.cejug.footprint.jaxb.Pdf2JdbcMapping;
import org.cejug.footprint.jaxb.PublisherI18N;
import org.cejug.footprint.jaxb.TrueTypeFileMapping;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

/**
 * A publisher based on JDBC connections, i.e. opens a database connection and
 * generate a document for each row.
 * 
 * @author Felipe Gaucho
 */
public class JdbcPublisher extends AbstractFootprintPublisher {

    /** The footprint configuration object. */
    private transient FootprintConfig config = null;

    /** The publisher logger. */
    private transient Logger logger = null;

    /**
     * Creates a publisher using the footprint default logger and a default
     * document exporter.
     * 
     * @param config
     *            the configuration object - mandatory, cannot be null.
     * @throws IOException
     */
    public JdbcPublisher(FootprintConfig config) throws IllegalArgumentException, SecurityException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        this(PdfExporterFactory.getPdfExporter(Exporter.DEFAULT_SIGNED_PDF_EXPORTER, new Object[] { config }), config, FootprintDefaults.getDefaultFootprintLogger());
    }

    /**
     * New PDF publisher - a class the generates PDF in the filesystem.
     * 
     * @param exporter
     *            the PDF exporter.
     * @param config
     *            the configuration object.
     * @param logger
     *            the logger where to write the messages.
     * @throws IOException
     *             impossible to save the PDF document.
     * @throws NullPointerException
     *             configuration problems.
     */
    public JdbcPublisher(Exporter exporter, FootprintConfig config, Logger logger) throws IOException {
        super();
        this.config = config;
        this.logger = logger;
        this.exporter = exporter;
        ConfigPdfTemplate template = config.getTemplate();
        File generatedPdfsFolder = new File(template.getPdfGeneratedPath());
        if (!generatedPdfsFolder.exists() && !generatedPdfsFolder.mkdirs()) {
            throw new IOException(generatedPdfsFolder.getAbsolutePath());
        }
    }

    /**
     * produces the output document.
     * 
     * @throws Exception
     *             the subclasses can treat or throws its exceptions.
     */
    public void publish() throws FootprintPublisherException {
        ResultSet results = null;
        String filename = null;
        Statement stmt = null;
        Connection conn = null;
        try { // load the driver into memory
            ConfigMapping mappingProperties = config.getPdf2Jdbc();
            ConfigJdbc jdbcProperties = config.getDatabase();
            Map<String, BaseFont> trueTypesMapping = loadTrueTypesMapping();

            Class.forName(jdbcProperties.getJdbcDriverClassname());
            conn = DriverManager.getConnection(jdbcProperties.getJdbcConnectionUrl(), convertDriverProperties(jdbcProperties.getDriverProperties()));

            // create a Statement object to execute the query with
            stmt = conn.createStatement();
            final String query = jdbcProperties.getJdbcQuery();

            results = stmt.executeQuery(query);
            logger.log(Level.INFO, PublisherI18N.PUBLISHER_EXECUTE_QUERY.value(), new String[] { query });

            // dump out the results
            String mailField = jdbcProperties.getEmailColumnName();
            int counter = 0;
            ConfigPdfTemplate templateProperties = config.getTemplate();
            Map<String, String> replacingValues = new HashMap<String, String>();
            Charset charset = Charset.forName(templateProperties.getPdfGeneratedCharset());

            while (results.next()) {
                replacingValues.clear();
                filename = templateProperties.getPdfGeneratedPath() + File.separator + templateProperties.getPdfGeneratedPrefix() + (mailField == null ? counter++ : results.getString(mailField))
                        + templateProperties.getPdfGeneratedExtension();

                List<Pdf2JdbcMapping> mapping = mappingProperties.getMapping();
                for (Pdf2JdbcMapping map : mapping) {
                    String pdfKey = map.getPdfField();
                    String jdbcColumnName = map.getDatabaseColumnName();
                    if (jdbcColumnName != null) {
                        replacingValues.put(pdfKey, results.getString(jdbcColumnName));
                    }
                }

                File newPdfFile = createNewPdfFile(filename, templateProperties.isReplaceable());
                OutputStream stream = null;
                try {
                    stream = new FileOutputStream(newPdfFile);
                    exporter.export(stream, charset, replacingValues, trueTypesMapping);
                    logger.log(Level.INFO, PublisherI18N.PUBLISHER_OK.value(), new String[] { filename });
                } finally {
                    stream.close();
                }
            }
        } catch (IllegalArgumentException error) {
            logger.throwing(JdbcPublisher.class.getName(), PublisherI18N.PUBLISHER_WRONG_PARAMETER.value(), error);

        } catch (ClassNotFoundException classpathProblem) {
            throw new FootprintPublisherException("Classpath problem.. please review your installation", classpathProblem);
        } catch (SQLException sqlError) {
            throw new FootprintPublisherException("Please review your query", sqlError);
        } catch (IOException e) {
            logger.throwing(JdbcPublisher.class.getName(), PublisherI18N.PUBLISHER_NOT_OK.value(), e);
        } finally {
            // connection cleanup
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) {
                logger.log(Level.INFO, CommonI18N.FAIL_TO_CLEANUP_RESOURCE.value(), new String[] { e.getMessage() });
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                logger.log(Level.INFO, CommonI18N.FAIL_TO_CLEANUP_RESOURCE.value(), new String[] { e.getMessage() });
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.INFO, CommonI18N.FAIL_TO_CLEANUP_RESOURCE.value(), new String[] { e.getMessage() });
            }
        }
    }

    /**
     * Create a new file for each record in the database.
     * 
     * @param filename
     *            the absolute path of the new file.
     * @param replaceable
     *            if an existing PDF file should be replaced or not - this value
     *            usually comes from the footprint configuration file (XML).
     * @return a reference to the new file.
     * @throws IOException
     *             problems writing to disk.
     */
    private File createNewPdfFile(String filename, boolean replaceable) throws IOException {
        File generatedPdf = new File(filename);
        File parent = null;
        if (generatedPdf.exists()) {
            if (replaceable && generatedPdf.delete() && generatedPdf.createNewFile()) {
                FootprintDefaults.getDefaultFootprintLogger().log(Level.INFO, PublisherI18N.PUBLISHER_PDF_REPLACED.value(), new String[] { generatedPdf.getAbsolutePath() });
            } else {
                FootprintDefaults.getDefaultFootprintLogger().log(Level.SEVERE, PublisherI18N.PUBLISHER_PDF_FAILED_TO_REPLACE.value(), new String[] { generatedPdf.getAbsolutePath() });
            }
        } else {
            parent = new File(generatedPdf.getParent());

            if ((parent.exists() || parent.mkdirs()) && generatedPdf.createNewFile()) {
                FootprintDefaults.getDefaultFootprintLogger().log(Level.FINEST, PublisherI18N.PUBLISHER_OK.value(), new String[] { parent.getAbsolutePath() });
            } else {
                // Not possible to create parent directory.
                FootprintDefaults.getDefaultFootprintLogger().log(Level.SEVERE, PublisherI18N.PUBLISHER_PATH_FAILURE.value(), new String[] { parent.getAbsolutePath() });
            }
        }
        return generatedPdf;
    }

    /**
     * 
     * @return
     */
    private Map<String, BaseFont> loadTrueTypesMapping() {
        Map<String, BaseFont> fonts = null;
        TrueTypeFileMapping fontMapping = config.getFonts();
        if (fontMapping != null) {
            fonts = new HashMap<String, BaseFont>();
            Collection<FontMapping> fontsByField = fontMapping.getMapping();
            for (FontMapping mapping : fontsByField) {
                String fieldName = mapping.getPdfField();
                String fontFile = mapping.getFontFile();
                URL fontUrl = Thread.currentThread().getContextClassLoader().getResource(fontFile);
                if (fontUrl != null) {
                    try {
                        BaseFont unicode = BaseFont.createFont(fontUrl.getFile(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                        fonts.put(fieldName, unicode);
                    } catch (DocumentException error) {
                        // Not possible to create parent directory.
                        FootprintDefaults.getDefaultFootprintLogger().log(Level.SEVERE, PublisherI18N.PUBLISHER_PDF_TRUETYPE_FONT_ERROR.value(), new String[] { fontUrl.getPath() });
                    } catch (IOException error) {
                        // Not possible to create parent directory.
                        FootprintDefaults.getDefaultFootprintLogger().log(Level.SEVERE, PublisherI18N.PUBLISHER_PDF_TRUETYPE_FONT_ERROR.value(), new String[] { fontUrl.getPath() });
                    }
                }
            }
        }
        return fonts;
    }

    /**
     * This method is a workaround until I find a way to represent maps in
     * schema. Actually, the map representation is not predicted in the schema
     * language, but there is a hope to create a more generic JAXB customization
     * to handle this problem.
     * 
     * @param driverConfiguration
     *            the configuration object with the values to be passed to the
     *            JDBC driver.
     * @return a Properties required by the database driver.
     */
    private Properties convertDriverProperties(DriverProperties driverConfiguration) {
        Properties driverProperties = new Properties();
        List<JdbcDriverProperties> list = driverConfiguration.getDriverProperties();
        for (JdbcDriverProperties prop : list) {
            driverProperties.put(prop.getName(), prop.getValue());
        }
        return driverProperties;
    }
}
