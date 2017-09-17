package com.aditya.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture bg, birds[], btube,ttube, gameover;
    int birdY = 0, i = 0, gamestate = 0, gap = 400, score = 0,scoringTube = 0,numOfTubes = 3;;
    float velocity =0, gravity = 2, maxTubeOffSet, distBetTubes, tubeVelocity = 4;
    float tubeX[] = new float[numOfTubes];
    float tubeOffSet[] = new float[numOfTubes];
    Random random;
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    Rectangle topTubeRect[], botTubeRect[];
    BitmapFont font;

    @Override
	public void create () {
		batch = new SpriteBatch();
		bg = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        btube = new Texture("bottomtube.png");
        ttube = new Texture("toptube.png");
        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
        random = new Random();
        maxTubeOffSet = Gdx.graphics.getHeight()/2 - gap/2 - 100;
        distBetTubes = Gdx.graphics.getWidth();
        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        topTubeRect = new Rectangle[numOfTubes];
        botTubeRect = new Rectangle[numOfTubes];
        gameover = new Texture("gameover.png");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        startgame();
	}

    public void startgame()
    {
        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
        for(int i = 0; i < numOfTubes; i++)
        {
            tubeOffSet[i] = (random.nextFloat() -0.5f)*(Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth()/2 - ttube.getWidth()/2 + Gdx.graphics.getWidth() + i*distBetTubes;
            topTubeRect[i] = new Rectangle();
            botTubeRect[i] = new Rectangle();
        }
    }

	@Override
	public void render () {

        batch.begin();
        batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(gamestate == 1)
        {
            if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2)
            {
                score++;
                Gdx.app.log("Score",""+score);
                if(scoringTube < numOfTubes - 1)
                    scoringTube++;
                else
                    scoringTube = 0;
            }
            for(int i = 0; i < numOfTubes; i++)
            {
                if(tubeX[i] < -ttube.getWidth())
                {
                    tubeOffSet[i] = (random.nextFloat() -0.5f)*(Gdx.graphics.getHeight() - gap - 200);
                    tubeX[i] += distBetTubes * numOfTubes;
                }
                else
                    tubeX[i] -= tubeVelocity;

                batch.draw(ttube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i]);
                batch.draw(btube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - btube.getHeight() + tubeOffSet[i]);

                topTubeRect[i] = new Rectangle((int)tubeX[i], (int)(Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i]), ttube.getWidth(), ttube.getHeight());
                botTubeRect[i] = new Rectangle((int)tubeX[i], (int)(Gdx.graphics.getHeight() / 2 - gap / 2 - btube.getHeight() + tubeOffSet[i]), btube.getWidth(), btube.getHeight());

            }
            if(Gdx.input.justTouched())
            {
                velocity = -20;
            }
            if(birdY > 0)
            {
                velocity += gravity;
                birdY -= velocity;
            }
            else
            {
                gamestate = 2;
            }
        }
        else if(gamestate == 0)
        {
            if(Gdx.input.justTouched())
            {
                gamestate = 1;
            }

        }
        else if(gamestate == 2)
        {
            batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
            if(Gdx.input.justTouched())
            {
                gamestate = 1;
                startgame();
                score = 0;
                scoringTube = 0;
                velocity = 0;
            }

        }

        if(i == 0)
            i = 1;
        else
            i = 0;

        font.draw(batch, ""+score, 100, 100);
        batch.draw(birds[i], Gdx.graphics.getWidth() / 2 - birds[i].getWidth() / 2, birdY);
        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[i].getHeight() / 2, birds[i].getWidth() / 2);

        for(int i = 0; i < numOfTubes; i++)
        {
            if(Intersector.overlaps(birdCircle, topTubeRect[i]) || Intersector.overlaps(birdCircle, botTubeRect[i]))
                gamestate = 2;
        }

	}
}
