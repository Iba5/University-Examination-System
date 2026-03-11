package src.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/exam_hall_allocation";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static boolean initialized = false;

    private DBConnection() {}

    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (!initialized) {
                createTablesIfNotExist(connection);
                createTriggersIfNotExist(connection);
                initialized = true;
            }

            return connection;

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Database connection failed", e);
        }
    }

    private static void createTablesIfNotExist(Connection connection) {

        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS COURSE (
                    course_code VARCHAR(50) PRIMARY KEY,
                    course_name VARCHAR(100) NOT NULL,
                    credits INT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS STUDENT (
                    roll_number VARCHAR(50) PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    department VARCHAR(100) NOT NULL,
                    semester INT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS COURSE_OFFERING (
                    offering_id INT AUTO_INCREMENT PRIMARY KEY,
                    course_code VARCHAR(50) NOT NULL,
                    department VARCHAR(100) NOT NULL,
                    semester INT NOT NULL,
                    FOREIGN KEY (course_code)
                        REFERENCES COURSE(course_code)
                        ON DELETE CASCADE
                )
            """);

            try {
                stmt.executeUpdate("""
                    CREATE INDEX idx_course_offering_dept_sem
                    ON COURSE_OFFERING(department, semester)
                """);
            } catch (SQLException ignored) {}

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS STUDENT_COURSE (
                    student_course_id INT AUTO_INCREMENT PRIMARY KEY,
                    roll_number VARCHAR(50) NOT NULL,
                    course_code VARCHAR(50) NOT NULL,
                    UNIQUE (roll_number, course_code),
                    FOREIGN KEY (roll_number)
                        REFERENCES STUDENT(roll_number)
                        ON DELETE CASCADE,
                    FOREIGN KEY (course_code)
                        REFERENCES COURSE(course_code)
                        ON DELETE CASCADE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS HALL (
                    hall_id INT AUTO_INCREMENT PRIMARY KEY,
                    hall_name VARCHAR(100) UNIQUE NOT NULL,
                    seating_capacity INT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS INVIGILATOR (
                    invigilator_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    department VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS EXAM (
                    exam_id INT AUTO_INCREMENT PRIMARY KEY,
                    course_code VARCHAR(50) NOT NULL,
                    exam_date DATE NOT NULL,
                    session ENUM('FN','AN') NOT NULL,
                    UNIQUE (course_code, exam_date, session),
                    FOREIGN KEY (course_code)
                        REFERENCES COURSE(course_code)
                        ON DELETE CASCADE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS STUDENT_SEAT_ALLOCATION (
                    allocation_id INT AUTO_INCREMENT PRIMARY KEY,
                    roll_number VARCHAR(50) NOT NULL,
                    exam_id INT NOT NULL,
                    hall_id INT NOT NULL,
                    UNIQUE (roll_number, exam_id),
                    FOREIGN KEY (roll_number)
                        REFERENCES STUDENT(roll_number)
                        ON DELETE CASCADE,
                    FOREIGN KEY (exam_id)
                        REFERENCES EXAM(exam_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (hall_id)
                        REFERENCES HALL(hall_id)
                        ON DELETE CASCADE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS INVIGILATOR_ALLOCATION (
                    invigilator_allocation_id INT AUTO_INCREMENT PRIMARY KEY,
                    invigilator_id INT NOT NULL,
                    exam_id INT NOT NULL,
                    hall_id INT NOT NULL,
                    UNIQUE (invigilator_id, exam_id),
                    FOREIGN KEY (invigilator_id)
                        REFERENCES INVIGILATOR(invigilator_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (exam_id)
                        REFERENCES EXAM(exam_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (hall_id)
                        REFERENCES HALL(hall_id)
                        ON DELETE CASCADE
                )
            """);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createTriggersIfNotExist(Connection connection) {

        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("DROP TRIGGER IF EXISTS auto_allocate_courses");

            stmt.executeUpdate("""
                CREATE TRIGGER auto_allocate_courses
                AFTER INSERT ON STUDENT
                FOR EACH ROW
                INSERT INTO STUDENT_COURSE (roll_number, course_code)
                SELECT NEW.roll_number, co.course_code
                FROM COURSE_OFFERING co
                WHERE co.department = NEW.department
                AND co.semester = NEW.semester
            """);

            stmt.executeUpdate("DROP TRIGGER IF EXISTS auto_allocate_on_semester_update");

            stmt.executeUpdate("""
                CREATE TRIGGER auto_allocate_on_semester_update
                AFTER UPDATE ON STUDENT
                FOR EACH ROW
                INSERT IGNORE INTO STUDENT_COURSE (roll_number, course_code)
                SELECT NEW.roll_number, co.course_code
                FROM COURSE_OFFERING co
                WHERE co.department = NEW.department
                AND co.semester = NEW.semester
            """);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}