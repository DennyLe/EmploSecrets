package ru.emplosecrets.web.controllers;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ValidationUtils {

    public static ValidationUtils validationObjects;

    public static ValidationUtils getInstance() {
        if (validationObjects == null) {
            validationObjects = new ValidationUtils();
        }

        return validationObjects;
    }

    private ValidationUtils() {
    }
    
    
    
    private ResourceBundle bundle = ResourceBundle.getBundle("ru.emplosecrets.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());

    public boolean isNullOrEmpty(Object obj) {
        if (obj == null || obj.toString().equals("")) {
            return true;
        }

        return false;
    }

    public void failValidation(String message_key) {
        FacesContext.getCurrentInstance().validationFailed();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString(message_key), bundle.getString("error")));
    }

    public String getBundleMessage(String message_key) {
        return bundle.getString(message_key);
    }

    public void showMessage(String message_key) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString(message_key)));

    }

    public String getUserName() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getUserPrincipal().getName();
    }
}
