package src.services;

import src.DBConnection.DBConnection;
import src.dao.AllocationDAO;
import src.models.Allocation;
import src.validators.AllocationValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class AllocationService {

    // -------------------------
    // ALLOCATE (CREATE with checks)
    // -------------------------
    public Allocation allocate(Allocation a) {

        AllocationValidator.validate(a);

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            if (studentHasClash(con, a)) {
                throw new IllegalStateException("Student has another exam in same session");
            }

            if (hallSessionOccupied(con, a)) {
                throw new IllegalStateException("Hall already used in this session");
            }

            if (hallFull(con, a)) {
                throw new IllegalStateException("Hall capacity exceeded");
            }

            AllocationDAO dao = new AllocationDAO();

            Allocation saved = dao.insert(a, con);

            con.commit();

            return saved;

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    // -------------------------
    // STUDENT CLASH CHECK
    // -------------------------
    private boolean studentHasClash(Connection con, Allocation a) throws Exception {

        String sql =
                "SELECT 1 FROM STUDENT_SEAT_ALLOCATION al " +
                "JOIN EXAM e ON al.exam_id = e.exam_id " +
                "JOIN EXAM e2 ON e2.exam_id = ? " +
                "WHERE al.roll_number = ? " +
                "AND e.exam_date = e2.exam_date " +
                "AND e.session = e2.session";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getExamId());
            ps.setString(2, a.getRollNumber());

            ResultSet rs = ps.executeQuery();

            return rs.next();
        }
    }

    // -------------------------
    // HALL SESSION CHECK
    // -------------------------
    private boolean hallSessionOccupied(Connection con, Allocation a) throws Exception {

        String sql =
                "SELECT 1 FROM STUDENT_SEAT_ALLOCATION al " +
                "JOIN EXAM e ON al.exam_id = e.exam_id " +
                "JOIN EXAM e2 ON e2.exam_id = ? " +
                "WHERE al.hall_id = ? " +
                "AND e.exam_date = e2.exam_date " +
                "AND e.session = e2.session";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getExamId());
            ps.setInt(2, a.getHallId());

            ResultSet rs = ps.executeQuery();

            return rs.next();
        }
    }

    // -------------------------
    // CAPACITY CHECK
    // -------------------------
    private boolean hallFull(Connection con, Allocation a) throws Exception {

        String countSql =
                "SELECT COUNT(*) FROM STUDENT_SEAT_ALLOCATION WHERE hall_id=? AND exam_id=?";

        String capacitySql =
                "SELECT seating_capacity FROM HALL WHERE hall_id=?";

        int count = 0;
        int capacity = 0;

        try (PreparedStatement ps = con.prepareStatement(countSql)) {

            ps.setInt(1, a.getHallId());
            ps.setInt(2, a.getExamId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        }

        try (PreparedStatement ps = con.prepareStatement(capacitySql)) {

            ps.setInt(1, a.getHallId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                capacity = rs.getInt(1);
            }
        }

        return count >= capacity;
    }

    // -------------------------
    // READ ALL
    // -------------------------
    public List<Allocation> getAll() {

        try {

            AllocationDAO dao = new AllocationDAO();

            return dao.getAll();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    // -------------------------
    // UPDATE BY STUDENT + EXAM
    // -------------------------
   public boolean updateByStudentExam(Allocation a){

    AllocationValidator.validate(a);

    try{

        AllocationDAO dao = new AllocationDAO();

        return dao.updateByStudentExam(a);

    }catch(Exception e){
        throw new RuntimeException(e);
    }
}


public boolean deleteByStudentExam(String rollNumber,int examId){

    try{

        AllocationDAO dao = new AllocationDAO();

        return dao.deleteByStudentExam(rollNumber,examId);

    }catch(Exception e){
        throw new RuntimeException(e);
    }
}
}