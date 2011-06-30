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

import java.net.URI;

/**
 * Convenience class for manipulating URIs. This class does no encoding/escaping
 * itself; values must be "raw" as defined in the {@code java.net.URI} class.
 * 
 * Mutable. Not thread safe.
 * 
 * @author McDowell
 * @see java.net.URI
 */
public final class UriBuilder implements Builder<URI>, Parser<UriBuilder> {

  private String scheme;
  private String userInfo;
  private String host;
  private int port = -1;
  private String path;
  private String query;
  private String fragment;

  private UriBuilder(String scheme, String userInfo, String host, Integer port,
      String path, String query, String fragment) {
    setAll(scheme, userInfo, host, port, path, query, fragment);
  }

  private void setAll(String scheme, String userInfo, String host,
      Integer port, String path, String query, String fragment) {
    this.scheme = scheme;
    this.userInfo = userInfo;
    this.host = host;
    this.port = (port == null) ? -1 : port.intValue();
    this.path = path;
    this.query = query;
    this.fragment = fragment;
  }

  public static UriBuilder create(String scheme, String userInfo, String host,
      Integer port, String path, String query, String fragment) {
    return new UriBuilder(scheme, userInfo, host, port, path, query, fragment);
  }

  public static UriBuilder create(URI uri) {
    return create(uri.getScheme(), uri.getRawUserInfo(), uri.getHost(), uri
        .getPort(), uri.getRawPath(), uri.getRawQuery(), uri.getRawFragment());
  }

  public static UriBuilder create(String uri) {
    return create(URI.create(uri));
  }

  /**
   * Creates an empty builder populated with null values.
   */
  public static UriBuilder create() {
    return create(null, null, null, null, null, null, null);
  }

  public String getScheme() {
    return scheme;
  }

  /**
   * Sets the value to the toString value of the argument.
   * 
   * @param scheme
   *          an object whose toString value forms the scheme part; may be null
   * @return this
   */
  public UriBuilder setScheme(Object scheme) {
    this.scheme = (scheme == null) ? null : scheme.toString();
    return this;
  }

  public String getUserInfo() {
    return userInfo;
  }

  /**
   * Sets the value to the toString value of the argument.
   * 
   * @param userInfo
   *          an object whose toString value forms the user info part; may be
   *          null
   * @return this
   */
  public UriBuilder setUserInfo(Object userInfo) {
    this.userInfo = (userInfo == null) ? null : userInfo.toString();
    return this;
  }

  public String getHost() {
    return host;
  }

  /**
   * Sets the value to the toString value of the argument.
   * 
   * @param host
   *          an object whose toString value forms the host part; may be null
   * @return this
   */
  public UriBuilder setHost(Object host) {
    this.host = (host == null) ? null : host.toString();
    return this;
  }

  /**
   * Returns the port or -1 if not set.
   * 
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * @param port
   *          the port number or -1 to unset
   * @return this
   */
  public UriBuilder setPort(int port) {
    this.port = (port < 0) ? -1 : port;
    return this;
  }

  public String getPath() {
    return path;
  }

  /**
   * Sets the value to the toString value of the argument.
   * 
   * @param path
   *          an object whose toString value forms the fragment part; may be
   *          null
   * @return this
   */
  public UriBuilder setPath(Object path) {
    this.path = (path == null) ? null : path.toString();
    return this;
  }

  public String getQuery() {
    return query;
  }

  /**
   * Sets the value to the toString value of the argument.
   * 
   * @param query
   *          an object whose toString value forms the query part; may be null
   * @return this
   */
  public UriBuilder setQuery(Object query) {
    this.query = (query == null) ? null : query.toString();
    return this;
  }

  public String getFragment() {
    return fragment;
  }

  /**
   * Sets the value to the toString value of the argument.
   * 
   * @param fragment
   *          an object whose toString value forms the fragment part; may be
   *          null
   * @return this
   */
  public UriBuilder setFragment(Object fragment) {
    this.fragment = (fragment == null) ? null : fragment.toString();
    return this;
  }

  public URI build() {
    return URI.create(toString());
  }

  public UriBuilder parse(String uriString) {
    URI uri = URI.create(uriString);
    setAll(uri.getScheme(), uri.getRawUserInfo(), uri.getHost(), uri.getPort(),
        uri.getRawPath(), uri.getRawQuery(), uri.getRawFragment());
    return this;
  }

  /**
   * Returns the URI as a string.
   */
  @Override
  public String toString() {
    int len = len(scheme, 1) + len(userInfo, 2) + len(host, 2) + 6
        + len(path, 1) + len(query, 1) + len(fragment, 1);
    StringBuilder sb = new StringBuilder(len);
    // see URI javadoc for this process
    // yes, all this is necessary
    if (notEmpty(scheme)) {
      sb.append(scheme).append(":");
    }
    if (notEmpty(userInfo) || notEmpty(host) || port >= 0) {
      sb.append("//");
    }
    if (notEmpty(userInfo)) {
      sb.append(userInfo);
      sb.append("@");
    }
    if (notEmpty(host)) {
      sb.append(host);
    }
    if (port >= 0) {
      sb.append(":");
      sb.append(port);
    }
    if (notEmpty(path)) {
      if (sb.length() > 0 && !path.startsWith("/")) {
        sb.append("/");
      }
      sb.append(path);
    }
    if (notEmpty(query)) {
      sb.append("?");
      sb.append(query);
    }
    if (notEmpty(fragment)) {
      sb.append("#");
      sb.append(fragment);
    }
    return sb.toString();
  }

  private boolean notEmpty(String s) {
    return s != null && (s.length() > 0);
  }

  private int len(String s, int extra) {
    return (s == null) ? 0 : s.length() + extra;
  }
}
