package src.dao;

import src.DBConnection.DBConnection;
import src.models.Allocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AllocationDAO {

    // -------------------------
    // INSERT
    // -------------------------
    public Allocation insert(Allocation allocation) throws Exception {

        String sql = "INSERT INTO STUDENT_SEAT_ALLOCATION (roll_number, exam_id, hall_id) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, allocation.getRollNumber());
            ps.setInt(2, allocation.getExamId());
            ps.setInt(3, allocation.getHallId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                allocation.setAllocationId(rs.getInt(1));
            }
        }

        return allocation;
    }

    // -------------------------
    // INSERT with external connection (for transactions)
    // -------------------------
    public Allocation insert(Allocation allocation, Connection con) throws Exception {

        String sql = "INSERT INTO STUDENT_SEAT_ALLOCATION (roll_number, exam_id, hall_id) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, allocation.getRollNumber());
            ps.setInt(2, allocation.getExamId());
            ps.setInt(3, allocation.getHallId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                allocation.setAllocationId(rs.getInt(1));
            }
        }

        return allocation;
    }

    // -------------------------
    // DELETE
   // -------------------------
// DELETE BY STUDENT + EXAM
// -------------------------
public boolean deleteByStudentExam(String rollNumber,int examId) throws Exception{

    String sql = "DELETE FROM STUDENT_SEAT_ALLOCATION WHERE roll_number=? AND exam_id=?";

    try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){

        ps.setString(1,rollNumber);
        ps.setInt(2,examId);

        return ps.executeUpdate()>0;
    }
}
    // -------------------------
    // UPDATE
    // -------------------------
   // -------------------------
// UPDATE BY STUDENT + EXAM
// -------------------------
public boolean updateByStudentExam(Allocation allocation) throws Exception {

    String sql = "UPDATE STUDENT_SEAT_ALLOCATION SET hall_id=? WHERE roll_number=? AND exam_id=?";

    try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){

        ps.setInt(1, allocation.getHallId());
        ps.setString(2, allocation.getRollNumber());
        ps.setInt(3, allocation.getExamId());

        return ps.executeUpdate() > 0;
    }
}

    // -------------------------
    // SELECT BY ID
    // -------------------------
    public Allocation getById(int allocationId) throws Exception {

        String sql = "SELECT * FROM STUDENT_SEAT_ALLOCATION WHERE allocation_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, allocationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        }

        return null;
    }

    // -------------------------
    // SELECT ALL
    // -------------------------
    public List<Allocation> getAll() throws Exception {

        String sql = "SELECT * FROM STUDENT_SEAT_ALLOCATION";

        List<Allocation> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        }

        return list;
    }

    // -------------------------
    // GET BY EXAM ID
    // -------------------------
    public List<Allocation> getByExamId(int examId) throws Exception {

        String sql = "SELECT * FROM STUDENT_SEAT_ALLOCATION WHERE exam_id = ?";

        List<Allocation> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, examId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        }

        return list;
    }

    // -------------------------
    // GET BY ROLL NUMBER
    // -------------------------
    public List<Allocation> getByRollNumber(String rollNumber) throws Exception {

        String sql = "SELECT * FROM STUDENT_SEAT_ALLOCATION WHERE roll_number = ?";

        List<Allocation> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNumber);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        }

        return list;
    }

    // -------------------------
    // GET BY HALL ID
    // -------------------------
    public List<Allocation> getByHallId(int hallId) throws Exception {

        String sql = "SELECT * FROM STUDENT_SEAT_ALLOCATION WHERE hall_id = ?";

        List<Allocation> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hallId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        }

        return list;
    }

    // -------------------------
    // HELPER: Map ResultSet to Object
    // -------------------------
    private Allocation mapResultSet(ResultSet rs) throws Exception {

        return new Allocation(
                rs.getInt("allocation_id"),
                rs.getString("roll_number"),
                rs.getInt("exam_id"),
                rs.getInt("hall_id")
        );
    }
}
