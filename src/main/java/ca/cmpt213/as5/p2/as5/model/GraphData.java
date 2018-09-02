package ca.cmpt213.as5.p2.as5.model;

public class GraphData {
    private int semesterCode;
    private int totalCoursesTaken;

    public GraphData(int semesterCode, int totalCoursesTaken) {
        this.semesterCode = semesterCode;
        this.totalCoursesTaken = totalCoursesTaken;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(int semesterCode) {
        this.semesterCode = semesterCode;
    }

    public int getTotalCoursesTaken() {
        return totalCoursesTaken;
    }

    public void setTotalCoursesTaken(int totalCoursesTaken) {
        this.totalCoursesTaken = totalCoursesTaken;
    }

    public void addToEnrollmentTotal(int addingAmount) {
        totalCoursesTaken = totalCoursesTaken + addingAmount;
    }
}
