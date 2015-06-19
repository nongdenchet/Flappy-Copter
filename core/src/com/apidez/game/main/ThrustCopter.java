package com.apidez.game.main;

import com.apidez.game.helper.OverlapPolygon;
import com.apidez.game.resource.Resource;
import com.apidez.game.resource.Value;
import com.apidez.game.target.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.List;

public class ThrustCopter extends ApplicationAdapter {
    static final String TAG = "Flappy Copter";

    public enum GameState {
        GAMEOVER, INIT, ACTION
    }

    // Game state
    private GameState gameState = GameState.INIT;

    // Core
    private SpriteBatch batch;
    private FPSLogger fpsLogger;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private TextureAtlas atlas;

    // Obstacles
    private List<Pillar> pillars;

    // Stars
    private List<Star> stars;
    private ParticleEffect starEffect;

    // Saving
    private Preferences prefs;
    private int score;

    // MovingBackground
    private Sprite backgroundSprite;

    // Target
    private MovingBackground movingBackground;
    private Plane plane;
    private TouchStub touchStub;

    // Bitmap
    private TextureRegion gameoverBtn;
    private TextureRegion tap1;

    private void init() {
        // Init screen batch and camera
        atlas = new TextureAtlas(Gdx.files.internal("test.pack"));
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(Value.WIDTH, Value.HEIGHT, camera);
        viewport.apply();
        camera.position.set(Value.WIDTH / 2, Value.HEIGHT / 2, 0);
        Resource.instance().load();

        // Init the frame detector
        fpsLogger = new FPSLogger();

        // effects
        starEffect = new ParticleEffect();
        starEffect.load(Gdx.files.internal("star_capture_effect.p"), atlas);
    }

    @Override
    public void create() {
        this.init();

        pillars = new ArrayList<Pillar>();
        stars = new ArrayList<Star>();
        prefs = Gdx.app.getPreferences("saving");
        backgroundSprite = new Sprite(atlas.findRegion("background"));
        backgroundSprite.setPosition(0.0f, 0.0f);
        tap1 = atlas.findRegion("tap1");
        gameoverBtn = atlas.findRegion("gameover");
        movingBackground = MovingBackground.newInstance(atlas);
        plane = Plane.newInstance(atlas);
        touchStub = TouchStub.newInstance(camera, atlas, plane);
        Resource.instance().playLoop();
    }

    @Override
    public void resume() {
        super.resume();
        Resource.instance().playLoop();
    }

    @Override
    public void pause() {
        super.pause();
        Resource.instance().stopLoop();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fpsLogger.log();
         updateScene();
         drawScene();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void drawScene() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawComponent();
        batch.end();
    }

    // Add new pillar
    private void addPillar() {
        float x, y;
        if (pillars.size() == 0) {
            x = plane.getPlanePosition().x + Value.INIT_PILLAR;
        } else {
            x = pillars.get(pillars.size() - 1).getPos().x + Value.DIS_PILLAR;
        }
        y = MathUtils.randomBoolean() ? 0 : Value.HEIGHT;
        pillars.add(Pillar.newInstance(new Vector2(x, y), atlas));
    }

    // Add new star
    private void addStar() {
        float x, y;
        y = MathUtils.random(Value.STAR_SIDE, Value.HEIGHT - 2 * Value.STAR_SIDE);
        if (stars.size() == 0) {
            x = plane.getPlanePosition().x + Value.MAX_STAR_DIS;
        } else {
            x = stars.get(stars.size() - 1).getPostion().x + MathUtils.random(Value.MIN_STAR_DIS, Value.MAX_STAR_DIS);
        }
        stars.add(Star.newInstance(new Vector2(x, y), atlas));
    }

