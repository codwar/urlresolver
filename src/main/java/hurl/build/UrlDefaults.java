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

import hurl.escape.Escaper;
import hurl.escape.FormEscaper;
import hurl.escape.UriEscaper;
import hurl.escape.prep.PrepEscaperAdapter;
import hurl.escape.prep.ProhibitedException;
import hurl.escape.prep.ValidationPrep;
import hurl.parts.Delimiter;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Class for providing default mechanisms conforming to <a
 * href="http://tools.ietf.org/html/rfc3986">RFC 3986</a>.
 * 
 * @author McDowell
 */
public final class UrlDefaults {

  private UrlDefaults() {
  }

  private static final Escaper QUERY_ESCAPER = new FormEscaper(Charset
      .forName("UTF-8"));

  /**
   * Queries escaped with this code will be safe for the query part of RFC 3986
   * URIs (which defines the query as being unstructured); however, it escapes
   * as per {@code application/x-www-form-urlencoded} since query parts of this
   * form are in general use in Servlet containers.
   * 
   * @return the escaper
   * @see QueryBuilder
   */
  public static Escaper queryEscaper() {
    return QUERY_ESCAPER;
  }

  private static final Delimiter QUERY_DELIMITER = new Delimiter("&", Pattern
      .compile("&"));

  /**
   * The default delimiter used in queries. This delimiter splits on and
   * concatenates using the ampersand "&".
   * 
   * @return the delimiter
   * @see QueryBuilder
   */
  public static Delimiter queryDelimiter() {
    return QUERY_DELIMITER;
  }

  private static final Pattern PATH_UNSAFE = unsafePath();

  private static final Escaper PATH_ESCAPER = new UriEscaper(Charset
      .forName("UTF-8"), PATH_UNSAFE);

  /**
   * This escaper escapes unsafe path parts as defined in RFC 3986.
   * 
   * It will also escape "%" and "/", which are reserved by the conventions of
   * URI percentage escaping and the {@link PathBuilder}.
   * 
   * @return an escaper for escaping path parts
   */
  public static Escaper pathEscaper() {
    return PATH_ESCAPER;
  }

  /**
   * The set of characters not allowed in a URI query part (defined by RFC3986
   * and RFC2396) including '/' and '%' which are reserved by the conventions of
   * this API.
   * 
   * @return unsafe character matcher
   */
  private static Pattern unsafePath() {
    // RFC3986 allowed in query part (excluding escaped and alphanumeric)
    String unreserved = "-" + "." + "_" + "~";
    String subDelims = "!" + "$" + "&" + "'" + "(" + ")" + "*" + "+" + ","
        + ";" + "=";
    String pchar = unreserved + subDelims + ":" + "@";

    // reserve '%' and '/'
    String safe = BuilderUtil.remove(pchar, '%', '/');

    // regex
    String unsafe = "[^\\p{Alnum}" + BuilderUtil.regexQuoted(safe) + "]++";
    return Pattern.compile(unsafe);
  }

  private static final Delimiter PATH_DELIMITER = new Delimiter("/", Pattern
      .compile(Pattern.quote("/")));

  /**
   * The default delimiter used in paths. This delimiter splits on and
   * concatenates using the forward-slash "/".
   * 
   * @return the delimiter
   * @see PathBuilder
   */
  public static Delimiter pathDelimiter() {
    return PATH_DELIMITER;
  }

  private static final Pattern UNSAFE_FRAGMENT = unsafeFragment();

  /**
   * The set of characters not allowed in a URI query part (defined by RFC3986)
   * which are reserved by the conventions of this API.
   * 
   * @return unsafe character matcher
   */
  private static Pattern unsafeFragment() {
    // RFC3986 allowed in query part (excluding escaped and alphanumeric)
    String unreserved = "-" + "." + "_" + "~";
    String subDelims = "!" + "$" + "&" + "'" + "(" + ")" + "*" + "+" + ","
        + ";" + "=";
    String pchar = unreserved + subDelims + ":" + "@";
    String fragment = pchar + "/" + "?";

    // regex
    String unsafe = "[^\\p{Alnum}" + BuilderUtil.regexQuoted(fragment) + "]++";
    return Pattern.compile(unsafe);
  }

  private static final Escaper FRAGMENT_ESCAPER = new UriEscaper(Charset
      .forName("UTF-8"), UNSAFE_FRAGMENT);

  /**
   * Escaper that conforms to the fragment part of RFC 3986.
   * 
   * @return the escaper
   * @see FragmentBuilder
   */
  public static Escaper fragmentEscaper() {
    return FRAGMENT_ESCAPER;
  }

  private static final Pattern HTML4ID = Pattern
      .compile("|[A-Za-z][A-Za-z0-9\\-_:\\.]+");

  private static final Escaper HTML4_FRAGMENT_ESCAPER = new PrepEscaperAdapter(
      new ValidationPrep(HTML4ID));

  /**
   * An alternative {@link Escaper} to use with {@link FragmentBuilder}. This
   * class ensures that the fragment parts conform to the <a
   * href="http://www.w3.org/TR/html4/types.html#type-name">restrictions</a>
   * laid out by the HTML 4 spec. It will not transform strings, but any string
   * passed to {@link Escaper#escape(String)} that does not conform will cause a
   * {@link ProhibitedException} to be thrown.
   * 
   * @return the escaper
   * @see FragmentBuilder#create(Escaper)
   * @see hurl.escape.prep.ProhibitedException
   * @since 1.1
   */
  public static Escaper html4FragmentEscaper() {
    return HTML4_FRAGMENT_ESCAPER;
  }
}
