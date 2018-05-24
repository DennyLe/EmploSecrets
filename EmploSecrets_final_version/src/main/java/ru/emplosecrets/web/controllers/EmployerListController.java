package ru.emplosecrets.web.controllers;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RateEvent;
import org.primefaces.model.LazyDataModel;
import ru.emplosecrets.web.beans.CommentBean;
import ru.emplosecrets.web.beans.Pager;
import ru.emplosecrets.web.db.DataHelper;
import ru.emplosecrets.web.entity.Comment;
import ru.emplosecrets.web.entity.Employer;
import ru.emplosecrets.web.entity.User;
import ru.emplosecrets.web.models.EmployerListDataModel;

@ManagedBean(eager = true)
@SessionScoped
public class EmployerListController implements Serializable {

    private ValidationUtils validationUtils = ValidationUtils.getInstance();

    private Employer selectedEmployer;
    private Employer newEmployer;
    private Comment selectedComment;
    private Comment newComment;
    private transient DataHelper dataHelper;
    private LazyDataModel<Employer> employerListModel;
    private long selectedIndustryId;
    private long selectedCityId;
    private long selectedEmployerId;
    private String currentSearchString;
    private int rating;
    private Pager pager;
    private CommentBean commentBean;
    //-------
    private boolean editMode;// display edit mode
    private boolean addMode;// display add mode
    private boolean commentMode;

    public EmployerListController() {
        pager = new Pager();
        commentBean = new CommentBean();
        dataHelper = new DataHelper(pager, commentBean);
        employerListModel = new EmployerListDataModel(dataHelper, pager);
    }

    public DataHelper getDataHelper() {
        return dataHelper;
    }

    public Pager getPager() {
        return pager;
    }

    public CommentBean getCommentBean() {
        return commentBean;
    }

    private void submitValues(long selectedIndustryId, long selectedCityId) {
        this.selectedIndustryId = selectedIndustryId;
        this.selectedCityId = selectedCityId;
    }

//<editor-fold defaultstate="collapsed" desc="DataBase requests">
    public void fillEmployersAll() {
        dataHelper.getAllEmployers();

        cancelCommentMode();
    }

    public void fillEmployersByIndustry() {

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedIndustryId = Long.valueOf(params.get("industry_id"));
        submitValues(selectedIndustryId, -1);
        dataHelper.getEmployersByIndustry(selectedIndustryId);

        cancelCommentMode();
    }

    public void fillEmployersByCity() {

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedCityId = Long.valueOf(params.get("city_id"));
        submitValues(-1, selectedCityId);
        dataHelper.getEmployersByCity(selectedCityId);

        cancelCommentMode();
    }

    public void fillCommentsByEmployer() {

        switchCommentMode();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedEmployerId = Long.valueOf(params.get("emplo_id"));

        dataHelper.getCommentsByEmployer(selectedEmployerId);

    }

    public void fillEmployersByRate() {
        dataHelper.getEmployersByRate("desc");
        cancelCommentMode();
    }

    public void fillEmployersByAntiRate() {
        dataHelper.getEmployersByRate("asc");
        cancelCommentMode();
    }

    public void fillEmployersBySearch() {

        submitValues(-1, -1);

        if (currentSearchString.trim().length() == 0) {
            fillEmployersAll();
        } else {
            dataHelper.getEmployersByName(currentSearchString);
        }

        cancelCommentMode();
    }

    public void deleteEmployer() {
        dataHelper.deleteEmployer(selectedEmployer);
        dataHelper.populateList();
        validationUtils.showMessage("deleted");
    }

    public void deleteComment() {
        dataHelper.deleteComment(selectedComment);
        dataHelper.getCommentsByEmployer(selectedEmployerId);
        validationUtils.showMessage("comment_deleted");
    }

    public void onrate(RateEvent rateEvent) {
        Integer rattt = (Integer) rateEvent.getRating();
        rating = rattt;
    }

    public void rate(String username) {
        selectedEmployer.setRating(rating);
        dataHelper.rateEmployer(selectedEmployer, username);

    }

    public void saveComment() {

        if (!validateComment()) {
            return;
        }
        newComment.setEmployer(selectedEmployer);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        String username = facesContext.getExternalContext().getUserPrincipal().getName();
        newComment.setUser(new User(dataHelper.getUserIdByUserName(username)));

        newComment.setDate(getDate());

        rate(username);
        dataHelper.addComment(newComment.getComment());
        cancelAddComment();
        dataHelper.getCommentsByEmployer(selectedEmployerId);

        validationUtils.showMessage("Comment_Sended");

    }

