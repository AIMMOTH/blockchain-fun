package com.github.aimmoth.blockchain;

import java.security.MessageDigest;
import java.util.logging.Logger;

public class Miner {

  private Logger logger = Logger.getLogger(Miner.class.getName());

  public static String firstHash = "0";
  private MinerUtil util;

  private String name;

  public Miner(MessageDigest digest) {
    this(digest, "name");
  }

  public Miner(MessageDigest digest, String name) {
    this.name = name;
    util = new MinerUtil(digest, name);
  }

  public Block mineFirst(String data) {
    return mineBlock(data, firstHash);
  }

  public Block mineBlock(String data, String previousHash) {
    logger.info(name + " mineBlock");

    long timeStamp = System.nanoTime();
    int nonce = 0;
    String hash = util.calculateBlockHash(previousHash, timeStamp, nonce, data);
    while (!hash.substring(0, util.prefix).equals(util.prefixString)) {
      timeStamp = System.nanoTime();
      nonce++;
      hash = util.calculateBlockHash(previousHash, timeStamp, nonce, data);
    }
    logger.info(name + " new block");
    return new Block(data, hash, previousHash, timeStamp, nonce);
  }
}
