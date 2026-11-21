package main;

import java.util.Scanner;

import interfaces.Displayable;
import model.BlockchainGateway;
import model.Degree;
import model.DegreeRepository;
import model.Employer;
import model.EthereumGateway;
import model.IssuanceController;
import model.Registrar;
import model.SimulatedGateway;
import model.Student;
import model.User;
import model.VerificationController;
import model.VerificationResult;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        DegreeRepository repo = new DegreeRepository();
        loadSampleData(repo);

        System.out.println("=== Blockchain Based Degree Verification System ===");
        System.out.println("Choose blockchain gateway:");
        System.out.println("1 = Simulated (in-memory)");
        System.out.println("2 = Ethereum (placeholder)");
        int g = readInt("Choice: ");

        SimulatedGateway sim = new SimulatedGateway();
        EthereumGateway eth = new EthereumGateway();
        BlockchainGateway gateway;                 // parent-class reference
        if (g == 2) {
            gateway = eth;
        } else {
            gateway = sim;
            g = 1;
        }
        gateway.connect();

        IssuanceController issuance = new IssuanceController(repo, gateway);
        VerificationController verification = new VerificationController(repo, gateway);

        Registrar registrar = new Registrar("U-101", "registrar1", "pass123",
                "STF-01", "Examination Office", "Registrar");
        Employer employer = new Employer("U-102", "employer1", "emp123",
                "Tech Solutions Pvt Ltd", "hr@techsolutions.example");
        Student student = new Student("U-103", "irshad", "stu123",
                "24P-0542", "BS Computer Science", 2028, 3.45f);

        // Login (FR-01)
        System.out.println("Registrar login (demo: registrar1 / pass123)");
        boolean loggedIn = false;
        int tries = 0;
        while (tries < 3 && !loggedIn) {
            String u = readText("Username: ");
            String p = readText("Password: ");
            loggedIn = registrar.login(u, p);
            if (!loggedIn) {
                tries++;
                System.out.println("Wrong username or password.");
            }
        }
        if (!loggedIn) {
            System.out.println("Too many failed attempts. Program closed.");
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1:
                    addDegree(issuance);
                    break;
                case 2:
                    viewPending(registrar, issuance);
                    break;
                case 3:
                    issueDegree(registrar, issuance);
                    break;
                case 4:
                    verifyById(employer, verification);
                    break;
                case 5:
                    verifyByQr(employer, verification);
                    break;
                case 6:
                    revokeDegree(registrar, issuance);
                    break;
                case 7:
                    showDetails(repo);
                    break;
                case 8:
                    studentView(student, repo);
                    break;
                case 9:
                    showPermissions(registrar, student, employer);
                    break;
                case 10:
                    if (g == 1) {
                        sim.setFailureMode(!sim.isFailureMode());
                        System.out.println("Simulated network failure mode is now: " + sim.isFailureMode());
                    } else {
                        System.out.println("Failure mode is only available for the simulated gateway.");
                    }
                    break;
                case 11:
                    showStatistics(repo, issuance, verification);
                    break;
                case 0:
                    running = false;
                    registrar.logout();
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    static void printMenu() {
        System.out.println();
        System.out.println("========== MENU ==========");
        System.out.println("1  Add Degree Record        (UC-02)");
        System.out.println("2  View Pending Degrees     (UC-03 step 2)");
        System.out.println("3  Issue Degree             (UC-03)");
        System.out.println("4  Verify Degree by ID      (UC-09)");
        System.out.println("5  Verify Degree by QR link (UC-09)");
        System.out.println("6  Revoke Degree            (UC-06)");
        System.out.println("7  Display Degree Details");
        System.out.println("8  Student: View My Degree  (UC-07)");
        System.out.println("9  Show Role Permissions");
        System.out.println("10 Switch Blockchain Failure Mode (test)");
        System.out.println("11 Show Statistics");
        System.out.println("0  Exit");
        System.out.println("==========================");
    }

    static void loadSampleData(DegreeRepository repo) {
        repo.save(new Degree("D-1001", "Muhammad Irshad", "24P-0542", "BS Computer Science", 3.45f, 2028));
        repo.save(new Degree("D-1002", "Ali Khan", "24P-0101", "BS Software Engineering", 3.10f, 2028));
        repo.save(new Degree("D-1003", "Sara Ahmed", "24P-0777"));
    }

    static void addDegree(IssuanceController issuance) {
        String id = readText("Degree ID (e.g. D-2001): ");
        String name = readText("Student name: ");
        String roll = readText("Roll number: ");
        String program = readText("Program: ");
        float cgpa = readFloat("CGPA (0.0 - 4.0): ");
        while (cgpa < 0.0f || cgpa > 4.0f) {
            System.out.println("CGPA must be between 0.0 and 4.0.");
            cgpa = readFloat("CGPA (0.0 - 4.0): ");
        }
        int year = readInt("Graduation year: ");
        while (year < 2000 || year > 2100) {
            System.out.println("Enter a year between 2000 and 2100.");
            year = readInt("Graduation year: ");
        }
        Degree d = issuance.addDegreeRecord(id, name, roll, program, cgpa, year);
        if (d == null) {
            System.out.println("Could not add record: " + issuance.getLastError());
        } else {
            System.out.println("Degree record added. Status: " + d.getStatus());
        }
    }

    static void viewPending(Registrar registrar, IssuanceController issuance) {
        Degree[] pending = registrar.viewPendingDegrees(issuance);
        if (pending.length == 0) {
            System.out.println("No pending degrees.");
        } else {
            System.out.println("Pending degrees: " + pending.length);
            showList(pending);
        }
    }

    static void issueDegree(Registrar registrar, IssuanceController issuance) {
        String id = readText("Degree ID to issue: ");
        Degree d = registrar.issueDegree(issuance, id);
        if (d == null) {
            System.out.println("Issue failed: " + issuance.getLastError());
        } else {
            System.out.println("Degree issued successfully! Status: " + d.getStatus());
            System.out.println("Transaction ID: " + d.getTransaction().getTransactionId());
            System.out.println("Hash          : " + d.getDegreeHash());
            System.out.println("Verification link (QR): " + d.getQrCode().getLink());
        }
    }

    static void verifyById(Employer employer, VerificationController verification) {
        String id = readText("Degree ID to verify: ");
        VerificationResult r = employer.verifyDegree(verification, id);
        r.display();
        System.out.println(employer.viewResult(r));
    }

    static void verifyByQr(Employer employer, VerificationController verification) {
        String qr = readText("Paste the QR / verification link: ");
        System.out.println("Degree ID read from QR: " + employer.scanQRCode(qr));
        VerificationResult r = verification.verifyByQR(qr);
        r.display();
    }

    static void revokeDegree(Registrar registrar, IssuanceController issuance) {
        String id = readText("Degree ID to revoke: ");
        boolean ok = registrar.revokeDegree(issuance, id);
        if (ok) {
            System.out.println("Degree " + id + " has been revoked.");
        } else {
            System.out.println("Revoke failed: " + issuance.getLastError());
        }
    }

    static void showDetails(DegreeRepository repo) {
        String id = readText("Degree ID: ");
        Degree d = repo.findById(id);
        if (d == null) {
            System.out.println("Degree not found.");
        } else {
            Displayable item = d;       // interface reference
            item.display();
        }
    }

    static void studentView(Student student, DegreeRepository repo) {
        Degree[] mine = student.viewMyDegree(repo);
        if (mine.length == 0) {
            System.out.println("No degree found for roll number " + student.getRollNumber());
        } else {
            showList(mine);
            System.out.println("Verification link: " + student.getVerificationLink(mine[0]));
        }
    }

    // Runtime polymorphism: User[] holds different child objects
    static void showPermissions(Registrar r, Student s, Employer e) {
        User[] users = { r, s, e };
        for (int i = 0; i < users.length; i++) {
            System.out.println("Role: " + users[i].getRole());
            String[] perms = users[i].getPermissions();     // same call, different answers
            for (int j = 0; j < perms.length; j++) {
                System.out.println("   - " + perms[j]);
            }
        }
    }

    static void showStatistics(DegreeRepository repo, IssuanceController issuance,
                               VerificationController verification) {
        Degree[] all = repo.getRecords();
        int size = repo.getCount();
        System.out.println("Total records : " + size);
        System.out.println("Pending       : " + repo.countByStatus(all, size, Degree.PENDING));
        System.out.println("Issued        : " + repo.countByStatus(all, size, Degree.ISSUED));
        System.out.println("Revoked       : " + repo.countByStatus(all, size, Degree.REVOKED));
        System.out.println("Issued in this run   : " + issuance.getIssuedCount());
        System.out.println("Verification requests: " + verification.getTotalRequests());
        System.out.println("Not valid results    : " + verification.getInvalidPercentage() + " %");
    }

    // Array passed to a method
    static void showList(Degree[] list) {
        for (int i = 0; i < list.length; i++) {
            list[i].display();
        }
    }

    static String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Please enter a whole number: ");
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    static float readFloat(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextFloat()) {
            sc.next();
            System.out.print("Please enter a number: ");
        }
        float value = sc.nextFloat();
        sc.nextLine();
        return value;
    }
}