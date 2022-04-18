package edu.byu.cs329.rd;

import java.util.Set;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;

public interface ReachingDefinitions {

  public Set<Definition> getReachingDefinitions(final Statement s);

  public static class Definition {
    public SimpleName name;
    public Statement statement;
  }
}
