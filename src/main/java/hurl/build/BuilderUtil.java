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

package hurl.build;

import hurl.incidental.CodepointIterator;

import java.util.regex.Pattern;

class BuilderUtil {

  /**
   * Quotes a bunch of individual characters for regex.
   */
  public static String regexQuoted(String characters) {
    StringBuilder sb = new StringBuilder();
    CodepointIterator iterator = new CodepointIterator(characters);
    while (iterator.hasNext()) {
      String quoteMe = new String(Character.toChars(iterator.next()));
      sb.append(Pattern.quote(quoteMe));
    }
    return sb.toString();
  }

  public static String remove(String sequence, char... c) {
    for (char ch : c) {
      sequence = sequence.replace(Character.toString(ch), "");
    }
    return sequence;
  }
}
