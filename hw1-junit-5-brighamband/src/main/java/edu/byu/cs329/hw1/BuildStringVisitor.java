package edu.byu.cs329.hw1;

public class BuildStringVisitor extends ListNodeVisitor {

  private String value;

  public BuildStringVisitor() {
    value = "";
  }

  public String getResult() {
    return value;
  }

  @Override
  public boolean visit(CharacterNode n) {
    value += n.getCharacter();
    return true;
  }

  @Override
  public void endVisit(CharacterNode n) {
    value += n.getCharacter();
  }


}

