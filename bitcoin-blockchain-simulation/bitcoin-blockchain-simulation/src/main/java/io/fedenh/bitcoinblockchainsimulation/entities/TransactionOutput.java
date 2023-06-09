package io.fedenh.bitcoinblockchainsimulation.entities;

import io.fedenh.bitcoinblockchainsimulation.utils.StringUtil;

import java.security.PublicKey;

public class TransactionOutput {
    public String idTransactionOutput;
    public PublicKey owner;
    public TransactionOutput amount;
    public String parentTransactionId;

    public TransactionOutput(PublicKey owner, TransactionOutput amount, String parentTransactionId) {
        this.owner = owner;
        this.amount = amount;
        this.parentTransactionId = parentTransactionId;
        this.idTransactionOutput = calculateHash();
    }

    public String calculateHash(){
        String input = StringUtil.getStringFromKey(owner)+Double.toString(amount)+parentTransactionId;
        return StringUtil.applySha256(input);
    }
    public boolean isMine(PublicKey publicKey){
        return owner == publicKey;
    }
}
