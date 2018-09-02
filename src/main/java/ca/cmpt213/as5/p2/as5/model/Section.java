package ca.cmpt213.as5.p2.as5.model;

public class Section {
    private String location;
    private String type;
    private int enrollmentTotal;
    private int enrollmentCap;

    public Section(String location, String type, int enrollmentTotal, int enrollmentCap) {
        this.location = location;
        this.type = type;
        this.enrollmentTotal = enrollmentTotal;
        this.enrollmentCap = enrollmentCap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void setEnrollmentTotal(int enrollmentTotal) {
        this.enrollmentTotal = enrollmentTotal;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public void setEnrollmentCap(int enrollmentCap) {
        this.enrollmentCap = enrollmentCap;
    }

    public void atToEnrollmentCap(int addingAmount) {
        enrollmentCap = enrollmentCap + addingAmount;
    }

    public void atToEnrollmentTotal(int addingAmount) {
        enrollmentTotal = enrollmentTotal + addingAmount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean equals(Section section) {
        if(type.equalsIgnoreCase(section.getType()) && location.equalsIgnoreCase(section.getLocation())) {
            return true;
        }
        return false;
    }

    public boolean equals(String location, String type) {
        if(this.location.equalsIgnoreCase(location) && this.type.equalsIgnoreCase(type)) {
            return true;
        }
        return false;
    }

}
