package com.github.aimmoth.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestNode {

  private MessageDigest digest;

  @BeforeEach
  public void beforeEach() throws NoSuchAlgorithmException {
    digest = MessageDigest.getInstance("SHA-256");
  }

  @Test
  public void test_constructor() {
    var node = Node.createFirstNode(new Https(), digest, "node 1");

    Assertions.assertNotNull(node.getName());
  }

  @Test
  public void test_secondNode() {
    var https = new Https();
    var node = Node.createFirstNode(https, digest, "node 1");

    var node2 = new Node(https, digest, "node 2");

    Assertions.assertNotNull(node.getName());
    Assertions.assertNotNull(node2.getName());
  }

  @Test
  public void test_thirdNode() {
    var https = new Https();
    var node = Node.createFirstNode(https, digest, "node 1");

    var node2 = new Node(https, digest, "node 2");

    var node3 = new Node(https, digest, "node 3");

    Assertions.assertEquals(1, node.size());
    Assertions.assertEquals(1, node2.size());
    Assertions.assertEquals(1, node3.size());
  }

  @Test
  public void test_thirdNodeWithSecondBlock() {
    var https = new Https();
    var node = Node.createFirstNode(https, digest, "node 1");

    var node2 = new Node(https, digest, "node 2");
    node2.mineBlock("node 2 block 1");

    var node3 = new Node(https, digest, "node 3");

    Assertions.assertEquals(2, node.size());
    Assertions.assertEquals(2, node2.size());
    Assertions.assertEquals(2, node3.size());
  }
}
