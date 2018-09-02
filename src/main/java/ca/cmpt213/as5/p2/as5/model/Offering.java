package ca.cmpt213.as5.p2.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Offering {
    private int courseOfferingId;
    private ObjectNode course;
    private String location;
    private String instructors;
    private String term;
    private int semesterCode;
    private int year;

    @JsonIgnore
    private ArrayList<Section> sections = new ArrayList<>();

    public Offering(String location, String instructors, int semesterCode, int courseOfferingId, int courseId, String catalogNumber) {
        this.courseOfferingId = courseOfferingId;
        this.location = location;
        this.instructors = instructors;
        this.semesterCode = semesterCode;
        this.year = convertSemesterCodeToYear(semesterCode);
        this.term = convertSemesterCodeToTerm(semesterCode);
        ObjectMapper mapper = new ObjectMapper();
        course = mapper.createObjectNode();
        this.course.put("courseId", courseId);
        this.course.put("catalogNumber", catalogNumber);
    }

    private String convertSemesterCodeToTerm(int semesterCode) {
        String semCodeString = Integer.toString(semesterCode);
        char[] semCodeChars = semCodeString.toCharArray();
        char termCode = semCodeChars[semCodeChars.length-1];
        switch(termCode) {
            case '1':
                return "Fall";
            case '4':
                return "Spring";
            case '7':
                return "Summer";

        }
        return "";
    }

    private int convertSemesterCodeToYear(int semesterCode) {
        int year = -1;
        String semCodeString = Integer.toString(semesterCode);
        char[] semCodeChars = semCodeString.toCharArray();
        String yearString = "20";
        yearString = yearString + semCodeChars[1] + semCodeChars[2];
        year = Integer.parseInt(yearString);
        return year;
    }


    public long getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(int courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInstructors() {
        return instructors;
    }

    public void setInstructors(String instructors) {
        this.instructors = instructors;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(int semesterCode) {
        this.semesterCode = semesterCode;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public ObjectNode getCourse() {
        return course;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public boolean equals(String location, int semesterCode) {
        if(this.location.equalsIgnoreCase(location) && this.semesterCode == semesterCode) {
            return true;
        }
        return false;
    }


}
