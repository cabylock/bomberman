package core.graphics;

import javafx.scene.image.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

/**
 * Lưu trữ thông tin các pixel của 1 sprite (hình ảnh game)
 */
public class Sprite {

	public static final int DEFAULT_SIZE = 48;

	public final int SIZE;
	private int _x, _y;
	public int[] _pixels;
	protected int _realWidth;
	protected int _realHeight;
	private SpriteSheet _sheet;
	private static final Map<Integer, Sprite> spriteCache = new HashMap<>();
	private Image cachedImage = null;

	// Sprite IDs - Tổ chức theo nhóm để dễ quản lý
	// Terrain
	public static final int GRASS = 0;
	public static final int BRICK = 1;
	public static final int WALL = 2;
	public static final int PORTAL = 3;

	// Player1
	public static final int PLAYER1_UP_0 = 4;
	public static final int PLAYER1_UP_1 = 5;
	public static final int PLAYER1_UP_2 = 6;
	public static final int PLAYER1_DOWN_0 = 7;
	public static final int PLAYER1_DOWN_1 = 8;
	public static final int PLAYER1_DOWN_2 = 9;
	public static final int PLAYER1_LEFT_0 = 10;
	public static final int PLAYER1_LEFT_1 = 11;
	public static final int PLAYER1_LEFT_2 = 12;
	public static final int PLAYER1_RIGHT_0 = 13;
	public static final int PLAYER1_RIGHT_1 = 14;
	public static final int PLAYER1_RIGHT_2 = 15;
	public static final int PLAYER1_DEAD_0 = 16;
	public static final int PLAYER1_DEAD_1 = 17;
	public static final int PLAYER1_DEAD_2 = 18;
	public static final int PLAYER1_DEAD_3 = 19;
	public static final int PLAYER1_DEAD_4 = 20;
	public static final int PLAYER1_DEAD_5 = 21;
	public static final int PLAYER1_DEAD_6 = 22;
	public static final int PLAYER1_DEAD_7 = 23;

	// Player2
	public static final int PLAYER2_UP_0 = 24;
	public static final int PLAYER2_UP_1 = 25;
	public static final int PLAYER2_UP_2 = 26;
	public static final int PLAYER2_DOWN_0 = 27;
	public static final int PLAYER2_DOWN_1 = 28;
	public static final int PLAYER2_DOWN_2 = 29;
	public static final int PLAYER2_LEFT_0 = 30;
	public static final int PLAYER2_LEFT_1 = 31;
	public static final int PLAYER2_LEFT_2 = 32;
	public static final int PLAYER2_RIGHT_0 = 33;
	public static final int PLAYER2_RIGHT_1 = 34;
	public static final int PLAYER2_RIGHT_2 = 35;
	public static final int PLAYER2_DEAD_0 = 36;
	public static final int PLAYER2_DEAD_1 = 37;
	public static final int PLAYER2_DEAD_2 = 38;
	public static final int PLAYER2_DEAD_3 = 39;
	public static final int PLAYER2_DEAD_4 = 40;
	public static final int PLAYER2_DEAD_5 = 41;
	public static final int PLAYER2_DEAD_6 = 42;
	public static final int PLAYER2_DEAD_7 = 43;

	// Enemies - Balloom
	public static final int BALLOOM_LEFT_0 = 44;
	public static final int BALLOOM_LEFT_1 = 45;
	public static final int BALLOOM_LEFT_2 = 46;
	public static final int BALLOOM_RIGHT_0 = 47;
	public static final int BALLOOM_RIGHT_1 = 48;
	public static final int BALLOOM_RIGHT_2 = 49;
	public static final int BALLOOM_DEAD_0 = 50;

	// Enemies - Oneal
	public static final int ONEAL_LEFT_0 = 51;
	public static final int ONEAL_LEFT_1 = 52;
	public static final int ONEAL_LEFT_2 = 53;
	public static final int ONEAL_RIGHT_0 = 54;
	public static final int ONEAL_RIGHT_1 = 55;
	public static final int ONEAL_RIGHT_2 = 56;
	public static final int ONEAL_DEAD_0 = 57;

