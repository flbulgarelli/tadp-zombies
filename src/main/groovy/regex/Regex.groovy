package regex;

import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;

class Regex {
  
  static require() {
    String.mixin(Regex)
  }
  
  static match(String string, block) {
    def binder = new Binder(value: string)
    binder.with(block)
    binder.eval()
  }
}

class Binder {
  def value
  def result = []

  def fallback(action) {
    if(result)
      return
    else
      action(value)
  }

  def when(expr, action) {
    if(result) return;
    def match = value =~ expr
    if(match)
      result = [action(_bind(match[0]))]
  }

  def _bind(List match) {
    match.tail()
  }
  def _bind(match) {
    match
  }

  def eval() {
    if(!result)
      throw new IllegalArgumentException("Pattern matching failure");
    result[0]
  }
}



