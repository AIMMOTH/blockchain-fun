# Blockchain Fun

Inspired by Baeldung blog [link](https://www.baeldung.com/java-blockchain)

## Run

Add JUnit 5 to modulepath and run com.github.aimmoth.Simulator.main()

## Parts

**Block** contains data, timestamp, nonce and previous hash. These 4 fields creates a hash. The miner generates hash until it starts with "000". This can be changed by setting prefix to a bigger number (but will take longer to mine).

**Blockchain** contains list of Block with some verifying. To add, the chain need to be blocked by using tryAdd first.

Create first **Node** with Node.createFirstNode(). Node contains Blockchain and list of other node names. To add, use tryAdd first to lock the chain.

All "communication" goes through a single instance of **Https**
