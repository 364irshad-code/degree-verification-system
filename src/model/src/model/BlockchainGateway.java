package model;

public abstract class BlockchainGateway {
    private String networkUrl;
    private String contractAddress;
    private int timeoutSeconds;
    private boolean connected;

    public BlockchainGateway(String networkUrl, String contractAddress, int timeoutSeconds) {
        this.networkUrl = networkUrl;
        this.contractAddress = contractAddress;
        this.timeoutSeconds = timeoutSeconds;
        this.connected = false;
    }

    public String getNetworkUrl() { return networkUrl; }
    public void setNetworkUrl(String networkUrl) { this.networkUrl = networkUrl; }
    public String getContractAddress() { return contractAddress; }
    public void setContractAddress(String contractAddress) { this.contractAddress = contractAddress; }
    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }

    // Concrete methods
    public boolean connect() {
        connected = true;
        System.out.println("Connected to blockchain network: " + networkUrl);
        return connected;
    }

    public boolean isReachable() {
        return connected;
    }

    // Abstract methods: each gateway must implement them
    public abstract BlockchainTransaction storeHash(String degreeId, String hash, String issueDate);
    public abstract BlockchainTransaction getStoredRecord(String degreeId);
    public abstract BlockchainTransaction revokeOnChain(String degreeId, String date);
}