package model;

public class VerificationController {
    private int timeoutSeconds;
    private int totalRequests;
    private int invalidCount;
    private String lastRequestAt;
    private DegreeRepository repository;
    private BlockchainGateway gateway;

    public VerificationController(DegreeRepository repository, BlockchainGateway gateway) {
        this(repository, gateway, 10);
    }

    public VerificationController(DegreeRepository repository, BlockchainGateway gateway,
                                  int timeoutSeconds) {
        this.repository = repository;
        this.gateway = gateway;
        this.timeoutSeconds = timeoutSeconds;
        this.totalRequests = 0;
        this.invalidCount = 0;
        this.lastRequestAt = "never";
    }

    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public int getTotalRequests() { return totalRequests; }
    public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
    public int getInvalidCount() { return invalidCount; }
    public void setInvalidCount(int invalidCount) { this.invalidCount = invalidCount; }
    public String getLastRequestAt() { return lastRequestAt; }
    public void setLastRequestAt(String lastRequestAt) { this.lastRequestAt = lastRequestAt; }

    public VerificationResult verifyDegree(String degreeId) {
        totalRequests = totalRequests + 1;
        lastRequestAt = "2026-11-20";

        Degree degree = repository.findById(degreeId);
        if (degree == null) {
            return buildResult("Not Found", degreeId);
        }

        String calculated = degree.calculateHash();
        BlockchainTransaction stored = gateway.getStoredRecord(degreeId);   // dynamic binding

        if (stored == null) {
            return buildResult("Invalid", degreeId);
        } else if (stored.getStatus().equals("Revoked")) {
            return buildResult("Revoked", degreeId);
        } else if (compareHashes(calculated, stored.getDegreeHash())) {
            return buildResult("Valid", degreeId);
        } else {
            return buildResult("Invalid", degreeId);
        }
    }

    public VerificationResult verifyByQR(String qrData) {
        String degreeId = QRCode.decode(qrData);
        if (degreeId.equals("")) {
            totalRequests = totalRequests + 1;
            return buildResult("Not Found", "(unreadable QR)");
        }
        return verifyDegree(degreeId);
    }

    public boolean compareHashes(String calculated, String stored) {
        return calculated != null && stored != null && calculated.equals(stored);
    }

    private VerificationResult buildResult(String outcome, String degreeId) {
        String message;
        if (outcome.equals("Valid")) {
            message = "Degree is authentic. The hashes match.";
        } else if (outcome.equals("Revoked")) {
            message = "This degree has been revoked by the university.";
        } else if (outcome.equals("Not Found")) {
            message = "No degree record was found for this ID.";
        } else {
            message = "Degree is INVALID. No matching hash on the blockchain.";
        }
        if (!outcome.equals("Valid")) {
            invalidCount = invalidCount + 1;
        }
        return new VerificationResult("VR-" + totalRequests, degreeId, outcome, message);
    }

    public double getInvalidPercentage() {
        if (totalRequests == 0) {
            return 0.0;
        }
        return (double) invalidCount * 100 / totalRequests;     // type casting
    }
}