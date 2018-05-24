package ru.emplosecrets.web.beans;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.util.List;
import javax.faces.bean.SessionScoped;
import ru.emplosecrets.web.entity.Employer;

@SessionScoped
public class Pager {

    private static Pager pager;

    public Pager() {
    }

    private int totalEmployersCount;
    private Employer selectedEmployer;
    private List<Employer> list;
    private int from;
    private int to;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<Employer> getList() {
        return list;
    }

    public void setList(List<Employer> list) {
        this.list = list;
    }

    public void setTotalEmployersCount(int totalEmployersCount) {
        this.totalEmployersCount = totalEmployersCount;
    }

    public int getTotalEmployersCount() {
        return totalEmployersCount;
    }

    public Employer getSelectedEmployer() {
        return selectedEmployer;
    }

    public void setSelectedEmployer(Employer selectedEmployer) {
        this.selectedEmployer = selectedEmployer;
    }

}
