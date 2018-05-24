package ru.emplosecrets.web.controllers;

/*
 *       Developed by Leshchenko Denis    den-kuzen@mail.ru 
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.primefaces.context.RequestContext;

@RequestScoped
@ManagedBean
public class MailController {

    private ValidationUtils validationUtils = ValidationUtils.getInstance();

    private String message;
    private String subject;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private final String MAIL_SUPPORT_PARAM = "mail.SUPPORT";

//    @Resource(name = "mail/GMAIL")
//    private Session mailSession;
    public void sendMessage() {
        try {

            if (validationUtils.isNullOrEmpty(message) || validationUtils.isNullOrEmpty(subject)) {
                validationUtils.failValidation("required_fields");
                return;
            }

            String mailRecipient = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(MAIL_SUPPORT_PARAM);

            Message msg = new MimeMessage(getSession());

            InternetAddress address = new InternetAddress(mailRecipient);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(getMessage());

            multipart.addBodyPart(bodyPart);

            msg.setSubject(validationUtils.getBundleMessage("message_from") + validationUtils.getUserName() + ": \"" + getSubject() + "\"");
            msg.setRecipient(RecipientType.TO, address);
            msg.setContent(multipart);

            Transport.send(msg);

            hideMailForm();

            validationUtils.showMessage("mail_sended");

        } catch (NamingException ex) {
            Logger.getLogger(MailController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException me) {
            Logger.getLogger(MailController.class.getName()).log(Level.SEVERE, null, me);
        }
    }

    private Session getSession() throws NamingException {
        InitialContext ic = new InitialContext();
        return (javax.mail.Session) ic.lookup("mail/GMAIL");
    }

    public void showMailForm() {
        RequestContext.getCurrentInstance().execute("PF('dlgSendMail').show()");
    }

    public void hideMailForm() {
        RequestContext.getCurrentInstance().execute("PF('dlgSendMail').hide()");
    }
}
