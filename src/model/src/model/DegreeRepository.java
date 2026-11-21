package model;

public class DegreeRepository {
    private String connectionString;
    private String tableName;
    private int maxConnections;
    private boolean connected;
    private Degree[] records;
    private int count;

    public DegreeRepository() {
        this("memory://degree-db", "degree_records", 5);
    }

    public DegreeRepository(String connectionString, String tableName, int maxConnections) {
        this.connectionString = connectionString;
        this.tableName = tableName;
        this.maxConnections = maxConnections;
        this.connected = true;
        this.records = new Degree[50];
        this.count = 0;
    }

    public String getConnectionString() { return connectionString; }
    public void setConnectionString(String connectionString) { this.connectionString = connectionString; }
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public int getMaxConnections() { return maxConnections; }
    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }
    public Degree[] getRecords() { return records; }
    public int getCount() { return count; }

    public Degree findById(String degreeId) {
        for (int i = 0; i < count; i++) {
            if (records[i].getDegreeId().equals(degreeId)) {
                return records[i];
            }
        }
        return null;
    }

    public Degree[] findPending() {
        int n = 0;
        for (int i = 0; i < count; i++) {
            if (records[i].getStatus().equals(Degree.PENDING)) {
                n++;
            }
        }
        Degree[] result = new Degree[n];
        int k = 0;
        for (int i = 0; i < count; i++) {
            if (records[i].getStatus().equals(Degree.PENDING)) {
                result[k] = records[i];
                k++;
            }
        }
        return result;
    }

    public Degree[] findByRollNumber(String rollNumber) {
        int n = 0;
        for (int i = 0; i < count; i++) {
            if (records[i].getRollNumber().equals(rollNumber)) {
                n++;
            }
        }
        Degree[] result = new Degree[n];
        int k = 0;
        for (int i = 0; i < count; i++) {
            if (records[i].getRollNumber().equals(rollNumber)) {
                result[k] = records[i];
                k++;
            }
        }
        return result;
    }

    public void save(Degree degree) {
        for (int i = 0; i < count; i++) {
            if (records[i].getDegreeId().equals(degree.getDegreeId())) {
                records[i] = degree;
                return;
            }
        }
        if (count < records.length) {
            records[count] = degree;
            count = count + 1;
        } else {
            System.out.println("Repository is full.");
        }
    }

    // An array is passed to this method
    public int countByStatus(Degree[] list, int size, String status) {
        int total = 0;
        for (int i = 0; i < size; i++) {
            if (list[i].getStatus().equals(status)) {
                total++;
            }
        }
        return total;
    }
}