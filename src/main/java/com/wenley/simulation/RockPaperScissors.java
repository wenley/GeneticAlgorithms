package com.wenley.simulation;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import com.wenley.genome.Genome;

public class RockPaperScissors {
  private static final int GAMES_PER_FIGHT = 100;

  public enum Move {
    ROCK('R'),
    PAPER('P'),
    SCISSORS('S');

    private static final Set<Move> moves = new HashSet<>();
    static {
      moves.add(ROCK);
      moves.add(PAPER);
      moves.add(SCISSORS);
    }

    private final char code;

    private Move(char code) {
      this.code = code;
    }

    public static Set<Move> moves() {
      return moves;
    }

    public static Set<Character> characterSet() {
      Set<Character> set = new HashSet<Character>(moves.stream().map(move -> move.code).collect(Collectors.toSet()));
      set.add(',');
      return set;
    }

    public static Move fromCode(char code) {
      switch (code) {
        case 'R':
          return ROCK;
        case 'P':
          return PAPER;
        case 'S':
          return SCISSORS;
        default:
          throw new RuntimeException("Unknown code: " + code);
      }
    }

    public int winsAgainst(Move that) {
      if (this.equals(that)) {
        return 0;
      }
      List<Move> moves = Arrays.asList(this.getClass().getEnumConstants());
      int delta = moves.indexOf(this) - moves.indexOf(that);
      delta = (delta + 3) % 3;

      // System.out.println("In moves: " + moves.toString());
      // System.out.println(this.code + " vs. " + that.code + " = " + delta);

      if (delta == 1) {
        return 1;
      } else if (delta == 0) {
        return 0;
      } else if (delta == 2) {
        return -1;
      } else {
        throw new RuntimeException("Weird fight outcome");
      }
    }
  }

  static class FightResult {
    int leftWins;
    int ties;
    int rightWins;
  }

  // TODO: Something to verify genomes are RPS genomes
  public static FightResult fight(Genome left, Genome right, Random random) {
    StringBuilder history = new StringBuilder();
    FightResult fightResult = new FightResult();
    for (int i = 0; i < GAMES_PER_FIGHT; i++) {
      char leftMove = left.getBehaviorFor(history.toString(), random);
      char rightMove = right.getBehaviorFor(history.toString(), random);

      int result = Move.fromCode(leftMove).winsAgainst(Move.fromCode(rightMove));
      if (result == 0) {
        fightResult.ties++;
      } else if (result == 1) {
        fightResult.leftWins++;
      } else {
        fightResult.rightWins++;
      }

      history.append(leftMove);
      history.append(rightMove);
    }
    return fightResult;
  }
}
