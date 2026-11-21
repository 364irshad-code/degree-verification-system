package model;

public class IssuanceController {
    private int maxRetries;
    private int retryDelaySeconds;
    private String verificationBaseUrl;
    private int issuedCount;
    private String currentDate;
    private String lastError;
    private DegreeRepository repository;      // aggregation
    private BlockchainGateway gateway;        // aggregation (parent type)

    public IssuanceController(DegreeRepository repository, BlockchainGateway gateway) {
        this(repository, gateway, 3, "https://degree-verify.example/verify");
    }

    public IssuanceController(DegreeRepository repository, BlockchainGateway gateway,
                              int maxRetries, String verificationBaseUrl) {
        this.repository = repository;
        this.gateway = gateway;
        this.maxRetries = maxRetries;
        this.retryDelaySeconds = 2;
        this.verificationBaseUrl = verificationBaseUrl;
        this.issuedCount = 0;
        this.currentDate = "2026-11-20";
        this.lastError = "";
    }

    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public int getRetryDelaySeconds() { return retryDelaySeconds; }
    public void setRetryDelaySeconds(int retryDelaySeconds) { this.retryDelaySeconds = retryDelaySeconds; }
    public String getVerificationBaseUrl() { return verificationBaseUrl; }
    public void setVerificationBaseUrl(String verificationBaseUrl) { this.verificationBaseUrl = verificationBaseUrl; }
    public int getIssuedCount() { return issuedCount; }
    public void setIssuedCount(int issuedCount) { this.issuedCount = issuedCount; }
    public String getCurrentDate() { return currentDate; }
    public void setCurrentDate(String currentDate) { this.currentDate = currentDate; }
    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }

    public Degree addDegreeRecord(String degreeId, String studentName, String rollNumber,
                                  String program, float cgpa, int graduationYear) {
        lastError = "";
        if (repository.findById(degreeId) != null) {
            lastError = "Degree ID already exists";
            return null;
        }
        Degree degree = new Degree(degreeId, studentName, rollNumber, program, cgpa, graduationYear);
        repository.save(degree);
        return degree;
    }

    public Degree[] listPendingDegrees() {
        return repository.findPending();
    }

    public Degree issueDegree(String degreeId, String registrarId) {
        lastError = "";

        // Step 1: check
        Degree degree = repository.findById(degreeId);
        if (degree == null) {
            lastError = "Degree not found";
            return null;
        } else if (degree.isIssued()) {
            lastError = "Degree already issued";
            return null;
        }

        // Step 2: hash and Processing
        String hash = degree.calculateHash();
        degree.setDegreeHash(hash);
        degree.markProcessing();
        repository.save(degree);

        // Step 3: store on blockchain with retries
        int attempts = 0;
        BlockchainTransaction tx = null;
        while (attempts < maxRetries && (tx == null || !tx.isConfirmed())) {
            tx = gateway.storeHash(degreeId, hash, currentDate);   // runtime polymorphism
            if (tx == null || !tx.isConfirmed()) {
                attempts = attempts + 1;
                System.out.println("Blockchain attempt " + attempts + " of " + maxRetries + " failed.");
            }
        }

        if (tx == null || !tx.isConfirmed()) {
            degree.revertToPending();
            repository.save(degree);
            lastError = "Blockchain transaction failed";
            return null;
        }

        // Step 4: complete
        degree.markIssued(tx, currentDate, registrarId);
        degree.generateQRCode(verificationBaseUrl);
        repository.save(degree);
        issuedCount = issuedCount + 1;
        return degree;
    }

    public boolean revokeDegree(String degreeId, String registrarId) {
        lastError = "";
        Degree degree = repository.findById(degreeId);
        if (degree == null) {
            lastError = "Degree not found";
            return false;
        } else if (!degree.getStatus().equals(Degree.ISSUED)) {
            lastError = "Only an issued degree can be revoked";
            return false;
        }
        BlockchainTransaction tx = gateway.revokeOnChain(degreeId, currentDate);
        if (tx == null) {
            lastError = "Blockchain revoke failed";
            return false;
        }
        degree.markRevoked(tx);
        degree.setIssuedBy(registrarId);
        repository.save(degree);
        return true;
    }
}