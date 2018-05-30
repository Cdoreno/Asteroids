package asteroidsdam.entidades;

import java.awt.Graphics2D;
import java.util.Random;

import asteroidsdam.Juego;
import asteroidsdam.Espacio;
import asteroidsdam.Vector;

/**
 * Represents an Meteroito within the game world.
 * @author Brendan Jones
 *
 */
public class Meteroito extends Entidad {
	
	/**
	 * The minimum speed at which the asteroid can rotate.
	 */
	private static final double MIN_ROTATION = 0.0075;
	
	/**
	 * The maximum speed at which the asteroid can rotate.
	 */
	private static final double MAX_ROTATION = 0.0175;
	
	/**
	 * The variation between the asteroid rotation speeds.
	 */
	private static final double ROTATION_VARIANCE = MAX_ROTATION - MIN_ROTATION;
	
	/**
	 * The minimum velocity at which the asteroid can move.
	 */
	private static final double MIN_VELOCITY = 0.75;
	
	/**
	 * The maximum velocity at which the asteroid can move.
	 */
	private static final double MAX_VELOCITY = 1.65;
	
	/**
	 * The variation between the asteroid velocities.
	 */
	private static final double VELOCITY_VARIANCE = MAX_VELOCITY - MIN_VELOCITY;
	
	/**
	 * The minimum distance from the player spawn that a new asteroid can spawn.
	 */
	private static final double MIN_DISTANCE = 200.0;
	
	/**
	 * The maximum distance from the player spawn that a new asteroid can spawn.
	 */
	private static final double MAX_DISTANCE = Espacio.WORLD_SIZE / 2.0;
	
	/**
	 * The variation between the spawn distances.
	 */
	private static final double DISTANCE_VARIANCE = MAX_DISTANCE - MIN_DISTANCE;
	
	/**
	 * The number of updates to execute after spawning.
	 */
	private static final float SPAWN_UPDATES = 10;
	
	/**
	 * The Size.
	 */
	private MeteoritoSize size;
	
	/**
	 * The rotation speed.
	 */
	private double rotationSpeed;
	
	/**
	 * Creates a new Asteroid randomly in the world.
	 * @param random The Random instance.
	 */
	public Meteroito(Random random) {
		super(calculatePosition(random), calculateVelocity(random), MeteoritoSize.Large.radius, MeteoritoSize.Large.killValue);
		this.rotationSpeed = -MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
		this.size = MeteoritoSize.Large;
	}
	
	/**
	 * Creates a new Asteroid from a parent Asteroid.
	 * @param parent The parent.
	 * @param size The size.
	 * @param random The Random instance.
	 */
	public Meteroito(Meteroito parent, MeteoritoSize size, Random random) {
		super(new Vector(parent.position), calculateVelocity(random), size.radius, size.killValue);
		this.rotationSpeed = MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
		this.size = size;
		
		/*
		 * While not necessary, calling the update method here makes the asteroid
		 * appear to have a different starting position than it's parent or sibling.
		 */
		for(int i = 0; i < SPAWN_UPDATES; i++) {
			update(null);
		}
	}
	
	/**
	 * Calculates a random valid spawn point for an Meteroito.
	 * @param random The random instance.
	 * @return The spawn point.
	 */
	private static Vector calculatePosition(Random random) {
		Vector vec = new Vector(Espacio.WORLD_SIZE / 2.0, Espacio.WORLD_SIZE / 2.0);
		return vec.add(new Vector(random.nextDouble() * Math.PI * 2).scale(MIN_DISTANCE + random.nextDouble() * DISTANCE_VARIANCE));
	}
	
	/**
	 * Calculates a random valid velocity for an Meteroito.
	 * @param random The random instance.
	 * @return The velocity.
	 */
	private static Vector calculateVelocity(Random random) {
		return new Vector(random.nextDouble() * Math.PI * 2).scale(MIN_VELOCITY + random.nextDouble() * VELOCITY_VARIANCE);
	}
	
	@Override
	public void update(Juego game) {
		super.update(game);
		rotate(rotationSpeed); //Rotate the image each frame.
	}

	@Override
	public void draw(Graphics2D g, Juego game) {
		g.drawPolygon(size.polygon); //Draw the Meteroito.
	}
	
	@Override
	public void handleCollision(Juego game, Entidad other) {
		//Prevent collisions with other asteroids.
		if(other.getClass() != Meteroito.class) {
			//Only spawn "children" if we're not a Small asteroid.
			if(size != MeteoritoSize.Small) {
				//Determine the Size of the children.
				MeteoritoSize spawnSize = MeteoritoSize.values()[size.ordinal() - 1];
				
				//Create the children Asteroids.
				for(int i = 0; i < 2; i++) {
					game.registerEntity(new Meteroito(this, spawnSize, game.getRandom()));
				}
			}
			
			//Delete this Meteroito from the world.
			flagForRemoval();
			
			//Award the player points for killing the Meteroito.
			game.addScore(getKillScore());		
		}
	}
	
}
