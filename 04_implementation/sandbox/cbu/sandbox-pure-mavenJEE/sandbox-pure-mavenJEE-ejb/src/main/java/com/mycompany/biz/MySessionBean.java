/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.biz;

import com.mycompany.model.Person;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author martin
 */
@Stateless
@LocalBean
public class MySessionBean {

    public Person getPerson() {
        Person p = new Person();
        p.setFirstName("Alistair");
        p.setLastName("Miller");
        return p;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
