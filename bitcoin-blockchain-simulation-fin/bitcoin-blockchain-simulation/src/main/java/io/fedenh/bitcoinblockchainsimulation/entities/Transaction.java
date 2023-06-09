package io.fedenh.bitcoinblockchainsimulation.entities;

import io.fedenh.bitcoinblockchainsimulation.BitcoinBlockchainSimulation;
import io.fedenh.bitcoinblockchainsimulation.utils.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    public String idTransaction;
    public PublicKey sender;
    public PublicKey receiver;
    public double amount;
    public byte[] signature;
    public ArrayList<TransactionInput> transactionInputs = new ArrayList<>();
    public ArrayList<TransactionOutput> transactionOutputs = new ArrayList<>();
    private Long timestamp;

    public static int sequence;

    public Transaction(PublicKey sender,
                       PublicKey receiver,
                       double amount,
                       ArrayList<TransactionInput> transactionInputs) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.transactionInputs = transactionInputs;
        this.timestamp = new Date().getTime();

    }

    public String calculateHash() {
        sequence++;
        return StringUtil.applySha256(getInput());
    }

    public void generateSignature(PrivateKey privateKey) {
        signature = StringUtil.applyECDSASig(privateKey, getInput());
    }

    public boolean verifySignature() {
        return StringUtil.verifyECDSASig(sender, getInput(), signature);
    }

    private String getInput() {
        return StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(receiver)
                + Double.toString(amount)
                + Long.toString(timestamp)
                + Integer.toString(sequence);
    }

    public boolean treatment() {
        if (!verifySignature()) {
            System.out.println("Signature incorrecte");
            return false;
        }
        for (TransactionInput transactionInput : transactionInputs) {
            transactionInput.UTXO = BitcoinBlockchainSimulation.UTXOs.get(transactionInput.transactionOutputId);
        }
        double totalAmount = totalAmountInputs();
        if (totalAmount < amount) {
            System.out.println("Montant insuffisant");
            return false;
        }
        idTransaction = calculateHash();
        double reliquat = totalAmount - amount;
        transactionOutputs.add(new TransactionOutput(receiver, amount, idTransaction));
        if (reliquat != 0) {
            transactionOutputs.add(new TransactionOutput(sender, reliquat, idTransaction));
        }
        for (TransactionOutput transactionOutput : transactionOutputs) {
            BitcoinBlockchainSimulation.UTXOs.put(transactionOutput.idTransactionOutput, transactionOutput);
        }
        for (TransactionInput transactionInput : transactionInputs) {
            BitcoinBlockchainSimulation.UTXOs.remove(transactionInput.transactionOutputId);
        }
        return true;
    }

    public double totalAmountInputs() {
        double total = 0;
        for (TransactionInput transactionInput : transactionInputs) {
            total += transactionInput.UTXO.amount;
        }
        return total;
    }
}
