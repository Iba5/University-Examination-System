package src.models;

public class Allocation {

    private int allocationId;
    private String rollNumber;
    private int examId;
    private int hallId;

    public Allocation(int allocationId, String rollNumber, int examId, int hallId) {
        this.allocationId = allocationId;
        this.rollNumber = rollNumber;
        this.examId = examId;
        this.hallId = hallId;
    }

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }
}
