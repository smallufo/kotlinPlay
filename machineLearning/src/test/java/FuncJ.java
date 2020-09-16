import kotlin.Triple;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kevin.huang on 2020-08-31.
 */

public class FuncJ {

  BiFunction<Integer, Integer, Triple<Integer, Integer, Integer>> biFun1 = (first, second) -> {
    System.out.println("calculating " + first + " * " + second);
    return new Triple<>(first, second, first * second);
  };


  BiFunction<Integer, List<Integer>, Stream<Triple<Integer, Integer, Integer>>> biFun2 = (Integer a, List<Integer> listB) -> listB.stream().map(b -> biFun1.apply(a, b));

  List<Integer> listA = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

  List<Integer> listB = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

  @Test
  public void test99() {
    Stream<Triple<Integer, Integer, Integer>> s = listA.stream().flatMap(a -> biFun2.apply(a, listB));

    s.filter(triple -> triple.getFirst() % 2 == 0).collect(Collectors.toList()).forEach(triple -> {
      Integer a = triple.component1();
      Integer b = triple.component2();
      Integer c = triple.component3();
      System.out.println(a + " * " + b + " = " + c);
    });
  }

  @Test
  public void testLazy() {
    List<Integer> list = listA.stream().map(i -> {
      //System.out.println("calculating " + i);
      return i*i;
    })
      .filter(i -> i%2 == 0)
      .peek(i -> System.out.println("peeking " + i))
      .collect(Collectors.toList());

    list.forEach(i -> System.out.println("i = " + i));
  }

}
