package tech.intellispaces.ixora.cli.configuration;

import tech.intellispaces.ixora.cli.ConsolesCustomizer;
import tech.intellispaces.ixora.cli.MovableConsole;
import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Projection;

@Configuration
public class CliConfiguration {

  /**
   * Projection to module console.
   */
  @Projection
  public MovableConsole console() {
    return ConsolesCustomizer.get(System.out);
  }
}
