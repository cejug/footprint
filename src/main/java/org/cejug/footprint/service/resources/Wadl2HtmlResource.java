/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (C) 2009 Footprint Project
 
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
 
 This file is part of the Footprint Project: https://github.com/cejug/footprint
 
 
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
package org.cejug.footprint.service.resources;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author Felipe Gaucho
 * 
 */
@Produces({ MediaType.TEXT_HTML, MediaType.APPLICATION_XML })
@Singleton
@Path("wadl")
public class Wadl2HtmlResource {

    private static final String NET_SF_SAXON_TRANSFORMER_FACTORY_IMPL = "net.sf.saxon.TransformerFactoryImpl";

    // private WadlApplicationContext wadlContext;
    //
    // private Application application;
    //
    // private byte[] wadlHtmlRepresentation;
    //
    // public Wadl2HtmlResource(@Context WadlApplicationContext wadlContext) {
    // this.wadlContext = wadlContext;
    // this.application = wadlContext.getApplication();
    // // System.setProperty("javax.xml.transform.TransformerFactory",
    // // "org.apache.xalan.processor.TransformerFactoryImpl");
    // // System.setProperty("javax.xml.transform.TransformerFactory",
    // // "org.apache.xalan.processor.TransformerFactoryImpl");
    // // System.setProperty("javax.xml.transform.TransformerFactory",
    // // "net.sf.saxon.TransformerFactoryImpl");
    // }

    // @GET
    // public synchronized Response getWadl(@Context UriInfo uriInfo) {
    // if (wadlHtmlRepresentation == null) {
    // if (application.getResources().getBase() == null) {
    // application.getResources().setBase(
    // uriInfo.getBaseUri().toString());
    // }
    // try {
    // application.getDoc().get(0).setTitle("Footprint Service API");
    // /*
    // * Get the current XML WADL into a byte array.
    // */
    // final Marshaller marshaller = wadlContext.getJAXBContext()
    // .createMarshaller();
    // final ByteArrayOutputStream os = new ByteArrayOutputStream();
    // marshaller.marshal(application, os);
    // byte[] wadlXmlRepresentation = os.toByteArray();
    // os.close();
    //
    // TransformerFactory tFactory = TransformerFactory.newInstance(
    // NET_SF_SAXON_TRANSFORMER_FACTORY_IMPL, Thread
    // .currentThread().getContextClassLoader());
    // URL xslUrl = new URL(
    // "http://www.mnot.net/webdesc/wadl_documentation.xsl");
    //
    // Transformer transformer = tFactory
    // .newTransformer(new StreamSource(xslUrl.openStream()));
    //
    // /*
    // * Create a StreamSource for the WADL XML byte array
    // */
    // InputStream xmlStream = new BufferedInputStream(
    // new ByteArrayInputStream(wadlXmlRepresentation));
    // StreamSource xmlSource = new StreamSource(xmlStream);
    //
    // /*
    // * Perform the transform to an output stream
    // */
    // ByteArrayOutputStream htmlOs = new ByteArrayOutputStream();
    // transformer.transform(xmlSource, new StreamResult(htmlOs));
    // wadlHtmlRepresentation = htmlOs.toByteArray();
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // return Response.ok(new ByteArrayInputStream(wadlHtmlRepresentation))
    // .build();
    // }

}