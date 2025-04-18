package tech.intellispaces.ixora.http;

import tech.intellispaces.jaquarius.object.reference.MovableObjectHandle;
import tech.intellispaces.jaquarius.system.Modules;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractInboundHttpPortTest {

  private static final int PORT_NUMBER = 8080;

  public abstract MovableInboundHttpPort createPort(
      int portNumber, MovableObjectHandle<?> overlyingHandle
  );

  public void init() {
    Modules.load(TestHttpPortExchangeGuideImpl.class).start();
  }

  public void deinit() {
    Modules.unload();
  }

  public void testHello() throws Exception {
    MovableTestPortHandle testPort = TestPorts.create(overlyingHandle -> this.createPort(PORT_NUMBER, overlyingHandle));
    testPort.open();
    HttpResponse<String> res = callServer();
    String message = res.body();
    testPort.shut();
    assertThat(message).isEqualTo("Hello");
  }

  private HttpResponse<String> callServer() throws Exception {
    var req = java.net.http.HttpRequest.newBuilder()
        .uri(new URI("http://localhost:" + PORT_NUMBER))
        .GET()
        .build();
    return HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
  }
}
