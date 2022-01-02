package com.github.aimmoth.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Inspired by Baeldung blog https://www.baeldung.com/java-blockchain
 *
 * @see https://www.baeldung.com/java-blockchain
 * @author Carl
 *
 */
public class Simulator {

  private static Logger logger = Logger.getLogger(Simulator.class.getName());

  public static void main(String[] args) throws NoSuchAlgorithmException {
    var firstSleep = 1000;
    var secondSleep = 3000;
    var thirdSleep = 5000;

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    Https https = new Https();

    var node1 = Node.createFirstNode(https, digest, "[ NODE 1 ]");

    var node2 = new Node(https, digest, "[ NODE 2 ]");

    var thread1 = new Thread() {

      @Override
      public void run() {
        logger.info("START thread 1");
        try {
          node1.mineBlock("second");
          sleep(secondSleep);
          node1.mineBlock("fourth");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        logger.info("END thread 1");
      }
    };
    var thread2 = new Thread() {

      @Override
      public void run() {
        logger.info("START thread 2");
        try {
          sleep(firstSleep);
          node2.mineBlock("third");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        logger.info("END thread 2");
      }
    };
    var thread3 = new Thread() {

      @Override
      public void run() {
        logger.info("START thread 3");
        try {
          sleep(thirdSleep);
          var node3 = new Node(https, digest, "[ NODE 3 ]");
          node3.mineBlock("fifth");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        logger.info("END thread 3");
      }
    };

    thread1.start();
    thread2.start();
    thread3.start();
  }

}
