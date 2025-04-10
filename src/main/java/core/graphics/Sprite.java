package core.graphics;

import javafx.scene.image.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Lưu trữ thông tin các pixel của 1 sprite (hình ảnh game)
 */
public class Sprite {

	public static final int DEFAULT_SIZE = 16;
	public static final int SCALED_SIZE = DEFAULT_SIZE * 3;
	private static final int TRANSPARENT_COLOR = 0xffff00ff;
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

	// Player
	public static final int PLAYER_UP = 4;
	public static final int PLAYER_DOWN = 5;
	public static final int PLAYER_LEFT = 6;
	public static final int PLAYER_RIGHT = 7;
	public static final int PLAYER_UP_1 = 8;
	public static final int PLAYER_UP_2 = 9;
	public static final int PLAYER_DOWN_1 = 10;
	public static final int PLAYER_DOWN_2 = 11;
	public static final int PLAYER_LEFT_1 = 12;
	public static final int PLAYER_LEFT_2 = 13;
	public static final int PLAYER_RIGHT_1 = 14;
	public static final int PLAYER_RIGHT_2 = 15;
	public static final int PLAYER_DEAD1 = 16;
	public static final int PLAYER_DEAD2 = 17;
	public static final int PLAYER_DEAD3 = 18;

	// Enemies - Balloom
	public static final int BALLOOM_LEFT1 = 19;
	public static final int BALLOOM_LEFT2 = 20;
	public static final int BALLOOM_LEFT3 = 21;
	public static final int BALLOOM_RIGHT1 = 22;
	public static final int BALLOOM_RIGHT2 = 23;
	public static final int BALLOOM_RIGHT3 = 24;
	public static final int BALLOOM_DEAD = 25;
	

	// Enemies - Oneal
	public static final int ONEAL_LEFT1 = 26;
	public static final int ONEAL_LEFT2 = 27;
	public static final int ONEAL_LEFT3 = 28;
	public static final int ONEAL_RIGHT1 = 29;
	public static final int ONEAL_RIGHT2 = 30;
	public static final int ONEAL_RIGHT3 = 31;
	public static final int ONEAL_DEAD = 32;

	// Enemies - Doll
	public static final int DOLL_LEFT1 = 33;
	public static final int DOLL_LEFT2 = 34;
	public static final int DOLL_LEFT3 = 35;
	public static final int DOLL_RIGHT1 = 36;
	public static final int DOLL_RIGHT2 = 37;
	public static final int DOLL_RIGHT3 = 38;
	public static final int DOLL_DEAD = 39;

	// Enemies - Minvo
	public static final int MINVO_LEFT1 = 40;
	public static final int MINVO_LEFT2 = 41;
	public static final int MINVO_LEFT3 = 42;
	public static final int MINVO_RIGHT1 = 43;
	public static final int MINVO_RIGHT2 = 44;
	public static final int MINVO_RIGHT3 = 45;
	public static final int MINVO_DEAD = 46;

	// Enemies - Kondoria
	public static final int KONDORIA_LEFT1 = 47;
	public static final int KONDORIA_LEFT2 = 48;
	public static final int KONDORIA_LEFT3 = 49;
	public static final int KONDORIA_RIGHT1 = 50;
	public static final int KONDORIA_RIGHT2 = 51;
	public static final int KONDORIA_RIGHT3 = 52;
	public static final int KONDORIA_DEAD = 53;

	// Mob dead
	public static final int MOB_DEAD1 = 54;
	public static final int MOB_DEAD2 = 55;
	public static final int MOB_DEAD3 = 56;

	// Bombs
	public static final int BOMB = 57;
	public static final int BOMB_1 = 58;
	public static final int BOMB_2 = 59;
	public static final int BOMB_EXPLODED = 60;
	public static final int BOMB_EXPLODED1 = 61;
	public static final int BOMB_EXPLODED2 = 62;

	// Explosions
	public static final int EXPLOSION_VERTICAL = 63;
	public static final int EXPLOSION_VERTICAL1 = 64;
	public static final int EXPLOSION_VERTICAL2 = 65;
	public static final int EXPLOSION_HORIZONTAL = 66;
	public static final int EXPLOSION_HORIZONTAL1 = 67;
	public static final int EXPLOSION_HORIZONTAL2 = 68;
	public static final int EXPLOSION_HORIZONTAL_LEFT_LAST = 69;
	public static final int EXPLOSION_HORIZONTAL_LEFT_LAST1 = 70;
	public static final int EXPLOSION_HORIZONTAL_LEFT_LAST2 = 71;
	public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST = 72;
	public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST1 = 73;
	public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST2 = 74;
	public static final int EXPLOSION_VERTICAL_TOP_LAST = 75;
	public static final int EXPLOSION_VERTICAL_TOP_LAST1 = 76;
	public static final int EXPLOSION_VERTICAL_TOP_LAST2 = 77;
	public static final int EXPLOSION_VERTICAL_DOWN_LAST = 78;
	public static final int EXPLOSION_VERTICAL_DOWN_LAST1 = 79;
	public static final int EXPLOSION_VERTICAL_DOWN_LAST2 = 80;

