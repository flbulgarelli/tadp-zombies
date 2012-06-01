package regex;

import static org.junit.Assert.*

import org.junit.Test
import org.junit.rules.ExpectedException;

import regex.Regex;
public class RegexTest {

  static { Regex.require() }  

  @Test
  public void testmatches() {
    assert "hello regex world".match {
      when(/(.*)regedx(.*)/) { h, w ->
        h + " " + w
      }
      when(/(.*) regex (.*)/) { h, w ->
        h + " " + w
      }
      when(/(.*)/) { it }
    } == "hello world"
  }
  
  @Test(expected = IllegalArgumentException)
  public void testName() throws Exception {
    "hello regex world".match {
      when(/(.*)regedx(.*)/) { h, w ->
        h + " " + w
      }
    } 
  }
  
}
