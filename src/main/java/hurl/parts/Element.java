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
 * Class for encapsulating an escaped, atomic part in a URL.
 * 
 * For an {@link Escaper} class that escapes {@code "Hello World"} as
 * {@code "Hello%20World"}, {@link #getValue()} returns "Hello World" and
 * {@link #toString()} returns "Hello%20World".
 * 
 * Immutable. Thread safe.
 * 
 * @author McDowell
 * @see hurl.build.FragmentBuilder
 * @see hurl.build.PathBuilder
 */
public final class Element implements EscapeHolder, Normalizing<Element>,
    ReEscaping<Element> {

  private final Escaper escaper;
  private final String unescaped;
  private final String escaped;

  /**
   * 
   * 
   * @param escaper
   *          the {@link Escaper} used to escape the value
   * @param pathElement
   *          the value encapsulated by this object
   * @param escapeState
   *          whether the pathElement is escaped or not
   */
  public Element(Escaper escaper, String pathElement, EscapeState escapeState) {
    Arguments.assertNotNull(escaper, pathElement, escapeState);
    this.escaper = escaper;
    if (escapeState == EscapeState.ESCAPED) {
      escaped = pathElement;
      unescaped = escaper.unescape(pathElement);
    } else {
      unescaped = pathElement;
      escaped = escaper.escape(pathElement);
    }
  }

  /**
   * @return the {@link Escaper} object
   */
  public Escaper getEscaper() {
    return escaper;
  }

  /**
   * @return the unescaped value
   */
  public String getValue() {
    return unescaped;
  }

  public Element normalize() {
    Element newElement = new Element(escaper, unescaped, EscapeState.UNESCAPED);
    if (newElement.getValue().equals(escaped)) {
      return this;
    }
    return newElement;
  }

  public Element reEscape(Escaper escaper) {
    return this.escaper.equals(escaper) ? this : new Element(escaper,
        unescaped, EscapeState.UNESCAPED);
  }

  /**
   * The escaped value.
   * 
   * @return the escaped value
   */
  @Override
  public String toString() {
    return escaped;
  }
}
