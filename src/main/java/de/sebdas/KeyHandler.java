package de.sebdas;

class KeyHandler {
  private final World world;

  KeyHandler(final World world) {
    this.world = world;
  }

  void onLeft() {
    world.onLeft();
  }

  void onRight() {
    world.onRight();
  }

  void onUp() {
    world.onUp();
  }

  void onDown() {
    world.onDown();
  }

  void pause() {
    System.out.println("pause (TODO)");
  }

  void cancel() {
    System.out.println("cancel (TODO)");
  }
}
