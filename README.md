# Blockchain Based Degree Verification System

**Student:** Muhammad Irshad
**Registration No:** 24P-0542
**Section:** BCS-9A
**Course:** Software Design & Analysis (Assignment 4)

A console Java program that implements the class design of Assignment 3.
Registrars issue degrees (the hash is stored through a blockchain gateway)
and employers verify them.

## Classes
- **interfaces:** Displayable
- **model:** User (abstract), Registrar, Student, Employer, Degree, QRCode,
  VerificationResult, BlockchainTransaction, BlockchainGateway (abstract),
  SimulatedGateway, EthereumGateway, DegreeRepository,
  IssuanceController, VerificationController
- **main:** Main

## How to Run
1. Open Eclipse and create a Java Project (untick "Create module-info.java").
2. Copy the `src` folder contents into the project.
3. Run `src/main/Main.java` as a Java Application.
4. Choose gateway `1`, then log in with `registrar1` / `pass123`.
5. Use the menu: issue degree `D-1001` (option 3), then verify it (option 4).

Uses only core Java. No external libraries.
