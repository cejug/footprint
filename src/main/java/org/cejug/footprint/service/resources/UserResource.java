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

import java.util.Collection;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.WebServiceException;

import org.cejug.footprint.jpa.entity.FpAbstractUser;
import org.cejug.footprint.jpa.entity.FpUser;
import org.cejug.footprint.jpa.entity.facade.FpAbstractUserFacade;
import org.cejug.footprint.jpa.entity.facade.FpUserFacade;

/**
 * 
 * @author Felipe Gaucho
 * 
 */
@Path("user")
public class UserResource {
    @EJB
    private FpAbstractUserFacade abstractUserFacade;

    @EJB
    private FpUserFacade userFacade;

    // private static final Logger LOGGER =
    // Logger.getLogger(UserResource.class.getName());

    /**
     * Creates a new FpUserWritable record in the database.
     * 
     * <pre>
     * curl -v -H &quot;Content-Type: application/xml&quot; -X PUT --data-binary @2.xml http://fgaucho.dyndns.org:8080/footprint-service/user/create
     * 
     * 
     * 
     * curl -v -H &quot;Content-Type:application/xml&quot; -X PUT --data-binary &quot;&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;yes&quot;?&gt;&lt;ns2:fpUser xmlns:ns2=&quot;http://footprint.dev.java.net/service/entity&quot; version=&quot;1&quot;&gt;&lt;id/&gt;&lt;name&gt;Felipe Gaúcho&lt;/name&gt;&lt;email&gt;fgaucho@gmail.com&lt;/email&gt;&lt;organization version=&quot;1&quot;&gt;&lt;id&gt;3&lt;/id&gt;&lt;logotype&gt;http://netcetera.ch/docroot/netcetera/images/design/netcetera.logo.gif&lt;/logotype&gt;&lt;name&gt;Netcetera AG&lt;/name&gt;&lt;website&gt;http://netcetera.ch&lt;/website&gt;&lt;/organization&gt;&lt;/ns2:fpUser&gt;&quot; http://fgaucho.dyndns.org:8080/footprint-service/event/create
     * 
     * 
     * </pre>
     * 
     * @param newUser
     *            the new user to be inserted in the database.
     * @return the newly created user entity.
     * 
     */
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @PUT
    @Path("create")
    public FpUser create(FpUser newUser) {
        FpUser user = userFacade.create(newUser);
        return user;
    }

    /**
     * Returns the item if existing.
     * 
     * @response.representation.200.qname 
     *                                    {http://footprint.dev.java.net/service/
     *                                    entity}fpAbstractUser
     * @response.representation.200.mediaType application/xml
     * @response.representation.200.example aaaaaaaaa
     * 
     * @return the requested item.
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("read/{id}")
    public JAXBElement<FpAbstractUser> read(@PathParam("id") String id) {
        return new JAXBElement<FpAbstractUser>(FpAbstractUser.QUALIFIED_NAME, FpAbstractUser.class, abstractUserFacade.read(Integer.valueOf(id)));
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("edit/{id}")
    public FpUser edit(@PathParam("id") String id) {
        return userFacade.read(Integer.valueOf(id));
    }

    /**
     * <pre>
     * curl -v -H &quot;Content-Type: application/xml&quot; -X PUT  --data-binary @8.xml http://fgaucho.dyndns.org:8080/footprint-service/user/create
     * 
     * curl -v -H &quot;Content-Type: application/xml&quot; -X POST  --data-binary @8.xml http://fgaucho.dyndns.org:8080/footprint-service/user/update
     * 
     * curl -v -H &quot;Content-Type: application/xml&quot; -X PUT --data-binary @2.xml http://fgaucho.dyndns.org:8080/footprint-service/user/updateab
     * 
     * curl -v -H &quot;Content-Type: application/xml&quot; -X GET http://fgaucho.dyndns.org:8080/footprint-service/user/readall
     * </pre>
     * 
     * 
     * curl -b cookies.txt
     * http://fgaucho.dyndns.org:8080/cejug-classifieds-richfaces
     * /pages/advertisement/publishAdvertisement.xhtml
     * 
     * curl -v -b cookies.txt
     * http://fgaucho.dyndns.org:8080/cejug-classifieds-richfaces
     * /pages/advertisement/publishAdvertisement.xhtml curl -v -H
     * "Content-Type:application/xml" -b cookies.txt -d
     * "j_username=fgaucho&j_password=test" -X PUT --data-binary @5.xml
     * http://fgaucho.dyndns.org:8080/footprint-service/user/create curl -v -H
     * "Content-Type:application/xml" -b cookies.txt -X PUT --data-binary @5.xml
     * http://fgaucho.dyndns.org:8080/footprint-service/user/create
     * 
     * curl -v -H "Content-Type:application/xml" -b cookies.txt -d
     * "j_username=fgaucho&j_password=test" -X GET --data-binary @5.xml
     * http://fgaucho.dyndns.org:8080/footprint-service/user/create
     * http://fgaucho.dyndns.org:8080/cejug-classifieds-richfaces/index.faces#
     * 
     * 
     * curl -c cookies.txt -d "j_username=fgaucho&j_password=test"
     * http://fgaucho
     * .dyndns.org:8080/cejug-classifieds-richfaces/pages/security/login.faces
     * 
     * curl -c cookies.txt -d "j_username=fgaucho&j_password=test"
     * http://fgaucho
     * .dyndns.org:8080/cejug-classifieds-richfaces/pages/security/
     * j_security_check curl -b "Acookie.file" -e "www.domain.com" -F
     * "op=login&redir=&nick=MyNickName&pass=MyPassword"
     * www.domain.com/index.php
     * 
     * @param newUser
     * @return
     * 
     */
    @Produces({ MediaType.APPLICATION_XML })
    @Consumes(MediaType.APPLICATION_XML)
    @PUT
    @Path("update")
    public FpUser update(FpUser user) {
        return userFacade.update(user);
    }

    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("count")
    public String count() {
        return Long.toString(abstractUserFacade.count());
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("readall/{offset}/{limit}")
    public Collection<FpAbstractUser> read(@PathParam("offset") int offset, @PathParam("limit") int limit) {
        return abstractUserFacade.readAll(offset, limit);
    }

    @DELETE
    @Path("delete/{id}")
    public void delete(@PathParam("id") String id) {
        try {
            userFacade.delete(Integer.valueOf(id));
        } catch (Exception o) {
            throw new WebServiceException(o);
        }
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("editall/{offset}/{limit}")
    public Collection<FpUser> edit(@PathParam("offset") int offset, @PathParam("limit") int limit) {
        return userFacade.readAll(offset, limit);
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("search")
    public Collection<FpAbstractUser> search(@QueryParam("name") String name, @QueryParam("organization") Integer organization, @PathParam("limit") Integer limit) {
        return abstractUserFacade.findByName(name);
    }
}
