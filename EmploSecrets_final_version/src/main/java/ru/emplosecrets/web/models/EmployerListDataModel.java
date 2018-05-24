package ru.emplosecrets.web.models;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import ru.emplosecrets.web.beans.Pager;
import ru.emplosecrets.web.db.DataHelper;
import ru.emplosecrets.web.entity.Employer;

public class EmployerListDataModel extends LazyDataModel<Employer> {

    private List<Employer> employerList;

    private DataHelper dataHelper;
    private Pager pager;

    public EmployerListDataModel(DataHelper dataHelper, Pager pager) {
        this.dataHelper = dataHelper;
        this.pager = pager;
    }

    @Override
    public Employer getRowData(String rowKey) {

        for (Employer employer : employerList) {
            if (employer.getEmploId().intValue() == Long.valueOf(rowKey).intValue()) {
                return employer;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Employer employer) {
        return employer.getEmploId();
    }

    @Override
    public List<Employer> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        pager.setFrom(first);
        pager.setTo(pageSize);

        dataHelper.populateList();

        this.setRowCount(pager.getTotalEmployersCount());

        return pager.getList();
    }

    @Override
    public List<Employer> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        return super.load(first, pageSize, multiSortMeta, filters); //To change body of generated methods, choose Tools | Templates.
    }

}
