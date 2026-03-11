package src.services;

import src.dao.StudentDAO;
import src.models.Students;
import src.validators.StudentValidator;

import java.util.List;

public class StudentService {

    private final StudentDAO dao = new StudentDAO();

    // -------------------------
    // CREATE
    // -------------------------
    public Students add(Students student) {

        if(student == null){
            throw new IllegalArgumentException(
                    "Student information cannot be empty."
            );
        }

        StudentValidator.validate(student);

        try{

            Students existing = dao.getByRollNumber(student.getRollNumber());

            if(existing != null){
                throw new IllegalArgumentException(
                        "A student with this roll number already exists."
                );
            }

            return dao.insert(student);

        }catch(IllegalArgumentException e){

            throw e;

        }catch(Exception e){

            throw new RuntimeException(
                    "Unable to create the student record."
            );
        }
    }

    // -------------------------
    // READ ALL
    // -------------------------
    public List<Students> getAllStudents(){

        try{

            return dao.getAll();

        }catch(Exception e){

            throw new RuntimeException(
                    "Unable to retrieve the list of students."
            );
        }
    }

    // -------------------------
    // READ BY ROLL NUMBER
    // -------------------------
    public Students getByRollNumber(String rollNumber){

        try{

            Students student = dao.getByRollNumber(rollNumber);

            if(student == null){
                throw new IllegalArgumentException(
                        "The requested student could not be found."
                );
            }

            return student;

        }catch(IllegalArgumentException e){

            throw e;

        }catch(Exception e){

            throw new RuntimeException(
                    "Unable to retrieve the student record."
            );
        }
    }

    // -------------------------
    // READ BY DEPARTMENT
    // -------------------------
    public List<Students> getByDepartment(String department){

        try{

            return dao.getByDepartment(department);

        }catch(Exception e){

            throw new RuntimeException(
                    "Unable to retrieve students for the selected department."
            );
        }
    }

    // -------------------------
    // READ BY SEMESTER
    // -------------------------
    public List<Students> getBySemester(int semester){

        try{

            return dao.getBySemester(semester);

        }catch(Exception e){

            throw new RuntimeException(
                    "Unable to retrieve students for the selected semester."
            );
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    public Students delete(String rollNumber){

        try{

            Students student = dao.getByRollNumber(rollNumber);

            if(student == null){
                throw new IllegalArgumentException(
                        "The selected student does not exist."
                );
            }

            dao.deleteByRollNumber(rollNumber);

            return student;

        }catch(IllegalArgumentException e){

            throw e;

        }catch(Exception e){

            throw new RuntimeException(
                    "Unable to delete the student record."
            );
        }
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public Students update(Students student){

        if(student == null){
            throw new IllegalArgumentException(
                    "Student information cannot be empty."
            );
        }

        StudentValidator.validate(student);

        try{

            Students existing = dao.getByRollNumber(student.getRollNumber());

            if(existing == null){
                throw new IllegalArgumentException(
                        "The selected student does not exist."
                );
            }

            return dao.update(student);

        }catch(IllegalArgumentException e){

            throw e;

        }catch(Exception e){

            throw new RuntimeException(
                    "Unable to update the student record."
            );
        }
    }
}