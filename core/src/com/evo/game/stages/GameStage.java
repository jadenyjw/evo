package com.evo.game.stages;

import com.evo.game.box2d.BotUserData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.evo.game.actors.Bot;
import com.evo.game.actors.Food;
import com.evo.game.actors.Runner;
import com.evo.game.utils.BodyUtils;
import com.evo.game.utils.WorldUtils;

public class GameStage extends Stage implements ContactListener {
	// This will be our viewport measurements while working with the debug
	// renderer
	private static final int VIEWPORT_WIDTH = 20;
	private static final int VIEWPORT_HEIGHT = 13;

	private World world;
	private Body border;
	private Runner runner;
	private Food food;
	private Array<Bot> bot = new Array<Bot>();

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	private Array<Body> deletedBodies = new Array<Body>();
	private Array<Body> bodies = new Array<Body>();

	public GameStage() {
		setUpWorld();
		setupCamera();
		Gdx.input.setInputProcessor(this);
		renderer = new Box2DDebugRenderer();
	}

	private void setupCamera() {
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
	}

	private void setUpWorld() {
		world = WorldUtils.createWorld();
		world.setContactListener(this);
		setUpBorder();
		setUpFood();
		setUpRunner();
		setUpBots();
	}

	private void setUpBorder() {
		border = WorldUtils.createBorder(world);
	}

	private void setUpRunner() {
		runner = new Runner(WorldUtils.createRunner(world));
		addActor(runner);
	}

	private void setUpFood() {
		for (int x = 0; x < 200; x++) {
			food = new Food(
					WorldUtils.createFood(world, (float) Math.random() * (28) + 1, (float) Math.random() * (28) + 1));
		}
	}

	private void setUpBots() {
		for (int x = 0; x < 10; x++) {

			bot.add(new Bot(
					WorldUtils.createBot(world, (float) Math.random() * (28) + 1, (float) Math.random() * (28) + 1)));
			bot.get(x).getUserData().setID(x);

		}
	}

	private void update(Body body) {
		deletedBodies.removeValue(body, true);
		world.destroyBody(body);

	}

	@Override
	public void act(float delta) {

		super.act(delta);
		for (Body body : deletedBodies) {
			update(body);
		}

		// Calculate for neural network
		world.getBodies(bodies);

		for (int x = 0; x < bot.size; x++) {

			if (bot.get(x).body.isActive()) {

				Bot currentBot = bot.get(x);

				float lowestDistanceToBot = Float.POSITIVE_INFINITY;
				float angleToBot;
				float sizeOfBot;

				float lowestDistanceToFood = Float.POSITIVE_INFINITY;
				float angleToFood;

				for (int y = 0; y < bodies.size; y++) {

					Body targetBody = bodies.get(y);
					if (bodies.get(y).isActive()) {

						// If body is food
						if (BodyUtils.bodyIsFood(targetBody)) {
							if (calculateDistance(currentBot.body, targetBody) < lowestDistanceToFood) {

								lowestDistanceToFood = calculateDistance(currentBot.body, targetBody);
								angleToFood = calculateAngle(currentBot.body, targetBody);

								((BotUserData) (currentBot.getUserData()))
										.setDistanceToNearestPlayer(lowestDistanceToFood);
								((BotUserData) (currentBot.getUserData())).setAngleToNearestPlayer(angleToFood);

							}

						}

						// If body is a runner and is not himself

						if ((BodyUtils.bodyIsRunner(targetBody) || BodyUtils.bodyIsBot(targetBody))
								&& targetBody != currentBot.body) {

							if (calculateDistance(currentBot.body, targetBody) < lowestDistanceToBot) {
								lowestDistanceToBot = calculateDistance(currentBot.body, targetBody);
								angleToBot = calculateAngle(currentBot.body, targetBody);
								sizeOfBot = calculateSize(targetBody);

								((BotUserData) (currentBot.getUserData()))
										.setDistanceToNearestPlayer(lowestDistanceToBot);
								((BotUserData) (currentBot.getUserData())).setAngleToNearestPlayer(angleToBot);
								((BotUserData) (currentBot.getUserData())).setSizeOfNearestPlayer(sizeOfBot);
							}

						}

					}

				}

			}

		}
		
		
		System.out.println(((BotUserData) bot.get(1).getUserData()).getDistanceToNearestPlayer());

		// Input keys
		if (leftKeyPressed) {
			runner.turnLeft();
		}
		if (rightKeyPressed) {
			runner.turnRight();
		}

		if (upKeyPressed) {
			runner.moveForward();
		} else if (!upKeyPressed) {
			runner.stop();
		}

		camera.position.set(runner.getX(), runner.getY(), 0);
		camera.update();

		// Fixed timestep
		accumulator += delta;

		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}

