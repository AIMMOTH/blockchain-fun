package com.github.aimmoth.blockchain;

import java.security.MessageDigest;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.github.aimmoth.util.Pair;

/**
 * Example:
   * <pre>
   * MessageDigest digest = MessageDigest.getInstance("SHA-256");
   * Https https = new Https();
   *
   * Node node1 = Node.createFirstNode(https, digest, "[ NODE 1 ]");
   *
   * List<String> nodeNames = new ArrayList<String>();
   * nodeNames.add(node1.getName());
   * Node node2 = new Node(https, digest, nodeNames, "[ NODE 2 ]");
   *
   * node1.mineBlock("data content 1");
   * node2.mineBlock("data content 2");
   *
   * </pre>
   *
 * @author Carl
 *
 */
public class Node {

  private final static Logger logger = Logger.getLogger(Node.class.getName());

  private Miner miner;

  private Blockchain chain;
  private final String name;
  private boolean started = false;

  private Https https;

  /**
   * Use this to create the first Node.
   *
   * @param https Create one and use for all nodes Https
   * @param digest Create one and use for all nodes by MessageDigest.getInstance("SHA-256")
   * @param name
   */
  public static Node createFirstNode(Https https, MessageDigest digest, String name) {
    var node = new Node(https, digest, name);

    var first = node.miner.mineFirst("github.com/Aimmoth/blockchain-fun");
    if (node.chain.tryAdd(first)) {
      node.chain.add(first);
    }
    return node;
  }

  /**
   * First use the other method to create the first node and block.
   *
   * @param https
   * @param digest
   * @param nodeNames
   * @param name
   */
  public Node(Https https, MessageDigest digest, String name) {
    this(https, digest, new Blockchain(digest, name), name);
  }

  private Node(Https https, MessageDigest digest, Blockchain chain, String name) {
    this.https = https;
    this.chain = chain;
    this.name = name;
    this.miner = new Miner(digest, name);
    this.https.addNode(this);

    logger.info(name + " Node created");

    startup();
  }

  private void startup() {
    Set<String> nodeNames = getOtherNodeNames();

    if (nodeNames.size() > 1) {
      logger.info(name + " more than 1 other nodes");

      BiFunction<Optional<Pair<String, Integer>>, Pair<String, Integer>, Optional<Pair<String, Integer>>> accumulator = (o, pair) -> {
        if (o.isPresent() && o.get().right < pair.right) {
          return o;
        }
        return Optional.of(pair);
      };
      BinaryOperator<Optional<Pair<String, Integer>>> combiner = (o1, o2) -> {
        if (o1.isPresent() && o2.isPresent()) {
          if (o1.get().right < o2.get().right) {
            return o1;
          } else {
            return o2;
          }
        } else if (o1.isPresent()) {
          return o1;
        } else {
          return o2;
        }
      };
      nodeNames.stream()
        .filter(name -> https.isStarted(name))
        .map(name -> Pair.pair(name, https.getSize(name)))
        .reduce(Optional.<Pair<String, Integer>>empty(), accumulator, combiner)
        .ifPresent(other -> {
          this.chain = https.getChain(other.left);
          logger.info(name + " chain:" + chain);
        })
        ;
    } else if (nodeNames.size() == 1) {
      logger.info(name + " 1 other node");

      this.chain = https.getChain(nodeNames.iterator().next());
      logger.info(name + " chain:" + chain);
    }
    started = true;
  }

  private Set<String> getOtherNodeNames() {
    return https.getNodeNames().stream()
        .filter(otherNodeName -> !otherNodeName.equals(this.name))
        .collect(Collectors.toSet())
        ;
  }

  public int size() {
    return chain.size();
  }

  public String getName() {
    return name;
  }

  public boolean isStarted() {
    return started;
  }

  @Override
  public String toString() {
    return "Node [chain=" + chain + ", name=" + name + ", started=" + started + "]";
  }

  public void mineBlock(String data) {
    logger.info(name + " mine block (" + data + ")");

    Set<String> nodeNames = getOtherNodeNames();

    var last = chain.get(chain.size() - 1);
    var previous = last.getHash();
    var block = miner.mineBlock(data, previous);
    if (chain.tryAdd(block)) {
      var fail = nodeNames.stream()
          .map(node -> https.tryAdd(node, block))
          .anyMatch(value -> !value)
          ;
      if (!fail) {
        chain.add(block);
        nodeNames.stream().forEach(node -> https.add(node, block));

        logger.info(name + " chain:" + chain);
      }
    } else {
      logger.info(name + " could not add new block");
    }
  }

  /**
   * First use tryAdd to lock chain!
   *
   * @param block
   * @return
   */
  boolean add(Block block) {
    if (chain.add(block)) {
      logger.info(name + " chain:" + chain);
      return true;
    }
    return false;
  }

  /**
   * Use this to lock chain then add.
   *
   * @param block
   * @return
   */
  boolean tryAdd(Block block) {
    return chain.tryAdd(block);
  }

  public Blockchain cloneBlockchain() {
    return chain.cloneSafe();
  }
}
