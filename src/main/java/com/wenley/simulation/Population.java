package com.wenley.simulation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import com.wenley.genome.Breeding;
import com.wenley.genome.Genome;
import com.wenley.util.MyLists;

public class Population {

  private final Set<Genome> population;
  private final Set<Character> validCharacters;

  private Population(Set<Genome> population, Set<Character> bases) {
    this.population = population;
    this.validCharacters = bases;
  }

  public static Population of(Set<Character> validCharacters, int size, Random random) {
    Set<Genome> population = new HashSet<>();
    for (int i = 0; i < size; i++) {
      population.add(randomGenome(validCharacters, 5, random));
    }

    return new Population(population, validCharacters);
  }

  private static Genome randomGenome(Set<Character> validCharacters, int length, Random random) {
    StringBuilder genome = new StringBuilder();
    List<Character> characters = new ArrayList<>();

    for (Character c : validCharacters) {
      characters.add(c);
    }

    for (int i = 0; i < length; i++) {
      genome.append(MyLists.sample(characters, random));
    }

    return Genome.of(genome.toString());
  }

  public Iterable<Genome> genomes() {
    return population;
  }

  public Population fromFittestParents(Function<Genome, Double> fitness, int numParents, Random random) {
    Breeding breeder = new Breeding(validCharacters, random);
    List<Genome> parents = population.stream()
      .sorted(new Comparator<Genome>() {
        public int compare(Genome one, Genome two) {
          double x = fitness.apply(one);
          double y = fitness.apply(two);

          if (x < y) { return -1; }
          else if (x > y) { return 1; }
          else { return 0; }
        }
      }).limit(numParents)
      .collect(Collectors.toList());

    Set<Genome> newPopulation = new HashSet<>();
    for (int i = 0; i < population.size(); i++) {
      // Some possibility of self-breeding
      Genome left = MyLists.sample(parents, random);
      Genome right = MyLists.sample(parents, random);
      newPopulation.add(breeder.breed(left, right));
    }

    return new Population(newPopulation, validCharacters);
  }
}
