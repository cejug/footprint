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
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.TransactionRequiredException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.WebServiceException;

import org.cejug.footprint.jpa.entity.FpAbstractEvent;
import org.cejug.footprint.jpa.entity.FpEvent;
import org.cejug.footprint.jpa.entity.FpTimeSlot;
import org.cejug.footprint.jpa.entity.facade.FpAbstractEventFacade;
import org.cejug.footprint.jpa.entity.facade.FpEventFacade;

/**
 * 
 * @author Felipe Gaucho
 * 
 */
@Path("event")
public class EventResource {
    /** The facade for the Event entities. */
    @EJB
    private FpEventFacade eventFacade;

    /** The facade for entities with partial information about Events. */
    @EJB
    private FpAbstractEventFacade abstractEventFacade;

    @PUT
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("create")
    public FpEvent create(FpEvent event) {
        return eventFacade.create(event);
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("read/{id}")
    public JAXBElement<FpAbstractEvent> read(@PathParam("id") String id) {
        return new JAXBElement<FpAbstractEvent>(FpAbstractEvent.QUALIFIED_NAME, FpAbstractEvent.class, abstractEventFacade.read(Integer.valueOf(id)));
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("edit/{id}")
    public FpEvent edit(@PathParam("id") String id) {
        return eventFacade.read(Integer.valueOf(id));
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("editall/{offset}/{limit}")
    public Collection<FpEvent> edit(@PathParam("offset") int offset, @PathParam("limit") int limit) {
        return eventFacade.readAll(offset, limit);
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("readall/{offset}/{limit}")
    public Collection<FpAbstractEvent> readAll(@PathParam("offset") int offset, @PathParam("limit") int limit) {
        return abstractEventFacade.readAll(offset, limit);
    }

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML })
    @Path("update")
    public JAXBElement<FpAbstractEvent> update(FpAbstractEvent event) throws TransactionRequiredException, IllegalStateException, IllegalArgumentException, InvocationTargetException,
            IllegalAccessException {
        return new JAXBElement<FpAbstractEvent>(FpAbstractEvent.QUALIFIED_NAME, FpAbstractEvent.class, abstractEventFacade.update(event));
    }

    @DELETE
    @Path("delete/{id}")
    public Response delete(@PathParam("id") String id) throws InvocationTargetException, IllegalAccessException {
        eventFacade.delete(Integer.valueOf(id));
        return Response.ok().build();
    }

    @PUT
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("slot/add")
    public FpTimeSlot addSlot(FpTimeSlot slot) {
        throw new WebServiceException("not yet implemented");
    }

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("slot/remove")
    public Response removeSlot(FpTimeSlot slot) {
        return Response.notModified("not yet implemented").build();
    }

    @POST
    @Path("speaker/add/{id}")
    public Response addSpeaker(@QueryParam("slot") List<String> slot, @PathParam("id") String speakerId) {
        return Response.notModified("not yet implemented").build();
    }

    @POST
    @Path("speaker/remove/{id}")
    public Response removeSpeaker(@QueryParam("slot") List<String> slot, @PathParam("id") String speakerId) {
        return Response.notModified("not yet implemented").build();
    }

    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("count")
    public String count() {
        return Long.toString(eventFacade.count());
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("search")
    public Collection<FpAbstractEvent> search(@QueryParam("name") String name, @QueryParam("organization") Integer organization, @PathParam("limit") Integer limit) {
        return abstractEventFacade.findByName(name);
    }

}
