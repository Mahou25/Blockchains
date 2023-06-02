package io.fedenh.bitcoinblockchainsimulation.entities;

import io.fedenh.bitcoinblockchainsimulation.utils.StringUtil;

import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private Long timestamp;
    private int nonce;


    public Block(String previousHash, String data) {
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = previousHash + data + Long.toString(timestamp) + Integer.toString(nonce);
        return StringUtil.applySha256(input);
    }

    public boolean miner(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
            System.out.println("Minage en cours : nonce = " + nonce + "...");
        }
        System.out.println("Block miné : HASH = " + hash);
        return true;
    }
}
