/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cbu
 */
public class BaseResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseResource.class);

    @Context
    protected UriInfo context;

    private final ObjectMapper mapper = new ObjectMapper();

    public BaseResource() {
    }

    @GET
    @Path("ping")
    public Response ping() {
        return ok("Alive");
    }

    protected <T> Response ok(T object) {
        try {
            ResponseWithError<T> response = ResponseWithError.ok(object);
            String entity = mapper.writeValueAsString(response);
            return Response.ok(entity, MediaType.APPLICATION_JSON_TYPE).build();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Conversion to JSON failed", ex);
        }
    }

    @AroundInvoke
    public Object interceptMethods(final InvocationContext ctx) throws Exception {
        try {
            final Object result = interceptMethodsDelegate(ctx);
            return result;
        } catch (final Exception ex) {
            LOGGER.error("Web Operation failed", ex); //$NON-NLS-1$
            ResponseWithError<Object> response = ResponseWithError.error(ex);
            String entity = mapper.writeValueAsString(response);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(entity).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }

    protected Object interceptMethodsDelegate(final InvocationContext ctx) throws Exception {
        return ctx.proceed();
    }

}
