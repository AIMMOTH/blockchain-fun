package com.github.aimmoth.blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Blockchain {

  private final Logger logger = Logger.getLogger(Blockchain.class.getName());
  private final MinerUtil util;

  private List<Block> chain;
  private boolean locked = false;
  private MessageDigest digest;
  private String name;

  public Blockchain(MessageDigest digest) {
    this(digest, UUID.randomUUID().toString());
  }

  public Blockchain(MessageDigest digest, String name) {
    this(digest, name, new ArrayList<>());
  }

  public Blockchain(MessageDigest digest, String name, List<Block> chain) {
    this.digest = digest;
    this.name = name;
    this.chain = chain;
    util = new MinerUtil(digest, name);
  }

  public boolean tryAdd(Block block) {
    if (locked) {
      return false;
    } else {
      locked = true;
      var cloned = chain.stream().map(Block::cloneSafe).collect(Collectors.toList());
      cloned.add(block.cloneSafe());
      return util.verify(block) && verifyChain(cloned);
    }
  }

  /**
   * Cannot add before tryAdd (which locks this blockchain)!
   *
   * @param block
   * @return
   */
  public boolean add(Block block) {
    if (locked) {
      logger.info(name + " add");
      chain.add(block);
      locked = false;
      return true;
    } else {
      logger.info(name + " not locked!");
      return false;
    }
  }

  private boolean verifyChain(List<Block> chain) {
    logger.info(name + " verifyChain");

    for (int i = 0; i < chain.size(); i++) {
      String previousHash = i == 0 ? Miner.firstHash : chain.get(i - 1).getHash();

      boolean hashCalculationVerified = chain.get(i).getHash().equals(util.calculateBlockHash(chain.get(i)));
      boolean previousHashVerified = previousHash.equals(chain.get(i).getPreviousHash());
      boolean hashVerified = util.verify(chain.get(i));

      if (!(hashCalculationVerified && previousHashVerified && hashVerified)) {
        logger.info(name + " verify false!");
        return false;
      }
    }
    return true;
  }

  public Block get(int index) {
    return chain.get(index);
  }

  public int size() {
    return chain.size();
  }

  protected Blockchain cloneSafe() {
    var cloned = chain.stream().map(Block::cloneSafe).collect(Collectors.toList());
    return new Blockchain(digest, new String(name), cloned);
  }

  @Override
  public String toString() {
    return "Blockchain [chain=" + chain + ", locked=" + locked + ", name=" + name + "]";
  }

}
