package ca.cmpt213.as5.p2.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class Course {
    @JsonIgnore
    private String deptName;

    @JsonIgnore
    ArrayList<Offering> courseOfferings = new ArrayList<>();

    private int courseId;
    private String catalogNumber;



    public Course(String deptName, String catalogNumber, int courseId) {
        this.courseId = courseId;
        this.deptName = deptName;
        this.catalogNumber = catalogNumber;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public ArrayList<Offering> getCourseOfferings() {
        return courseOfferings;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void addOffering(Offering offering) {
        courseOfferings.add(offering);
    }

    public boolean equals(String deptName, String catalogNumber) {
        if(this.deptName.equalsIgnoreCase(deptName) && this.catalogNumber.equalsIgnoreCase(catalogNumber)) {
            return true;
        }
        return false;
    }
}