	// Enemies - Doll
	public static final int DOLL_LEFT_0 = 58;
	public static final int DOLL_LEFT_1 = 59;
	public static final int DOLL_LEFT_2 = 60;
	public static final int DOLL_RIGHT_0 = 61;
	public static final int DOLL_RIGHT_1 = 62;
	public static final int DOLL_RIGHT_2 = 63;
	public static final int DOLL_DEAD_0 = 64;

	// Enemies - Minvo
	public static final int MINVO_LEFT_0 = 65;
	public static final int MINVO_LEFT_1 = 66;
	public static final int MINVO_LEFT_2 = 67;
	public static final int MINVO_RIGHT_0 = 68;
	public static final int MINVO_RIGHT_1 = 69;
	public static final int MINVO_RIGHT_2 = 70;
	public static final int MINVO_DEAD_0 = 71;

	// Enemies - Kondoria
	public static final int KONDORIA_LEFT_0 = 72;
	public static final int KONDORIA_LEFT_1 = 73;
	public static final int KONDORIA_LEFT_2 = 74;
	public static final int KONDORIA_RIGHT_0 = 75;
	public static final int KONDORIA_RIGHT_1 = 76;
	public static final int KONDORIA_RIGHT_2 = 77;
	public static final int KONDORIA_DEAD_0 = 78;

	// Mob dead
	public static final int MOB_DEAD_0 = 79;
	public static final int MOB_DEAD_1 = 80;
	public static final int MOB_DEAD_2 = 81;

	// Bombs
	
	public static final int BOMB_0 = 82;
	public static final int BOMB_1 = 83;
	public static final int BOMB_2 = 84;
	public static final int BOMB_EXPLODED_0 = 85;
	public static final int BOMB_EXPLODED_1 = 86;
	public static final int BOMB_EXPLODED_2 = 87;

	// Explosions
	public static final int EXPLOSION_VERTICAL_0 = 88;
	public static final int EXPLOSION_VERTICAL_1 = 89;
	public static final int EXPLOSION_VERTICAL_2 = 90;
	public static final int EXPLOSION_HORIZONTAL_0 = 91;
	public static final int EXPLOSION_HORIZONTAL_1 = 92;
	public static final int EXPLOSION_HORIZONTAL_2 = 93;
	public static final int EXPLOSION_HORIZONTAL_LEFT_LAST_0 = 94;
	public static final int EXPLOSION_HORIZONTAL_LEFT_LAST_1 = 95;
	public static final int EXPLOSION_HORIZONTAL_LEFT_LAST_2 = 96;

	public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST_0 = 97;
	public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST_1 = 98;
	public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST_2 = 99;
	
	public static final int EXPLOSION_VERTICAL_TOP_LAST_0 = 100;
	public static final int EXPLOSION_VERTICAL_TOP_LAST_1 = 101;
	public static final int EXPLOSION_VERTICAL_TOP_LAST_2 = 102;
	public static final int EXPLOSION_VERTICAL_DOWN_LAST_0 = 103;
	public static final int EXPLOSION_VERTICAL_DOWN_LAST_1 = 104;
	public static final int EXPLOSION_VERTICAL_DOWN_LAST_2 = 105;

	// Brick explosions

	public static final int BRICK_EXPLODED_0 = 106;
	public static final int BRICK_EXPLODED_1 = 107;
	public static final int BRICK_EXPLODED_2 = 108;

	// Powerups
	public static final int POWERUP_BOMBS = 109;
	public static final int POWERUP_FLAMES = 110;
	public static final int POWERUP_SPEED = 111;
	public static final int POWERUP_WALLPASS = 112;
	public static final int POWERUP_HEALTH_UP = 113;
	public static final int POWERUP_BOMB_PASS = 114;
	public static final int POWERUP_FLAME_PASS = 115;
	public static final int POWERUP_DETONATOR_PASS = 116;
	public static final int POWERUP_SPEED_PASS = 117;
	public static final int POWERUP_WALL_PASS = 118;
	public static final int ANIMATION_NULL = 119;

