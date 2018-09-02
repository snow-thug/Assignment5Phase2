package ca.cmpt213.as5.p2.as5.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVHandler {
    private File csvFile;
    private ArrayList<Department> departments = new ArrayList<>();
    private AtomicInteger deptId = new AtomicInteger();
    private AtomicInteger courseId = new AtomicInteger();
    private AtomicInteger offeringId = new AtomicInteger();
    private ArrayList<Watcher> watchers;

    public CSVHandler(File file, ArrayList<Watcher> watchers) {
        this.csvFile = file;
        this.watchers = watchers;
    }

    public ArrayList<Department> dealWithData() throws IOException {
        BufferedReader csvLine = new BufferedReader(new FileReader(csvFile));
        csvLine.readLine(); //skip first line
        String currentLine;
        while((currentLine = csvLine.readLine()) != null) {
            String[] lineData = currentLine.split(",");

            int semesterCode = Integer.parseInt(lineData[0]);;
            String subjectName = lineData[1].trim();
            String catalogNumber = lineData[2].trim();
            String location = lineData[3].trim();
            int enrollmentCapacity = Integer.parseInt(lineData[4]);
            int enrollmentTotal = Integer.parseInt(lineData[5]);
            String instructors = "";
            String componentCode = "";

            if(lineData.length == 8) {
                instructors = instructors + lineData[6].replaceAll("(.*)(null)(.*)","");
                componentCode = lineData[7].trim();

            } else if(lineData.length > 8) {
                componentCode = lineData[lineData.length-1].trim();
                instructors = lineData[6].trim();
                for(int x = 7; x < lineData.length-1; x++) {
                    instructors = instructors + ", " + lineData[x].trim();
                }
                instructors = instructors.replaceAll("[\"]", "");
            } else {
                System.out.println("Error. Problem with file");
            }

            addNewSection(subjectName, catalogNumber, location, instructors, semesterCode, componentCode, enrollmentTotal, enrollmentCapacity);


        }
        sortArrayList(departments);
        return departments;
    }

    public Section addNewSection(String subjectName, String catalogNumber, String location, String instructors,
                               int semesterCode, String componentCode, int enrollmentTotal, int enrollmentCapacity) {

        if(departments.size() == 0) {
            Department department = new Department(subjectName, deptId.incrementAndGet());
            Course course = new Course(subjectName, catalogNumber, courseId.incrementAndGet());
            Offering offering = new Offering(location, instructors, semesterCode, courseId.incrementAndGet(), course.getCourseId(), course.getCatalogNumber());

            Section newSection = new Section(location, componentCode, enrollmentTotal, enrollmentCapacity);

            if(watchers != null) {
                addWatcherEvent(department.getDeptId(), course.getCourseId(), newSection);
            }

            offering.addSection(newSection);
            course.addOffering(offering);
            department.addCourse(course);
            departments.add(department);
            return newSection;

        } else {
            Department department = getDepartment(subjectName, departments);
            Course course = getCourse(subjectName, catalogNumber, department);
            Offering offering = getOffering(location, instructors, semesterCode, course);

            Section watcherSec = new Section(location, componentCode, enrollmentTotal, enrollmentCapacity);
            if(watchers != null) {
                addWatcherEvent(department.getDeptId(), course.getCourseId(), watcherSec);
            }
            Section section = getSection(location, componentCode, enrollmentTotal, enrollmentCapacity, offering);

            return section;
        }
    }

    private void addWatcherEvent(int deptId, int courseId, Section section) {
        for(Watcher watcher : watchers) {
            if(watcher.equals(deptId, courseId)) {
                Calendar calender = Calendar.getInstance();
                String dateString = calender.getTime().toString();

                String infoString = " Added section " + section.getType() + "with enrollment (" +
                        section.getEnrollmentTotal() + "/" + section.getEnrollmentCap() + ")\n" +
                        "to offering Spring 2019";

                String event = dateString + infoString;
                watcher.addNewEvent(event);
            }
        }
    }

    public void aggregateDepartmentData() {
        if(departments.size() > 0) {
            for(Department department : departments) {
                for(Course course : department.getCourses()) {
                    for(Offering offering : course.getCourseOfferings()) {
                        for(Section section : offering.getSections()) {
                            lookUpGraphData(offering.getSemesterCode(), department, section.getEnrollmentTotal());
                        }

                    }
                }

                Collections.sort(department.getGraphData(), new Comparator<GraphData>() {
                    @Override
                    public int compare(GraphData o1, GraphData o2) {
                        return o1.getSemesterCode() - o2.getSemesterCode();
                    }
                });
            }
        }

    }

    private GraphData lookUpGraphData(int semesterCode, Department department, int enrollmentTotal) {
        for(GraphData gd : department.getGraphData()) {
            if(gd.getSemesterCode() == semesterCode) {
                gd.addToEnrollmentTotal(enrollmentTotal);
                return  gd;
            }
        }
        GraphData gd = new GraphData(semesterCode, enrollmentTotal);
        department.addGraphData(gd);
        return gd;
    }


    private Department getDepartment(String subjectNumber, ArrayList<Department> departments) {
        for(Department department : departments) {
            if(department.equals(subjectNumber)) {
                return department;
            }
        }
        Department department = new Department(subjectNumber, deptId.incrementAndGet());
        departments.add(department);
        return department;
    }

    private Course getCourse(String subjectNumber, String catalogNumber, Department department) {
        if(department.getCourses().size() > 0) {
            for(Course course : department.getCourses()) {
                if(course.equals(subjectNumber, catalogNumber)) {
                    return course;
                }
            }
        }

        Course course = new Course(subjectNumber, catalogNumber, courseId.incrementAndGet());
        department.addCourse(course);
        return course;
    }

    private Offering getOffering(String location, String instructors, int semesterCode, Course course) {
        if(course.getCourseOfferings().size() > 0) {
            for(Offering offering : course.getCourseOfferings()) {
                if(offering.equals(location, semesterCode)) {
                    return offering;
                }
            }
        }

        Offering offering = new Offering(location, instructors, semesterCode, offeringId.incrementAndGet(),course.getCourseId(), course.getCatalogNumber());
        course.addOffering(offering);
        return offering;
    }

    private Section getSection(String location, String type, int enrollmentTotal, int enrollmentCapacity, Offering offering) {
        if(offering.getSections().size() > 0) {
            for(Section section : offering.getSections()) {
                if(section.equals(location, type)) {
                    section.atToEnrollmentTotal(enrollmentTotal);
                    section.atToEnrollmentCap(enrollmentCapacity);
                    return section;
                }
            }
        }

        Section section = new Section(location, type, enrollmentTotal, enrollmentCapacity);
        offering.addSection(section);
        return section;
    }

    public void dumpDataToServerTerminal(ArrayList<Department> departments) {
        System.out.println("Model Dump from '" + csvFile.getName() + "' file\n");
        sortArrayList(departments);
        for(Department department : departments) {
            for(Course course : department.getCourses()) {
                System.out.println(department.getName() + " " + course.getCatalogNumber());
                for(Offering offering : course.getCourseOfferings()) {
                    System.out.println("\t" + offering.getSemesterCode() + " in " + offering.getLocation() + " by " + offering.getInstructors());
                    for(Section section : offering.getSections()) {
                        System.out.println("\t\tType=" + section.getType() + ", Enrollment=" + section.getEnrollmentTotal() + "/" + section.getEnrollmentCap());
                    }
                }
            }
        }
    }

    public void sortArrayList(ArrayList<Department> departments) {
        Collections.sort(departments, new Comparator<Department>() {
            @Override
            public int compare(Department o1, Department o2) {
                return  o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        for(Department department : departments) {
            ArrayList<Course> courses = department.getCourses();
            Collections.sort(courses, new Comparator<Course>() {
                @Override
                public int compare(Course o1, Course o2) {
                    return o1.getCatalogNumber().compareToIgnoreCase(o2.getCatalogNumber());
                }
            });

            for(Course course : courses) {
                ArrayList<Offering> offerings = course.getCourseOfferings();
                Collections.sort(offerings, new Comparator<Offering>() {
                    @Override
                    public int compare(Offering o1, Offering o2) {
                        return o1.getSemesterCode() - o2.getSemesterCode();
                    }
                });

                for(Offering offering : offerings) {
                    ArrayList<Section> sections = offering.getSections();
                    Collections.sort(sections, new Comparator<Section>() {
                        @Override
                        public int compare(Section o1, Section o2) {
                            return o1.getType().compareToIgnoreCase(o2.getType());
                        }
                    });
                }
            }

        }


    }

}