		// TODO: Implement interpolation

	}

	@Override
	public void draw() {
		super.draw();
		renderer.render(world, camera.combined);
	}

	// Calculation methods

	public float calculateDistance(Body a, Body target) {

		Vector2 position1 = a.getPosition();
		Vector2 position2 = target.getPosition();

		return position1.dst(position2);

	}

	public float calculateAngle(Body a, Body target) {

		Vector2 position1 = a.getPosition();
		Vector2 position2 = target.getPosition();

		float angleOfa = a.getAngle();
		float angleOfvector = position1.angleRad(position2);

		return (float) Math.abs(angleOfvector - angleOfa % (MathUtils.PI));

	}

	public float calculateSize(Body target) {
		return target.getFixtureList().first().getShape().getRadius();

	}

	public boolean leftKeyPressed;
	public boolean upKeyPressed;
	public boolean rightKeyPressed;

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.LEFT) {
			leftKeyPressed = true;
		}
		if (keycode == Input.Keys.RIGHT) {
			rightKeyPressed = true;
		}
		if (keycode == Input.Keys.UP) {
			upKeyPressed = true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.LEFT) {
			leftKeyPressed = false;
		}
		if (keycode == Input.Keys.RIGHT) {
			rightKeyPressed = false;
		}
		if (keycode == Input.Keys.UP) {
			upKeyPressed = false;
		}

		return false;
	}

	@Override
	public void beginContact(Contact contact) {

		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();

		if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsFood(b))
				|| (BodyUtils.bodyIsFood(a) && BodyUtils.bodyIsRunner(b))) {

			if (BodyUtils.bodyIsFood(a) && !(deletedBodies.contains(a, true))) {

				deletedBodies.add(a);
			} else if (BodyUtils.bodyIsFood(b) && !(deletedBodies.contains(b, true))) {
				deletedBodies.add(b);
			}

			runner.grow(0.02f);

		}
		if ((BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsFood(b))
				|| (BodyUtils.bodyIsFood(a) && BodyUtils.bodyIsBot(b))) {

			if (BodyUtils.bodyIsFood(a) && !(deletedBodies.contains(a, true))) {

				deletedBodies.add(a);
				bot.get(((BotUserData) (b.getUserData())).getID()).grow(0.02f);

			} else if (BodyUtils.bodyIsFood(b) && !(deletedBodies.contains(b, true))) {

				deletedBodies.add(b);
				bot.get(((BotUserData) (a.getUserData())).getID()).grow(0.02f);
			}

		}

		if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsBot(b))
				|| (BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsRunner(b))) {

			if (BodyUtils.bodyIsBot(a) && !(deletedBodies.contains(a, true))) {
				System.out.println(((BotUserData) (a.getUserData())).getID());
				if (((BotUserData) a.getUserData()).getRadius() < runner.getUserData().getRadius()) {
					deletedBodies.add(a);
					runner.grow(0.02f);
				} else if (((BotUserData) a.getUserData()).getRadius() > runner.getUserData().getRadius()) {
					deletedBodies.add(b);
					runner.remove();
				}

			} else if (BodyUtils.bodyIsBot(b) && !(deletedBodies.contains(b, true))) {
				System.out.println(((BotUserData) (b.getUserData())).getID());
				if (((BotUserData) b.getUserData()).getRadius() < runner.getUserData().getRadius()) {
					deletedBodies.add(b);
					runner.grow(0.02f);
				} else if (((BotUserData) b.getUserData()).getRadius() > runner.getUserData().getRadius()) {
					deletedBodies.add(a);
					runner.remove();
				}
			}

		}

		if ((BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsBot(b)) || (BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsBot(b))) {

			if (BodyUtils.bodyIsBot(a) && !(deletedBodies.contains(a, true))) {
				System.out.println(((BotUserData) (a.getUserData())).getID());

				if (((BotUserData) a.getUserData()).getRadius() < ((BotUserData) b.getUserData()).getRadius()) {
					deletedBodies.add(a);
					bot.removeIndex(((BotUserData) a.getUserData()).getID());

				} else if (((BotUserData) a.getUserData()).getRadius() > ((BotUserData) b.getUserData()).getRadius()) {
					deletedBodies.add(b);
					bot.removeIndex(((BotUserData) b.getUserData()).getID());
				}

			} else if (BodyUtils.bodyIsBot(b) && !(deletedBodies.contains(b, true))) {

				if (((BotUserData) b.getUserData()).getRadius() < ((BotUserData) a.getUserData()).getRadius()) {
					deletedBodies.add(b);
					bot.removeIndex(((BotUserData) b.getUserData()).getID());
				} else if (((BotUserData) b.getUserData()).getRadius() > ((BotUserData) a.getUserData()).getRadius()) {

					deletedBodies.add(a);
					bot.removeIndex(((BotUserData) a.getUserData()).getID());

				}
			}

		}

	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
