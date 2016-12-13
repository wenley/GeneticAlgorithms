
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

public abstract class CharacterEnum {
  private final char code;
  protected static final char separator = ',';

  protected CharacterEnum(char code) {
    this.code = code;
  }

  public static Set<CharacterEnum> instances() {
    throw new RuntimeException("Cannot call instances on base class CharacterEnum");
  }

  public static Set<Character> characterSet() {
    Set<Character> set = new HashSet<Character>(instances().stream().map(move -> move.code).collect(Collectors.toSet()));
    set.add(',');
    return set;
  }

  public static <T> T fromCode(char code) {
    return null;
  }
}
