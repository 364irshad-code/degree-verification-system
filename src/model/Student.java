package model;

public class Student extends User {
    private String rollNumber;
    private String program;
    private int graduationYear;
    private float cgpa;

    public Student(String userId, String username, String password,
                   String rollNumber, String program, int graduationYear, float cgpa) {
        super(userId, username, password, "Student");
        this.rollNumber = rollNumber;
        this.program = program;
        this.graduationYear = graduationYear;
        this.cgpa = cgpa;
    }

    public Student(String userId, String username, String password, String rollNumber) {
        this(userId, username, password, rollNumber, "Not set", 0, 0.0f);
    }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    public int getGraduationYear() { return graduationYear; }
    public void setGraduationYear(int graduationYear) { this.graduationYear = graduationYear; }
    public float getCgpa() { return cgpa; }
    public void setCgpa(float cgpa) { this.cgpa = cgpa; }

    @Override
    public boolean login(String username, String password) {
        boolean ok = super.login(username, password);
        if (ok) {
            System.out.println("Student " + rollNumber + " logged in.");
        }
        return ok;
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "view own degree", "get verification link", "share QR code" };
    }

    public Degree[] viewMyDegree(DegreeRepository repository) {
        return repository.findByRollNumber(rollNumber);
    }

    public String getVerificationLink(Degree degree) {
        if (degree.getQrCode() == null) {
            return "Degree not issued yet";
        }
        return degree.getQrCode().getLink();
    }
}