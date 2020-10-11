package funsets

import org.junit._

/**
  * This class is a test suite for the methods in object FunSets.
  *
  * To run this test suite, start "sbt" then run the "test" command.
  */
class FunSetSuite {

  import FunSets._

  @Test def `contains is implemented`: Unit = {
    assert(contains(x => true, 100))
  }

  /**
    * When writing tests, one would often like to re-use certain values for multiple
    * tests. For instance, we would like to create an Int-set and have multiple test
    * about it.
    *
    * Instead of copy-pasting the code for creating the set into every test, we can
    * store it in the test class using a val:
    *
    *   val s1 = singletonSet(1)
    *
    * However, what happens if the method "singletonSet" has a bug and crashes? Then
    * the test methods are not even executed, because creating an instance of the
    * test class fails!
    *
    * Therefore, we put the shared values into a separate trait (traits are like
    * abstract classes), and create an instance inside each test method.
    */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
    * This test is currently disabled (by using @Ignore) because the method
    * "singletonSet" is not yet implemented and the test would fail.
    *
    * Once you finish your implementation of "singletonSet", remvoe the
    * @Ignore annotation.
    */
  @Test def `singleton set one contains one`: Unit = {

    /**
      * We create a new instance of the "TestSets" trait, this gives us access
      * to the values "s1" to "s3".
      */
    new TestSets {

      /**
        * The string argument of "assert" is a message that is printed in case
        * the test fails. This helps identifying which assertion failed.
        */
      assert(contains(s1, 1), "Singleton")
    }
  }

  @Test def `union contains all elements of each set`: Unit = {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  @Test def `intersect contains all elements in the intersection`: Unit = {
    new TestSets {
      val s12 = union(s1, s2)
      val s23 = union(s2, s3)
      val inter = intersect(s12, s23)

      assert(!contains(inter, 1), "Intersect 1")
      assert(contains(inter, 2), "Intersect 2")
      assert(!contains(inter, 3), "Intersect 3")
    }
  }

  @Test def `diff contains all of the first set that are not in the second`
      : Unit = {
    new TestSets {
      val s123 = union(union(s1, s2), s3)
      val d = diff(s123, s1)

      assert(!contains(d, 1), "Diff 1")
      assert(contains(d, 2), "Diff 2")
      assert(contains(d, 3), "Diff 3")
    }
  }

  @Test def `filter contains all elements in the set that hold for the predicate`
      : Unit = {
    new TestSets {
      val s123 = union(union(s1, s2), s3)
      val odds = filter(s123, n => n % 2 == 1)

      assert(contains(odds, 1), "Filter 1")
      assert(!contains(odds, 2), "Filter 2")
      assert(contains(odds, 3), "Filter 3")
    }
  }

  @Test def `forall returns if all elements in the set hold for the predicate`
      : Unit = {
    new TestSets {
      val s123 = union(union(s1, s2), s3)

      assert(forall(s123, _ < 5), "Forall _ < 5")
      assert(!forall(s123, _ < 3), "Forall _ < 3")
    }
  }

  @Test def `exists returns if any elements in the set holds for the predicate`
      : Unit = {
    new TestSets {
      val s123 = union(union(s1, s2), s3)

      assert(!exists(s123, _ < 1), "Exists _ < 1")
      assert(exists(s123, _ < 3), "Exists _ < 3")
    }
  }

  @Test def `map returns a transformed set`: Unit = {
    new TestSets {
      val s123 = union(union(s1, s2), s3)
      val doubled = map(s123, _ * 2)

      assert(!contains(doubled, 1), "Map 1")
      assert(contains(doubled, 2), "Map 2")
      assert(!contains(doubled, 3), "Map 3")
      assert(contains(doubled, 4), "Map 4")
    }
  }

  @Rule def individualTestTimeout = new org.junit.rules.Timeout(10 * 1000)
}
