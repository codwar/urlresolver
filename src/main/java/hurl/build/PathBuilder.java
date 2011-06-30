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

import hurl.escape.EscapeHolder;
import hurl.escape.Escaper;
import hurl.parts.Delimiter;
import hurl.parts.DelimiterHolder;
import hurl.parts.Element;
import hurl.parts.EscapeState;
import hurl.parts.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for building the path part of a URL.
 * <p>
 * Mutable. Not thread safe.
 * 
 * @author McDowell
 */
public final class PathBuilder implements EscapeHolder, DelimiterHolder,
    Builder<Path>, Parser<PathBuilder> {

  private final Escaper escaper;
  private final Delimiter delimiter;
  private final ArrayList<String> elements = new ArrayList<String>();

  private PathBuilder(Escaper escaper, Delimiter delimiter) {
    this.escaper = escaper;
    this.delimiter = delimiter;
    elements.add("");
  }

  public Escaper getEscaper() {
    return escaper;
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }

  /**
   * The forward slash character '/' is reserved by the conventions of this API.
   * The provided escaper must escape these characters.
   * 
   * @param escaper
   *          the escaper to use
   * @param delimiter
   *          the delimiter to use
   * @return a new builder
   */
  public static PathBuilder create(Escaper escaper, Delimiter delimiter) {
    return new PathBuilder(escaper, delimiter);
  }

  /**
   * Creates a builder suitable for making path the part of a URL. This conforms
   * to RFC 3986.
   * 
   * @return a new builder
   * @see UrlDefaults#pathEscaper()
   * @see UrlDefaults#pathDelimiter()
   */
  public static PathBuilder create() {
    return create(UrlDefaults.pathEscaper(), UrlDefaults.pathDelimiter());
  }

  /**
   * @return true if the first element is an empty string
   * @see Path#hasLeadingSeparator()
   */
  public boolean hasLeadingSeparator() {
    return elements.size() > 0 && elements.get(0).length() == 0;
  }

  public PathBuilder setLeadingSeparator(boolean leadingSeparator) {
    boolean hasLeadingSeparator = hasLeadingSeparator();
    if (!hasLeadingSeparator && leadingSeparator) {
      elements.add(0, "");
    } else if (hasLeadingSeparator && !leadingSeparator) {
      elements.remove(0);
    }
    return this;
  }

  /**
   * @return true if the last element is an empty string
   * @see Path#hasTrailingSeparator()
   */
  public boolean hasTrailingSeparator() {
    return elements.size() > 0
        && elements.get(elements.size() - 1).length() == 0;
  }

  public PathBuilder setTrailingSeparator(boolean trailingSeparator) {
    boolean hasLeadingSeparator = hasTrailingSeparator();
    if (!hasLeadingSeparator && trailingSeparator) {
      elements.add(elements.size() - 1, "");
    } else if (hasLeadingSeparator && trailingSeparator) {
      elements.remove(elements.size() - 1);
    }
    return this;
  }

  public PathBuilder setPath(List<Element> newPath) {
    elements.clear();

    elements.ensureCapacity(newPath.size());
    for (Element element : newPath) {
      elements.add(element.getValue());
    }

    return this;
  }

  public PathBuilder setPath(Path path) {
    return setPath(path.getElements());
  }

  public PathBuilder parse(String path) {
    return setPath(new Path(escaper, delimiter, path));
  }

  public PathBuilder addElement(String unescaped) {
    elements.add(unescaped);
    return this;
  }

  public PathBuilder addElement(Element element) {
    return addElement(element.getValue());
  }

  public Path build() {
    List<Element> list = new ArrayList<Element>(elements.size());
    for (String element : elements) {
      list.add(new Element(escaper, element, EscapeState.UNESCAPED));
    }
    return new Path(escaper, delimiter, list);
  }

  /**
   * Creates an new URI with the path part replaced with the one specified by
   * the {@code QueryBuilder}. The encoding scheme of the URI must match that
   * used by this instance.
   * 
   * @param uri
   *          the uri
   * @return a new instance with the path set to the current state of this
   *         builder
   */

  /**
   * @see Path#toString()
   */
  @Override
  public String toString() {
    return build().toString();
  }
}
