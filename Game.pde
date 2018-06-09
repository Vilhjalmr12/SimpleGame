
//Import
import processing.sound.*;

//Global variables
//General
private String showW = "MENU"; //Tells if has to show menu, help or game
SoundFile hitWallSound, pointSound, finishSound, menuClickedSound, startGameSound;
int time, playingTime = 60000; // <-- 1 Minute //180000; // <-- 3 Minutes
boolean gameOn = true, right1On, right2On, left1On, left2On;

//Translations
String languageCode = "en"; //sv //use ISO 639-1 code
Translations trObj = new Translations(languageCode); //load translations object for given language code

//Balls
PShape ball, earth, jupiter, mars, mercury, neptune, pluto, saturn;
PShape sun, uranus, venus;
PImage ballTexture, earthTexture, jupiterTexture, marsTexture; 
PImage mercuryTexture, neptuneTexture, plutoTexture, saturnTexture;
PImage sunTexture, uranusTexture, venusTexture;
float xPos = 360, yPos = 240, xMove = 3, yMove = 3;
int ballRadius = 24;
float chosenBallX, chosenBallY;

//Paddles
PShape paddle1, paddle2;
PImage paddleTexture;
int xPosPaddle1, xPosPaddle2, yPosPaddle1, yPosPaddle2, paddleSpeed = 12;
int paddleWidth;

//Arena
PShape wallTop, wallRight, wallBottom, wallLeft;
PImage backgroundImg, wallsTexture;
int arenaWidth, arenaHeight, minX, minY, maxX, maxY;
int wallWidth = 24, wallHeight = 24;
float angle = PI/4, density = 1, addedDensity = 0.3;

void setup() { 
  size(780, 801, P3D); 
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
  earth = createShape(SPHERE, ballRadius*1.5);
  earthTexture = loadImage("earth.jpg");
  earth.setTexture(earthTexture);
  jupiter = createShape(SPHERE, ballRadius*1.5);
  jupiterTexture = loadImage("jupiter.jpg");
  jupiter.setTexture(jupiterTexture);
  mars = createShape(SPHERE, ballRadius*1.5);
  marsTexture = loadImage("mars.jpg");
  mars.setTexture(marsTexture);
  mercury = createShape(SPHERE, ballRadius*1.5);
  mercuryTexture = loadImage("mercury.jpg");
  mercury.setTexture(mercuryTexture);
  neptune = createShape(SPHERE, ballRadius*1.5);
  neptuneTexture = loadImage("neptune.jpg");
  neptune.setTexture(neptuneTexture);
  pluto = createShape(SPHERE, ballRadius*1.5);
  plutoTexture = loadImage("pluto.jpg");
  pluto.setTexture(plutoTexture);
  saturn = createShape(SPHERE, ballRadius*1.5);
  saturnTexture = loadImage("saturn.jpg");
  saturn.setTexture(saturnTexture);
  sun = createShape(SPHERE, ballRadius*1.5);
  sunTexture = loadImage("sun.jpg");
  sun.setTexture(sunTexture);
  uranus = createShape(SPHERE, ballRadius*1.5);
  uranusTexture = loadImage("uranus.jpg");
  uranus.setTexture(uranusTexture);
  venus = createShape(SPHERE, ballRadius*1.5);
  venusTexture = loadImage("venus.jpg");
  venus.setTexture(venusTexture);
  
  ball = createShape(SPHERE, ballRadius);
  ball.setTexture(earthTexture);
}

void draw() {
  if (showW.equals("MENU")) {
    drawMenu();
  } else if (showW.equals("HELP")) {
    
  } else if (showW.equals("GAME")) {
    drawGame();
  }
}

//Runs when right mouse button is clicked
void mouseClicked(){    
  //only check if appropriate screen shown
  if(showW.equals("MENU")){
    menuMouseListener();
  }
}

void keyPressed() {
  if(showW.equals("GAME")){
    gameKeyPressedListener();
  }
}

void keyReleased() {
  if(showW.equals("GAME")){
    gameKeyReleasedListener();
  }
} 


/*-------------------------------MENU-------------------------------------
------------------------------------------------------------------------*/

void drawMenu() {
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
  angle += 0.015;
  popMatrix();
  pushMatrix();
  translate(width/2 - (width*36/100)/2, height*84/100, 0);
  fill(90, 90, 120, 75);
  rect(0, 0, width*36/100, height*10/100, 12);
  fill(150, 150, 180);
  textSize(32);
  textAlign(CENTER, CENTER);
  text(trObj.startGameString, 0, 0, width*36/100, height*9/100);
  noFill();
  strokeWeight(3);
  stroke(180, 180, 210);
  popMatrix();
  pushMatrix();
  translate(chosenBallX, chosenBallY, -ballRadius);
  ellipse(0, 0, ballRadius*3.3, ballRadius*3.3);
  popMatrix();
}

void menuMouseListener() {
  //check if start button is clicked, if so go to game
  if((mouseX >= (width/2 - (width*36/100)/2)) 
      && (mouseX <= width/2 + (width*36/100)/2)){
    if((mouseY >= height*84/100) 
      && (mouseY <= height*84/100 + height*10/100)){ 
        startGameSound.play();
        time = millis(); //Store the current time
        showW = "GAME";
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

void drawGame() {
  pushMatrix();
  noStroke();
  backgroundImg.resize(width, height);
  background(backgroundImg);
  //ambient(250, 100, 100);
  //ambientLight(40, 20, 40);
  //lightSpecular(255, 215, 215);
  pointLight(255, 255, 240, (minX+maxX)/2, height/4, 100);
  pointLight(255, 255, 240, (minX+maxX)/2, 3*height/4, 100); 
  shininess(255.0);
  specular(255, 255, 240);
  
  if(millis() - time >= playingTime){
    finishGame();
  }
  drawArena();
  drawPaddles();  
  //Move and draw the ball
  drawBall();
  popMatrix();
}

void drawArena() {
  popMatrix();
  pushMatrix();
  translate(minX-wallWidth, minY-wallWidth, -ballRadius*2);
  fill(240, 240, 255, 45);
  rect(0, 0, arenaWidth, arenaHeight, 12); //Last parameter, rounded corners
  popMatrix();
  pushMatrix();
  noFill();
  translate((width/2)-(width*10/100), height*5/100, -ballRadius*2);
  shape(wallTop);
  translate(-(arenaWidth/2), (height*90/100)/2, 0);
  shape(wallLeft);
  translate(((width*75/100)/2), (height*90/100)/2, 0);
  shape(wallBottom);
  translate((arenaWidth/2), - (height*90/100)/2, 0);
  shape(wallRight);
}

void drawBall() {
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
  if (yPos+ballRadius >= maxY || yPos-ballRadius <= minY) {
    yMove *= -1;
    pointSound.play();
  //If the ball touches second paddle  
  } else if (yPos+ballRadius+wallWidth-3 >= yPosPaddle2) {
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
  angle += 0.01;
}

void drawPaddles(){
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

void gameKeyPressedListener() {
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

void gameKeyReleasedListener() {
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

void finishGame() {
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
}
