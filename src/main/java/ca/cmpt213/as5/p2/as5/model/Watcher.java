package ca.cmpt213.as5.p2.as5.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.util.ArrayList;

public class Watcher {
    private int id;
    ObjectNode department;
    ObjectNode course;
    ArrayList<String> events = new ArrayList<>();



    public Watcher(int id, int deptId, String name, int courseId, String catalogNumber) {
        this.id = id;
        ObjectMapper mapper = new ObjectMapper();
        department = mapper.createObjectNode();
        department.put("deptId", deptId);
        department.put("name", name);

        course = mapper.createObjectNode();
        course.put("courseId", courseId);
        course.put("catalogNumber", catalogNumber);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addNewEvent(String event) {
        events.add(event);
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public ObjectNode getDepartment() {
        return department;
    }

    public void setDepartment(ObjectNode department) {
        this.department = department;
    }

    public ObjectNode getCourse() {
        return course;
    }

    public void setCourse(ObjectNode course) {
        this.course = course;
    }

    public boolean equals(int deptId, int courseId) {
        return (deptId == department.get("deptId").asInt() && courseId == course.get("courseId").asInt());
    }
}
