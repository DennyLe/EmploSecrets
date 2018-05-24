package ru.emplosecrets.web.beans;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(eager = true)
@SessionScoped
public class LocaleChanger implements Serializable {

    private Locale currentLocale = new Locale("ru");

    public LocaleChanger() {
    }

    public void changeLocale(String localeCode) {
        currentLocale = new Locale(localeCode);
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

}
