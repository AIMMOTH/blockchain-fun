package com.github.aimmoth.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestMinerUtil {

  private MinerUtil underTest;

  @BeforeEach
  private void beforeEach() throws NoSuchAlgorithmException {
    underTest = new MinerUtil(MessageDigest.getInstance("SHA-256"));
  }

  @Test
  public void test_calculateBlockHash() throws NoSuchAlgorithmException {
    var previousHash = "previous hash";
    long timestamp = 123l;
    int nonce = 456;
    var data = "carl emmoth 2022";

    var result1 = underTest.calculateBlockHash(previousHash, timestamp, nonce, data);
    var result2 = underTest.calculateBlockHash(previousHash, timestamp, nonce, data);

    Assertions.assertEquals(result1, result2);
  }

  @Test
  public void test_calculateBlockHash_differentMiners() throws NoSuchAlgorithmException {
    var previousHash = "previous hash";
    long timestamp = 123l;
    int nonce = 456;
    var data = "carl emmoth 2022";

    var result1 = underTest.calculateBlockHash(previousHash, timestamp, nonce, data);
    var result2 = underTest.calculateBlockHash(previousHash, timestamp, nonce, data);

    Assertions.assertEquals(result1, result2);
  }

  @Test
  public void test_calculateBlockHash_differentMinersSameDigest() throws NoSuchAlgorithmException {
    var previousHash = "previous hash";
    long timestamp = 123l;
    int nonce = 456;
    var data = "carl emmoth 2022";

    var result1 = underTest.calculateBlockHash(previousHash, timestamp, nonce, data);
    var result2 = underTest.calculateBlockHash(previousHash, timestamp, nonce, data);

    Assertions.assertEquals(result1, result2);
  }
}
