package model;

import interfaces.Displayable;

public class Degree implements Displayable {
    public static final String PENDING = "Pending";
    public static final String PROCESSING = "Processing";
    public static final String ISSUED = "Issued";
    public static final String REVOKED = "Revoked";

    private String degreeId;
    private String studentName;
    private String rollNumber;
    private String program;
    private float cgpa;
    private int graduationYear;
    private String status;
    private String degreeHash;
    private String issueDate;
    private String issuedBy;
    private QRCode qrCode;                    // composition: Degree has a QRCode
    private BlockchainTransaction transaction;

    public Degree(String degreeId, String studentName, String rollNumber,
                  String program, float cgpa, int graduationYear) {
        this.degreeId = degreeId;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.program = program;
        this.cgpa = cgpa;
        this.graduationYear = graduationYear;
        this.status = PENDING;
        this.degreeHash = "";
        this.issueDate = "";
        this.issuedBy = "";
        this.qrCode = null;
        this.transaction = null;
    }

    public Degree(String degreeId, String studentName, String rollNumber) {
        this(degreeId, studentName, rollNumber, "Not set", 0.0f, 0);
    }

    public String getDegreeId() { return degreeId; }
    public void setDegreeId(String degreeId) { this.degreeId = degreeId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    public float getCgpa() { return cgpa; }
    public void setCgpa(float cgpa) { this.cgpa = cgpa; }
    public int getGraduationYear() { return graduationYear; }
    public void setGraduationYear(int graduationYear) { this.graduationYear = graduationYear; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDegreeHash() { return degreeHash; }
    public void setDegreeHash(String degreeHash) { this.degreeHash = degreeHash; }
    public String getIssueDate() { return issueDate; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
    public String getIssuedBy() { return issuedBy; }
    public void setIssuedBy(String issuedBy) { this.issuedBy = issuedBy; }
    public QRCode getQrCode() { return qrCode; }
    public void setQrCode(QRCode qrCode) { this.qrCode = qrCode; }
    public BlockchainTransaction getTransaction() { return transaction; }
    public void setTransaction(BlockchainTransaction transaction) { this.transaction = transaction; }

    // Method overloading: same name, different parameters
    public boolean updateDetails(float newCgpa) {
        if (status.equals(PENDING)) {
            cgpa = newCgpa;
            return true;
        }
        return false;
    }

    public boolean updateDetails(String newProgram, int newYear, float newCgpa) {
        if (status.equals(PENDING)) {
            program = newProgram;
            graduationYear = newYear;
            cgpa = newCgpa;
            return true;
        }
        return false;
    }

    // Simple hash (replaces SHA-256 because no library is allowed)
    public String calculateHash() {
        String data = degreeId + "|" + studentName + "|" + rollNumber + "|" + program
                + "|" + (int) (cgpa * 100) + "|" + graduationYear;     // type casting
        long h = 7;
        for (int i = 0; i < data.length(); i++) {
            h = h * 31 + data.charAt(i);
            h = h % 1000000007L;
        }
        return "HASH-" + h;
    }

    public boolean isIssued() {
        return status.equals(ISSUED) || status.equals(REVOKED);
    }

    public void markProcessing() { status = PROCESSING; }

    public void markIssued(BlockchainTransaction tx, String date, String registrarId) {
        this.transaction = tx;
        this.issueDate = date;
        this.issuedBy = registrarId;
        this.status = ISSUED;
    }

    public void markRevoked(BlockchainTransaction tx) {
        this.transaction = tx;
        this.status = REVOKED;
    }

    public void revertToPending() { status = PENDING; }

    // Composition: Degree creates its own QRCode
    public QRCode generateQRCode(String baseUrl) {
        qrCode = QRCode.generate(degreeId, baseUrl);
        return qrCode;
    }

    @Override
    public void display() {
        System.out.println("------------------------------------");
        System.out.println("Degree ID       : " + degreeId);
        System.out.println("Student Name    : " + studentName);
        System.out.println("Roll Number     : " + rollNumber);
        System.out.println("Program         : " + program);
        System.out.println("CGPA            : " + cgpa);
        System.out.println("Graduation Year : " + graduationYear);
        System.out.println("Status          : " + status);
        if (isIssued()) {
            System.out.println("Issue Date      : " + issueDate);
            System.out.println("Issued By       : " + issuedBy);
            System.out.println("Degree Hash     : " + degreeHash);
        }
        if (qrCode != null) {
            System.out.println("Verification URL: " + qrCode.getLink());
        }
        System.out.println("------------------------------------");
    }
}