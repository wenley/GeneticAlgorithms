package com.wenley.util;

import java.util.Random;

public class FunctionalRandom {
  public static class Result<T> {
    private final T value;
    private final FunctionalRandom next;

    private Result(T value, FunctionalRandom next) {
      this.value = value;
      this.next = next;
    }

    public T value() {
      return value;
    }

    public FunctionalRandom next() {
      return next;
    }
  }

  private final long seed;

  private FunctionalRandom(long seed) {
    this.seed = seed;
  }

  public static FunctionalRandom fromSeed(long seed) {
    return new FunctionalRandom(seed);
  }

  public Result<FunctionalRandom> split() {
    Random generator = new Random(seed);
    long leftSeed = generator.nextLong();
    long rightSeed = generator.nextLong();

    return new Result<FunctionalRandom>(new FunctionalRandom(leftSeed), new FunctionalRandom(rightSeed));
  }

  public Result<Long> nextLong() {
    Random generator = new Random(seed);
    long value = generator.nextLong();
    long nextSeed = generator.nextLong();

    return new Result<Long>(value, new FunctionalRandom(nextSeed));
  }
}
