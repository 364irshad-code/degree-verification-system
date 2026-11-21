package model;

public class Employer extends User {
    private String organizationName;
    private String contactEmail;
    private int verificationCount;

    public Employer(String userId, String username, String password,
                    String organizationName, String contactEmail) {
        super(userId, username, password, "Employer");
        this.organizationName = organizationName;
        this.contactEmail = contactEmail;
        this.verificationCount = 0;
    }

    public Employer(String userId, String username, String password, String organizationName) {
        this(userId, username, password, organizationName, "not provided");
    }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public int getVerificationCount() { return verificationCount; }
    public void setVerificationCount(int verificationCount) { this.verificationCount = verificationCount; }

    @Override
    public boolean login(String username, String password) {
        boolean ok = super.login(username, password);
        if (ok) {
            System.out.println("Employer from " + organizationName + " logged in.");
        }
        return ok;
    }

    @Override
    public String[] getPermissions() {
        return new String[] { "verify degree by Degree ID or QR code" };
    }

    public VerificationResult verifyDegree(VerificationController controller, String degreeId) {
        verificationCount = verificationCount + 1;
        return controller.verifyDegree(degreeId);
    }

    public String scanQRCode(String qrData) {
        return QRCode.decode(qrData);
    }

    public String viewResult(VerificationResult result) {
        return "[" + result.getOutcome() + "] " + result.getMessage();
    }
}