/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.transport;

import com.mycompany.biz.MySessionBean;
import com.mycompany.model.Person;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author cbu
 */
@Path("/foo")
@Stateless
public class SomeResource {
    
    @EJB
    private MySessionBean bizBean; 
    
    @GET
    @Path("bar")
    public String getSomething() {
        Person p = bizBean.getPerson();
        return p.getFirstName();
    }
}
