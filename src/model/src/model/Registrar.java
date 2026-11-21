package model;

public class Registrar extends User {
    private String staffId;
    private String department;
    private String designation;

    public Registrar(String userId, String username, String password,
                     String staffId, String department, String designation) {
        super(userId, username, password, "Registrar");   // super in constructor
        this.staffId = staffId;
        this.department = department;
        this.designation = designation;
    }

    public Registrar(String userId, String username, String password) {
        this(userId, username, password, "N/A", "Registrar Office", "Officer");
    }

    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    @Override
    public boolean login(String username, String password) {
        boolean ok = super.login(username, password);      // super in method
        if (ok) {
            System.out.println("Registrar " + staffId + " (" + department + ") logged in successfully.");
        }
        return ok;
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "add degree record", "view pending degrees", "issue degree", "revoke degree" };
    }

    public Degree[] viewPendingDegrees(IssuanceController controller) {
        return controller.listPendingDegrees();
    }

    public Degree issueDegree(IssuanceController controller, String degreeId) {
        return controller.issueDegree(degreeId, staffId);
    }

    public boolean revokeDegree(IssuanceController controller, String degreeId) {
        return controller.revokeDegree(degreeId, staffId);
    }
}