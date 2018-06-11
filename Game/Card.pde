
class Card {
  //Variables
  private float x, y, z, cardWidth, cardHeight, cardCornerRoudness;
  private float textWidth, textHeight, textX, textY;
  private float targetX = 0, targetY = 0;
  private color instrColor;
  private int numInstr;
  private boolean isMovingIn, isMovingUp, isMovingDown, isMovingDiagonal, isUp;
  
  //Constructor
  public Card(float x, float y, int numInstr) {
    this.x = x;
    this.y = y;
    this.numInstr = numInstr;
    cardCornerRoudness = 18;
    isMovingIn = isMovingUp = isMovingDown = isMovingDiagonal = isUp = false;
    if (numInstr == 0) {
      instrColor = color(150, 204, 222);
    } else if (numInstr == 1) {
      instrColor = color(240, 240, 150);
    } else if (numInstr == 2) {
      instrColor = color(195, 210, 165);
    } else if (numInstr == 3) {
      instrColor = color(240, 210, 222);
    } else if (numInstr == 4) {
      instrColor = color(186, 186, 216);
    } else if (numInstr == 5) {
      instrColor = color(240, 198, 135);
    }
  }
  
  //Getters and Setters
  public float getX() {return this.x;}
  
  public float getY() {return this.y;}
  
  public void setWidth(float cardWidth) {
    this.cardWidth = cardWidth;
    textWidth = cardWidth*90/100;
    textX = x + cardWidth*5/100; //Each card has a 5% margin
  }
  
  public void setHeight(float cardHeight) {
    this.cardHeight = cardHeight;
    textHeight = cardHeight*90/100;
    textY = y + cardHeight*5/100;
  }
  
  public void setTargetX(float targetX) {this.targetX = targetX;}
  
  public void setTargetY(float targetY) {this.targetY = targetY;}
  
  public void setMovingIn(boolean isMovingIn, float targetX) {
    this.isMovingIn = isMovingIn;
    this.targetX = targetX;
  }
  
  public void setMovingUp(boolean isMovingUp) {
    this.isMovingUp = isMovingUp;
    targetY = y - (cardHeight - cardWidth*5/100);
  }
  
  public void setMovingDown(boolean isMovingDown) {
    this.isMovingDown = isMovingDown;
    targetY = y + (cardHeight - cardWidth*5/100);
  }
  
  public void setMovingDiagonal(boolean isMovingDiagonal) {
    this.isMovingDiagonal = isMovingDiagonal;
  }
  
  //Move the card
  public void updatePosition() {
    if (targetX != 0 || targetY != 0) {
      if (isMovingIn) {
        x += 21;
        textX += 21;
        if (x >= targetX) {
          targetX = 0;
          targetY = 0;
          isMovingIn = false;
        }
      }
      if (isMovingUp) {
        if (!isUp) {
          y -= 12;
          textY -= 12;
          if (y <= targetY) {
            isUp = true;
          }
        }
        if (isUp) {
          targetX = 0;
          targetY = 0;
          isMovingUp = false;
        }
      }
      if (isMovingDown) {
        if (isUp) {
          y += 12;
          textY += 12;
          if (y >= targetY) {
            isUp = false;
          }
        }
        if (!isUp) {
          targetX = 0;
          targetY = 0;
          isMovingDown = false;
        }
      }
      if (isMovingDiagonal) {
        x++;
        y--;
        textX++;
        textY--;
        if (targetX == x && targetY == y) {
          targetX = 0;
          targetY = 0;
          isMovingDiagonal = false;
          if (isUp) {
            setMovingDown(true);
          }
        }
      }
    }
  }

  //Show the card
  void show() {
    fill(instrColor);
    noStroke();
    rect(x, y, cardWidth, cardHeight, cardCornerRoudness);
    String str = "";
    if (numInstr == 0) {
      str = "1. Use the mouse to choose a ball and click on 'Start playing' to start the game.";
    } else if (numInstr == 1) {
      str = "2. The goal of this game is to compete against another player and get the highest score.";
    } else if (numInstr == 2) {
      str = "3. You will get a point every time the ball touches the opposite player's wall.";
    } else if (numInstr == 3) {
      str = "4. Every game lasts 3 minutes, but you never know how much time you have left.";
    } else if (numInstr == 4) {
      str = "5. Control your paddle to hit the ball. Player1 uses the keys Q and E. Player2 uses the arrow keys.";
    } else if (numInstr == 5) {
      str = "6. The ball will get random speed each time it touches one of the paddles.";
    }
    fill(36);
    textSize(30);
    textAlign(CENTER, CENTER);
    text(str, textX, textY, textWidth, textHeight);
  }
}
