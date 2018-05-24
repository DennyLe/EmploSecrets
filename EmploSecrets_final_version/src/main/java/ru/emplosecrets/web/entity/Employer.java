package ru.emplosecrets.web.entity;
// Generated Apr 16, 2018 10:01:01 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Employer generated by hbm2java
 */
public class Employer implements java.io.Serializable {

    private Long emploId;
    private City city;
    private Industry industry;
    private String name;
    private byte[] image;
    private String telephone;
    private String email;
    private String site;
    private String descr;
    private Integer rating;
    private Long voteCount;
    private Set votes = new HashSet(0);
    private Set comments = new HashSet(0);

    private byte[] uploadedImage;

    public Employer() {
        setVoteCount(0L);
        setRating(0);
    }

    public Employer(City city, Industry industry, String name) {
        this.city = city;
        this.industry = industry;
        this.name = name;
    }

    public Employer(City city, Industry industry, String name, byte[] image, String telephone, String email, String site, String descr, Integer rating, Long voteCount, Set votes, Set comments) {
        this.city = city;
        this.industry = industry;
        this.name = name;
        this.image = image;
        this.telephone = telephone;
        this.email = email;
        this.site = site;
        this.descr = descr;
        this.rating = rating;
        this.voteCount = voteCount;
        this.votes = votes;
        this.comments = comments;
    }

    public Long getEmploId() {
        return this.emploId;
    }

    public void setEmploId(Long emploId) {
        this.emploId = emploId;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Industry getIndustry() {
        return this.industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return this.site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDescr() {
        return this.descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getVoteCount() {
        return this.voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Set getVotes() {
        return this.votes;
    }

    public void setVotes(Set votes) {
        this.votes = votes;
    }

    public Set getComments() {
        return this.comments;
    }

    public void setComments(Set comments) {
        this.comments = comments;
    }

    public byte[] getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(byte[] uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

    public String getShortName() {
        if (getName() != null && getName().length() > 20) {
            return new StringBuilder().append(getName().substring(0, 19)).append("...").toString();
        }

        return getName();
    }

    public Employer getEmployer() {
        Employer employer = new Employer();
        employer.setCity(getCity());
        employer.setIndustry(getIndustry());
        employer.setName(getName());
        employer.setImage(getUploadedImage());
        employer.setTelephone(getTelephone());
        employer.setEmail(getEmail());
        employer.setSite(getSite());
        employer.setDescr(getDescr());
        employer.setRating(getRating());
        employer.setVoteCount(getVoteCount());
        employer.setVotes(getVotes());
        employer.setComments(getComments());
        return employer;
    }

}
