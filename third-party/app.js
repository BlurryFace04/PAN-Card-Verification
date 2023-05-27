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
const org1UserId = 'javascriptAppUser2';

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

app.post('/verifyPanCard', async (req, res) => {
    try {
        const { panNumber, name, dob, fatherName} = req.body;
      
        const contract = await setupGateway();
        await contract.submitTransaction('verifyPanCard', panNumber, name, dob, fatherName); 
        res.status(200).json({ message: 'PAN card verified successfully.' }); 
    } catch (error) {
        console.error(`******** FAILED to run the application: ${error}`);
        res.status(500).json({ message: 'Error verifying PAN card.' });
    } finally {
        gateway.disconnect();
    }
});

app.listen(4000, () => {
    console.log('Server is running on http://localhost:4000');
});
