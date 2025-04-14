package core.graphics;

import javafx.scene.image.*;
import java.awt.image.BufferedImage;

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
	public static final Sprite[] sprites = new Sprite[100];
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

	static {
		// Terrain
		sprites[GRASS] = new Sprite(DEFAULT_SIZE, "grass.png");
		sprites[BRICK] = new Sprite(DEFAULT_SIZE, "brick.png");
		sprites[WALL] = new Sprite(DEFAULT_SIZE, "wall.png");
		sprites[PORTAL] = new Sprite(DEFAULT_SIZE, "portal.png");

		// Player sprites
		sprites[PLAYER_UP] = new Sprite(DEFAULT_SIZE, "player_up.png");
		sprites[PLAYER_DOWN] = new Sprite(DEFAULT_SIZE, "player_down.png");
		sprites[PLAYER_LEFT] = new Sprite(DEFAULT_SIZE, "player_left.png");
		sprites[PLAYER_RIGHT] = new Sprite(DEFAULT_SIZE, "player_right.png");
		sprites[PLAYER_UP_1] = new Sprite(DEFAULT_SIZE, "player_up_1.png");
		sprites[PLAYER_UP_2] = new Sprite(DEFAULT_SIZE, "player_up_2.png");
		sprites[PLAYER_DOWN_1] = new Sprite(DEFAULT_SIZE, "player_down_1.png");
		sprites[PLAYER_DOWN_2] = new Sprite(DEFAULT_SIZE, "player_down_2.png");
		sprites[PLAYER_LEFT_1] = new Sprite(DEFAULT_SIZE, "player_left_1.png");
		sprites[PLAYER_LEFT_2] = new Sprite(DEFAULT_SIZE, "player_left_2.png");
		sprites[PLAYER_RIGHT_1] = new Sprite(DEFAULT_SIZE, "player_right_1.png");
		sprites[PLAYER_RIGHT_2] = new Sprite(DEFAULT_SIZE, "player_right_2.png");
		sprites[PLAYER_DEAD1] = new Sprite(DEFAULT_SIZE, "player_dead1.png");
		sprites[PLAYER_DEAD2] = new Sprite(DEFAULT_SIZE, "player_dead2.png");
		sprites[PLAYER_DEAD3] = new Sprite(DEFAULT_SIZE, "player_dead3.png");

		// Balloom sprites
		sprites[BALLOOM_LEFT1] = new Sprite(DEFAULT_SIZE, "balloom_left1.png");
		sprites[BALLOOM_LEFT2] = new Sprite(DEFAULT_SIZE, "balloom_left2.png");
		sprites[BALLOOM_LEFT3] = new Sprite(DEFAULT_SIZE, "balloom_left3.png");
		sprites[BALLOOM_RIGHT1] = new Sprite(DEFAULT_SIZE, "balloom_right1.png");
		sprites[BALLOOM_RIGHT2] = new Sprite(DEFAULT_SIZE, "balloom_right2.png");
		sprites[BALLOOM_RIGHT3] = new Sprite(DEFAULT_SIZE, "balloom_right3.png");
		sprites[BALLOOM_DEAD] = new Sprite(DEFAULT_SIZE, "balloom_dead.png");

		// Oneal sprites
		sprites[ONEAL_LEFT1] = new Sprite(DEFAULT_SIZE, "oneal_left1.png");
		sprites[ONEAL_LEFT2] = new Sprite(DEFAULT_SIZE, "oneal_left2.png");
		sprites[ONEAL_LEFT3] = new Sprite(DEFAULT_SIZE, "oneal_left3.png");
		sprites[ONEAL_RIGHT1] = new Sprite(DEFAULT_SIZE, "oneal_right1.png");
		sprites[ONEAL_RIGHT2] = new Sprite(DEFAULT_SIZE, "oneal_right2.png");
		sprites[ONEAL_RIGHT3] = new Sprite(DEFAULT_SIZE, "oneal_right3.png");
		sprites[ONEAL_DEAD] = new Sprite(DEFAULT_SIZE, "oneal_dead.png");

		// Doll sprites
		sprites[DOLL_LEFT1] = new Sprite(DEFAULT_SIZE, "doll_left1.png");
		sprites[DOLL_LEFT2] = new Sprite(DEFAULT_SIZE, "doll_left2.png");
		sprites[DOLL_LEFT3] = new Sprite(DEFAULT_SIZE, "doll_left3.png");
		sprites[DOLL_RIGHT1] = new Sprite(DEFAULT_SIZE, "doll_right1.png");
		sprites[DOLL_RIGHT2] = new Sprite(DEFAULT_SIZE, "doll_right2.png");
		sprites[DOLL_RIGHT3] = new Sprite(DEFAULT_SIZE, "doll_right3.png");
		sprites[DOLL_DEAD] = new Sprite(DEFAULT_SIZE, "doll_dead.png");

		// Minvo sprites
		sprites[MINVO_LEFT1] = new Sprite(DEFAULT_SIZE, "minvo_left1.png");
		sprites[MINVO_LEFT2] = new Sprite(DEFAULT_SIZE, "minvo_left2.png");
		sprites[MINVO_LEFT3] = new Sprite(DEFAULT_SIZE, "minvo_left3.png");
		sprites[MINVO_RIGHT1] = new Sprite(DEFAULT_SIZE, "minvo_right1.png");
		sprites[MINVO_RIGHT2] = new Sprite(DEFAULT_SIZE, "minvo_right2.png");
		sprites[MINVO_RIGHT3] = new Sprite(DEFAULT_SIZE, "minvo_right3.png");
		sprites[MINVO_DEAD] = new Sprite(DEFAULT_SIZE, "minvo_dead.png");

		// Kondoria sprites
		sprites[KONDORIA_LEFT1] = new Sprite(DEFAULT_SIZE, "kondoria_left1.png");
		sprites[KONDORIA_LEFT2] = new Sprite(DEFAULT_SIZE, "kondoria_left2.png");
		sprites[KONDORIA_LEFT3] = new Sprite(DEFAULT_SIZE, "kondoria_left3.png");
		sprites[KONDORIA_RIGHT1] = new Sprite(DEFAULT_SIZE, "kondoria_right1.png");
		sprites[KONDORIA_RIGHT2] = new Sprite(DEFAULT_SIZE, "kondoria_right2.png");
		sprites[KONDORIA_RIGHT3] = new Sprite(DEFAULT_SIZE, "kondoria_right3.png");
		sprites[KONDORIA_DEAD] = new Sprite(DEFAULT_SIZE, "kondoria_dead.png");

		// Mob dead sprites
		sprites[MOB_DEAD1] = new Sprite(DEFAULT_SIZE, "mob_dead1.png");
		sprites[MOB_DEAD2] = new Sprite(DEFAULT_SIZE, "mob_dead2.png");
		sprites[MOB_DEAD3] = new Sprite(DEFAULT_SIZE, "mob_dead3.png");

		// Bomb sprites
		sprites[BOMB] = new Sprite(DEFAULT_SIZE, "bomb.png");
		sprites[BOMB_1] = new Sprite(DEFAULT_SIZE, "bomb_1.png");
		sprites[BOMB_2] = new Sprite(DEFAULT_SIZE, "bomb_2.png");
		sprites[BOMB_EXPLODED] = new Sprite(DEFAULT_SIZE, "bomb_exploded.png");
		sprites[BOMB_EXPLODED1] = new Sprite(DEFAULT_SIZE, "bomb_exploded1.png");
		sprites[BOMB_EXPLODED2] = new Sprite(DEFAULT_SIZE, "bomb_exploded2.png");

		// Explosion sprites
		sprites[EXPLOSION_VERTICAL] = new Sprite(DEFAULT_SIZE, "explosion_vertical.png");
		sprites[EXPLOSION_VERTICAL1] = new Sprite(DEFAULT_SIZE, "explosion_vertical1.png");
		sprites[EXPLOSION_VERTICAL2] = new Sprite(DEFAULT_SIZE, "explosion_vertical2.png");
		sprites[EXPLOSION_HORIZONTAL] = new Sprite(DEFAULT_SIZE, "explosion_horizontal.png");
		sprites[EXPLOSION_HORIZONTAL1] = new Sprite(DEFAULT_SIZE, "explosion_horizontal1.png");
		sprites[EXPLOSION_HORIZONTAL2] = new Sprite(DEFAULT_SIZE, "explosion_horizontal2.png");
		sprites[EXPLOSION_HORIZONTAL_LEFT_LAST] = new Sprite(DEFAULT_SIZE, "explosion_horizontal_left_last.png");
		sprites[EXPLOSION_HORIZONTAL_LEFT_LAST1] = new Sprite(DEFAULT_SIZE, "explosion_horizontal_left_last1.png");
		sprites[EXPLOSION_HORIZONTAL_LEFT_LAST2] = new Sprite(DEFAULT_SIZE, "explosion_horizontal_left_last2.png");
		sprites[EXPLOSION_HORIZONTAL_RIGHT_LAST] = new Sprite(DEFAULT_SIZE, "explosion_horizontal_right_last.png");
		sprites[EXPLOSION_HORIZONTAL_RIGHT_LAST1] = new Sprite(DEFAULT_SIZE, "explosion_horizontal_right_last1.png");
		sprites[EXPLOSION_HORIZONTAL_RIGHT_LAST2] = new Sprite(DEFAULT_SIZE, "explosion_horizontal_right_last2.png");
		sprites[EXPLOSION_VERTICAL_TOP_LAST] = new Sprite(DEFAULT_SIZE, "explosion_vertical_top_last.png");
		sprites[EXPLOSION_VERTICAL_TOP_LAST1] = new Sprite(DEFAULT_SIZE, "explosion_vertical_top_last1.png");
		sprites[EXPLOSION_VERTICAL_TOP_LAST2] = new Sprite(DEFAULT_SIZE, "explosion_vertical_top_last2.png");
		sprites[EXPLOSION_VERTICAL_DOWN_LAST] = new Sprite(DEFAULT_SIZE, "explosion_vertical_down_last.png");
		sprites[EXPLOSION_VERTICAL_DOWN_LAST1] = new Sprite(DEFAULT_SIZE, "explosion_vertical_down_last1.png");
		sprites[EXPLOSION_VERTICAL_DOWN_LAST2] = new Sprite(DEFAULT_SIZE, "explosion_vertical_down_last2.png");

		// Brick explosion sprites
		sprites[BRICK_EXPLODED] = new Sprite(DEFAULT_SIZE, "brick_exploded.png");
		sprites[BRICK_EXPLODED1] = new Sprite(DEFAULT_SIZE, "brick_exploded1.png");
		sprites[BRICK_EXPLODED2] = new Sprite(DEFAULT_SIZE, "brick_exploded2.png");

		// Powerup sprites
		sprites[POWERUP_BOMBS] = new Sprite(DEFAULT_SIZE, "powerup_bombs.png");
		sprites[POWERUP_FLAMES] = new Sprite(DEFAULT_SIZE, "powerup_flames.png");
		sprites[POWERUP_SPEED] = new Sprite(DEFAULT_SIZE, "powerup_speed.png");
		sprites[POWERUP_WALLPASS] = new Sprite(DEFAULT_SIZE, "powerup_wallpass.png");
		sprites[POWERUP_BOMB_PASS] = new Sprite(DEFAULT_SIZE, "powerup_bombpass.png");
		sprites[POWERUP_FLAME_PASS] = new Sprite(DEFAULT_SIZE, "powerup_flamepass.png");
		sprites[POWERUP_DETONATOR_PASS] = new Sprite(DEFAULT_SIZE, "powerup_detonator.png");
		sprites[POWERUP_SPEED_PASS] = new Sprite(DEFAULT_SIZE, "powerup_speed.png");
		sprites[POWERUP_WALL_PASS] = new Sprite(DEFAULT_SIZE, "powerup_wallpass.png");
		sprites[ANIMATION_NULL] = new Sprite(DEFAULT_SIZE, "animation_null.png");
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

	public Sprite(int size, String filename) {
		SIZE = size;
		_pixels = new int[SIZE * SIZE];
		BufferedImage image = SpriteManager.getSprite(filename);
		if (image != null) {
			_realWidth = image.getWidth();
			_realHeight = image.getHeight();
			image.getRGB(0, 0, _realWidth, _realHeight, _pixels, 0, _realWidth);
		}
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
		if (_sheet != null) {
			for (int y = 0; y < SIZE; y++) {
				for (int x = 0; x < SIZE; x++) {
					_pixels[x + y * SIZE] = _sheet._pixels[(x + _x) + (y + _y) * _sheet.SIZE];
				}
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
