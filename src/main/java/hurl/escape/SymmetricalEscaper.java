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

import hurl.incidental.Arguments;

/**
 * Debugging escaper that throws an exception if the escaping/unescaping
 * operation is not symmetrical. This class can be used to detect character
 * corruption bugs.
 * 
 * @author McDowell
 */
public class SymmetricalEscaper implements Escaper, EscapeHolder {

  public static enum Action {
    ESCAPING, UNESCAPING
  }

  private final Escaper decorated;
  private volatile boolean enabled = true;

  public SymmetricalEscaper(Escaper decorated) {
    Arguments.assertNotNull(decorated);
    this.decorated = decorated;
  }

  public String escape(String unescaped) {
    String escaped = decorated.escape(unescaped);
    if (enabled) {
      if (!unescaped.equals(decorated.unescape(escaped))) {
        handleAsymetry(Action.ESCAPING, unescaped);
      }
    }
    return escaped;
  }

  public String unescape(String escaped) {
    String unescaped = decorated.unescape(escaped);
    if (enabled) {
      if (!escaped.equals(decorated.escape(unescaped))) {
        handleAsymetry(Action.ESCAPING, escaped);
      }
    }
    return unescaped;
  }

  /**
   * Throws an IllegalArgumentException. This method can be overriden to perform
   * some other action.
   * 
   * @param action
   *          whether the string is being escaped or unescaped
   * @param argument
   *          the argument passed to the escaper
   */
  protected void handleAsymetry(Action action, String argument) {
    throw new IllegalArgumentException(argument);
  }

  /**
   * When true, the decorated escaper is tested to check that escape/unescape
   * operations are symmetrical.
   * 
   * @return true by default
   */
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * The decorated Escaper.
   */
  public Escaper getEscaper() {
    return decorated;
  }
}
