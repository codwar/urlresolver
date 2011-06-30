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
import hurl.incidental.Arguments;
import hurl.parts.Element;
import hurl.parts.EscapeState;

/**
 * Class for building the fragment part of a URL.
 * 
 * @author McDowell
 */
public final class FragmentBuilder implements EscapeHolder, Builder<Element>,
    Parser<FragmentBuilder> {

  private final Escaper escaper;
  private String unescaped;

  private FragmentBuilder(Escaper escaper) {
    Arguments.assertNotNull(escaper);
    this.escaper = escaper;
  }

  public Escaper getEscaper() {
    return escaper;
  }

  public static FragmentBuilder create(Escaper escaper) {
    return new FragmentBuilder(escaper);
  }

  /**
   * Creates a new {@code FragmentBuilder} suitable for encoding RFC 3986 URLs.
   * 
   * Note: the default escaper does NOT ensure conformity to the rules laid down
   * by HTML 4. Use {@link #create(Escaper)} with
   * {@link UrlDefaults#html4FragmentEscaper()} for enforcing HTML 4 rules.
   * 
   * @return a new builder
   * @see UrlDefaults#fragmentEscaper()
   */
  public static FragmentBuilder create() {
    return create(UrlDefaults.fragmentEscaper());
  }

  /**
   * Builds the fragment.
   * 
   * @return a new {@link Element}
   */
  public Element build() {
    return new Element(escaper, unescaped, EscapeState.UNESCAPED);
  }

  /**
   * Parses the given fragment part. The escape scheme of the argument must
   * match that of the {@link Escaper}.
   * 
   * @param escapedValue
   *          the fragment part of a URL
   * @return this
   */
  public FragmentBuilder parse(String escapedValue) {
    unescaped = escaper.unescape(escapedValue);
    return this;
  }

  /**
   * Sets the value.
   * 
   * @param unescaped
   *          the unescaped value
   * @return this
   */
  public FragmentBuilder setValue(String unescaped) {
    this.unescaped = unescaped;
    return this;
  }

  /**
   * Returns the value of the builder.
   * 
   * @return the unescaped value encapsulated by this builder
   */
  public String getValue() {
    return unescaped;
  }

  /**
   * Sets the value from the {@link Element}. The {@link Escaper} used by the
   * argument need not be the same as the one used by this builder.
   * 
   * @param value
   *          the value
   * @return this
   */
  public FragmentBuilder setValue(Element value) {
    return setValue(value.getValue());
  }

  /**
   * @see Element#toString()
   */
  @Override
  public String toString() {
    return build().toString();
  }
}
