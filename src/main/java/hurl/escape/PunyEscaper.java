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

package hurl.escape;

import hurl.incidental.CodepointIterator;

/**
 * A Punycode (<a href="http://tools.ietf.org/html/rfc3492">RFC3492</a>)
 * implementation. Immutable. Thread safe.
 * 
 * @author McDowell
 * @deprecated this is a work in progress
 */
@Deprecated
public final class PunyEscaper implements Escaper {

  private static final int BASE = 36;
  private static final int TMIN = 1;
  private static final int TMAX = 26;
  private static final int SKEW = 38;
  private static final int DAMP = 700;
  private static final int INITIAL_BIAS = 72;
  private static final int INITIAL_N = 0x80;

  private static final char DELIMITER = '\u002D';

  /**
   * An instance of this class.
   */
  public static final Escaper INSTANCE = new PunyEscaper();

  public String escape(String unescaped) {
    final int length = unescaped.codePointCount(0, unescaped.length());
    int n = INITIAL_N;
    int delta = 0;
    int bias = INITIAL_BIAS;
    final int b = countBasic(unescaped);
    int h = b;

    StringBuilder out = new StringBuilder();
    // copy them to the output in order, followed by a delimiter if b > 0
    if (h > 0) {
      CodepointIterator iterator = new CodepointIterator(unescaped);
      char[] buffer = new char[1];
      while (iterator.hasNext()) {
        int codepoint = iterator.next();
        if (isBasic(codepoint)) {
          Character.toChars(codepoint, buffer, 0);
          out.append(buffer);
        }
      }
      out.append(DELIMITER);
    }

    while (h < length) {
      // the minimum {non-basic} code point >= n in the input
      int m = minNonBasicCodepoint(unescaped, n);
      delta = delta + (m - n) * (h + 1); // TODO: fail on overflow
      n = m;
      CodepointIterator iterator = new CodepointIterator(unescaped);
      while (iterator.hasNext()) {
        int c = iterator.next();
        // if c < n {or c is basic} then increment delta, fail on overflow
        if (c < n || isBasic(c)) {
          delta++;
        }
        if (c == n) {
          int q = delta;
          // for k = base to infinity in steps of base do begin
          for (int k = BASE; true; k += BASE) {
            // let t = tmin if k <= bias {+ tmin}, or
            // tmax if k >= bias + tmax, or k - bias otherwise
            int t;
            if (k <= (bias + TMIN)) {
              t = TMIN;
            } else if (k >= (bias + TMAX)) {
              t = TMAX;
            } else {
              t = k - bias;
            }
            if (q < t) {
              break;
            }
            // output the code point for digit t + ((q - t) mod (base - t))
            output(out, toDigit(t + ((q - t) % (BASE - t))));
            q = (q - t) / (BASE - t);
          }
          // output the code point for digit q
          output(out, toDigit(q));
          bias = adapt(delta, h + 1, h == b);
          delta = 0;
          h++;
        }
      }
      // increment delta and n
      delta++;
      n++;
    }

    return out.toString();
  }

  public String unescape(String escaped) {
    validateAllBasic(escaped);
    final int lastDelim = escaped.lastIndexOf(DELIMITER);
    // if (lastDelim < 0) {
    // return escaped;
    // }

    int n = INITIAL_N;
    int i = 0;
    int bias = INITIAL_BIAS;
    // let output = an empty string indexed from 0
    StringBuilder out = new StringBuilder();
    // consume all code points before the last delimiter (if there is one)
    // and copy them to output, fail on any non-basic code point
    // if more than zero code points were consumed then consume one more
    // (which will be the last delimiter)
    if (lastDelim > 0) {
      out.append(escaped, 0, lastDelim);
    }

    CodepointIterator input = new CodepointIterator(escaped
        .substring(lastDelim + 1));
    outer: while (true) {
      int oldi = i;
      int w = 1;
      // for k = base to infinity in steps of base do begin
      for (int k = BASE; true; k += BASE) {
        // consume a code point, or fail if there was none to consume
        if (!input.hasNext()) {
          break outer;
        }
        int codepoint = input.next();
        // let digit = the code point's digit-value, fail if it has none
        int digit = fromDigit(codepoint);
        i = i + digit * w;
        // let t = tmin if k <= bias {+ tmin}, or
        // tmax if k >= bias + tmax, or k - bias otherwise
        int t;
        if (k <= (bias + TMIN)) {
          t = TMIN;
        } else if (k >= bias + TMAX) {
          t = TMAX;
        } else {
          t = k - bias;
        }
        if (digit < t) {
          break;
        }
        w = w * (BASE - t);
      }
      bias = adapt(i - oldi, out.length() + 1, oldi == 0);
      n = n + i / (out.length() + 1);
      i = i % (out.length() + 1);
      // {if n is a basic code point then fail}
      if (isBasic(n)) {
        throw new IllegalArgumentException(escaped);
      }
      // insert n into output at position i
      out.insert(i, Character.toChars(n));
      i++;
    }

    return out.toString();
  }

  private void validateAllBasic(String input) {
    CodepointIterator iterator = new CodepointIterator(input);
    while (iterator.hasNext()) {
      if (!isBasic(iterator.next())) {
        throw new IllegalArgumentException(input);
      }
    }
  }

  private static int adapt(int delta, int numpoints, boolean firsttime) {
    delta /= firsttime ? DAMP : 2;
    delta = delta + (delta / numpoints);
    int k = 0;
    while (delta > ((BASE - TMIN) * TMAX) / 2) {
      delta = delta / (BASE - TMIN);
      k = k + BASE;
    }
    return k + (((BASE - TMIN + 1) * delta) / (delta + SKEW));
  }

  private static void output(StringBuilder builder, int codepoint) {
    builder.append(Character.toChars(codepoint));
  }

  private int countBasic(String input) {
    int count = 0;
    CodepointIterator iterator = new CodepointIterator(input);
    while (iterator.hasNext()) {
      int codepoint = iterator.next();
      if (isBasic(codepoint)) {
        count++;
      } else if (codepoint < INITIAL_N) {
        // {if the input contains a non-basic code point < n then fail}
        throw new IllegalArgumentException(Integer.toHexString(codepoint));
      }
    }
    return count;
  }

  private static boolean isBasic(int codepoint) {
    return codepoint >= 0 && codepoint <= 0x7F;
  }

  private int minNonBasicCodepoint(String input, int n) {
    CodepointIterator iterator = new CodepointIterator(input);
    int min = Integer.MAX_VALUE;
    while (iterator.hasNext()) {
      int codepoint = iterator.next();
      if (codepoint >= n && !isBasic(codepoint) && codepoint < min) {
        min = codepoint;
      }
    }
    return min;
  }

  private static int fromDigit(int codepoint) {
    if (codepoint >= 'A' && codepoint <= 'Z') {
      return codepoint - 'A';
    }
    if (codepoint >= 'a' && codepoint <= 'z') {
      return codepoint - 'a';
    }
    if (codepoint >= '0' && codepoint <= '9') {
      return codepoint - '0' + 26;
    }
    String str = new String(Character.toChars(codepoint));
    throw new IllegalArgumentException(String.format("U+%04x (%s)", codepoint,
        str));
  }

  private static int toDigit(int digit) {
    // TODO: case handling
    if (digit >= 0 && digit <= 25) {
      return 'a' + digit;
    }
    if (digit >= 26 && digit <= 35) {
      return '0' - 26 + digit;
    }
    throw new IllegalArgumentException(String.format(
        "0x%04x (value must be >= 0 <= 35)", digit));
  }
}
