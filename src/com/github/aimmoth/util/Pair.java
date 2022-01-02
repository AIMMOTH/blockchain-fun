package com.github.aimmoth.util;

public class Pair<L, R> {

  public final L left;
  public final R right;

  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public static <L, R> Pair<L, R> pair(L left, R right) {
    return new Pair<L, R>(left, right);
  }
}
