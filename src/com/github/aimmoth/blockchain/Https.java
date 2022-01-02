package com.github.aimmoth.blockchain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Https {

  private final Logger logger = Logger.getLogger(Https.class.getName());

  private Map<String, Node> nodeNameToNode = new HashMap<>();

  public Set<String> getNodeNames() {
    return nodeNameToNode.keySet();
  }

  public void addNode(Node node) {
    logger.info("addNode");
    if (!nodeNameToNode.containsKey(node.getName())) {
      nodeNameToNode.put(node.getName(), node);
    } else {
      throw new IllegalArgumentException("Node name already exists!");
    }
  }

  public boolean isStarted(String nodeName) {
    logger.info(nodeName + " isStarted");
    return nodeNameToNode.get(nodeName).isStarted();
  }

  public Integer getSize(String nodeName) {
    logger.info(nodeName + " size");
    return nodeNameToNode.get(nodeName).size();
  }

  public Blockchain getChain(String nodeName) {
    logger.info(nodeName + " getChain");
    return nodeNameToNode.get(nodeName).cloneBlockchain();
  }

  public boolean tryAdd(String nodeName, Block block) {
    logger.info(nodeName + " tryAdd");
    return nodeNameToNode.get(nodeName).tryAdd(block);
  }

  public boolean add(String nodeName, Block block) {
    logger.info(nodeName + " add");
    return nodeNameToNode.get(nodeName).add(block);
  }
}
