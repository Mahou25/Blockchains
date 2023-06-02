package io.fedenh.bitcoinblockchainsimulation;

import com.google.gson.GsonBuilder;
import io.fedenh.bitcoinblockchainsimulation.entities.Block;
import io.fedenh.bitcoinblockchainsimulation.entities.Transaction;
import io.fedenh.bitcoinblockchainsimulation.entities.TransactionOutput;
import io.fedenh.bitcoinblockchainsimulation.entities.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BitcoinBlockchainSimulation {
    public static ArrayList<Block> blockChain = new ArrayList<>();
    public static int difficultyLevel = 1;

    public static Wallet portefeuille1 = new Wallet();
    public static Wallet portefeuille2 = new Wallet();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

    public static void main(String[] args) {
        Block b = new Block("0", "Je suis le bloc de genèse");
        System.out.println("Début du minage");
        if (b.miner(difficultyLevel)) {
            blockChain.add(b);
        }

        b = new Block(blockChain.get(blockChain.size() - 1).hash, "Je suis le second bloc");
        if (b.miner(difficultyLevel)) {
            blockChain.add(b);
        }

        b = new Block(blockChain.get(blockChain.size() - 1).hash, "Je suis le troisième bloc");
        if (b.miner(difficultyLevel)) {
            blockChain.add(b);
        }

        String json = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println(json);

        System.out.println("Blockchain validity : " + verifyBlockchain());

        System.out.println(portefeuille1.cleprivee);
        System.out.println(portefeuille1.clepublic);

        System.out.println(portefeuille2.cleprivee);
        System.out.println(portefeuille2.clepublic);

        Transaction transaction = new Transaction(
                portefeuille1.clepublic,
                portefeuille2.clepublic,50000,new ArrayList<>());
    }



    public static boolean verifyBlockchain() {
        Block previous;
        Block current;
        for (int i = 0; i < blockChain.size(); i++) {
            current = blockChain.get(i);
            if (i > 0) {
                previous = blockChain.get(i - 1);
                if (!Objects.equals(previous.hash, current.previousHash)) {
                    return false;
                }
            }
            String calculatedHash = current.calculateHash();
            if (!Objects.equals(calculatedHash, current.hash)) {
                return false;
            }
        }
        return true;
    }
}