	/**
	 * Gets a sprite by its ID, loading it only if needed
	 */
	public static Sprite getSprite(int id) {
		if (!spriteCache.containsKey(id)) {
			loadSprite(id);
		}
		return spriteCache.get(id);
	}

	/**
	 * Load a specific sprite into the cache
	 */
	private static void loadSprite(int id) {
		Sprite sprite = null;

		switch (id) {
			case GRASS:
				sprite = new Sprite("grass.png");
				break;
			case BRICK:
				sprite = new Sprite("brick.png");
				break;
			case WALL:
				sprite = new Sprite("wall.png");
				break;
			case PORTAL:
				sprite = new Sprite("portal.png");
				break;
			// Player1 sprites
			case PLAYER1_UP_0:
				sprite = new Sprite("player1_up_0.png");
				break;
			case PLAYER1_UP_1:
				sprite = new Sprite("player1_up_1.png");
				break;
			case PLAYER1_UP_2:
				sprite = new Sprite("player1_up_2.png");
				break;
			case PLAYER1_DOWN_0:
				sprite = new Sprite("player1_down_0.png");
				break;
			case PLAYER1_DOWN_1:
				sprite = new Sprite("player1_down_1.png");
				break;
			case PLAYER1_DOWN_2:
				sprite = new Sprite("player1_down_2.png");
				break;
			case PLAYER1_LEFT_0:
				sprite = new Sprite("player1_left_0.png");
				break;
			case PLAYER1_LEFT_1:
				sprite = new Sprite("player1_left_1.png");
				break;
			case PLAYER1_LEFT_2:
				sprite = new Sprite("player1_left_2.png");
				break;
			case PLAYER1_RIGHT_0:
				sprite = new Sprite("player1_right_0.png");
				break;
			case PLAYER1_RIGHT_1:
				sprite = new Sprite("player1_right_1.png");
				break;
			case PLAYER1_RIGHT_2:
				sprite = new Sprite("player1_right_2.png");
				break;
			case PLAYER1_DEAD_0:
				sprite = new Sprite("player1_dead_0.png");
				break;
			case PLAYER1_DEAD_1:
				sprite = new Sprite("player1_dead_1.png");
				break;
			case PLAYER1_DEAD_2:
				sprite = new Sprite("player1_dead_2.png");
				break;
			case PLAYER1_DEAD_3:
				sprite = new Sprite("player1_dead_3.png");
				break;
			case PLAYER1_DEAD_4:
				sprite = new Sprite("player1_dead_4.png");
				break;
			case PLAYER1_DEAD_5:
				sprite = new Sprite("player1_dead_5.png");
				break;
			case PLAYER1_DEAD_6:
				sprite = new Sprite("player1_dead_6.png");
				break;
			case PLAYER1_DEAD_7:
				sprite = new Sprite("player1_dead_7.png");
				break;
			// Player2 sprites
			case PLAYER2_UP_0:
				sprite = new Sprite("player2_up_0.png");
				break;
			case PLAYER2_UP_1:
				sprite = new Sprite("player2_up_1.png");
				break;
			case PLAYER2_UP_2:
				sprite = new Sprite("player2_up_2.png");
				break;
			case PLAYER2_DOWN_0:
				sprite = new Sprite("player2_down_0.png");
				break;
			case PLAYER2_DOWN_1:
				sprite = new Sprite("player2_down_1.png");
				break;
			case PLAYER2_DOWN_2:
				sprite = new Sprite("player2_down_2.png");
				break;
			case PLAYER2_LEFT_0:
				sprite = new Sprite("player2_left_0.png");
				break;
			case PLAYER2_LEFT_1:
				sprite = new Sprite("player2_left_1.png");
				break;
			case PLAYER2_LEFT_2:
				sprite = new Sprite("player2_left_2.png");
				break;
			case PLAYER2_RIGHT_0:
				sprite = new Sprite("player2_right_0.png");
				break;
			case PLAYER2_RIGHT_1:
				sprite = new Sprite("player2_right_1.png");
				break;
			case PLAYER2_RIGHT_2:
				sprite = new Sprite("player2_right_2.png");
				break;
			case PLAYER2_DEAD_0:
				sprite = new Sprite("player2_dead_0.png");
				break;
			case PLAYER2_DEAD_1:
				sprite = new Sprite("player2_dead_1.png");
				break;
			case PLAYER2_DEAD_2:
				sprite = new Sprite("player2_dead_2.png");
				break;
			case PLAYER2_DEAD_3:
				sprite = new Sprite("player2_dead_3.png");
				break;
			case PLAYER2_DEAD_4:
				sprite = new Sprite("player2_dead_4.png");
				break;
			case PLAYER2_DEAD_5:
				sprite = new Sprite("player2_dead_5.png");
				break;
			case PLAYER2_DEAD_6:
				sprite = new Sprite("player2_dead_6.png");
				break;
			case PLAYER2_DEAD_7:
				sprite = new Sprite("player2_dead_7.png");
				break;
			// Balloom sprites
			case BALLOOM_LEFT_0:
				sprite = new Sprite("balloom_left_0.png");
				break;
			case BALLOOM_LEFT_1:
				sprite = new Sprite("balloom_left_1.png");
				break;
			case BALLOOM_LEFT_2:
				sprite = new Sprite("balloom_left_2.png");
				break;
			case BALLOOM_RIGHT_0:
				sprite = new Sprite("balloom_right_0.png");
				break;
			case BALLOOM_RIGHT_1:
				sprite = new Sprite("balloom_right_1.png");
				break;
			case BALLOOM_RIGHT_2:
				sprite = new Sprite("balloom_right_2.png");
				break;
			case BALLOOM_DEAD_0:
				sprite = new Sprite("balloom_dead.png");
				break;
			// Oneal sprites
			case ONEAL_LEFT_0:
				sprite = new Sprite("oneal_left_0.png");
				break;
			case ONEAL_LEFT_1:
				sprite = new Sprite("oneal_left_1.png");
				break;
			case ONEAL_LEFT_2:
				sprite = new Sprite("oneal_left_2.png");
				break;
			case ONEAL_RIGHT_0:
				sprite = new Sprite("oneal_right_0.png");
				break;
			case ONEAL_RIGHT_1:
				sprite = new Sprite("oneal_right_1.png");
				break;
			case ONEAL_RIGHT_2:
				sprite = new Sprite("oneal_right_2.png");
				break;
			case ONEAL_DEAD_0:
				sprite = new Sprite("oneal_dead.png");
				break;
			// Doll sprites
			case DOLL_LEFT_0:
				sprite = new Sprite("doll_left_0.png");
				break;
			case DOLL_LEFT_1:
				sprite = new Sprite("doll_left_1.png");
				break;
			case DOLL_LEFT_2:
				sprite = new Sprite("doll_left_2.png");
				break;
			case DOLL_RIGHT_0:
				sprite = new Sprite("doll_right_0.png");
				break;
			case DOLL_RIGHT_1:
				sprite = new Sprite("doll_right_1.png");
				break;
			case DOLL_RIGHT_2:
				sprite = new Sprite("doll_right_2.png");
				break;
			case DOLL_DEAD_0:
				sprite = new Sprite("doll_dead.png");
				break;
			// Minvo sprites
			case MINVO_LEFT_0:
				sprite = new Sprite("minvo_left_0.png");
				break;
			case MINVO_LEFT_1:
				sprite = new Sprite("minvo_left_1.png");
				break;
			case MINVO_LEFT_2:
				sprite = new Sprite("minvo_left_2.png");
				break;
			case MINVO_RIGHT_0:
				sprite = new Sprite("minvo_right_0.png");
				break;
			case MINVO_RIGHT_1:
				sprite = new Sprite("minvo_right_1.png");
				break;
			case MINVO_RIGHT_2:
				sprite = new Sprite("minvo_right_2.png");
				break;
			case MINVO_DEAD_0:
				sprite = new Sprite("minvo_dead.png");
				break;
			// Kondoria sprites
			case KONDORIA_LEFT_0:
				sprite = new Sprite("kondoria_left_0.png");
				break;
			case KONDORIA_LEFT_1:
				sprite = new Sprite("kondoria_left_1.png");
				break;
			case KONDORIA_LEFT_2:
				sprite = new Sprite("kondoria_left_2.png");
				break;
			case KONDORIA_RIGHT_0:
				sprite = new Sprite("kondoria_right_0.png");
				break;
			case KONDORIA_RIGHT_1:
				sprite = new Sprite("kondoria_right_1.png");
				break;
			case KONDORIA_RIGHT_2:
				sprite = new Sprite("kondoria_right_2.png");
				break;
			case KONDORIA_DEAD_0:
				sprite = new Sprite("kondoria_dead.png");
				break;
			// Mob dead sprites
			case MOB_DEAD_0:
				sprite = new Sprite("mob_dead_0.png");
				break;
			case MOB_DEAD_1:
				sprite = new Sprite("mob_dead_1.png");
				break;
			case MOB_DEAD_2:
				sprite = new Sprite("mob_dead_2.png");
				break;
			// Bomb sprites
			
			case BOMB_0:
				sprite = new Sprite("bomb_0.png");
				break;
			case BOMB_1:
				sprite = new Sprite("bomb_1.png");
				break;
			case BOMB_2:
				sprite = new Sprite("bomb_2.png");
				break;
			
			case BOMB_EXPLODED_0:
				sprite = new Sprite("bomb_exploded_0.png");
				break;
			case BOMB_EXPLODED_1:
				sprite = new Sprite("bomb_exploded_1.png");
				break;
			case BOMB_EXPLODED_2:
				sprite = new Sprite("bomb_exploded_2.png");
				break;
			// Explosion sprites
			
			case EXPLOSION_VERTICAL_0:
				sprite = new Sprite("explosion_vertical_0.png");
				break;
			case EXPLOSION_VERTICAL_1:
				sprite = new Sprite("explosion_vertical_1.png");
				break;
			case EXPLOSION_VERTICAL_2:
				sprite = new Sprite("explosion_vertical_2.png");
				break;
			
			case EXPLOSION_HORIZONTAL_0:
				sprite = new Sprite("explosion_horizontal_0.png");
				break;
			case EXPLOSION_HORIZONTAL_1:
				sprite = new Sprite("explosion_horizontal_1.png");
				break;
			case EXPLOSION_HORIZONTAL_2:
				sprite = new Sprite("explosion_horizontal_2.png");
				break;	
			
			case EXPLOSION_HORIZONTAL_LEFT_LAST_0:
				sprite = new Sprite("explosion_horizontal_left_last_0.png");
				break;
			case EXPLOSION_HORIZONTAL_LEFT_LAST_1:
				sprite = new Sprite("explosion_horizontal_left_last_1.png");
				break;
			case EXPLOSION_HORIZONTAL_LEFT_LAST_2:
				sprite = new Sprite("explosion_horizontal_left_last_2.png");
				break;
			
			case EXPLOSION_HORIZONTAL_RIGHT_LAST_0:
				sprite = new Sprite("explosion_horizontal_right_last0.png");
				break;
			case EXPLOSION_HORIZONTAL_RIGHT_LAST_1:
				sprite = new Sprite("explosion_horizontal_right_last1.png");
				break;
			case EXPLOSION_HORIZONTAL_RIGHT_LAST_2:
				sprite = new Sprite("explosion_horizontal_right_last2.png");
				break;
		
			case EXPLOSION_VERTICAL_TOP_LAST_0:
				sprite = new Sprite("explosion_vertical_top_last_0.png");
				break;
			case EXPLOSION_VERTICAL_TOP_LAST_1:
				sprite = new Sprite("explosion_vertical_top_last_1.png");
				break;
			case EXPLOSION_VERTICAL_TOP_LAST_2:
				sprite = new Sprite("explosion_vertical_top_last_2.png");
				break;
			
			case EXPLOSION_VERTICAL_DOWN_LAST_0:
				sprite = new Sprite("explosion_vertical_down_last_0.png");
				break;
			case EXPLOSION_VERTICAL_DOWN_LAST_1:
				sprite = new Sprite("explosion_vertical_down_last_1.png");
				break;
			case EXPLOSION_VERTICAL_DOWN_LAST_2:
				sprite = new Sprite("explosion_vertical_down_last_2.png");
				break;
			// Brick explosion sprites
			
			case BRICK_EXPLODED_0:
				sprite = new Sprite("brick_exploded_0.png");
				break;
			case BRICK_EXPLODED_1:
				sprite = new Sprite("brick_exploded_1.png");
				break;
			case BRICK_EXPLODED_2:
				sprite = new Sprite("brick_exploded_2.png");
				break;
			// Powerup sprites
			case POWERUP_BOMBS:
				sprite = new Sprite("powerup_bombs.png");
				break;
			case POWERUP_FLAMES:
				sprite = new Sprite("powerup_flames.png");
				break;
			case POWERUP_SPEED:
				sprite = new Sprite("powerup_speed.png");
				break;
			case POWERUP_WALLPASS:
				sprite = new Sprite("powerup_wallpass.png");
				break;
			case POWERUP_HEALTH_UP:
				sprite = new Sprite("powerup_health_up.png");
				break;
			case POWERUP_BOMB_PASS:
				sprite = new Sprite("powerup_bombpass.png");
				break;
			case POWERUP_FLAME_PASS:
				sprite = new Sprite("powerup_flamepass.png");
				break;
			case POWERUP_DETONATOR_PASS:
				sprite = new Sprite("powerup_detonator.png");
				break;
			case POWERUP_SPEED_PASS:
				sprite = new Sprite("powerup_speed.png");
				break;
			case POWERUP_WALL_PASS:
				sprite = new Sprite("powerup_wallpass.png");
				break;
			case ANIMATION_NULL:
				sprite = new Sprite("animation_null.png");
				break;
			default:
				// Return a default/error sprite or null
				sprite = new Sprite(0xFFFF0000); // Red square as error indicator
		}

		if (sprite != null) {
			spriteCache.put(id, sprite);
		}
	}

