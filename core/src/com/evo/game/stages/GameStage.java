package com.evo.game.stages;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.evo.game.actors.Runner;
import com.evo.game.utils.WorldUtils;
public class GameStage extends Stage{
	 // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    private World world;
    private Body border;
    private Runner runner;
    
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    public GameStage() {
    	setUpWorld();
    	setupCamera();
        renderer = new Box2DDebugRenderer();
       
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }
    
    private void setUpWorld() {
        world = WorldUtils.createWorld();
        setUpBorder();
        setUpRunner();
    }
    
    private void setUpBorder() {
    	border = WorldUtils.createBorder(world);
    }
    private void setUpRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        //TODO: Implement interpolation

    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }


}
