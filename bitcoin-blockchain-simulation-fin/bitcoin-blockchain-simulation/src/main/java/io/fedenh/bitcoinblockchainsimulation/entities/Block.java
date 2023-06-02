package io.fedenh.bitcoinblockchainsimulation.entities;

import io.fedenh.bitcoinblockchainsimulation.utils.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {
    public String hash;
    public String previousHash;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private Long timestamp;
    private int nonce;


    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = previousHash + Long.toString(timestamp) + Integer.toString(nonce);
        return StringUtil.applySha256(input);
    }

    public boolean miner(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
//            System.out.println("Minage en cours : nonce = " + nonce + "...");
        }
        System.out.println("Block miné : HASH = " + hash);
        return true;
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        if (!Objects.equals(previousHash, "0")){
            if(!transaction.treatment()){
                System.out.println("Transaction non aboutie");
                return false;
            }
        }
        System.out.println("Transaction effectuée");
        transactions.add(transaction);
        return true;
    }
}
