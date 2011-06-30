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

import hurl.build.QueryBuilder;
import hurl.escape.EscapeHolder;
import hurl.escape.Escaper;
import hurl.incidental.Arguments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Type for representing a HTTP URL query in HTML applications.
 * 
 * The form of a query is {@code param|param|param} with parameters being
 * separated by delimiters.
 * 
 * Immutable. Thread safe.
 * 
 * @author McDowell
 * @see QueryBuilder
 */
public final class Query implements EscapeHolder, DelimiterHolder,
    Normalizing<Query>, ReEscaping<Query> {
  private final Escaper escaper;
  private final String encodedQuery;
  private final List<Param> params;
  private final Delimiter delimiter;

  /**
   * The encoded form of the query must conform to the escape mechanism
   * provided. The ampersand ("&amp;") character is used to split the query into
   * its component parameters.
   * 
   * @param escaper
   *          the escape mechanism
   * @param delimiter
   *          the delimiter
   * @param encodedQuery
   *          the query encoded as a string; cannot be null but may be an empty
   *          string; this value will be returned by {@link #toString()}
   */
  public Query(Escaper escaper, Delimiter delimiter, String encodedQuery) {
    Arguments.assertNotNull(escaper, delimiter, encodedQuery);
    this.escaper = escaper;
    this.delimiter = delimiter;
    this.encodedQuery = encodedQuery;
    List<Param> list;
    if (encodedQuery.length() == 0) {
      list = Collections.emptyList();
    } else {
      List<String> params = delimiter.split(encodedQuery);
      list = new ArrayList<Param>(params.size());
      for (String param : params) {
        list.add(new Param(escaper, param));
      }
    }
    params = Collections.unmodifiableList(list);
  }

  private List<Param> copyToListSafely(Collection<? extends Param> input) {
    ArrayList<Param> output = new ArrayList<Param>(input.size());
    for (Param param : input) {
      output.add(param.reEscape(escaper));
    }
    return output;
  }

  /**
   * If a parameter in the list use a different escaper to the one specified,
   * the parameter will be re-encoded from its unescaped value.
   * 
   * @param escaper
   *          the escape mechanism to encode the parameters
   * @param delimiter
   *          the delimiter for concatenation
   * @param queryParams
   *          the list of parameters
   */
  public Query(Escaper escaper, Delimiter delimiter,
      Collection<? extends Param> queryParams) {
    Arguments.assertNotNull(escaper, delimiter, queryParams);
    this.escaper = escaper;
    this.delimiter = delimiter;
    params = copyToListSafely(queryParams);
    encodedQuery = delimiter.concatenate(params);
  }

  /**
   * All parameters.
   * 
   * @return an immutable list of {@link Param} objects; never null
   */
  public List<Param> getParams() {
    return params;
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }

  /**
   * Returns the first {@link Param} matching the key.
   * 
   * @param unescapedKey
   *          the unescaped key
   * @return the value or null
   * @see Param#getKey()
   */
  public Param findParam(String unescapedKey) {
    for (Param param : params) {
      if (unescapedKey.equals(param.getKey())) {
        return param;
      }
    }
    return null;
  }

  /**
   * Returns the first non-null value from a {@link Param} with a matching key.
   * 
   * @param unescapedKey
   *          the unescaped key
   * @return the value or null
   * @see Param#getKey()
   */
  public String findValue(String unescapedKey) {
    for (Param param : params) {
      if (param.getValue() != null && unescapedKey.equals(param.getKey())) {
        return param.getValue();
      }
    }
    return null;
  }

  /**
   * Returns all {@link Param} objects matching the given key.
   * 
   * @param unescapedKey
   *          the unescaped key
   * @return an immutable list of parameters; never null
   * @see Param#getKey()
   */
  public List<Param> findParams(String unescapedKey) {
    List<Param> values = new ArrayList<Param>();
    for (Param param : params) {
      if (unescapedKey.equals(param.getKey())) {
        values.add(param);
      }
    }
    return Collections.unmodifiableList(values);
  }

  /**
   * Returns all values matching the given key. Any {@link Param} with a null
   * value is omitted.
   * 
   * @param unescapedKey
   * @return an immutable list of parameters; never null
   * @see Param#getKey()
   */
  public List<String> findValues(String unescapedKey) {
    List<String> values = new ArrayList<String>(params.size());
    for (Param param : params) {
      if (unescapedKey.equals(param.getKey()) && param.getValue() != null) {
        values.add(param.getValue());
      }
    }
    return Collections.unmodifiableList(values);
  }

  /**
   * Gets an immutable set of the parameter keys.
   * 
   * @return the set of parameter keys
   */
  public Set<String> getParamKeys() {
    Set<String> keys = new HashSet<String>();
    for (Param param : params) {
      keys.add(param.getKey());
    }
    return Collections.unmodifiableSet(keys);
  }

  /**
   * Determines if there is a matching parameter.
   * 
   * @param unescapedKey
   *          the key to match
   * @return true if there is a {@link Param} with a matching key
   */
  public boolean hasParam(String unescapedKey) {
    return findParam(unescapedKey) != null;
  }

  public Escaper getEscaper() {
    return escaper;
  }

  /**
   * Normalizes the query into a standard escaped form.
   * 
   * @param sort
   *          if true, parameters are sorted into an internally defined order
   * @return a new instance
   */
  public Query normalize(boolean sort) {
    Collection<Param> normalizedParams = sort ? new TreeSet<Param>(
        ParamSorter.INSTANCE) : new ArrayList<Param>(params.size());
    for (Param param : params) {
      normalizedParams.add(param.normalize());
    }
    Query normalized = new Query(escaper, delimiter, normalizedParams);
    if (normalized.encodedQuery.equals(encodedQuery)) {
      return this;
    }
    return normalized;
  }

  /**
   * Normalizes with parameter sorting.
   * 
   * @see #normalize(boolean)
   */
  public Query normalize() {
    return normalize(true);
  }

  public Query reEscape(Escaper escaper) {
    return this.escaper.equals(escaper) ? this : new Query(escaper, delimiter,
        params);
  }

  private static class ParamSorter implements Comparator<Param> {
    public static final Comparator<Param> INSTANCE = new ParamSorter();

    public int compare(Param param1, Param param2) {
      int comp = param1.getKey().compareTo(param2.getKey());
      if (comp == 0) {
        return param1.getValue().compareTo(param2.getValue());
      }
      return comp;
    }
  }

  /**
   * Returns the query as an escaped string; e.g.
   * {@code "msg=Hello,%20World!&msg=Hello,%20World!"}
   */
  @Override
  public String toString() {
    return encodedQuery;
  }
}
