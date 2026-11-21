package model;

// NOTE: no external library is allowed, so this class only imitates the real
// Ethereum network. It prints what a real call would do and keeps the data in an array.
public class EthereumGateway extends BlockchainGateway {
    private String web3Provider;
    private String signerAlias;
    private long gasLimit;
    private int chainId;
    private BlockchainTransaction[] chain;
    private int chainSize;

    public EthereumGateway() {
        this("http://localhost:8545", "registrar-wallet", 3000000L, 1337);
    }

    public EthereumGateway(String web3Provider, String signerAlias, long gasLimit, int chainId) {
        super(web3Provider, "0xDegreeContract", 10);
        this.web3Provider = web3Provider;
        this.signerAlias = signerAlias;
        this.gasLimit = gasLimit;
        this.chainId = chainId;
        this.chain = new BlockchainTransaction[100];
        this.chainSize = 0;
    }

    public String getWeb3Provider() { return web3Provider; }
    public void setWeb3Provider(String web3Provider) { this.web3Provider = web3Provider; }
    public String getSignerAlias() { return signerAlias; }
    public void setSignerAlias(String signerAlias) { this.signerAlias = signerAlias; }
    public long getGasLimit() { return gasLimit; }
    public void setGasLimit(long gasLimit) { this.gasLimit = gasLimit; }
    public int getChainId() { return chainId; }
    public void setChainId(int chainId) { this.chainId = chainId; }

    private int indexOf(String degreeId) {
        for (int i = 0; i < chainSize; i++) {
            if (chain[i].getDegreeId().equals(degreeId)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public BlockchainTransaction storeHash(String degreeId, String hash, String issueDate) {
        if (!isReachable() || chainSize >= chain.length) {
            return null;
        }
        System.out.println("[Ethereum] Calling smart contract on chain " + chainId
                + " signed by " + signerAlias + " (gas limit " + gasLimit + ")");
        BlockchainTransaction tx = new BlockchainTransaction("0xETH" + (5000 + chainSize),
                degreeId, hash, issueDate);
        chain[chainSize] = tx;
        chainSize = chainSize + 1;
        return tx;
    }

    @Override
    public BlockchainTransaction getStoredRecord(String degreeId) {
        if (!isReachable()) {
            return null;
        }
        int pos = indexOf(degreeId);
        if (pos >= 0) {
            System.out.println("[Ethereum] Reading record from smart contract...");
            return chain[pos];
        }
        return null;
    }

    @Override
    public BlockchainTransaction revokeOnChain(String degreeId, String date) {
        int pos = indexOf(degreeId);
        if (pos < 0) {
            return null;
        }
        System.out.println("[Ethereum] Setting status Revoked in smart contract...");
        chain[pos].setStatus("Revoked");
        chain[pos].setTimestamp(date);
        return chain[pos];
    }
}