package com.wenley.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.wenley.genome.Genome;
import com.wenley.simulation.Population;
import com.wenley.simulation.RockPaperScissors;
import com.wenley.simulation.RockPaperScissors.FightResult;

public class Evolution {
  private static final int POPULATION_ITERATIONS = 100;
  private static final int POPULATION_SIZE = 100;
  private static final int PARENT_POPULATION_SIZE = 10;
  private static final long SEED = 123456;

  public void run() {
    Random random = new Random(SEED);
    Population initial = Population.of(RockPaperScissors.Move.moves(), POPULATION_SIZE, random);
    List<Population> iterations = new ArrayList<>();

    iterations.add(initial);

    Population lastPopulation = initial;
    for (int i = 1; i <= POPULATION_ITERATIONS; i++) {
      Map<Genome, Integer> wins = new HashMap<>();
      for (Genome genome : lastPopulation.genomes()) {
        wins.put(genome, 0);
        for (Genome opponent : lastPopulation.genomes()) {
          FightResult result = RockPaperScissors.fight(genome, opponent, random);
          int score = result.leftWins + result.ties / 3 + wins.get(genome);
          wins.put(genome, score);
        }
      }
      Population nextPopulation = lastPopulation.fromFittestParents(genome -> {
        return (double) wins.get(genome);
      }, PARENT_POPULATION_SIZE, random);
      iterations.add(nextPopulation);
      lastPopulation = nextPopulation;
      System.out.println("Finished running generation " + i);
    }

    System.out.println("Final Genomes:");
    for (Genome genome : lastPopulation.genomes()) {
      System.out.println(genome.getGenome());
    }
  }
}