	// Brick explosions
	public static final int BRICK_EXPLODED = 81;
	public static final int BRICK_EXPLODED1 = 82;
	public static final int BRICK_EXPLODED2 = 83;

	// Powerups
	public static final int POWERUP_BOMBS = 84;
	public static final int POWERUP_FLAMES = 85;
	public static final int POWERUP_SPEED = 86;
	public static final int POWERUP_WALLPASS = 87;
	public static final int POWERUP_HEALTH_UP = 88;
	public static final int POWERUP_BOMB_PASS = 89;
	public static final int POWERUP_FLAME_PASS = 90;
	public static final int POWERUP_DETONATOR_PASS = 91;
	public static final int POWERUP_SPEED_PASS = 92;
	public static final int POWERUP_WALL_PASS = 93;
	public static final int ANIMATION_NULL = 94;

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
				sprite = new Sprite(DEFAULT_SIZE, 6, 0, SpriteSheet.tiles, 16, 16);
				break;
			case BRICK:
				sprite = new Sprite(DEFAULT_SIZE, 7, 0, SpriteSheet.tiles, 16, 16);
				break;
			case WALL:
				sprite = new Sprite(DEFAULT_SIZE, 5, 0, SpriteSheet.tiles, 16, 16);
				break;
			case PORTAL:
				sprite = new Sprite(DEFAULT_SIZE, 4, 0, SpriteSheet.tiles, 14, 14);
				break;
			// Player sprites
			case PLAYER_UP:
				sprite = new Sprite(DEFAULT_SIZE, 0, 0, SpriteSheet.tiles, 12, 16);
				break;
			case PLAYER_DOWN:
				sprite = new Sprite(DEFAULT_SIZE, 2, 0, SpriteSheet.tiles, 12, 15);
				break;
			case PLAYER_LEFT:
				sprite = new Sprite(DEFAULT_SIZE, 3, 0, SpriteSheet.tiles, 10, 15);
				break;
			case PLAYER_RIGHT:
				sprite = new Sprite(DEFAULT_SIZE, 1, 0, SpriteSheet.tiles, 10, 16);
				break;
			case PLAYER_UP_1:
				sprite = new Sprite(DEFAULT_SIZE, 0, 1, SpriteSheet.tiles, 12, 16);
				break;
			case PLAYER_UP_2:
				sprite = new Sprite(DEFAULT_SIZE, 0, 2, SpriteSheet.tiles, 12, 15);
				break;
			case PLAYER_DOWN_1:
				sprite = new Sprite(DEFAULT_SIZE, 2, 1, SpriteSheet.tiles, 12, 15);
				break;
			case PLAYER_DOWN_2:
				sprite = new Sprite(DEFAULT_SIZE, 2, 2, SpriteSheet.tiles, 12, 16);
				break;
			case PLAYER_LEFT_1:
				sprite = new Sprite(DEFAULT_SIZE, 3, 1, SpriteSheet.tiles, 11, 16);
				break;
			case PLAYER_LEFT_2:
				sprite = new Sprite(DEFAULT_SIZE, 3, 2, SpriteSheet.tiles, 12, 16);
				break;
			case PLAYER_RIGHT_1:
				sprite = new Sprite(DEFAULT_SIZE, 1, 1, SpriteSheet.tiles, 11, 16);
				break;
			case PLAYER_RIGHT_2:
				sprite = new Sprite(DEFAULT_SIZE, 1, 2, SpriteSheet.tiles, 12, 16);
				break;
			case PLAYER_DEAD1:
				sprite = new Sprite(DEFAULT_SIZE, 4, 2, SpriteSheet.tiles, 14, 16);
				break;
			case PLAYER_DEAD2:
				sprite = new Sprite(DEFAULT_SIZE, 5, 2, SpriteSheet.tiles, 13, 15);
				break;
			case PLAYER_DEAD3:
				sprite = new Sprite(DEFAULT_SIZE, 6, 2, SpriteSheet.tiles, 16, 16);
				break;
			// Balloom sprites
			case BALLOOM_LEFT1:
				sprite = new Sprite(DEFAULT_SIZE, 9, 0, SpriteSheet.tiles, 16, 16);
				break;
			case BALLOOM_LEFT2:
				sprite = new Sprite(DEFAULT_SIZE, 9, 1, SpriteSheet.tiles, 16, 16);
				break;
			case BALLOOM_LEFT3:
				sprite = new Sprite(DEFAULT_SIZE, 9, 2, SpriteSheet.tiles, 16, 16);
				break;
			case BALLOOM_RIGHT1:
				sprite = new Sprite(DEFAULT_SIZE, 10, 0, SpriteSheet.tiles, 16, 16);
				break;
			case BALLOOM_RIGHT2:
				sprite = new Sprite(DEFAULT_SIZE, 10, 1, SpriteSheet.tiles, 16, 16);
				break;
			case BALLOOM_RIGHT3:
				sprite = new Sprite(DEFAULT_SIZE, 10, 2, SpriteSheet.tiles, 16, 16);
				break;
			case BALLOOM_DEAD:
				sprite = new Sprite(DEFAULT_SIZE, 9, 3, SpriteSheet.tiles, 16, 16);
				break;
			// Oneal sprites
			case ONEAL_LEFT1:
				sprite = new Sprite(DEFAULT_SIZE, 11, 0, SpriteSheet.tiles, 16, 16);
				break;
			case ONEAL_LEFT2:
				sprite = new Sprite(DEFAULT_SIZE, 11, 1, SpriteSheet.tiles, 16, 16);
				break;
			case ONEAL_LEFT3:
				sprite = new Sprite(DEFAULT_SIZE, 11, 2, SpriteSheet.tiles, 16, 16);
				break;
			case ONEAL_RIGHT1:
				sprite = new Sprite(DEFAULT_SIZE, 12, 0, SpriteSheet.tiles, 16, 16);
				break;
			case ONEAL_RIGHT2:
				sprite = new Sprite(DEFAULT_SIZE, 12, 1, SpriteSheet.tiles, 16, 16);
				break;
			case ONEAL_RIGHT3:
				sprite = new Sprite(DEFAULT_SIZE, 12, 2, SpriteSheet.tiles, 16, 16);
				break;
			case ONEAL_DEAD:
				sprite = new Sprite(DEFAULT_SIZE, 11, 3, SpriteSheet.tiles, 16, 16);
				break;
			// Doll sprites
			case DOLL_LEFT1:
				sprite = new Sprite(DEFAULT_SIZE, 13, 0, SpriteSheet.tiles, 16, 16);
				break;
			case DOLL_LEFT2:
				sprite = new Sprite(DEFAULT_SIZE, 13, 1, SpriteSheet.tiles, 16, 16);
				break;
			case DOLL_LEFT3:
				sprite = new Sprite(DEFAULT_SIZE, 13, 2, SpriteSheet.tiles, 16, 16);
				break;
			case DOLL_RIGHT1:
				sprite = new Sprite(DEFAULT_SIZE, 14, 0, SpriteSheet.tiles, 16, 16);
				break;
			case DOLL_RIGHT2:
				sprite = new Sprite(DEFAULT_SIZE, 14, 1, SpriteSheet.tiles, 16, 16);
				break;
			case DOLL_RIGHT3:
				sprite = new Sprite(DEFAULT_SIZE, 14, 2, SpriteSheet.tiles, 16, 16);
				break;
			case DOLL_DEAD:
				sprite = new Sprite(DEFAULT_SIZE, 13, 3, SpriteSheet.tiles, 16, 16);
				break;
			// Minvo sprites
			case MINVO_LEFT1:
				sprite = new Sprite(DEFAULT_SIZE, 8, 5, SpriteSheet.tiles, 16, 16);
				break;
			case MINVO_LEFT2:
				sprite = new Sprite(DEFAULT_SIZE, 8, 6, SpriteSheet.tiles, 16, 16);
				break;
			case MINVO_LEFT3:
				sprite = new Sprite(DEFAULT_SIZE, 8, 7, SpriteSheet.tiles, 16, 16);
				break;
			case MINVO_RIGHT1:
				sprite = new Sprite(DEFAULT_SIZE, 9, 5, SpriteSheet.tiles, 16, 16);
				break;
			case MINVO_RIGHT2:
				sprite = new Sprite(DEFAULT_SIZE, 9, 6, SpriteSheet.tiles, 16, 16);
				break;
			case MINVO_RIGHT3:
				sprite = new Sprite(DEFAULT_SIZE, 9, 7, SpriteSheet.tiles, 16, 16);
				break;
			case MINVO_DEAD:
				sprite = new Sprite(DEFAULT_SIZE, 8, 8, SpriteSheet.tiles, 16, 16);
				break;
			// Kondoria sprites
			case KONDORIA_LEFT1:
				sprite = new Sprite(DEFAULT_SIZE, 10, 5, SpriteSheet.tiles, 16, 16);
				break;
			case KONDORIA_LEFT2:
				sprite = new Sprite(DEFAULT_SIZE, 10, 6, SpriteSheet.tiles, 16, 16);
				break;
			case KONDORIA_LEFT3:
				sprite = new Sprite(DEFAULT_SIZE, 10, 7, SpriteSheet.tiles, 16, 16);
				break;
			case KONDORIA_RIGHT1:
				sprite = new Sprite(DEFAULT_SIZE, 11, 5, SpriteSheet.tiles, 16, 16);
				break;
			case KONDORIA_RIGHT2:
				sprite = new Sprite(DEFAULT_SIZE, 11, 6, SpriteSheet.tiles, 16, 16);
				break;
			case KONDORIA_RIGHT3:
				sprite = new Sprite(DEFAULT_SIZE, 11, 7, SpriteSheet.tiles, 16, 16);
				break;
			case KONDORIA_DEAD:
				sprite = new Sprite(DEFAULT_SIZE, 10, 8, SpriteSheet.tiles, 16, 16);
				break;
			// Mob dead sprites
			case MOB_DEAD1:
				sprite = new Sprite(DEFAULT_SIZE, 15, 0, SpriteSheet.tiles, 16, 16);
				break;
			case MOB_DEAD2:
				sprite = new Sprite(DEFAULT_SIZE, 15, 1, SpriteSheet.tiles, 16, 16);
				break;
			case MOB_DEAD3:
				sprite = new Sprite(DEFAULT_SIZE, 15, 2, SpriteSheet.tiles, 16, 16);
				break;
			// Bomb sprites
			case BOMB:
				sprite = new Sprite(DEFAULT_SIZE, 0, 3, SpriteSheet.tiles, 15, 15);
				break;
			case BOMB_1:
				sprite = new Sprite(DEFAULT_SIZE, 1, 3, SpriteSheet.tiles, 13, 15);
				break;
			case BOMB_2:
				sprite = new Sprite(DEFAULT_SIZE, 2, 3, SpriteSheet.tiles, 12, 14);
				break;
			// Explosion sprites
			case BOMB_EXPLODED:
				sprite = new Sprite(DEFAULT_SIZE, 0, 4, SpriteSheet.tiles, 16, 16);
				break;
			case BOMB_EXPLODED1:
				sprite = new Sprite(DEFAULT_SIZE, 0, 5, SpriteSheet.tiles, 16, 16);
				break;
			case BOMB_EXPLODED2:
				sprite = new Sprite(DEFAULT_SIZE, 0, 6, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL:
				sprite = new Sprite(DEFAULT_SIZE, 1, 5, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL1:
				sprite = new Sprite(DEFAULT_SIZE, 2, 5, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL2:
				sprite = new Sprite(DEFAULT_SIZE, 3, 5, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL:
				sprite = new Sprite(DEFAULT_SIZE, 1, 7, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL1:
				sprite = new Sprite(DEFAULT_SIZE, 1, 8, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL2:
				sprite = new Sprite(DEFAULT_SIZE, 1, 9, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL_LEFT_LAST:
				sprite = new Sprite(DEFAULT_SIZE, 0, 7, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL_LEFT_LAST1:
				sprite = new Sprite(DEFAULT_SIZE, 0, 8, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL_LEFT_LAST2:
				sprite = new Sprite(DEFAULT_SIZE, 0, 9, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL_RIGHT_LAST:
				sprite = new Sprite(DEFAULT_SIZE, 2, 7, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL_RIGHT_LAST1:
				sprite = new Sprite(DEFAULT_SIZE, 2, 8, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_HORIZONTAL_RIGHT_LAST2:
				sprite = new Sprite(DEFAULT_SIZE, 2, 9, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL_TOP_LAST:
				sprite = new Sprite(DEFAULT_SIZE, 1, 4, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL_TOP_LAST1:
				sprite = new Sprite(DEFAULT_SIZE, 2, 4, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL_TOP_LAST2:
				sprite = new Sprite(DEFAULT_SIZE, 3, 4, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL_DOWN_LAST:
				sprite = new Sprite(DEFAULT_SIZE, 1, 6, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL_DOWN_LAST1:
				sprite = new Sprite(DEFAULT_SIZE, 2, 6, SpriteSheet.tiles, 16, 16);
				break;
			case EXPLOSION_VERTICAL_DOWN_LAST2:
				sprite = new Sprite(DEFAULT_SIZE, 3, 6, SpriteSheet.tiles, 16, 16);
				break;
			// Brick explosion sprites
			case BRICK_EXPLODED:
				sprite = new Sprite(DEFAULT_SIZE, 7, 1, SpriteSheet.tiles, 16, 16);
				break;
			case BRICK_EXPLODED1:
				sprite = new Sprite(DEFAULT_SIZE, 7, 2, SpriteSheet.tiles, 16, 16);
				break;
			case BRICK_EXPLODED2:
				sprite = new Sprite(DEFAULT_SIZE, 7, 3, SpriteSheet.tiles, 16, 16);
				break;
			// Powerup sprites
			case POWERUP_BOMBS:
				sprite = new Sprite(DEFAULT_SIZE, 0, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_FLAMES:
				sprite = new Sprite(DEFAULT_SIZE, 1, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_SPEED:
				sprite = new Sprite(DEFAULT_SIZE, 2, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_WALLPASS:
				sprite = new Sprite(DEFAULT_SIZE, 3, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_HEALTH_UP:
				sprite = new Sprite(DEFAULT_SIZE, 4, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_BOMB_PASS:
				sprite = new Sprite(DEFAULT_SIZE, 5, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_FLAME_PASS:
				sprite = new Sprite(DEFAULT_SIZE, 6, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_DETONATOR_PASS:
				sprite = new Sprite(DEFAULT_SIZE, 7, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_SPEED_PASS:
				sprite = new Sprite(DEFAULT_SIZE, 8, 10, SpriteSheet.tiles, 16, 16);
				break;
			case POWERUP_WALL_PASS:
				sprite = new Sprite(DEFAULT_SIZE, 9, 10, SpriteSheet.tiles, 16, 16);
				break;
			case ANIMATION_NULL:
				sprite = new Sprite(DEFAULT_SIZE, 11, 11, SpriteSheet.tiles, 16, 16);
				break;
			default:
				// Return a default/error sprite or null
				sprite = new Sprite(DEFAULT_SIZE, 0xFFFF0000); // Red square as error indicator
		}

		if (sprite != null) {
			spriteCache.put(id, sprite);
		}
	}

	public Sprite(int size, int x, int y, SpriteSheet sheet, int rw, int rh) {
		SIZE = size;
		_pixels = new int[SIZE * SIZE];
		_x = x * SIZE;
		_y = y * SIZE;
		_sheet = sheet;
		_realWidth = rw;
		_realHeight = rh;
		load();
	}

	public Sprite(int size, int color) {
		SIZE = size;
		_pixels = new int[SIZE * SIZE];
		setColor(color);
	}

	private void setColor(int color) {
		for (int i = 0; i < _pixels.length; i++) {
			_pixels[i] = color;
		}
	}

	private void load() {
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				_pixels[x + y * SIZE] = _sheet._pixels[(x + _x) + (y + _y) * _sheet.SIZE];
			}
		}
	}

	public static Sprite movingSprite(Sprite normal, Sprite x1, Sprite x2, int animate, int time) {
		int calc = animate % time;
		int diff = time / 3;

		if (calc < diff) {
			return normal;
		}

		if (calc < diff * 2) {
			return x1;
		}

		return x2;
	}

	public static Sprite movingSprite(Sprite x1, Sprite x2, int animate, int time) {
		int diff = time / 2;
		return (animate % time > diff) ? x1 : x2;
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
					if (_pixels[x + y * SIZE] == TRANSPARENT_COLOR) {
						pw.setArgb(x, y, 0);
					} else {
						pw.setArgb(x, y, _pixels[x + y * SIZE]);
					}
				}
			}
			Image input = new ImageView(wr).getImage();
			cachedImage = resample(input, SCALED_SIZE / DEFAULT_SIZE);
		}
		return cachedImage;
	}

	private Image resample(Image input, int scaleFactor) {
		final int W = (int) input.getWidth();
		final int H = (int) input.getHeight();
		final int S = scaleFactor;

		WritableImage output = new WritableImage(
				W * S,
				H * S);

		PixelReader reader = input.getPixelReader();
		PixelWriter writer = output.getPixelWriter();

		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				final int argb = reader.getArgb(x, y);
				for (int dy = 0; dy < S; dy++) {
					for (int dx = 0; dx < S; dx++) {
						writer.setArgb(x * S + dx, y * S + dy, argb);
					}
				}
			}
		}

		return output;
	}
}
