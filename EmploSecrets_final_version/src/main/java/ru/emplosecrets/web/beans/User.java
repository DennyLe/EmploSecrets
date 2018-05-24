package ru.emplosecrets.web.beans;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class User implements Serializable {

    private String username;
    private String password;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String login() {

        try {
            Thread.sleep(500);//2000
        } catch (Exception e) {
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        try {

            if (request.getUserPrincipal() == null || (request.getUserPrincipal() != null && !request.getUserPrincipal().getName().equals(username))) {
                request.logout();
                request.login(username, password);
            }

            return "/pages/employers.xhtml?faces-redirect=true";
        } catch (ServletException ex) {

            ResourceBundle bundle = ResourceBundle.getBundle("ru.emplosecrets.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(bundle.getString("login_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("formLogin", message);

        }

        return "index";

    }

    public String logout() {
        String result = "/index.xhtml?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
        } catch (ServletException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return result;
    }

    public String goHome() {
        return "/index.xhtml?faces-redirect=true";
    }

    public String goEmployers() {
        return "/pages/employers.xhtml?faces-redirect=true";
    }
}
