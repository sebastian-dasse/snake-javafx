package de.sebdas;

class KeyHandler {
  private final SnakeGame snakeGame;
  private World world;

  KeyHandler(final SnakeGame snakeGame, final World world) {
    this.snakeGame = snakeGame;
    this.world = world;
  }

  void setWorld(final World world) {
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

  void togglePause() {
    world.togglePause();
  }

  void restart() {
    snakeGame.reset();
  }

  void cancel() {
    System.out.println("cancel (TODO)");
  }
}
