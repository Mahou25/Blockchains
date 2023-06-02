package io.fedenh.bitcoinblockchainsimulation;

import io.fedenh.bitcoinblockchainsimulation.entities.Block;
import io.fedenh.bitcoinblockchainsimulation.entities.Transaction;
import io.fedenh.bitcoinblockchainsimulation.entities.TransactionOutput;
import io.fedenh.bitcoinblockchainsimulation.entities.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BitcoinBlockchainSimulation {
    public static ArrayList<Block> blockChain = new ArrayList<>();
    public static int difficultyLevel = 3;
    public static Wallet portefeuilleMere = new Wallet();
    public static Wallet portefeuille1 = new Wallet();
    public static Wallet portefeuille2 = new Wallet();
    public static Wallet portefeuille3 = new Wallet();
    public static Transaction genesisTransaction;
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

    public static void main(String[] args) {
        genesisTransaction = new Transaction(portefeuilleMere.publicKey,
                portefeuille1.publicKey, 10000, null);
        genesisTransaction.generateSignature(portefeuilleMere.privateKey);
        genesisTransaction.idTransaction = "0";

        TransactionOutput utxo = new TransactionOutput(genesisTransaction.receiver,
                genesisTransaction.amount, genesisTransaction.idTransaction);
        genesisTransaction.transactionOutputs.add(utxo);
        UTXOs.put(genesisTransaction.transactionOutputs.get(0).idTransactionOutput,
                genesisTransaction.transactionOutputs.get(0));

        System.out.println("Début du minage du bloc de genèse");
        Block genesisBlock = new Block("0");
        genesisBlock.addTransaction(genesisTransaction);
        showBalances();
        addBlock(genesisBlock);

        Block b = new Block(blockChain.get(blockChain.size() - 1).hash);
        b.addTransaction(portefeuille1.send(portefeuille2.publicKey, 100));
        b.addTransaction(portefeuille1.send(portefeuille3.publicKey, 200));
        b.addTransaction(portefeuille2.send(portefeuille3.publicKey, 200));
        showBalances();
        addBlock(b);

        b = new Block(blockChain.get(blockChain.size() - 1).hash);
        b.addTransaction(portefeuille1.send(portefeuille3.publicKey, 3000));
        b.addTransaction(portefeuille3.send(portefeuille2.publicKey, 725));
        b.addTransaction(portefeuille2.send(portefeuille1.publicKey, 350));
        b.addTransaction(portefeuille2.send(portefeuille1.publicKey, 350000));
        showBalances();
        addBlock(b);


        /*Block b = new Block("0");
        System.out.println("Début du minage");
        if (b.miner(difficultyLevel)) {
            blockChain.add(b);
        }

        b = new Block(blockChain.get(blockChain.size() - 1).hash);
        if (b.miner(difficultyLevel)) {
            blockChain.add(b);
        }

        b = new Block(blockChain.get(blockChain.size() - 1).hash);
        if (b.miner(difficultyLevel)) {
            blockChain.add(b);
        }

        String json = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println(json);

        System.out.println("Blockchain validity : " + verifyBlockchain());

        System.out.println(StringUtil.getStringFromKey(portefeuille1.clePublique));
        System.out.println(StringUtil.getStringFromKey(portefeuille1.clePrivee));

        Transaction transaction = new Transaction(
                portefeuille1.clePublique,
                portefeuille2.clePublique,
                50000,
                new ArrayList<>());

        transaction.generateSignature(portefeuille1.clePrivee);

        System.out.println(Arrays.toString(transaction.signature));
        System.out.println(transaction.verifySignature());*/
    }

    private static void showBalances() {
        System.out.println("Portefeuille 1 : " + portefeuille1.balance());
        System.out.println("Portefeuille 2 : " + portefeuille2.balance());
        System.out.println("Portefeuille 3 : " + portefeuille3.balance());
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

    static void addBlock(Block b) {
        if (b.miner(difficultyLevel)) {
            blockChain.add(b);
        }
    }
}
