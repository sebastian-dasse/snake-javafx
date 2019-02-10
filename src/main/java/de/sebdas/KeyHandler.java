package de.sebdas;

class KeyHandler {
  private final SnakeGame snakeGame;
  private World world;
  private Decision decision;

  private enum Decision {
    YES      (true),
    NO       (false),
    UNDECIDED(false);

    private final boolean isTrue;

    Decision(final boolean isTrue) {
      this.isTrue = isTrue;
    }

    boolean isTrue() {
      return isTrue;
    }
  }

  KeyHandler(final SnakeGame snakeGame) {
    this.snakeGame = snakeGame;
    this.world = null;
    this.decision = Decision.UNDECIDED;
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
    final boolean userHasConfirmedRestart = askForConfirmation();
    if (userHasConfirmedRestart) {
      snakeGame.reset();
    }
  }

  void cancel() {
    final boolean userHasConfirmedCancellation = askForConfirmation();
    if (userHasConfirmedCancellation) {
      snakeGame.exit();
    }
  }
  void yes() {
    decision = Decision.YES;
  }

  void no() {
    decision = Decision.NO;
  }

  private boolean askForConfirmation() {
    world.togglePause();
    System.out.println("askForConfirmation (TODO)");
    return true;
  }
}
