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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Singleton;
import javax.persistence.TransactionRequiredException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.cejug.footprint.jpa.entity.FpAddress;
import org.cejug.footprint.jpa.entity.FpCertificate;
import org.cejug.footprint.jpa.entity.FpEvent;
import org.cejug.footprint.jpa.entity.FpGLatLng;
import org.cejug.footprint.jpa.entity.FpOrganization;
import org.cejug.footprint.jpa.entity.FpUser;
import org.cejug.footprint.jpa.entity.facade.EntityFacadeConstants;
import org.cejug.footprint.jpa.entity.facade.FpCertificateFacade;
import org.cejug.footprint.jpa.entity.facade.FpEventFacade;
import org.cejug.footprint.jpa.entity.facade.FpUserFacade;

/**
 * 
 * @author Felipe Gaucho
 * 
 */
@Singleton
@Path("mock")
public class MockResource {
    @EJB
    private FpUserFacade userFacade;

    @EJB
    private FpCertificateFacade certificateFacade;

    @EJB
    private FpEventFacade eventFacade;

    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    // @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("create")
    public Collection<FpCertificate> mockup() throws TransactionRequiredException, IllegalStateException, IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        FpAddress address = new FpAddress();
        address.setCountry("Switzerland");
        address.setCity("Zürich");
        // address.setComplement("");
        FpGLatLng lan = new FpGLatLng();
        lan.setLatitude(3d);
        lan.setLongitude(13d);
        address.setCoordinates(lan);
        address.setName("Netcetera AG");
        address.setNumber(71);
        address.setPhone("+41 44 247 70 70");
        address.setStreet("Zypressenstrasse");
        address.setZip("8040");

        FpUser user = new FpUser();
        user.setEmail("fgaucho@gmail.com");
        user.setName("Felipe Gaúcho");

        FpOrganization sun = new FpOrganization();
        // sun.setAddress(address);
        sun.setName("SUN Microsystems");
        sun.setWebsite("http://www.sun.com/");
        sun.setLogo("http://www.uclasoles.com/rldc09/images/sun-microsystems.jpg");

        FpOrganization nca = new FpOrganization();
        // nca.setAddress(address);
        nca.setName("Netcetera AG");
        nca.setWebsite("http://netcetera.ch");
        nca.setLogo("http://netcetera.ch/docroot/netcetera/images/design/netcetera.logo.gif");
        user.setOrganization(nca);

        user = userFacade.create(user);

        FpEvent jazoon = null;
        FpEvent javaone = null;
        if (eventFacade.count() < 2) {
            jazoon = new FpEvent();
            jazoon.setAddress(address);
            jazoon.setLastUpdated(System.currentTimeMillis());
            jazoon.setName("Jazoon 2009");
            jazoon.setWebsite("http://jazoon.com/");
            jazoon = eventFacade.create(jazoon);
            jazoon.getUser().add(user);
            jazoon.getSponsor().add(nca);
            eventFacade.update(jazoon);

            javaone = new FpEvent();
            javaone.setAddress(address);
            javaone.setLastUpdated(System.currentTimeMillis());
            javaone.setName("JavaONE");
            javaone.setWebsite("http://java.sun.com/javaone/");
            javaone = eventFacade.create(javaone);
            javaone.getUser().add(user);
            javaone.getSponsor().add(sun);
            javaone.getSponsor().add(nca);
            eventFacade.update(javaone);
        } else {
            Collection<FpEvent> events = eventFacade.readAll(0, EntityFacadeConstants.MAX_PAGINATION_CHUNK.INT_VALUE);
            FpEvent[] eventsArray = new FpEvent[events.size()];
            events.toArray(eventsArray);
            jazoon = eventsArray[0];
            javaone = eventsArray[1];

        }
        List<FpCertificate> certs = new ArrayList<FpCertificate>();

        FpCertificate certJavaOne = new FpCertificate();
        certJavaOne.setPath("/docs/" + user.getId() + "/" + javaone.getId() + "/cert");
        certJavaOne.setUser(user);
        certs.add(certificateFacade.create(certJavaOne));

        FpCertificate certJazoon = new FpCertificate();
        certJazoon.setPath("/docs/" + user.getId() + "/" + jazoon.getId() + "/cert");
        certJazoon.setUser(user);
        certs.add(certificateFacade.create(certJazoon));
        return certs;
    }
}
