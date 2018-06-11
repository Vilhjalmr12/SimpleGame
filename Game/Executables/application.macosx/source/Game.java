import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Game extends PApplet {


//Import


//Global variables
//General
private String showW = "MENU"; //Tells if has to show menu, help or game
private SoundFile hitWallSound, pointSound, finishSound, menuClickedSound, startGameSound;
private int time, playingTime = 180000; // <-- 3 Minutes
private boolean gameOn = true, right1On, right2On, left1On, left2On;
private int scoreP1, scoreP2;

//Balls
private PShape ball, earth, jupiter, mars, mercury, neptune, pluto, saturn;
private PShape sun, uranus, venus;
private PImage earthTexture, jupiterTexture, marsTexture; 
private PImage mercuryTexture, neptuneTexture, plutoTexture, saturnTexture;
private PImage sunTexture, uranusTexture, venusTexture;
private float xPos, yPos, xMove, yMove;
private int ballRadius = 24;
private float chosenBallX, chosenBallY;

//Paddles
private PShape paddle1, paddle2;
private PImage paddleTexture;
private int xPosPaddle1, xPosPaddle2, yPosPaddle1, yPosPaddle2, paddleSpeed = 12;
private int paddleWidth;

//Arena
private PShape wallTop, wallRight, wallBottom, wallLeft;
private PImage backgroundImg, wallsTexture;
private int arenaWidth, arenaHeight, minX, minY, maxX, maxY;
private int wallWidth = 24, wallHeight = 24;
private float angle = PI/4, density = 1, addedDensity = 0.3f;

//Help Cards
private int numInstr, numMaxCards, currentInstr, nextClick;
private float firstCardX, firstCardY, distanceCards;
private float cardWidth, cardHeight;
private ArrayList<Card> cards;

public void setup() { 
   
  //fullScreen(P3D, 0);
  //surface.setResizable(true);
  backgroundImg = loadImage("background.jpg");
  hitWallSound = new SoundFile(this, "hitWallSound.wav");
  pointSound = new SoundFile(this, "pointSound.wav");
  finishSound = new SoundFile(this, "finishSound.wav");
  menuClickedSound = new SoundFile(this, "menuClicked.wav");
  startGameSound = new SoundFile(this, "startGameSound.wav");
  
  noStroke();
  arenaWidth = width*75/100;
  arenaHeight = height*90/100;
  minX = (width/2-width*10/100) - ((arenaWidth/2) - wallWidth);
  maxX = (width/2-width*10/100) + ((arenaWidth/2) - wallWidth);
  minY = (height*5/100) + wallWidth;
  maxY = (height*5/100) + (arenaHeight - wallWidth);
  right1On = right2On = left1On = left2On = false;
  
  chosenBallX = width/6;
  chosenBallY = height*54/100;
  
  //Initialize paddles
  xPosPaddle1 = arenaWidth/2;
  xPosPaddle2 = arenaWidth/2;
  yPosPaddle1 = height*12/100;
  yPosPaddle2 = height*88/100;
  paddleWidth = 90;
  paddle1 = createShape(BOX, paddleWidth, 24, 24);
  paddleTexture = loadImage("metal2.jpg");
  paddle1.setTexture(paddleTexture);
  paddle2 = createShape(BOX, paddleWidth, 24, 24);
  paddle2.setTexture(paddleTexture);
  
  //Initialize walls
  wallsTexture = loadImage("metal3.jpg");
  wallTop = createShape(BOX, arenaWidth - 75, wallWidth, wallHeight);
  wallTop.setTexture(wallsTexture);
  wallRight = createShape(BOX, wallWidth, arenaHeight - 75, wallHeight);
  wallRight.setTexture(wallsTexture);
  wallBottom = createShape(BOX, arenaWidth - 75, wallWidth, wallHeight);
  wallBottom.setTexture(wallsTexture);
  wallLeft = createShape(BOX, wallWidth, arenaHeight - 75, wallHeight);
  wallLeft.setTexture(wallsTexture);
  
  //Initialize balls
  earth = createShape(SPHERE, ballRadius*1.5f);
  earthTexture = loadImage("earth.jpg");
  earth.setTexture(earthTexture);
  jupiter = createShape(SPHERE, ballRadius*1.5f);
  jupiterTexture = loadImage("jupiter.jpg");
  jupiter.setTexture(jupiterTexture);
  mars = createShape(SPHERE, ballRadius*1.5f);
  marsTexture = loadImage("mars.jpg");
  mars.setTexture(marsTexture);
  mercury = createShape(SPHERE, ballRadius*1.5f);
  mercuryTexture = loadImage("mercury.jpg");
  mercury.setTexture(mercuryTexture);
  neptune = createShape(SPHERE, ballRadius*1.5f);
  neptuneTexture = loadImage("neptune.jpg");
  neptune.setTexture(neptuneTexture);
  pluto = createShape(SPHERE, ballRadius*1.5f);
  plutoTexture = loadImage("pluto.jpg");
  pluto.setTexture(plutoTexture);
  saturn = createShape(SPHERE, ballRadius*1.5f);
  saturnTexture = loadImage("saturn.jpg");
  saturn.setTexture(saturnTexture);
  sun = createShape(SPHERE, ballRadius*1.5f);
  sunTexture = loadImage("sun.jpg");
  sun.setTexture(sunTexture);
  uranus = createShape(SPHERE, ballRadius*1.5f);
  uranusTexture = loadImage("uranus.jpg");
  uranus.setTexture(uranusTexture);
  venus = createShape(SPHERE, ballRadius*1.5f);
  venusTexture = loadImage("venus.jpg");
  venus.setTexture(venusTexture);
  
  ball = createShape(SPHERE, ballRadius);
  ball.setTexture(earthTexture);
  
  //Initialize cards
  firstCardX = width*9/100; //All are % to make them relative to screen size
  firstCardY = height*48/100;
  cardWidth = width*75/100;
  cardHeight = height*33/100;
  distanceCards = 9; //Distance in pixels between the cards (x and y)
  nextClick = 900; //Miliseconds until keyboard listener is active again
  numInstr = 6;
  numMaxCards = 6;
}

public void draw() {
  //ambient(250, 100, 100);
  //ambientLight(40, 20, 40);
  //lightSpecular(255, 215, 215);
  pointLight(255, 255, 240, maxX, height/4, 150);
  pointLight(255, 255, 240, minX, 3*height/4, 150);
  shininess(255.0f);
  specular(255, 255, 240);
  if (showW.equals("MENU")) {
    drawMenu();
  } else if (showW.equals("HELP")) {
    drawHelp();
  } else if (showW.equals("GAME")) {
    drawGame();
  }
}



/*-------------------------------LISTENERS-------------------------------
------------------------------------------------------------------------*/

//Runs when left mouse button is clicked
public void mouseClicked(){    
  if(showW.equals("MENU")){
    menuMouseListener();
  } else if(showW.equals("GAME")){
    gameMouseListener();
  } else if(showW.equals("HELP")){
    helpMouseListener();
  }
}

public void keyPressed() {
  if(showW.equals("GAME")){
    gameKeyPressedListener();
  } else if(showW.equals("HELP")){
    helpKeyPressedListener();
  }
}

public void keyReleased() {
  if(showW.equals("GAME")){
    gameKeyReleasedListener();
  }
} 


/*-------------------------------MENU-------------------------------------
------------------------------------------------------------------------*/

public void drawMenu() {
  backgroundImg.resize(width, height);
  background(backgroundImg);
  pushMatrix(); //Storing the origin
  sphereDetail(150);
  translate(width/6, height*54/100, -ballRadius);
  rotateY(angle);
  shape(earth);
  popMatrix();
  pushMatrix();
  translate(width*2/6, height*54/100, -ballRadius);
  rotateY(angle);
  shape(jupiter);
  popMatrix();
  pushMatrix();
  translate(width*3/6, height*54/100, -ballRadius);
  rotateY(angle);
  shape(mars);
  popMatrix();
  pushMatrix();
  translate(width*4/6, height*54/100, -ballRadius);
  rotateY(angle);
  shape(mercury);
  popMatrix();
  pushMatrix();
  translate(width*5/6, height*54/100, -ballRadius);
  rotateY(angle);
  shape(neptune);
  popMatrix();
  pushMatrix();
  translate(width/6, height*54/100 + width/6, -ballRadius);
  rotateY(angle);
  shape(pluto);
  popMatrix();
  pushMatrix();
  translate(width*2/6, height*54/100 + width/6, -ballRadius);
  rotateY(angle);
  shape(saturn);
  popMatrix();
  pushMatrix();
  translate(width*3/6, height*54/100 + width/6, -ballRadius);
  rotateY(angle);
  shape(sun);
  popMatrix();
  pushMatrix();
  translate(width*4/6, height*54/100 + width/6, -ballRadius);
  rotateY(angle);
  shape(uranus);
  popMatrix();
  pushMatrix();
  translate(width*5/6, height*54/100 + width/6, -ballRadius);
  rotateY(angle);
  shape(venus);
  angle += 0.015f;
  strokeWeight(3);
  stroke(180, 180, 210);
  popMatrix();
  pushMatrix();
  translate(width/2 - (width*36/100)/2, height*84/100, 0);
  fill(90, 90, 120, 75);
  rect(0, 0, width*36/100, height*10/100, 12);
  fill(180, 180, 210);
  textSize(32);
  textAlign(CENTER, CENTER);
  text("Start Playing", 0, 0, width*36/100, height*9/100);
  noFill();
  popMatrix();
  pushMatrix();
  translate(chosenBallX, chosenBallY, -ballRadius);
  ellipse(0, 0, ballRadius*3.3f, ballRadius*3.3f);
  popMatrix();
  pushMatrix();
  translate((maxX + (width - maxX)/2) - (width*9/100)/3, height*5/100, 0);
  fill(90, 90, 120, 75);
  rect(0, 0, width*9/100, height*4/100, 9);
  fill(180, 180, 210);
  textSize(18);
  textAlign(CENTER, CENTER);
  text("HELP", 0, 0, width*9/100, height*3.5f/100);
  popMatrix();
}

public void menuMouseListener() {
  //check if start button is clicked
  if((mouseX >= (width/2 - (width*36/100)/2)) 
      && (mouseX <= width/2 + (width*36/100)/2)){
    if((mouseY >= height*84/100) 
      && (mouseY <= height*84/100 + height*10/100)){ 
        startGameSound.play();
        xPos = 360;
        yPos = 240;
        xMove = 3;
        yMove = 3;
        scoreP1 = scoreP2 = 0;
        time = millis(); //Store the current time
        showW = "GAME";
    }
  }
  //check if help button is clicked
  if((mouseX >= ((maxX + (width - maxX)/2) - (width*9/100)/3)) 
      && (mouseX <= ((maxX + (width - maxX)/2) - (width*9/100)/3) + width*9/100)) {
    if((mouseY >= height*5/100) 
      && (mouseY <= height*5/100 + height*4/100)){
        currentInstr = -1;
        cards = new ArrayList<Card>();
        menuClickedSound.play();
        showW = "HELP";
    }
  }
  //Check if the planets are clicked
  if (mouseY >= (height*54/100 - ballRadius) 
     && mouseY <= (height*54/100 + ballRadius)) {
       if ((mouseX >= width/6 - ballRadius) 
         && (mouseX <= width/6 + ballRadius)) {
            chosenBallX = width/6;
            chosenBallY = height*54/100;
            ball.setTexture(earthTexture);
            menuClickedSound.play();
       }
       if ((mouseX >= width*2/6 - ballRadius) 
         && (mouseX <= width*2/6 + ballRadius)) {
            chosenBallX = width*2/6;
            chosenBallY = height*54/100;
            ball.setTexture(jupiterTexture);
            menuClickedSound.play();
       }
       if ((mouseX >= width*3/6 - ballRadius) 
         && (mouseX <= width*3/6 + ballRadius)) {
            chosenBallX = width*3/6;
            chosenBallY = height*54/100;
            ball.setTexture(marsTexture);
            menuClickedSound.play();
       }
       if ((mouseX >= width*4/6 - ballRadius) 
         && (mouseX <= width*4/6 + ballRadius)) {
            chosenBallX = width*4/6;
            chosenBallY = height*54/100;
            ball.setTexture(mercuryTexture);
            menuClickedSound.play();
       }
       if ((mouseX >= width*5/6 - ballRadius) 
         && (mouseX <= width*5/6 + ballRadius)) {
            chosenBallX = width*5/6;
            chosenBallY = height*54/100;
            ball.setTexture(neptuneTexture);
            menuClickedSound.play();
       }
  }
  if (mouseY >= ((height*54/100 + width/6) - ballRadius) 
     && mouseY <= ((height*54/100 + width/6) + ballRadius)) {
       if ((mouseX >= width/6 - ballRadius) 
         && (mouseX <= width/6 + ballRadius)) {
            chosenBallX = width/6;
            chosenBallY = (height*54/100 + width/6);
            ball.setTexture(plutoTexture);
            menuClickedSound.play();
       }
       if ((mouseX >= width*2/6 - ballRadius) 
         && (mouseX <= width*2/6 + ballRadius)) {
            chosenBallX = width*2/6;
            chosenBallY = (height*54/100 + width/6);
            ball.setTexture(saturnTexture);
            menuClickedSound.play();
       }
       if ((mouseX >= width*3/6 - ballRadius) 
         && (mouseX <= width*3/6 + ballRadius)) {
            chosenBallX = width*3/6;
            chosenBallY = (height*54/100 + width/6);
            ball.setTexture(sunTexture);
            menuClickedSound.play();
       }
       if ((mouseX >= width*4/6 - ballRadius) 
         && (mouseX <= width*4/6 + ballRadius)) {
            chosenBallX = width*4/6;
            chosenBallY = (height*54/100 + width/6);
            ball.setTexture(uranusTexture);
            menuClickedSound.play();
       }
       if ((mouseX >= width*5/6 - ballRadius) 
         && (mouseX <= width*5/6 + ballRadius)) {
            chosenBallX = width*5/6;
            chosenBallY = (height*54/100 + width/6);
            ball.setTexture(venusTexture);
            menuClickedSound.play();
       }
  }
}


/*-------------------------------GAME-------------------------------------
------------------------------------------------------------------------*/

public void drawGame() {
  pushMatrix();
  backgroundImg.resize(width, height);
  background(backgroundImg);
  if(millis() - time >= playingTime){
    finishGame();
  }
  drawArena();
  drawRightSide();
  drawPaddles();  
  //Move and draw the ball
  drawBall();
  popMatrix();
}

public void drawArena() {
  popMatrix();
  pushMatrix();
  translate(minX-wallWidth, minY-wallWidth, -ballRadius*2);
  fill(240, 240, 255, 36);
  noStroke();
  rect(0, 0, arenaWidth, arenaHeight, 12); //Last parameter, rounded corners
  popMatrix();
  pushMatrix();
  translate((width/2)-(width*10/100), height*5/100, -ballRadius*2);
  shape(wallTop);
  translate(-(arenaWidth/2), (height*90/100)/2, 0);
  shape(wallLeft);
  translate(((width*75/100)/2), (height*90/100)/2, 0);
  shape(wallBottom);
  translate((arenaWidth/2), - (height*90/100)/2, 0);
  shape(wallRight);
}

public void drawRightSide() {
  popMatrix();
  pushMatrix();
  stroke(180, 180, 210);
  strokeWeight(3);
  translate((maxX + (width - maxX)/2) - (width*9/100)/3, height*5/100, 0);
  fill(90, 90, 120, 75);
  rect(0, 0, width*9/100, height*4/100, 9);
  fill(180, 180, 210);
  textSize(18);
  textAlign(CENTER, CENTER);
  text("MENU", 0, 0, width*9/100, height*3.5f/100);
  fill(240);
  translate(0, height*6/100, 0);
  textSize(54);
  textAlign(CENTER, CENTER);
  text(Integer.toString(scoreP1), 0, 0, width*9/100, height*12/100);
  translate(0, height*12/100, 0);
  textSize(54);
  textAlign(CENTER, CENTER);
  text(Integer.toString(scoreP2), 0, 0, width*9/100, height*12/100);
}

public void drawBall() {
  popMatrix(); //Return translation to origin
  pushMatrix(); //Storing the origin
  sphereDetail(36);
  translate(xPos, yPos, -ballRadius);
  rotateX(angle);
  shape(ball);
  //If the ball touches x borders
  if (xPos+ballRadius >= maxX || xPos-ballRadius <= minX) {
    xMove *= -1;
    hitWallSound.play();
  }
  //If the ball touches y borders (score point)
  if (yPos+ballRadius >= maxY) {
    yMove *= -1;
    pointSound.play();
    scoreP1++;
  }
  if (yPos-ballRadius <= minY) {
    yMove *= -1;
    pointSound.play();
    scoreP2++;
  }
  //If the ball touches second paddle  
  else if (yPos+ballRadius+wallWidth-3 >= yPosPaddle2) {
      //If the ball touches the top of the paddle
      if (xPos >= xPosPaddle2-(paddleWidth/2) 
      && xPos <= xPosPaddle2+(paddleWidth/2)) {
        yMove = random(-12, -7);
        hitWallSound.play();
      //If the ball touches left side of the paddle
      } else if (xPos+ballRadius >= xPosPaddle2-(paddleWidth/2+3) 
            && xPos+ballRadius <= xPosPaddle2+(paddleWidth/2-3)) {
              if (yPos >= yPosPaddle2-wallWidth/2) {
                xMove = -6;
                //xPos -= 3;
              } else {
                yMove = random(-12, -7);
                xMove = random(-9, -4);
                //yPos -= 3;
              }  
              hitWallSound.play();
      //If the ball touches right side of the paddle
      } else if (xPos-ballRadius <= xPosPaddle2+(paddleWidth/2-3) 
            && xPos-ballRadius >= xPosPaddle2-(paddleWidth/2+3)) {
              if (yPos >= yPosPaddle2-wallWidth/2) {
                xMove = 6;
                //xPos += 3;
              } else {
                yMove = random(-12, -7);
                xMove = random(5, 10);
                //yPos -= 3;
              }
              hitWallSound.play();
      }
  //If the ball touches first paddle      
  } else if (yPos-ballRadius-wallWidth+3 <= yPosPaddle1) {
    //If the ball touches the top of the paddle
      if (xPos >= xPosPaddle1-(paddleWidth/2) && xPos <= xPosPaddle1+(paddleWidth/2)) {
        yMove = random(6, 12);
        hitWallSound.play();
      //If the ball touches left side of the paddle
      } else if (xPos+ballRadius >= xPosPaddle1-(paddleWidth/2+3) 
            && xPos+ballRadius <= xPosPaddle1+(paddleWidth/2-3)) {
              if (yPos <= yPosPaddle1+wallWidth/2) {
                xMove = -6;
                //xPos -= 3;
              } else {
                yMove = random(6, 12);
                xMove = random(-9, -4);
                //yPos += 3;
              }  
              hitWallSound.play();
      //If the ball touches right side of the paddle
      } else if (xPos-ballRadius <= xPosPaddle1+(paddleWidth/2-3) 
            && xPos-ballRadius >= xPosPaddle1-(paddleWidth/2+3)) {
              if (yPos <= yPosPaddle1+wallWidth/2) {
                xMove = 6;
                //xPos += 3;
              } else {
                yMove = random(6, 12);
                xMove = random(5, 10);
                //yPos += 3;
              }
              hitWallSound.play();
      }
  } 
  //Move the ball
  xPos += xMove;
  yPos += yMove;
  angle += 0.01f;
}

public void drawPaddles(){
  //Move the paddle only in case it's still within parameters
  if (right1On && (xPosPaddle1 + paddleWidth/2) < maxX) {
    xPosPaddle1 += paddleSpeed;
  }
  if (left1On && (xPosPaddle1 - paddleWidth/2) > minX) {
    xPosPaddle1 -= paddleSpeed;
  }
  if (right2On && (xPosPaddle2 + paddleWidth/2) < maxX) {
    xPosPaddle2 += paddleSpeed;
  }
  if (left2On && (xPosPaddle2 - paddleWidth/2) > minX) {
    xPosPaddle2 -= paddleSpeed;
  }
  popMatrix(); //Return translation to origin
  pushMatrix(); //Storing the origin
  translate(xPosPaddle1, yPosPaddle1, -ballRadius*2);
  shape(paddle1);

  popMatrix();
  pushMatrix();
  translate(xPosPaddle2, yPosPaddle2, -ballRadius*2);
  shape(paddle2);
}

public void gameKeyPressedListener() {
  if (key == 'q') {
    if (!left1On) {
      left1On = true;
      right1On = false;
    }
  } 
  if (key == 'e'){
    if (!right1On) {
      left1On = false;
      right1On = true;
    }
  }
  if (key == CODED) {
    if (keyCode == LEFT) {
      if (!left2On) {
        left2On = true;
        right2On = false;
      }
    } 
    if (keyCode == RIGHT) {
      if (!right2On) {
        left2On = false;
        right2On = true;
      }
    } 
  }
}

public void gameKeyReleasedListener() {
  //The behaviour of a held key can differ deppending of the OS.
  //With this method we can control it on our own
  if (key == 'q') {
    left1On = false;
  } 
  if (key == 'e'){
    right1On = false;
  }
  if (key == CODED) {
    if (keyCode == LEFT) {
      left2On = false;
    } 
    if (keyCode == RIGHT) {
      right2On = false;
    } 
  }
}

public void gameMouseListener() {
  //check if menu button is clicked
  if((mouseX >= ((maxX + (width - maxX)/2) - (width*9/100)/3)) 
      && (mouseX <= ((maxX + (width - maxX)/2) - (width*9/100)/3) + width*9/100)) {
    if((mouseY >= height*5/100) 
      && (mouseY <= height*5/100 + height*4/100)){ 
        menuClickedSound.play();
        xPos = 360;
        yPos = 240;
        xMove = 3;
        yMove = 3;
        showW = "MENU";
    }
  }
}

public void finishGame() {
  popMatrix();
  pushMatrix();
  translate(width/2-width*10/100, height/2, -ballRadius*2);
  for (int r = (maxX - 2*minX); r > 0; --r) {
    noFill();
    stroke(240, 240, 255, density);
    ellipse(0, 0, r, r);
    if (density >= 36 || density <= -r+240) {
      addedDensity *= -1;
    }
    density += addedDensity;
  }
  fill(3, 3, 3);
  ellipse(0, 0, 24, 24);
  if (gameOn) {
    gameOn = false;
    xMove = 0;
    yMove = 0;
    finishSound.play();
  }
  popMatrix();
  pushMatrix();
  translate((maxX-minX)/2 - width*14/100, height*30/100, 0);
  fill(90, 90, 120);
  noStroke();
  rect(0, 0, width*40/100, height*10/100, 9);
  fill(180, 180, 210);
  textSize(27);
  textAlign(CENTER, CENTER);
  String winner = "Winner: ";
  if (scoreP1 > scoreP2) {
    winner = "Winner: Player 1!";
  } else if (scoreP1 < scoreP2) {
    winner = "Winner: Player 2!";
  } else {
    winner = "Draw!";
  }
  text(winner, 0, 0, width*40/100, height*9.3f/100);
}



/*-------------------------------HELP-------------------------------------
------------------------------------------------------------------------*/

public void drawHelp() {
  backgroundImg.resize(width, height);
  background(backgroundImg);
  for (int i=0; i<cards.size(); ++i) {
    cards.get(i).updatePosition();
    cards.get(i).show();
  }
  pushMatrix();
  stroke(180, 180, 210);
  strokeWeight(3);
  translate((maxX + (width - maxX)/2) - (width*9/100)/3, height*5/100, 0);
  fill(90, 90, 120, 75);
  rect(0, 0, width*9/100, height*4/100, 9);
  fill(180, 180, 210);
  textSize(18);
  textAlign(CENTER, CENTER);
  text("MENU", 0, 0, width*9/100, height*3.5f/100);
  popMatrix();
  pushMatrix();
  translate(width*5/100, height*87/100, 0);
  fill(90, 90, 120);
  noStroke();
  rect(0, 0, width*90/100, height*9/100, 9);
  fill(180, 180, 210);
  textSize(21);
  textAlign(CENTER, CENTER);
  text("Use SPACE/ENTER to see the next card. You can read a previous card by clicking on it", 0, 0, width*90/100, height*9/100);
  popMatrix();
}

public void helpKeyPressedListener() {
  if (key == ' ' || key == '\n') {
    if(millis() - time >= nextClick) {
      menuClickedSound.play();
      currentInstr = (currentInstr + 1) % numInstr;
      Card newCard = new Card(-cardWidth, firstCardY, currentInstr);
      newCard.setWidth(cardWidth);
      newCard.setHeight(cardHeight);
      newCard.setMovingIn(true, firstCardX);
      if (cards.size() == numMaxCards) {
        cards.remove(0);
      }
      for (int i=0; i<cards.size(); i++) {
        cards.get(i).setTargetX(cards.get(i).getX() + distanceCards);
        cards.get(i).setTargetY(cards.get(i).getY() - distanceCards);
        cards.get(i).setMovingDiagonal(true);
      }
      cards.add(newCard);
      time = millis();
    }
  }
}

public void helpMouseListener() {
  //Check if menu button is clicked
  if((mouseX >= ((maxX + (width - maxX)/2) - (width*9/100)/3)) 
      && (mouseX <= ((maxX + (width - maxX)/2) - (width*9/100)/3) + width*9/100)) {
    if((mouseY >= height*5/100) 
      && (mouseY <= height*5/100 + height*4/100)){ 
        menuClickedSound.play();
        showW = "MENU";
    }
  }
  //Check if a card is pressed
  if(millis() - time >= nextClick) {
    for (int i=0; i<cards.size()-1; i++) {
      if (mouseX > (cards.get(i).getX() + cardWidth - distanceCards)) {
        if (mouseX <= (cards.get(i).getX() + cardWidth)) {
          if (mouseY >= (cards.get(i).getY())) {
            if (mouseY < (cards.get(i).getY() + cardHeight)) {
              for (int j=0; j<cards.size()-1; j++) {
                if (i != j) {
                  cards.get(j).setMovingDown(true);
                }
              }
              cards.get(i).setMovingUp(true);
              time = millis();
              menuClickedSound.play();
            }
          }
        }
      } else if (mouseY >= (cards.get(i).getY())) {
        if (mouseY < (cards.get(i).getY() + distanceCards)) {
          if (mouseX >= (cards.get(i).getX())) {
            if (mouseX < (cards.get(i).getX() + cardWidth)) {
              for (int j=0; j<cards.size()-1; j++) {
                if (i != j) {
                  cards.get(j).setMovingDown(true);
                }
              }
              cards.get(i).setMovingUp(true);
              time = millis();
              menuClickedSound.play();
            }
          }
        }
      }
    }
  }
}

class Card {
  //Variables
  private float x, y, z, cardWidth, cardHeight, cardCornerRoudness;
  private float textWidth, textHeight, textX, textY;
  private float targetX = 0, targetY = 0;
  private int instrColor;
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
  public void show() {
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
  public void settings() {  size(780, 801, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
