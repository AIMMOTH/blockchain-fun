package com.github.aimmoth.blockchain;

public class Block {

  private final String hash;
  private final String previousHash;
  private final String data;
  private final long timeStamp;
  private final int nonce;

  public Block(String data, String hash, String previousHash, long timeStamp, int nonce) {
    this.data = data;
    this.previousHash = previousHash;
    this.timeStamp = timeStamp;
    this.hash = hash;
    this.nonce = nonce;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((data == null) ? 0 : data.hashCode());
    result = prime * result + ((hash == null) ? 0 : hash.hashCode());
    result = prime * result + nonce;
    result = prime * result + ((previousHash == null) ? 0 : previousHash.hashCode());
    result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
    return result;
  }

  protected Block cloneSafe() {
    var newTimestamp = timeStamp;
    var newNonce = nonce;
    var clone = new Block(new String(data), new String(hash), new String(previousHash), newTimestamp, newNonce);
    return clone;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Block other = (Block) obj;
    if (data == null) {
      if (other.data != null) {
        return false;
      }
    } else if (!data.equals(other.data)) {
      return false;
    }
    if (hash == null) {
      if (other.hash != null) {
        return false;
      }
    } else if (!hash.equals(other.hash)) {
      return false;
    }
    if (nonce != other.nonce) {
      return false;
    }
    if (previousHash == null) {
      if (other.previousHash != null) {
        return false;
      }
    } else if (!previousHash.equals(other.previousHash)) {
      return false;
    }
    if (timeStamp != other.timeStamp) {
      return false;
    }
    return true;
  }

  public String getHash() {
    return hash;
  }

  public String getPreviousHash() {
    return previousHash;
  }

  @Override
  public String toString() {
    return "Block [hash=" + hash + ", previousHash=" + previousHash + ", data=" + data
        + ", timeStamp=" + timeStamp + ", nonce=" + nonce + "]";
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public int getNonce() {
    return nonce;
  }

  public String getData() {
    return data;
  }

}
