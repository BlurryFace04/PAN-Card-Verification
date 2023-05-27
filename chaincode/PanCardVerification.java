package org.hyperledger.fabric.samples.pancard;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.nio.charset.StandardCharsets;

import io.github.rctcwyvrn.blake3.Blake3;

@Contract(name = "PanCardVerification", info = @Info(title = "Pan Card Verification Chaincode", description = "", version = "0.0.1"))
@Default
public final class PanCardVerification implements ContractInterface {

    private enum PanCardVerificationErrors {
        PAN_CARD_ALREADY_EXISTS,
        PAN_CARD_NOT_FOUND,
        PAN_CARD_VERIFICATION_FAILED
    }

    @Transaction
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        String[] initialData = {
                "PAN0001:John Doe:1990-01-01:Robert Doe",
                "PAN0002:Jane Smith:1985-05-15:William Smith"
        };

        for (String panCardData : initialData) {
            String[] parts = panCardData.split(":");
            String panNumber = parts[0];
            String name = parts[1];
            String dob = parts[2];
            String fatherName = parts[3];

            createPanCard(ctx, panNumber, name, dob, fatherName);
        }
    }

    @Transaction
    public void createPanCard(final Context ctx, final String panNumber, final String name, final String dob, final String fatherName) {
        ChaincodeStub stub = ctx.getStub();
        String panCardKey = getPanCardKey(panNumber);
        byte[] panCardHashData = stub.getState(panCardKey);

        if (panCardHashData != null && panCardHashData.length > 0) {
            String errorMessage = String.format("Pan Card %s already exists", panNumber);
            throw new ChaincodeException(errorMessage, PanCardVerificationErrors.PAN_CARD_ALREADY_EXISTS.toString());
        }

        String panCardDetails = String.join(":", panNumber, name, dob, fatherName);
        byte[] panCardDetailsBytes = panCardDetails.getBytes(StandardCharsets.UTF_8);
        byte[] panCardHash = calculateHash(panCardDetailsBytes);

        // Store the PAN card hash on-chain in the World State
        stub.putStringState(panCardKey, new String(panCardHash, StandardCharsets.UTF_8));

        // Store the PAN card details off-chain in the "panCardCollection" private data collection
        stub.putPrivateData("panCardCollection", panCardKey, panCardDetailsBytes);
    }

    @Transaction
    public boolean verifyPanCard(final Context ctx, final String panNumber, final String name, final String dob, final String fatherName) {
        ChaincodeStub stub = ctx.getStub();
        String panCardKey = getPanCardKey(panNumber);

        // Get the PAN card hash from the World State
        String panCardHashData = stub.getStringState(panCardKey);

        if (panCardHashData == null || panCardHashData.isEmpty()) {
            String errorMessage = String.format("Pan Card %s does not exist", panNumber);
            throw new ChaincodeException(errorMessage, PanCardVerificationErrors.PAN_CARD_NOT_FOUND.toString());
        }

        String inputPanCardDetails = String.join(":", panNumber, name, dob, fatherName);
        byte[] inputPanCardDetailsBytes = inputPanCardDetails.getBytes(StandardCharsets.UTF_8);
        byte[] inputPanCardHash = calculateHash(inputPanCardDetailsBytes);
        String inputPanCardHashString = new String(inputPanCardHash, StandardCharsets.UTF_8);

        if (panCardHashData.equals(inputPanCardHashString)) {
            return true;
        } else {
            String combined = String.join(":", panCardHashData, inputPanCardHashString);
            throw new ChaincodeException(combined, PanCardVerificationErrors.PAN_CARD_VERIFICATION_FAILED.toString());
        }
    }

    @Transaction
    public void updatePanCard(final Context ctx, final String panNumber, final String name, final String dob, final String fatherName) {
        ChaincodeStub stub = ctx.getStub();
        String panCardKey = getPanCardKey(panNumber);

        // Check if the PAN card hash exists in the World State
        byte[] panCardHashData = stub.getState(panCardKey);

        if (panCardHashData == null || panCardHashData.length == 0) {
            String errorMessage = String.format("Pan Card %s does not exist", panNumber);
            throw new ChaincodeException(errorMessage, PanCardVerificationErrors.PAN_CARD_NOT_FOUND.toString());
        }

        String updatedPanCardDetails = String.join(":", panNumber, name, dob, fatherName);
        byte[] updatedPanCardDetailsBytes = updatedPanCardDetails.getBytes(StandardCharsets.UTF_8);
        byte[] updatedPanCardHash = calculateHash(updatedPanCardDetailsBytes);

        // Update the PAN card hash on-chain in the World State
        stub.putStringState(panCardKey, new String(updatedPanCardHash, StandardCharsets.UTF_8));

        // Update the PAN card details off-chain in the "panCardCollection" private data collection
        stub.putPrivateData("panCardCollection", panCardKey, updatedPanCardDetailsBytes);
    }

    @Transaction
    public void deletePanCard(final Context ctx, final String panNumber) {
        ChaincodeStub stub = ctx.getStub();
        String panCardKey = getPanCardKey(panNumber);

        // Check if the PAN card hash exists in the World State
        byte[] panCardHashData = stub.getState(panCardKey);

        if (panCardHashData == null || panCardHashData.length == 0) {
            String errorMessage = String.format("Pan Card %s does not exist", panNumber);
            throw new ChaincodeException(errorMessage, PanCardVerificationErrors.PAN_CARD_NOT_FOUND.toString());
        }

        // Delete the PAN card hash from the World State
        stub.delState(panCardKey);

        // Delete the PAN card details from the "panCardCollection" private data collection
        stub.delPrivateData("panCardCollection", panCardKey);
    }

    @Transaction
    public String queryPanCard(final Context ctx, final String panNumber) {
        ChaincodeStub stub = ctx.getStub();
        String panCardKey = getPanCardKey(panNumber);
        byte[] panCardData = stub.getPrivateData("panCardCollection", panCardKey);

        if (panCardData == null || panCardData.length == 0) {
            String errorMessage = String.format("Pan Card %s does not exist", panNumber);
            throw new ChaincodeException(errorMessage, PanCardVerificationErrors.PAN_CARD_NOT_FOUND.toString());
        }

        return new String(panCardData, StandardCharsets.UTF_8);
    }

    private String getPanCardKey(final String panNumber) {
        return String.format("PAN_%s", panNumber);
    }

    private byte[] calculateHash(final byte[] input) {
        Blake3 hasher = Blake3.newInstance();
        hasher.update(input);
        return hasher.digest();
    }
}
