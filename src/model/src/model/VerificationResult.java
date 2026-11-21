package model;

import interfaces.Displayable;

public class VerificationResult implements Displayable {
    private String resultId;
    private String degreeId;
    private String outcome;     // Valid, Invalid, Revoked, Not Found
    private String message;
    private String checkedAt;

    public VerificationResult(String resultId, String degreeId, String outcome, String message) {
        this(resultId, degreeId, outcome, message, "2026-11-20");
    }

    public VerificationResult(String resultId, String degreeId, String outcome,
                              String message, String checkedAt) {
        this.resultId = resultId;
        this.degreeId = degreeId;
        this.outcome = outcome;
        this.message = message;
        this.checkedAt = checkedAt;
    }

    public String getResultId() { return resultId; }
    public void setResultId(String resultId) { this.resultId = resultId; }
    public String getDegreeId() { return degreeId; }
    public void setDegreeId(String degreeId) { this.degreeId = degreeId; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCheckedAt() { return checkedAt; }
    public void setCheckedAt(String checkedAt) { this.checkedAt = checkedAt; }

    public boolean isValid() {
        return outcome.equals("Valid");
    }

    @Override
    public void display() {
        System.out.println("====================================");
        System.out.println("Result ID  : " + resultId);
        System.out.println("Degree ID  : " + degreeId);
        System.out.println("Outcome    : " + outcome);
        System.out.println("Message    : " + message);
        System.out.println("Checked At : " + checkedAt);
        System.out.println("====================================");
    }
}