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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Escapes strings as per {@code application/x-www-form-urlencoded}.
 * 
 * @author McDowell
 * @see java.net.URLEncoder#encode(String, String)
 * @see java.net.URLDecoder#decode(String, String)
 */
public final class FormEscaper implements Escaper {
  private final String encoding;

  /**
   * @param encoding
   */
  public FormEscaper(Charset encoding) {
    this.encoding = encoding.name();
  }

  public String escape(String unescaped) {
    try {
      return URLEncoder.encode(unescaped, encoding);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }

  public String unescape(String escaped) {
    try {
      return URLDecoder.decode(escaped, encoding);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }
}
