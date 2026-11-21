package model;

import interfaces.Displayable;

public class QRCode implements Displayable {
    private String qrId;
    private String verificationLink;
    private String imageData;
    private String createdOn;

    public QRCode(String qrId, String verificationLink) {
        this(qrId, verificationLink, "[QR:" + verificationLink + "]", "2026-11-20");
    }

    public QRCode(String qrId, String verificationLink, String imageData, String createdOn) {
        this.qrId = qrId;
        this.verificationLink = verificationLink;
        this.imageData = imageData;
        this.createdOn = createdOn;
    }

    public String getQrId() { return qrId; }
    public void setQrId(String qrId) { this.qrId = qrId; }
    public String getVerificationLink() { return verificationLink; }
    public void setVerificationLink(String verificationLink) { this.verificationLink = verificationLink; }
    public String getImageData() { return imageData; }
    public void setImageData(String imageData) { this.imageData = imageData; }
    public String getCreatedOn() { return createdOn; }
    public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }

    public String getImage() { return imageData; }
    public String getLink() { return verificationLink; }

    public static QRCode generate(String degreeId, String baseUrl) {
        String link = baseUrl + "?id=" + degreeId;
        return new QRCode("QR-" + degreeId, link);
    }

    // Reads the Degree ID back from the QR text (link)
    public static String decode(String qrData) {
        int pos = qrData.indexOf("id=");
        if (pos < 0) {
            return "";
        }
        return qrData.substring(pos + 3).trim();
    }

    @Override
    public void display() {
        System.out.println("QR Code " + qrId + " -> " + imageData + " (created " + createdOn + ")");
    }
}