package ca.cmpt213.as5.p2.as5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class Department {
    private int deptId;
    private String name;

    @JsonIgnore
    private ArrayList<Course> courses = new ArrayList<>();
    @JsonIgnore
    private ArrayList<GraphData> graphData = new ArrayList<>();



    public Department(String name, int deptId) {
        this.name = name;
        this.deptId = deptId;
     }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addGraphData(GraphData graphData) {
        this.graphData.add(graphData);
    }

    @Override
    public boolean equals(Object object) {
        if(object == null) {
            return false;
        }

        if(object.getClass() != this.getClass()) {
            return false;
        }

        Department department = (Department) object;
        if(this.name.equalsIgnoreCase(department.getName())) {
            return true;
        }

      return false;
    }

    public boolean equals(String name) {
        if(this.name.equalsIgnoreCase(name)) {
            return true;
        }
        return false;
    }

    public boolean equals(String name, int deptId) {
        if(this.name.equalsIgnoreCase(name) && this.deptId == deptId) {
            return true;
        }
        return false;
    }


    public ArrayList<GraphData> getGraphData() {
        return graphData;
    }

    public void setGraphData(ArrayList<GraphData> graphData) {
        this.graphData = graphData;
    }
}
