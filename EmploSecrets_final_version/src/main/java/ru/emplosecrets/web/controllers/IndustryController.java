package ru.emplosecrets.web.controllers;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import ru.emplosecrets.web.beans.Pager;
import ru.emplosecrets.web.comparators.ListComparator;
import ru.emplosecrets.web.db.DataHelper;
import ru.emplosecrets.web.entity.Industry;

@ManagedBean
@SessionScoped
public class IndustryController implements Serializable, Converter {

    private List<SelectItem> selectItems = new ArrayList<SelectItem>();
    private Map<Long, Industry> map;
    private List<Industry> list;
    private Pager pager;
    private DataHelper dataHelper;
    @ManagedProperty(value = "#{employerListController}")
    private EmployerListController employerListController;

    @PostConstruct
    public void init() {
        pager = employerListController.getPager();
        dataHelper = employerListController.getDataHelper();

        map = new HashMap<Long, Industry>();
        list = dataHelper.getAllIndustries();
        Collections.sort(list, ListComparator.getInstance());

        list.add(0, createEmptyIndustry());

        for (Industry industry : list) {
            map.put(industry.getIndId(), industry);
            selectItems.add(new SelectItem(industry, industry.getName_ru()));
        }

    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<Industry> getIndustryList() {
        return list;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return map.get(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Industry) value).getIndId().toString();
    }

    public EmployerListController getEmployerListController() {
        return employerListController;
    }

    public void setEmployerListController(EmployerListController employerListController) {
        this.employerListController = employerListController;
    }

    private Industry createEmptyIndustry() {
        Industry industry = new Industry();
        industry.setIndId(-1L);
        industry.setName_ru("");
        industry.setName_en("");
        return industry;
    }

}
