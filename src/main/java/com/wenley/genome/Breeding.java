package com.wenley.genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.wenley.util.MyLists;

public class Breeding {
  // Note: Misalignment effectively does insertion mutations
  private static final double MISALIGN_FACTOR = 0.1;
  private static final double CROSSING_FACTOR = 0.05;
  private static final double MUTATION_RATE = 0.01;
  private static final double CHOPPING_RATE = 0.01;

  private final List<Character> validCharacters;
  private final Random random;

  public Breeding(Set<Character> validCharacters, Random random) {
    this.validCharacters = new ArrayList<>(validCharacters);
    this.random = random;
  }

  public Genome offspring(List<Genome> parentPopulation) {
    // Some possibility of self-breeding
    Genome left = MyLists.sample(parentPopulation, random);
    Genome right = MyLists.sample(parentPopulation, random);
    return breed(left, right);
  }

  public Genome breed(Genome left, Genome right) {
    // The alignment of right relative to left
    // If negative, right is aligned before left
    int misalignment = getMisalignment(random);

    boolean useLeft = true;
    int leftIndex = 0;
    int rightIndex = 0;

    if (misalignment < 0) {
      Genome temp = left;
      left = right;
      right = left;
      misalignment = -misalignment;
    }

    String crossedGenome = cross(left.getGenome().substring(0, misalignment), left.getGenome().substring(misalignment), right.getGenome(), random);
    String mutatedGenome = mutate(crossedGenome, random, validCharacters);
    String choppedGenome = chop(mutatedGenome, random);

    return Genome.of(choppedGenome);
  }

  /**
   * @return A number roughly normally distributed with mean 0
   */
  private static int getMisalignment(Random random) {
    // Get value in [-1, 1] to do misalignment calculations
    double sample = (random.nextDouble() - 0.5) * 2;
    int misalignment = 0;
    int step;

    if (sample > 0) {
      step = 1;
    } else {
      step = -1;
      sample = -sample;
    }

    while (sample < MISALIGN_FACTOR) {
      misalignment += 1;
      sample *= 10;
    }

    return misalignment;
  }

  private static String cross(String head, String left, String right, Random random) {
    int leftLength = left.length();
    int rightLength = right.length();

    if (leftLength == 0 && rightLength == 0) {
      return head;
    } else if (leftLength == 0) {
      return head + right;
    } else if (rightLength == 0) {
      return head + left;
    }

    boolean shouldCross = random.nextDouble() < CROSSING_FACTOR;

    if (shouldCross) {
      return cross(head + left.charAt(0), right.substring(1), left.substring(1), random);
    } else {
      return cross(head + left.charAt(0), left.substring(1), right.substring(1), random);
    }
  }

  // Randomly point-mutate some chunks of the genome
  private static String mutate(String genome, Random random, List<Character> validCharacters) {
    StringBuilder newGenome = new StringBuilder();

    genome.chars().forEach(c -> {
      if (random.nextDouble() < MUTATION_RATE) {
        newGenome.append(MyLists.sample(validCharacters, random));
      } else {
        newGenome.append((char) c);
      }
    });

    return newGenome.toString();
  }

  // Randomly remove some chunks of the genome
  private static String chop(String genome, Random random) {
    return genome;
  }
}
