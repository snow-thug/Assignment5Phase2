package ca.cmpt213.as5.p2.as5.controllers;

import ca.cmpt213.as5.p2.as5.exceptions.CouldNotCreateWatcherException;
import ca.cmpt213.as5.p2.as5.exceptions.IdNotFoundException;
import ca.cmpt213.as5.p2.as5.model.*;
import ca.cmpt213.as5.p2.as5.requestbodies.NewCommandOffering;
import ca.cmpt213.as5.p2.as5.requestbodies.NewWatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ServerController {
    private ArrayList<Department> departments = new ArrayList<>();
    private ArrayList<Watcher> watchers = new ArrayList<>();
    private final File CSV_DATA = new File("data/course_data_2018.csv");
    private CSVHandler csvHandler = new CSVHandler(CSV_DATA, watchers);
    private AtomicInteger watcherId = new AtomicInteger();


    @GetMapping("/api/about")
    public About getData() {
        About about = new About("Steve's Classy App", "Steve Statia");
        return about;
    }

    @GetMapping("/api/dump-model")
    public void getModelDump() throws IOException {
        departments = csvHandler.dealWithData();
        csvHandler.aggregateDepartmentData();
        csvHandler.dumpDataToServerTerminal(departments);
    }

    @GetMapping("/api/departments")
    public ArrayList<Department> getDepartments() {
        return departments;
    }

    @GetMapping("/api/departments/{deptId}/courses")
    public ArrayList<Course> getAllCoursesFromDeptId(@PathVariable("deptId") int deptId) throws IdNotFoundException {
        for(Department department : departments) {
            if(department.getDeptId() == deptId) {
                return department.getCourses();
            }
        }
       throw new IdNotFoundException("Error. The given department ID does not match and departments listed.");
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings")
    public ArrayList<Offering> getCourseOfferingsFromDeptAndCourseIds(@PathVariable("deptId") int deptId, @PathVariable("courseId") int courseId) throws IdNotFoundException {
        ArrayList<Course> courses = getAllCoursesFromDeptId(deptId);
        if(courses != null) {
            System.out.println("course list from " + deptId);
            for(Course course : courses) {
                if(course.getCourseId() == courseId) {
                    return course.getCourseOfferings();
                }
            }
        }
        throw new IdNotFoundException("Error. The given course ID does not match and courses listed for the given department.");
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings/{courseOfferingId}")
    public ArrayList<Section> getListOfSectionsFromOfferingId(@PathVariable("deptId") int deptId, @PathVariable("courseId") int courseId, @PathVariable("courseOfferingId") int courseOfferingId) throws IdNotFoundException {
       ArrayList<Offering> offerings = getCourseOfferingsFromDeptAndCourseIds(deptId,courseId);
       if(offerings != null) {
           for(Offering offering : offerings) {
               if(offering.getCourseOfferingId() == courseOfferingId) {
                   return offering.getSections();
               }
           }
       }
       throw new IdNotFoundException("Error. The given course offering ID does not match any offerings listen in the given course.");
    }

    @GetMapping("/api/stats/students-per-semester")
    public ArrayList<GraphData> getStudentsPerSemester(@RequestParam(value = "deptId") int deptId) throws IdNotFoundException {
        ArrayList<GraphData> graphData = new ArrayList<>();
        for(Department department : departments) {
            if(department.getDeptId() == deptId) {
                return department.getGraphData();
            }
        }
        throw new IdNotFoundException("Error. Department Id could not be found for the given id.");
    }





    @PostMapping("/api/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public Section addOffering(@RequestBody NewCommandOffering nco) {
        return csvHandler.addNewSection(nco.getSubjectName(), nco.getCatalogNumber(), nco.getLocation(), nco.getInstructor(),
                nco.getSemester(), nco.getComponent(), nco.getEnrollmentTotal(), nco.getEnrollmentCap());

    }


    @GetMapping("/api/watchers")
    public ArrayList<Watcher> getWatchers() {
        return watchers;

    }

    @PostMapping("/api/watchers")
    @ResponseStatus(HttpStatus.CREATED)
    public Watcher createNewWatcher(@RequestBody NewWatcher newWatcher) throws CouldNotCreateWatcherException {
        int deptId = newWatcher.getDeptId();
        int courseId = newWatcher.getCourseId();

        for(Department department : departments) {
            if(department.getDeptId() == deptId) {
                for(Course course : department.getCourses()) {
                    if(course.getCourseId() == courseId) {
                        Watcher watcher = new Watcher(watcherId.incrementAndGet(), deptId, department.getName(), courseId, course.getCatalogNumber());
                        for(Watcher watch : watchers) {
                            if(watch.equals(deptId, courseId)) {
                                throw new CouldNotCreateWatcherException("Watcher with department id or course id already exists");
                            }
                        }
                        watchers.add(watcher);
                        return watcher;
                    }
                }
            }
        }
        throw new CouldNotCreateWatcherException("Error. Could not create watcher.");
    }

    @GetMapping("/api/watchers/{watcherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ArrayList<String> getWatcherFromId(@PathVariable(value = "watcherId") int watcherId) throws IdNotFoundException {
        for(Watcher watcher : watchers) {
            if(watcher.getId() == watcherId) {
                return watcher.getEvents();
            }
        }
        throw new IdNotFoundException("Error. Watcher could not be found with the given Id");
    }

    @DeleteMapping("/api/watchers/{watcherId}")
    public Watcher deleteWatcherFromId(@PathVariable("watcherId") int watcherId) throws IdNotFoundException {
        for(Watcher watcher : watchers) {
            if(watcher.getId() == watcherId) {
                watchers.remove(watcher);
                return watcher;
            }
        }
        throw new IdNotFoundException("Error. The given watcher Id does not match any of the watchers listed.");

    }

    //404 Exception
    @ExceptionHandler(IdNotFoundException.class)
    public void badDepartmentIdExceptionHandler(IdNotFoundException exception, HttpServletResponse message) throws IOException {
        message.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(CouldNotCreateWatcherException.class)
    public void badWatcherCreation(CouldNotCreateWatcherException exception, HttpServletResponse message) throws IOException {
        message.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }


}
