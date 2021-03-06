package ru.emplosecrets.web.entity;
// Generated Apr 16, 2018 10:01:01 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * City generated by hbm2java
 */
public class City implements java.io.Serializable {

    private Long cityId;
    private String name_ru;
    private String name_en;
    private Set employers = new HashSet(0);

    public City() {
    }

    public City(String name_ru, String name_en) {
        this.name_ru = name_ru;
        this.name_en = name_en;
    }

    public City(String name_ru, String name_en, Set employers) {
        this.name_ru = name_ru;
        this.name_en = name_en;
        this.employers = employers;
    }

    public Long getCityId() {
        return this.cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getName_ru() {
        return this.name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public Set getEmployers() {
        return this.employers;
    }

    public void setEmployers(Set employers) {
        this.employers = employers;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.cityId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final City other = (City) obj;
        if (!Objects.equals(this.cityId, other.cityId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name_ru;
    }

}
