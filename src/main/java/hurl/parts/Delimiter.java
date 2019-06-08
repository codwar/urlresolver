/*
Copyright (c) 2009 McDowell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package hurl.parts;

import hurl.incidental.Arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defines a delimiter type. This class can be used to match multiple delimiters
 * in parsing, such as when supporting both '&' and ';' as parameter separators
 * in URL queries.
 * 
 * @author McDowell
 */
public final class Delimiter {
  private final String delimiter;
  private final Pattern pattern;

  /**
   * @param delimiter
   *          the delimiter for concatenating lists
   * @param delimiterPattern
   *          the pattern for matching delimiters
   */
  public Delimiter(String delimiter, Pattern delimiterPattern) {
    Arguments.assertNotNull(delimiter, delimiterPattern);
    this.delimiter = delimiter;
    pattern = delimiterPattern;
  }

  public String getDelimiter() {
    return delimiter;
  }

  public Pattern getPattern() {
    return pattern;
  }

  /**
   * Splits using the encapsulated pattern.
   * 
   * The sequence {@code "&hello&world"} split on the pattern {@code "&"} will
   * return a list containing the three strings {@code ""}, {@code "hello"},
   * {@code "world"}.
   * 
   * @param sequence
   *          the string to be split
   * @return the segments after the split
   */
  public List<String> split(String sequence) {
    List<String> list = new ArrayList<String>();
    Matcher matcher = pattern.matcher(sequence);
    int last = 0;
    while (matcher.find()) {
      list.add(sequence.substring(last, matcher.start()));
      last = matcher.end();
    }
    list.add(sequence.substring(last, sequence.length()));
    return Collections.unmodifiableList(list);
  }

  /**
   * Concatenates the provided objects to a string, inserting this object's
   * delimiter between them.
   * 
   * @param list
   *          the objects to concatenate
   * @return the resultant string
   */
  public String concatenate(Iterable<? extends Object> list) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Object o : list) {
      if (first) {
        first = false;
      } else {
        sb.append(delimiter);
      }
      sb.append(o);
    }
    return sb.toString();
  }
}
