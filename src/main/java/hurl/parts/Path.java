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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the path part of a HTTP URL.
 * 
 * The form of a path is {@code /element/element}.
 * 
 * Leading and trailing separators are represented by empty elements.
 * 
 * @author McDowell
 * @see hurl.build.PathBuilder
 */
public final class Path implements EscapeHolder, DelimiterHolder,
    Normalizing<Path>, ReEscaping<Path> {

  private final Escaper escaper;
  private final Delimiter delimiter;
  private final List<Element> elements;
  private final String escapedPath;

  /**
   * @param escaper
   *          the escape mechanism
   * @param delimiter
   *          the delimiter to split the escaped path on
   * @param escapedPath
   *          the path to be parsed; this value will be returned by
   *          {@link #toString()}
   */
  public Path(Escaper escaper, Delimiter delimiter, String escapedPath) {
    this.escaper = escaper;
    this.delimiter = delimiter;
    this.escapedPath = escapedPath;
    // parse path
    List<String> pes = delimiter.split(escapedPath);
    List<Element> list = new ArrayList<Element>(pes.size());
    for (String pe : pes) {
      Element element = new Element(escaper, pe, EscapeState.ESCAPED);
      list.add(element);
    }
    elements = Collections.unmodifiableList(list);
  }

  /**
   * Creates a {@link Path} using the given elements. If the {@link Escaper}
   * encapsulated by an element is not the one provided to the constructor, the
   * element will be re-escaped.
   * 
   * @param escaper
   *          the escape mechanism
   * @param delimiter
   *          the delimiter to concatenate the escaped path on
   * @param pathElements
   *          the elements that make up the path
   */
  public Path(Escaper escaper, Delimiter delimiter, List<Element> pathElements) {
    this.escaper = escaper;
    this.delimiter = delimiter;
    // check path elements
    {
      List<Element> list = new ArrayList<Element>(pathElements.size());
      for (Element element : pathElements) {
        list.add(element.reEscape(escaper));
      }
      elements = Collections.unmodifiableList(list);
    }
    {
      escapedPath = delimiter.concatenate(elements);
    }
  }

  public Escaper getEscaper() {
    return escaper;
  }

  public List<Element> getElements() {
    return elements;
  }

  /**
   * Returns true if this path starts with a forward-slash (/). This method
   * would return true for these values: {@code "home/user/"} and
   * {@code "home/user/;sessionid=foo"}
   * 
   * @return true if this path ends with '/'
   */
  public boolean hasTrailingSeparator() {
    return escapedPath.endsWith(delimiter.getDelimiter());
  }

  /**
   * Returns true if this path starts with a forward-slash (/).
   * 
   * @return true if this path starts with '/'
   */
  public boolean hasLeadingSeparator() {
    return escapedPath.startsWith(delimiter.getDelimiter());
  }

  public Path normalize() {
    List<Element> normalizedElements = new ArrayList<Element>(elements.size());
    for (Element element : elements) {
      normalizedElements.add(element.normalize());
    }
    Path normalized = new Path(escaper, delimiter, normalizedElements);
    if (normalized.escapedPath.equals(escapedPath)) {
      return this;
    }
    return normalized;
  }

  public Path reEscape(Escaper escaper) {
    return this.escaper.equals(escaper) ? this : new Path(escaper, delimiter,
        elements);
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }

  /**
   * The escaped form of this path.
   */
  @Override
  public String toString() {
    return escapedPath;
  }
}
