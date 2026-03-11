package src.models;

import java.time.LocalDate;
import java.util.Objects;

public class Exam {

    private int examId;
    private String courseCode;
    private LocalDate examDate;
    private String session; // FN or AN

    public Exam() {}

    public Exam(int examId, String courseCode, LocalDate examDate, String session) {
        this.examId = examId;
        this.courseCode = courseCode;
        this.examDate = examDate;
        this.session = session;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return examId == exam.examId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(examId);
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examId=" + examId +
                ", courseCode='" + courseCode + '\'' +
                ", examDate=" + examDate +
                ", session='" + session + '\'' +
                '}';
    }
}