/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Gateway, Wallets } = require('fabric-network');
const FabricCAServices = require('fabric-ca-client');
const path = require('path');
const { buildCAClient, registerAndEnrollUser, enrollAdmin } = require('../../../test-application/javascript/CAUtil.js');
const { buildCCPOrg1, buildWallet } = require('../../../test-application/javascript/AppUtil.js');
const express = require('express');

const channelName = process.env.CHANNEL_NAME || 'mychannel';
const chaincodeName = process.env.CHAINCODE_NAME || 'basic';

const mspOrg1 = 'Org1MSP';
const walletPath = path.join(__dirname, 'wallet');
const org1UserId = 'javascriptAppUser';

const app = express();
app.use(express.static('static'));
app.use(express.json());

const gateway = new Gateway();

function prettyJSONString(inputString) {
	return JSON.stringify(JSON.parse(inputString), null, 2);
}

async function setupGateway() {
    try {
        const ccp = buildCCPOrg1();
        const wallet = await buildWallet(Wallets, walletPath);

        await gateway.connect(ccp, {
            wallet,
            identity: org1UserId,
            discovery: { enabled: true, asLocalhost: true }
        });

        const network = await gateway.getNetwork(channelName);
        const contract = network.getContract(chaincodeName);
 
        return contract;
    } catch (error) {
        console.error(`******** FAILED to setup gateway: ${error}`);
        process.exit(1);
    }
}

app.post('/createPanCard', async (req, res) => {
    try {
        const { panNumber, name, dob, fatherName} = req.body;
      
        const contract = await setupGateway();
        await contract.submitTransaction('createPanCard', panNumber, name, dob, fatherName); 
        res.status(200).json({ message: 'PAN card created successfully.' }); 
    } catch (error) {
        console.error(`******** FAILED to run the application: ${error}`);
        res.status(500).json({ message: 'Error creating PAN card.' });
    } finally {
        gateway.disconnect();
    }
});

app.get('/queryPanCard/:panNumber', async (req, res) => {
    try {
        const panNumber = req.params.panNumber;

        const contract = await setupGateway();
        let result = await contract.evaluateTransaction('queryPanCard', panNumber);
        res.status(200).json({ message: result.toString() });
    } catch (error) {
        console.error(`******** FAILED to run the application: ${error}`);
        res.status(500).json({ message: 'Error querying PAN card.' });
    } finally {
        gateway.disconnect();
    }
});

app.put('/updatePanCard', async (req, res) => {
    try {
        const { panNumber, name, dob, fatherName} = req.body;
      
        const contract = await setupGateway();
        await contract.submitTransaction('updatePanCard', panNumber, name, dob, fatherName); 
        res.status(200).json({ message: 'PAN card updated successfully.' }); 
    } catch (error) {
        console.error(`******** FAILED to run the application: ${error}`);
        res.status(500).json({ message: 'Error updating PAN card.' });
    } finally {
        gateway.disconnect();
    }
});

app.delete('/deletePanCard/:panNumber', async (req, res) => {
    try {
        const panNumber = req.params.panNumber;

        const contract = await setupGateway();
        await contract.submitTransaction('deletePanCard', panNumber);
        res.status(200).json({ message: 'PAN card deleted successfully' });
    } catch (error) {
        console.error(`******** FAILED to run the application: ${error}`);
        res.status(500).json({ message: 'Error deleting PAN card.' });
    } finally {
        gateway.disconnect();
    }
});

app.listen(3000, () => {
    console.log('Server is running on http://localhost:3000');
});
