package tech.intellispaces.ixora.internet.uri;

import java.net.URI;

public interface Uris {

  static Uri get(String string) {
    return new JavaUriWrapper(URI.create(string));
  }
}