    public void saveEmployer() {

        if (!validateFields()) {
            return;
        }

        if (editMode) {
            dataHelper.updateEmployer(selectedEmployer);
        } else if (addMode) {
            dataHelper.addEmployer(selectedEmployer.getEmployer());
        }

        cancelModes();
        dataHelper.populateList();

        validationUtils.showMessage("updated");
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Edit mode utils">
    public void switchEditMode() {
        editMode = true;
        RequestContext.getCurrentInstance().execute("PF('dlgEditEmployer').show()");

    }

    public void switchAddMode() {
        addMode = true;
        selectedEmployer = new Employer();
        RequestContext.getCurrentInstance().execute("PF('dlgEditEmployer').show()");

    }

    public void switchCommentMode() {
        commentMode = true;
    }

    public void cancelCommentMode() {
        commentMode = false;
    }

    public void switchAddComment() {
        newComment = new Comment();

        RequestContext.getCurrentInstance().execute("PF('dlgAddComment').show()");
    }

    public void cancelAddComment() {
        RequestContext.getCurrentInstance().execute("PF('dlgAddComment').hide()");
    }

    public void cancelModes() {
        if (addMode) {
            addMode = false;
        }

        if (editMode) {
            editMode = false;
        }

        if (selectedEmployer != null) {
            selectedEmployer.setUploadedImage(null);
        }

        RequestContext.getCurrentInstance().execute("PF('dlgEditEmployer').hide()");
    }
//</editor-fold>

    public void searchStringChanged(ValueChangeEvent e) {
        currentSearchString = e.getNewValue().toString();
    }

    public void showAboutProj() {
        RequestContext.getCurrentInstance().execute("PF('dlgAboutProj').show()");
    }

    public void showContacts() {
        RequestContext.getCurrentInstance().execute("PF('dlgContacts').show()");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    private Date getDate() {
        Date date = new Date();
        try {
            SimpleDateFormat DateNow = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
            String dateInString = DateNow.format(date);
            date = DateNow.parse(dateInString);

        } catch (ParseException ex) {
            Logger.getLogger(EmployerListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public boolean isAddMode() {
        return addMode;
    }

    public boolean isCommentMode() {
        return commentMode;
    }

    public String getSearchString() {
        return currentSearchString;
    }

    public void setSearchString(String searchString) {
        this.currentSearchString = searchString;
    }

    public LazyDataModel<Employer> getEmployerListModel() {
        return employerListModel;
    }

    public long getSelectedIndustryId() {
        return selectedIndustryId;
    }

    public void setSelectedIndustryId(int selectedIndustryId) {
        this.selectedIndustryId = selectedIndustryId;
    }

    public long getSelectedCityId() {
        return selectedCityId;
    }

    public void setSelectedCityId(int selectedCityId) {
        this.selectedCityId = selectedCityId;
    }

    public long getSelectedEmployerId() {
        return selectedEmployerId;
    }

    public void setSelectedEmployerId(int selectedEmployerId) {
        this.selectedEmployerId = selectedEmployerId;
    }

    public void setSelectedEmployer(Employer selectedEmployer) {
        this.selectedEmployer = selectedEmployer;
    }

    public Employer getSelectedEmployer() {
        return selectedEmployer;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment = selectedComment;
    }

    public Comment getSelectedComment() {
        return selectedComment;
    }

    public Employer getNewEmployer() {
        if (newEmployer == null) {
            newEmployer = new Employer();
        }
        return newEmployer;
    }

    public void setNewEmployer(Employer newEmployer) {
        this.newEmployer = newEmployer;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

//</editor-fold>
    private boolean validateFields() {

        if (validationUtils.isNullOrEmpty(selectedEmployer.getCity())
                || validationUtils.isNullOrEmpty(selectedEmployer.getDescr())
                || validationUtils.isNullOrEmpty(selectedEmployer.getIndustry())
                || validationUtils.isNullOrEmpty(selectedEmployer.getEmail())
                || validationUtils.isNullOrEmpty(selectedEmployer.getName())
                || validationUtils.isNullOrEmpty(selectedEmployer.getTelephone())
                || validationUtils.isNullOrEmpty(selectedEmployer.getSite())) {
            validationUtils.failValidation("error_fill_all_fields");
            return false;

        }

        if (dataHelper.isNameExist(selectedEmployer.getName(), selectedEmployer.getEmploId())) {
            validationUtils.failValidation("error_name_exist");
            return false;
        }

        if (addMode) {

            if (selectedEmployer.getUploadedImage() == null) {
                validationUtils.failValidation("error_load_image");
                return false;
            }

        }

        return true;

    }

    private boolean validateComment() {
        if (validationUtils.isNullOrEmpty(newComment.getText())) {
            validationUtils.failValidation("error_comment_text");
            return false;

        }
        return true;
    }

}
