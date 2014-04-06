package org.cejug.footprint.service.filter;

import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author Felipe Gaucho
 * 
 */
// Partial code listing showing the important bits...
public class WadlFormatFilter implements Filter {
    private ServletContext ctx;
    private FilterConfig filterConfig = null;
    private TransformerFactory xsltFactory = null;
    private Templates xsltTemplates = null;
    private static final Logger LOGGER = Logger.getLogger(WadlFormatFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        LOGGER.finest("WadlFormatFilter:doFilter()");

        if (request != null && response instanceof HttpServletResponse) {
            String type = request.getParameter("format");

            if (type != null && type.equals("html")) {
                String contentType = "text/html";

                OutputStream out = response.getOutputStream();
                ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) response);

                chain.doFilter(request, wrapper);

                ByteArrayInputStream test = new ByteArrayInputStream(wrapper.getData());

                Source xfrmSrc = new StreamSource(test);
                Transformer tmplXfrmer;
                try {
                    tmplXfrmer = xsltTemplates.newTransformer();
                    CharArrayWriter finalOut = new CharArrayWriter();
                    StreamResult xfrmResult = new StreamResult(finalOut);

                    tmplXfrmer.transform(xfrmSrc, xfrmResult);

                    response.setContentType(contentType);
                    response.setContentLength(finalOut.toString().length());
                    out.write(finalOut.toString().getBytes());
                    out.close();

                    LOGGER.finest("WadlFormatFilter:XSLT transform succeeded");
                } catch (TransformerConfigurationException e) {
                    throw new ServletException(e);

                } catch (TransformerException e) {
                    throw new ServletException(e);
                }
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            this.ctx = filterConfig.getServletContext();
            this.xsltFactory = TransformerFactory.newInstance();
            LOGGER.finest("WadlFormatFilter:Initializing filter");

            String xsltFile = this.filterConfig.getInitParameter("xsltfile");

            try {
                this.xsltTemplates = xsltFactory.newTemplates(new StreamSource(this.ctx.getRealPath(xsltFile)));
            } catch (Exception ex) {
                LOGGER.finest(ex.toString());
                throw new ServletException(ex.toString());
            }
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}