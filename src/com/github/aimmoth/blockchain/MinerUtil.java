package com.github.aimmoth.blockchain;

import java.security.MessageDigest;
import java.util.logging.Logger;

public class MinerUtil {

  private Logger logger = Logger.getLogger(MinerUtil.class.getName());

  public final int prefix = 3;
  public final String prefixString = new String(new char[prefix]).replace('\0', '0');

  private MessageDigest digest;
  private String name;

  public MinerUtil(MessageDigest digest) {
    this(digest, "miner util name");
  }

  public MinerUtil(MessageDigest digest, String name) {
    this.digest = digest;
    this.name = name;
  }

  public boolean verify(Block block) {
    logger.info(name + " verify");
    return block.getHash().substring(0, prefix).equals(prefixString);
  }

  public String calculateBlockHash(String previousHash, long timeStamp, int nonce, String data) {
    var concat = previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data;
    var dataBytes = concat.getBytes();
//    var dataBytes = concat.getBytes("UTF-8"); // Throws

    var buffer = new StringBuffer();
    byte[] bs = digest.digest(dataBytes); // No byte stream
    for (byte b : bs) {
      buffer.append(String.format("%02x", b));
    }
    return buffer.toString();
  }

  public String calculateBlockHash(Block block) {
    return calculateBlockHash(block.getPreviousHash(), block.getTimeStamp(), block.getNonce(), block.getData());
  }

  public void setName(String name) {
    this.name = name;
  }
}
