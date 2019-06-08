/*
Copyright (c) 2010 McDowell

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

package hurl.escape.prep;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ensures that a string matches a certain pattern of input.
 * 
 * @author McDowell
 * @since 1.1
 */
public final class ValidationPrep implements Prep {

  private final Pattern pattern;

  /**
   * @param pattern
   *          the pattern the input must match
   * @see Pattern#matcher(CharSequence)
   * @see Matcher#matches()
   */
  public ValidationPrep(Pattern pattern) {
    this.pattern = pattern;
  }

  /**
   * @param unpreparedValue
   *          the value to test
   * @return the argument, unaltered
   * @throws ProhibitedException
   *           if the input does not match the pattern
   */
  public String prepare(String unpreparedValue) throws ProhibitedException {
    if (!pattern.matcher(unpreparedValue).matches()) {
      throw new ProhibitedException(unpreparedValue);
    }
    return unpreparedValue;
  }
}
