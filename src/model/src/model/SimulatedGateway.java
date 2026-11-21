package model;

public class SimulatedGateway extends BlockchainGateway {
    private BlockchainTransaction[] ledger;
    private int ledgerSize;
    private boolean failureMode;
    private int txCounter;

    public SimulatedGateway() {
        this(false);
    }

    public SimulatedGateway(boolean failureMode) {
        super("memory://simulated-ledger", "SIM-CONTRACT", 5);
        this.ledger = new BlockchainTransaction[100];
        this.ledgerSize = 0;
        this.failureMode = failureMode;
        this.txCounter = 0;
    }

    public boolean isFailureMode() { return failureMode; }
    public void setFailureMode(boolean failureMode) { this.failureMode = failureMode; }
    public int getTxCounter() { return txCounter; }
    public void setTxCounter(int txCounter) { this.txCounter = txCounter; }
    public int getLedgerSize() { return ledgerSize; }

    private int indexOf(String degreeId) {
        for (int i = 0; i < ledgerSize; i++) {
            if (ledger[i].getDegreeId().equals(degreeId)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public BlockchainTransaction storeHash(String degreeId, String hash, String issueDate) {
        if (failureMode || !isReachable() || ledgerSize >= ledger.length) {
            return null;          // simulated network failure
        }
        txCounter = txCounter + 1;
        BlockchainTransaction tx = new BlockchainTransaction("SIM-TX-" + txCounter,
                degreeId, hash, issueDate);
        ledger[ledgerSize] = tx;
        ledgerSize = ledgerSize + 1;
        return tx;
    }

    @Override
    public BlockchainTransaction getStoredRecord(String degreeId) {
        if (!isReachable()) {
            return null;
        }
        int pos = indexOf(degreeId);
        if (pos >= 0) {
            return ledger[pos];
        }
        return null;
    }

    @Override
    public BlockchainTransaction revokeOnChain(String degreeId, String date) {
        int pos = indexOf(degreeId);
        if (failureMode || pos < 0) {
            return null;
        }
        ledger[pos].setStatus("Revoked");
        ledger[pos].setTimestamp(date);
        return ledger[pos];
    }

    public void resetLedger() {
        ledgerSize = 0;
        txCounter = 0;
    }
}