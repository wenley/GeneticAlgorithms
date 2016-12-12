package com.wenley.genome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.wenley.util.MyLists;

public class Genome {
  private static final int CROSSING_FACTOR = 0;
  // TODO: Make this a Trie to enable better prefix matching
  // TODO: Allow repeats of same segment to weight probabilities
  private final String genome;
  private final Map<String, Character> behaviors = new HashMap<>();

  private Genome(String genome) {
    this.genome = genome;
    for (String behavior : genome.split(",")) {
      if (behavior.length() >= 1) {
        String history = behavior.substring(0, behavior.length() - 1);
        behaviors.put(history, behavior.charAt(behavior.length() - 1));
      }
    }
  }

  public static Genome of(String genome) {
    return new Genome(genome);
  }

  String getGenome() {
    return genome;
  }

  public char getBehaviorFor(String history, Random random) {
    List<Character> potential = new ArrayList<Character>();
    for (String key : behaviors.keySet()) {
      if (history.endsWith(key)) {
        potential.add(behaviors.get(key));
      }
    }

    return MyLists.sample(potential, random);
  }
}
