package org.cejug.footprint.core.exporter;

/**
 * An exporter implementation can throw a set of diverse exceptions, but for the
 * external consumers this should be the only visible exception.
 * 
 * @author Felipe Gaucho
 * 
 */
public class FootprintExporterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FootprintExporterException(Throwable error) {
        super(error);
    }
}
