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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.cejug.footprint.jpa.entity.FpOrganization;
import org.cejug.footprint.jpa.entity.facade.FpOrganizationFacade;

/**
 * 
 * @author Felipe Gaucho
 * 
 */
@Path("organization")
public class OrganizationResource {
    @EJB
    private FpOrganizationFacade organizationFacade;

    private static final Logger LOGGER = Logger.getLogger(OrganizationResource.class.getName());

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("readall/{offset}/{limit}")
    public Collection<FpOrganization> readAll(@PathParam("offset") int offset, @PathParam("limit") int limit) {
        try {
            return organizationFacade.readAll(offset, limit);
        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }
}
