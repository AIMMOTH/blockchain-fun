package com.github.aimmoth.blockchain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBlock {

  private Miner miner;
  private Blockchain chain;

  @BeforeEach
  private void beforeEach() throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    miner = new Miner(digest);
    chain = new Blockchain(digest);
  }

  @Test
  public void test() {
    var block = miner.mineBlock("Carl Emmoth 2022", "0");
    chain.tryAdd(block);
    chain.add(block);

    Block newBlock = createNewBlock();
    Assertions.assertTrue(chain.tryAdd(newBlock));
    Assertions.assertTrue(chain.add(newBlock));
  }

  @Test
  public void givenBlockchain_whenValidated_thenSuccess() {
    var block = miner.mineBlock("Carl Emmoth 2022", "0");
    chain.tryAdd(block);
    var successful = chain.add(block);

    assertTrue(successful);
  }

  private Block createNewBlock() {
    return miner.mineBlock("The is a New Block.", chain.get(chain.size() - 1).getHash());
  }
}
