/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.transport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author cbu
 */
public class BaseResource {

    @Context
    protected UriInfo context;

    public BaseResource() {
    }
    
    @GET
    @Path("ping")
    public String ping() {
        return "Alive";
    }
    
}
