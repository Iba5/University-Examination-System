package src.services;

import src.dao.ExamDAO;
import src.models.Exam;
import src.validators.ExamValidator;

import java.time.LocalDate;
import java.util.List;

public class ExamService {

    private final ExamDAO dao = new ExamDAO();

    // -------------------------
    // CREATE (batch)
    // -------------------------
    public String add(List<Exam> exams) {

        if (exams == null || exams.isEmpty()) {
            throw new IllegalArgumentException(
                    "No exams were provided for creation."
            );
        }

        try {

            for (Exam exam : exams) {

                if (exam == null) {
                    throw new IllegalArgumentException(
                            "Exam data cannot be empty."
                    );
                }

                ExamValidator.validate(exam);

                Exam existing = dao.getById(exam.getExamId());

                if (existing != null) {
                    throw new IllegalArgumentException(
                            "An exam with ID " + exam.getExamId() +
                            " already exists."
                    );
                }

                dao.insert(exam);
            }

            return "Exams created successfully.";

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to add exams. Please try again."
            );
        }
    }

    // -------------------------
    // CREATE (single)
    // -------------------------
    public Exam addSingle(Exam exam) {

        if (exam == null) {
            throw new IllegalArgumentException(
                    "Exam information cannot be empty."
            );
        }

        ExamValidator.validate(exam);

        try {

            Exam existing = dao.getById(exam.getExamId());

            if (existing != null) {
                throw new IllegalArgumentException(
                        "An exam with this ID already exists."
                );
            }

            return dao.insert(exam);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to create the exam. Please try again."
            );
        }
    }

    // -------------------------
    // READ ALL
    // -------------------------
    public List<Exam> getAll() {

        try {

            return dao.getAll();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve exam records."
            );
        }
    }

    // -------------------------
    // READ BY ID
    // -------------------------
    public Exam getById(int examId) {

        try {

            Exam exam = dao.getById(examId);

            if (exam == null) {
                throw new IllegalArgumentException(
                        "Exam with ID " + examId + " was not found."
                );
            }

            return exam;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve the exam information."
            );
        }
    }

    // -------------------------
    // READ BY COURSE CODE
    // -------------------------
    public List<Exam> getByCourseCode(String courseCode) {

        try {

            return dao.getByCourseCode(courseCode);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve exams for the selected course."
            );
        }
    }

    // -------------------------
    // READ BY DATE AND SESSION
    // -------------------------
    public List<Exam> getByDateAndSession(LocalDate date, String session) {

        try {

            return dao.getByDateAndSession(date, session);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to retrieve exams for the selected schedule."
            );
        }
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public Exam update(Exam exam) {

        if (exam == null) {
            throw new IllegalArgumentException(
                    "Exam data cannot be empty."
            );
        }

        ExamValidator.validate(exam);

        try {

            Exam existing = dao.getById(exam.getExamId());

            if (existing == null) {
                throw new IllegalArgumentException(
                        "The selected exam does not exist."
                );
            }

            return dao.update(exam);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to update the exam."
            );
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    public Exam deleteById(int examId) {

        try {

            Exam exam = dao.getById(examId);

            if (exam == null) {
                throw new IllegalArgumentException(
                        "The selected exam could not be found."
                );
            }

            dao.delete(examId);

            return exam;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to delete the exam."
            );
        }
    }
}