	public Sprite(String filename) {
		SIZE = DEFAULT_SIZE;
		_pixels = new int[SIZE * SIZE];
		BufferedImage image = SpriteManager.getImage(filename);
		if (image != null) {
			_realWidth = image.getWidth();
			_realHeight = image.getHeight();
			// Resize ảnh về kích thước mục tiêu
			BufferedImage resizedImage = resizeImage(image, SIZE, SIZE);
			resizedImage.getRGB(0, 0, SIZE, SIZE, _pixels, 0, SIZE);
		}
	}

	public Sprite(int color) {
		SIZE = DEFAULT_SIZE;
		_pixels = new int[SIZE * SIZE];
		setColor(color);
	}

	private void setColor(int color) {
		for (int i = 0; i < _pixels.length; i++) {
			_pixels[i] = color;
		}
	}

	private void load() {
		if (_sheet != null) {
			for (int y = 0; y < SIZE; y++) {
				for (int x = 0; x < SIZE; x++) {
					_pixels[x + y * SIZE] = _sheet._pixels[(x + _x) + (y + _y) * _sheet.SIZE];
				}
			}
		}
	}

	public int getSize() {
		return SIZE;
	}

	public int getPixel(int i) {
		return _pixels[i];
	}

	public Image getFxImage() {
		if (cachedImage == null) {
			WritableImage wr = new WritableImage(SIZE, SIZE);
			PixelWriter pw = wr.getPixelWriter();
			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {

					pw.setArgb(x, y, _pixels[x + y * SIZE]);

				}
			}
			cachedImage = new ImageView(wr).getImage();
		}
		return cachedImage;
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		java.awt.Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		g.dispose();
		return resizedImage;
	}
}
