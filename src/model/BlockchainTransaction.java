package model;

import interfaces.Displayable;

public class BlockchainTransaction implements Displayable {
    private String transactionId;
    private String degreeId;
    private String degreeHash;
    private String status;       // Active or Revoked
    private String timestamp;
    private boolean confirmed;

    public BlockchainTransaction(String transactionId, String degreeId,
                                 String degreeHash, String timestamp) {
        this(transactionId, degreeId, degreeHash, "Active", timestamp, true);
    }

    public BlockchainTransaction(String transactionId, String degreeId, String degreeHash,
                                 String status, String timestamp, boolean confirmed) {
        this.transactionId = transactionId;
        this.degreeId = degreeId;
        this.degreeHash = degreeHash;
        this.status = status;
        this.timestamp = timestamp;
        this.confirmed = confirmed;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getDegreeId() { return degreeId; }
    public void setDegreeId(String degreeId) { this.degreeId = degreeId; }
    public String getDegreeHash() { return degreeHash; }
    public void setDegreeHash(String degreeHash) { this.degreeHash = degreeHash; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }

    public boolean isConfirmed() {
        return confirmed;
    }

    @Override
    public void display() {
        System.out.println("Transaction " + transactionId + " | Degree " + degreeId
                + " | " + status + " | " + timestamp);
    }
}