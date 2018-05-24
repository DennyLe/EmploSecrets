package ru.emplosecrets.web.controllers;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ImageController implements Serializable {

    private final int IMAGE_MAX_SIZE = 204800;

    @ManagedProperty(value = "#{employerListController}")
    private EmployerListController employerListController;

    public EmployerListController getEmployerListController() {
        return employerListController;
    }

    public void setEmployerListController(EmployerListController employerListController) {
        this.employerListController = employerListController;
    }

    public StreamedContent getDefaultImage() {
        return getStreamedContent(employerListController.getSelectedEmployer().getImage());
    }

    public StreamedContent getUploadedImage() {
        return getStreamedContent(employerListController.getSelectedEmployer().getUploadedImage());
    }

    public void handleFileUpload(FileUploadEvent event) {
        employerListController.getSelectedEmployer().setUploadedImage(event.getFile().getContents());
    }

    private DefaultStreamedContent getStreamedContent(byte[] image) {

        if (image == null) {
            return null;
        }

        InputStream inputStream = null;

        try {
            inputStream = new ByteArrayInputStream(image);
            return new DefaultStreamedContent(inputStream, "image/png");

        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getImageMaxSize() {
        return IMAGE_MAX_SIZE;
    }


}
