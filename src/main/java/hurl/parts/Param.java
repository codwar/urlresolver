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

import hurl.escape.EscapeHolder;
import hurl.escape.Escaper;
import hurl.incidental.Arguments;

/**
 * Type for representing a parameter in a HTTP URL (HTML application
 * conventions).
 * 
 * The form of a parameter is {@code foo=bar}. The equals character (=) is used
 * as a delimiter between the key and the value. The value part is optional and
 * may be null.
 * 
 * Immutable. Thread safe.
 * 
 * @author McDowell
 * @see hurl.build.QueryBuilder
 */
public final class Param implements EscapeHolder, Normalizing<Param>,
    ReEscaping<Param> {

  private final Escaper escaper;
  private final String unescapedKey;
  private final String unescapedValue;
  private final String escapedParam;

  /**
   * @param escaper
   *          for encoding/escaping the key/value
   * @param unescapedKey
   *          the unescaped form of the key; returned by {@link #getKey()}
   * @param unescapedValue
   *          the unescaped form of the value; may be null; returned by
   *          {@link #getValue()}
   */
  public Param(Escaper escaper, String unescapedKey, String unescapedValue) {
    Arguments.assertNotNull(escaper, unescapedKey);
    this.escaper = escaper;
    this.unescapedKey = unescapedKey;
    this.unescapedValue = unescapedValue;
    escapedParam = (unescapedValue == null) ? escaper.escape(unescapedKey)
        : escaper.escape(unescapedKey) + '=' + escaper.escape(unescapedValue);
  }

  /**
   * Example {@code encodedParam}: {@code "msg=Hello,%20World!"}. The
   * parameter is tokenized on the first equals ("=") character and split into a
   * key and value. If the equals character is not present, only the key is
   * decoded and the value becomes null.
   * 
   * The escaping/encoding scheme used by {@code encodedParam} must match that
   * of the {@link hurl.escape.Escaper}.
   * 
   * @param escaper
   *          the escaper
   * @param encodedParam
   *          the parameter to be parsed; returned by {@link #toString()}
   */
  public Param(Escaper escaper, String encodedParam) {
    Arguments.assertNotNull(escaper, encodedParam);
    this.escaper = escaper;
    escapedParam = encodedParam;
    int n = encodedParam.indexOf('=');
    if (n < 0) {
      unescapedKey = escaper.unescape(encodedParam);
      unescapedValue = null;
    } else {
      unescapedKey = escaper.unescape(encodedParam.substring(0, n));
      unescapedValue = escaper.unescape(encodedParam.substring(n + 1));
    }
  }

  /**
   * The unescaped key.
   * 
   * @return the key
   */
  public String getKey() {
    return unescapedKey;
  }

  /**
   * The unescaped value.
   * 
   * @return the value or null
   */
  public String getValue() {
    return unescapedValue;
  }

  /**
   * The escaped form of the parameter; e.g. {@code "msg=Hello,%20World!"}.
   */
  @Override
  public String toString() {
    return escapedParam;
  }

  public Escaper getEscaper() {
    return escaper;
  }

  public Param normalize() {
    Param normalized = new Param(escaper, unescapedKey, unescapedValue);
    if (escapedParam.equals(normalized.escapedParam)) {
      return this;
    }
    return normalized;
  }

  public Param reEscape(Escaper escaper) {
    return this.escaper.equals(escaper) ? this : new Param(escaper,
        unescapedKey, unescapedValue);
  }
}
