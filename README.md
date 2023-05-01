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
### Sample Block Data:
<p>
<img src="https://user-images.githubusercontent.com/64888928/235459540-bfb6e41f-459c-47e2-a93d-58f303f5b189.png" width="40%" height="40%" />
<img src="https://user-images.githubusercontent.com/64888928/235461958-48dfbfd3-e782-4d52-b903-520162dcd040.png" width="40%" height="40%" />
<p>
