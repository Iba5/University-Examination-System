package src.services;

import java.util.List;
import src.dao.CourseDAO;
import src.models.Courses;
import src.validators.CourseValidator;

public class CourseService {

    private final CourseDAO dao = new CourseDAO();

    // -------------------------
    // CREATE
    // -------------------------
    public Courses addCourse(Courses course) {

        CourseValidator.validate(course);

        try {
            return dao.insert(course);
        } catch (Exception e) {
            throw new RuntimeException("Error adding course", e);
        }
    }

    // -------------------------
    // READ ALL
    // -------------------------
    public List<Courses> getAllCourses() {

        try {
            return dao.getAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching courses", e);
        }
    }

    // -------------------------
    // READ BY COURSE CODE
    // -------------------------
    public Courses getCourseByCode(String courseCode) {

        try {
            return dao.getByCode(courseCode);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching course by code", e);
        }
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public Courses updateCourse(Courses course) {

        CourseValidator.validate(course);

        try {
            return dao.update(course);
        } catch (Exception e) {
            throw new RuntimeException("Error updating course", e);
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    public Courses deleteCourse(String courseCode) {

        try {

            Courses course = dao.getByCode(courseCode);

            if (course == null) {
                throw new IllegalArgumentException("Course not found");
            }

            dao.delete(courseCode);

            return course;

        } catch (Exception e) {
            throw new RuntimeException("Error deleting course", e);
        }
    }
}