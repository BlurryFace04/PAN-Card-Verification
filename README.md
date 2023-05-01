# Pan Card Verification using Hyperledger Fabric
This project presents a Pan Card Verification Application implemented using Hyperledger Fabric, a popular permissioned blockchain platform. The application aims to verify and maintain the authenticity of PAN card data using the distributed ledger technology. The project will introduce the application, explain its implementation, discuss the problem it solves, and explore its future scope.

## Introduction
Permanent Account Number (PAN) cards are unique identifiers issued to taxpayers in India. They are essential for various financial transactions and identity verification. Ensuring the authenticity of PAN card data is vital for the integrity of the financial system and preventing fraud. This Pan Card Verification Application uses Hyperledger Fabric, a permissioned blockchain platform, to create a secure, tamper-proof, and efficient system for verifying and maintaining PAN card data.

## Implementation
The application is built using Hyperledger Fabric, a platform that provides a modular architecture and a permissioned network for developing secure, scalable, and efficient blockchain solutions. The chaincode for the application is written in Java and follows the ContractInterface. The chaincode allows the creation, verification, updating, deletion, and querying of PAN card data.

The chaincode uses Blake3, a cryptographic hash function, to generate a unique hash for each PAN card's data. The PAN card hash is stored on-chain in the World State, while the PAN card details are stored off-chain in a private data collection. This design ensures data confidentiality while maintaining data integrity and authenticity.

The backend of the application is developed using Express.js, a popular web application framework for Node.js, and the Hyperledger Fabric JavaScript SDK.

### Web Application for KYC Service Provider:
![kyc-service-provider](https://user-images.githubusercontent.com/64888928/235459394-a31a53c7-83a0-4fb4-966d-11d8d29f8453.png)
### Web Application for Third Party:
![third-party](https://user-images.githubusercontent.com/64888928/235459475-4e674249-3ac8-4262-9863-a54b5cca6e94.png)
### Block Data Snapshots:
<p>
<img src="https://user-images.githubusercontent.com/64888928/235459540-bfb6e41f-459c-47e2-a93d-58f303f5b189.png" width="49%" height="49%" />
<img src="https://user-images.githubusercontent.com/64888928/235461958-48dfbfd3-e782-4d52-b903-520162dcd040.png" width="49%" height="49%" />
<p>

## Chaincode Overview
The java chaincode implementation consists of these key functions:
* **initLedger:** Initializes the ledger with existing PAN Card data.
* **createPanCard:** Creates a new PAN Card entry.
* **updatePanCard:** Updates the PAN Card information.
* **queryPanCard:** Retrieves the PAN Card details from the private data collection.
* **deletePanCard:** Deletes a PAN Card entry.
* **verifyPanCard:** Verifies the PAN Card details against the stored data.

## Problem and Solution
The Pan Card Verification Application addresses the problem of PAN card fraud, identity theft, and data tampering by leveraging the distributed ledger technology of Hyperledger Fabric. This technology ensures the authenticity, integrity, and security of PAN card data, providing a reliable and efficient way to verify, update, and maintain PAN card records. This minimizes the risk of fraudulent activities and improves the overall efficiency of the financial system.

For example, consider a scenario where an individual attempts to use a forged PAN card to open a bank account or apply for a loan. With traditional systems, the bank may have to rely on manual methods to verify the PAN card details, such as contacting the issuing authority or checking the data against a central database. This process can be time-consuming, error-prone, and vulnerable to data tampering or unauthorized access.

By using the Pan Card Verification Application built on Hyperledger Fabric, the bank can efficiently and securely verify the PAN card details. When the individual submits their PAN card information, the bank can use the application to verify the PAN card data stored on the blockchain. The application will retrieve the stored PAN card hash from the World State and compare it with the hash of the submitted PAN card details. If the hashes match, the PAN card is verified as authentic, ensuring that the individual's identity is legitimate and the PAN card has not been tampered with.

## Future Scope
1. **Integration with other identity verification systems:**<br>The application can be integrated with other identity verification systems such as Aadhaar to provide a comprehensive identity management solution.
2. **Implementation of advanced access control mechanisms:**<br>Role-based access control and attribute-based access control mechanisms can be implemented to ensure that only authorized users can access, update, or query PAN card data.
3. **Enhanced analytics and reporting:**<br>Advanced analytics and reporting features can be incorporated into the application, enabling organizations and government agencies to monitor trends, detect anomalies, and identify potential fraud or misuse of PAN card data.
