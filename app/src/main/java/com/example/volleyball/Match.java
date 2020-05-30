package com.example.volleyball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.ScaleAnimation;
import android.widget.Switch;

import androidx.navigation.Navigation;

import java.util.Stack;

class Match extends SurfaceView implements Runnable {

    private Thread thread;
    private volatile boolean playing;
    private boolean paused = true;
    private boolean gameOver = false;

    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;
    private int colorBack = Color.argb(255, 170, 206, 84);

    private long fps;
    private final int MS_IN_SECOND = 1000;

    private int width;
    private int height;
    private float screenDensity;
    private float blockSize;
    private float fontSize;
    private float fontMargin;

    private long startTime;

    private Area leftArea;
    private Area rightArea;

    private int leftScore;
    private int rightScore;
    private String scoreString = "0 : 0";
    private boolean selectedWho = false;

    private Ball ball;
    private float startBallX;
    private float startBallY;

    private float buttonSize;
    private float buttonMargin;
    private Button btnCommit;
    Data data;

    private Stack<Round> rounds;

    private GameInfo gameInfo;

    Match(Context context, int width, int height, float density) {
        super(context);

        data = new Data(context);
        this.width = width;
        this.height = height;
        screenDensity = density;
        fontSize = 32 * density;
        fontMargin = fontSize;
        blockSize = height / 5;
        
        holder = getHolder();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(fontSize);

        float halfW = width / 2;

        RectF rectLeft = new RectF(halfW - 3 * blockSize, height - 4 * blockSize, halfW, height - blockSize);
        RectF rectRight = new RectF(halfW, rectLeft.top, halfW + 3 * blockSize, rectLeft.bottom);
        leftArea = new Area(rectLeft, Area.LEFT, density);
        rightArea = new Area(rectRight, Area.RIGHT, density);

        startBallX = width / 2;
        startBallY = height / 2;
        ball = new Ball(getContext(), density);

        buttonSize = 40 * density;
        buttonMargin = 10 * density;
        float startButtonX = width - buttonSize - buttonMargin;
        float startButtonY = buttonMargin;
        RectF rect = new RectF(startButtonX, startButtonY, startButtonX + buttonSize, startButtonY + buttonSize);
        btnCommit = new Button(getContext(), rect);

        rounds = new Stack<>();

        startGame();
    }

    private void startGame() {
        leftScore = 0;
        rightScore = 0;
        paused = false;
        gameOver = false;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while(playing) {
            long startFrame = System.currentTimeMillis();

            if (!paused) {
                update();
            }

            draw();

            long timeFrame = System.currentTimeMillis() - startFrame;
            if (timeFrame > 0) {
                fps = MS_IN_SECOND / timeFrame;
            }
        }

    }

    private void update() {
    }

    private void draw() {
        if(holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();

            canvas.drawColor(colorBack);

            leftArea.draw(canvas);
            rightArea.draw(canvas);

            if (selectedWho) {
                ball.draw(canvas, paint);
                btnCommit.draw(canvas, paint);
            }

            paint.setTextSize(fontSize);
            canvas.drawText("FPS: " + fps, fontMargin, fontSize + fontMargin, paint);


            float scoreWidth = paint.measureText(scoreString);
            canvas.drawText(scoreString, (width - scoreWidth) / 2, fontSize + fontMargin, paint);


            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void pause() {
        playing = false;
        try {
            thread.join();
        } catch(Exception e) {
            // Error;
        }
    }

    public void resume() {
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (selectedWho && btnCommit.getRect().contains(event.getX(), event.getY())) {
                    commit();
                } else if (selectedWho) {
                    ball.setPosition(event.getX(), event.getY());
                } else {
                    if (leftArea.getRect().contains(event.getX(), event.getY())) {
                        selectArea(leftArea);
                    } else if (rightArea.getRect().contains(event.getX(), event.getY())) {
                        selectArea(rightArea);
                    }
                }
                break;
        }
        return true;
    }

    private void onEnd() {
        if (gameInfo == null) {
            return;
        }
        gameInfo.setTime(System.currentTimeMillis());
        long match_id = data.insertGame(gameInfo);
        for (Round r : rounds) {
            r.setMatchId(match_id);
            data.insertRound(r);
        }

        Bundle b = new Bundle();
        b.putLong(ResultGameFragment.KEY_MATCH_ID, match_id);
        Navigation.findNavController(this).navigate(R.id.matchToGame, b);
    }

    private void selectArea(Area area) {
        area.setSelected(true);
        selectedWho = true;
        resetBallPosition();
    }

    private void commit() {
        Round round = new Round(leftArea.isSelected(), rightArea.isSelected(), System.currentTimeMillis());
        rounds.push(round);
        updateScores();
        leftArea.setSelected(false);
        rightArea.setSelected(false);
        selectedWho = false;
    }

    private void updateScores() {
        int countLeft = 0;
        int countRight = 0;
        for(Round r : rounds) {
            if (r.team1) {
                countLeft++;
            } else {
                countRight++;
            }
        }

        scoreString = countLeft + " : " + countRight;
        if (countLeft >= 3 || countRight >= 3){
            onEnd();
        }

    }

    private void resetBallPosition() {
        ball.setPosition(startBallX, startBallY);
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }
}
