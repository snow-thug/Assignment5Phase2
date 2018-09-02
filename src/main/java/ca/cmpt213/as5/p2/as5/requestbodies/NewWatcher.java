package ca.cmpt213.as5.p2.as5.requestbodies;

public class NewWatcher {
    private int deptId;
    private int courseId;

    public NewWatcher(int deptId, int courseId) {
        this.deptId = deptId;
        this.courseId = courseId;
    }

    public NewWatcher() {

    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