    private void updateScene() {
        // Check game state
        switch (gameState) {
            case INIT:
                if (Gdx.input.justTouched()) {
                    resetGame();
                    gameState = GameState.ACTION;
                    plane.setGravity(new Vector2(0, Value.GRAVITY));
                }
                break;
            case ACTION: // Update score by time
                score += Gdx.graphics.getDeltaTime() * Value.SCORE_BY_SECOND;
                break;
            case GAMEOVER: // Stop the game
                if (prefs.getInteger("high_score", 0) < score) {
                    prefs.putInteger("high_score", score);
                    prefs.flush();
                }
                if (Gdx.input.justTouched()) {
                    resetGame();
                }
                return;
        }

        // Update pillars and stars
        if (gameState == GameState.ACTION) {

            // Pillars
            if (pillars.size() > 0 && pillars.get(0).getPos().x < -Value.DIS_PILLAR)
                pillars.remove(0);
            else if (pillars.size() < Value.MAX_PILLAR)
                addPillar();
            for (Pillar pillar : pillars) {
                pillar.update();
            }

            // Stars
            if (stars.size() > 0 && stars.get(0).getPostion().x < -Value.MIN_STAR_DIS)
                stars.remove(0);
            else if (stars.size() < Value.MAX_PILLAR)
                addStar();
            for (Star star : stars) {
                star.update();
            }
        }

        // Update component
        movingBackground.update();
        touchStub.update();
        plane.update();

        // Update the state of the game
        updateGameState();
    }

    private void updateGameState() {
        // Collision with stars
        int index = -1;
        for (Star star : stars) {
            if (OverlapPolygon.overlap(star.getBound(), plane.getBound())) {
                if (gameState == GameState.ACTION) {
                    Resource.instance().playStar();
                    index = stars.indexOf(star);
                }
            }
        }

        // Play effects
        if (index != -1) {
            Star star = stars.get(index);
            starEffect.setPosition(star.getPostion().x + Value.STAR_SIDE / 2,
                    star.getPostion().y + Value.STAR_SIDE / 2);
            stars.remove(index);
            score += Value.SCORE_BY_STAR;
            starEffect.start();
        }

        // Collision with terrains
        if (plane.getPlanePosition().y < movingBackground.getTerrainBelow().getRegionHeight() - Value.EPSILON
                || plane.getPlanePosition().y + Value.PLAN_HEIGHT
                > Value.HEIGHT - movingBackground.getTerrainBelow().getRegionHeight() + Value.EPSILON) {
            if (gameState != GameState.GAMEOVER) {
                Resource.instance().playCrash();
                gameState = GameState.GAMEOVER;
            }
        }

        // Collection with pillars
        for (Pillar pillar : pillars) {
            if (OverlapPolygon.overlap(pillar.getBound(), plane.getBound())) {
                if (gameState != GameState.GAMEOVER) {
                    Resource.instance().playCrash();
                    gameState = GameState.GAMEOVER;
                }
            }
        }
    }

    private void drawComponent() {
        backgroundSprite.draw(batch);
        drawStarsAndPillars();
        movingBackground.draw(batch);
        touchStub.draw(batch);
        plane.draw(batch);
        starEffect.draw(batch, Gdx.graphics.getDeltaTime());
        drawGameover();
        drawInitFinger();
        drawScore();
    }

    private void drawStarsAndPillars() {
        // Draw stars
        for (Star star : stars) {
            star.draw(batch);
        }

        // Draw pillars
        for (Pillar pillar : pillars) {
            pillar.draw(batch);
        }
    }

    // Draw gameover
    private void drawGameover() {
        if (gameState == GameState.GAMEOVER) {
            batch.draw(gameoverBtn, Value.GAMEOVER_POS_X, Value.GAMEOVER_POS_Y);
        }
    }

    // Draw init finger
    private void drawInitFinger() {
        if (gameState == GameState.INIT) {
            batch.draw(tap1, plane.getPlanePosition().x + Value.OFFSET_TOUCH_X,
                    plane.getPlanePosition().y + Value.OFFSET_TOUCH_Y);
        }
    }

    // Draw score
    private void drawScore() {
        switch (gameState) {
            case INIT:
            case GAMEOVER:
                Resource.instance().getFont().draw(batch, "High score: " + prefs.getInteger("high_score", 0),
                        10, Value.HEIGHT - 10);
                break;
            case ACTION:
                Resource.instance().getFont().draw(batch, String.valueOf(score),
                        Value.SCORE_X, Value.SCORE_Y);
                break;
        }
    }

    private void resetGame() {
        score = 0;
        gameState = GameState.INIT;
        pillars.clear();
        stars.clear();
        movingBackground.reset();
        touchStub.reset();
        plane.reset();
    }
}
