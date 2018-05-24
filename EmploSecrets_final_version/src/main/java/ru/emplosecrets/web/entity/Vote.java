package ru.emplosecrets.web.entity;
// Generated Apr 16, 2018 10:01:01 AM by Hibernate Tools 4.3.1


/**
 * Vote generated by hbm2java
 */
public class Vote  implements java.io.Serializable {


     private Long id;
     private Employer employer;
     private Integer value;
     private long userid;

    public Vote() {
    }

	
    public Vote(Employer employer, long userid) {
        this.employer = employer;
        this.userid = userid;
    }
    public Vote(Employer employer, Integer value, long userid) {
       this.employer = employer;
       this.value = value;
       this.userid = userid;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public Employer getEmployer() {
        return this.employer;
    }
    
    public void setEmployer(Employer employer) {
        this.employer = employer;
    }
    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(Integer value) {
        this.value = value;
    }
    public long getUserid() {
        return this.userid;
    }
    
    public void setUserid(long userid) {
        this.userid = userid;
    }




}


