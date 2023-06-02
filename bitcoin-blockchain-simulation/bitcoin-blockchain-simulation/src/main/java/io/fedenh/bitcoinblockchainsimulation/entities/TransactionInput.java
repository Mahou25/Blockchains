package io.fedenh.bitcoinblockchainsimulation.entities;

public class TransactionInput {
    public String transactionOutputId;
    public TransactionOutput utxo;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
