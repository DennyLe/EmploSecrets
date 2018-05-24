package ru.emplosecrets.web.controllers;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean
@RequestScoped
public class IndexController {
    
    private int employerIndex = -1;

    public int getEmployerIndex() {
        return employerIndex;
    }
    
    public int getIncrementemployerIndex() {
        return ++employerIndex;
    }
    
    
    

}
