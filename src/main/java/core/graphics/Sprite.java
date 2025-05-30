	package core.graphics;

	import javafx.scene.image.*;
	import java.awt.image.BufferedImage;
	import java.util.HashMap;
	import java.util.Map;

	public class Sprite {

		public static final int DEFAULT_SIZE = 48;

		public final int SIZE;
		public int[] _pixels;
		protected int _realWidth;
		protected int _realHeight;
		private static final Map<Integer, Sprite> spriteCache = new HashMap<>();
		private Image cachedImage = null;

		public static final int GRASS = 0;
		public static final int BRICK = 1;
		public static final int WALL = 2;
		public static final int PORTAL = 3;

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

		public static final int BALLOOM_LEFT_0 = 44;
		public static final int BALLOOM_LEFT_1 = 45;
		public static final int BALLOOM_LEFT_2 = 46;
		public static final int BALLOOM_LEFT_3 = 47;
		public static final int BALLOOM_RIGHT_0 = 48;
		public static final int BALLOOM_RIGHT_1 = 49;
		public static final int BALLOOM_RIGHT_2 = 50;
		public static final int BALLOOM_RIGHT_3 = 51;
		public static final int BALLOOM_DEAD_0 = 52;
		public static final int BALLOOM_DEAD_1 = 53;
		public static final int BALLOOM_DEAD_2 = 54;
		public static final int BALLOOM_DEAD_3 = 55;
		public static final int BALLOOM_DEAD_4 = 56;

		public static final int ONEAL_LEFT_0 = 57;
		public static final int ONEAL_LEFT_1 = 58;
		public static final int ONEAL_LEFT_2 = 59;
		public static final int ONEAL_LEFT_3 = 60;
		public static final int ONEAL_RIGHT_0 = 61;
		public static final int ONEAL_RIGHT_1 = 62;
		public static final int ONEAL_RIGHT_2 = 63;
		public static final int ONEAL_RIGHT_3 = 64;
		public static final int ONEAL_DEAD_0 = 65;
		public static final int ONEAL_DEAD_1 = 66;
		public static final int ONEAL_DEAD_2 = 67;
		public static final int ONEAL_DEAD_3 = 68;
		public static final int ONEAL_DEAD_4 = 69;

		public static final int DOLL_LEFT_0 = 70;
		public static final int DOLL_LEFT_1 = 71;
		public static final int DOLL_LEFT_2 = 72;
		public static final int DOLL_LEFT_3 = 73;
		public static final int DOLL_RIGHT_0 = 74;
		public static final int DOLL_RIGHT_1 = 75;
		public static final int DOLL_RIGHT_2 = 76;
		public static final int DOLL_RIGHT_3 = 77;
		public static final int DOLL_DEAD_0 = 78;
		public static final int DOLL_DEAD_1 = 79;
		public static final int DOLL_DEAD_2 = 80;
		public static final int DOLL_DEAD_3 = 81;
		public static final int DOLL_DEAD_4 = 82;

		public static final int MINVO_LEFT_0 = 83;
		public static final int MINVO_LEFT_1 = 84;
		public static final int MINVO_LEFT_2 = 85;
		public static final int MINVO_LEFT_3 = 86;
		public static final int MINVO_RIGHT_0 = 87;
		public static final int MINVO_RIGHT_1 = 88;
		public static final int MINVO_RIGHT_2 = 89;
		public static final int MINVO_RIGHT_3 = 90;
		public static final int MINVO_DEAD_0 = 91;
		public static final int MINVO_DEAD_1 = 92;
		public static final int MINVO_DEAD_2 = 93;
		public static final int MINVO_DEAD_3 = 94;
		public static final int MINVO_DEAD_4 = 95;

		public static final int GHOST_LEFT_0 = 96;
		public static final int GHOST_LEFT_1 = 97;
		public static final int GHOST_LEFT_2 = 98;
		public static final int GHOST_LEFT_3 = 99;
		public static final int GHOST_RIGHT_0 = 100;
		public static final int GHOST_RIGHT_1 = 101;
		public static final int GHOST_RIGHT_2 = 102;
		public static final int GHOST_RIGHT_3 = 103;
		public static final int GHOST_DEAD_0 = 104;
		public static final int GHOST_DEAD_1 = 105;
		public static final int GHOST_DEAD_2 = 106;
		public static final int GHOST_DEAD_3 = 107;
		public static final int GHOST_DEAD_4 = 108;

		public static final int PLAYER1_BOMB_0 = 109;
		public static final int PLAYER1_BOMB_1 = 110;
		public static final int PLAYER1_BOMB_2 = 111;
		public static final int PLAYER1_BOMB_3 = 112;
		public static final int PLAYER1_BOMB_4 = 113;
		public static final int PLAYER1_BOMB_5 = 114;
		public static final int PLAYER1_BOMB_6 = 115;
		public static final int PLAYER1_BOMB_7 = 116;
		public static final int PLAYER1_BOMB_8 = 117;
		public static final int PLAYER1_BOMB_9 = 118;
		public static final int PLAYER2_BOMB_0 = 119;
		public static final int PLAYER2_BOMB_1 = 120;
		public static final int PLAYER2_BOMB_2 = 121;
		public static final int BOMB_EXPLODED_0 = 122;
		public static final int BOMB_EXPLODED_1 = 123;
		public static final int BOMB_EXPLODED_2 = 124;

		public static final int EXPLOSION_VERTICAL_0 = 125;
		public static final int EXPLOSION_VERTICAL_1 = 126;
		public static final int EXPLOSION_VERTICAL_2 = 127;
		public static final int EXPLOSION_HORIZONTAL_0 = 128;
		public static final int EXPLOSION_HORIZONTAL_1 = 129;
		public static final int EXPLOSION_HORIZONTAL_2 = 130;
		public static final int EXPLOSION_HORIZONTAL_LEFT_LAST_0 = 131;
		public static final int EXPLOSION_HORIZONTAL_LEFT_LAST_1 = 132;
		public static final int EXPLOSION_HORIZONTAL_LEFT_LAST_2 = 133;

		public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST_0 = 134;
		public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST_1 = 135;
		public static final int EXPLOSION_HORIZONTAL_RIGHT_LAST_2 = 136;

		public static final int EXPLOSION_VERTICAL_TOP_LAST_0 = 137;
		public static final int EXPLOSION_VERTICAL_TOP_LAST_1 = 138;
		public static final int EXPLOSION_VERTICAL_TOP_LAST_2 = 139;
		public static final int EXPLOSION_VERTICAL_DOWN_LAST_0 = 140;
		public static final int EXPLOSION_VERTICAL_DOWN_LAST_1 = 141;
		public static final int EXPLOSION_VERTICAL_DOWN_LAST_2 = 142;

		public static final int BRICK_EXPLODED_0 = 143;
		public static final int BRICK_EXPLODED_1 = 144;
		public static final int BRICK_EXPLODED_2 = 145;

		public static final int POWERUP_BOMBS = 146;
		public static final int POWERUP_FLAMES = 147;
		public static final int POWERUP_SPEED = 148;
		public static final int POWERUP_WALLPASS = 149;
		public static final int POWERUP_HEALTH_UP = 150;
		public static final int POWERUP_BOMB_PASS = 151;
		public static final int POWERUP_FLAME_PASS = 152;
		public static final int POWERUP_DETONATOR_PASS = 153;
		public static final int POWERUP_SPEED_PASS = 154;
		public static final int POWERUP_WALL_PASS = 155;
		public static final int ANIMATION_NULL = 156;

		public static Sprite getSprite(int id) {
			if (!spriteCache.containsKey(id)) {
				loadSprite(id);
			}
			return spriteCache.get(id);
		}

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

				case BALLOOM_LEFT_0:
					sprite = new Sprite("balloom_left_0.png");
					break;
				case BALLOOM_LEFT_1:
					sprite = new Sprite("balloom_left_1.png");
					break;
				case BALLOOM_LEFT_2:
					sprite = new Sprite("balloom_left_2.png");
					break;
				case BALLOOM_LEFT_3:
					sprite = new Sprite("balloom_left_3.png");
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
				case BALLOOM_RIGHT_3:
					sprite = new Sprite("balloom_right_3.png");
					break;
				case BALLOOM_DEAD_0:
					sprite = new Sprite("balloom_dead_0.png");
					break;
				case BALLOOM_DEAD_1:
					sprite = new Sprite("balloom_dead_1.png");
					break;
				case BALLOOM_DEAD_2:
					sprite = new Sprite("balloom_dead_2.png");
					break;
				case BALLOOM_DEAD_3:
					sprite = new Sprite("balloom_dead_3.png");
					break;
				case BALLOOM_DEAD_4:
					sprite = new Sprite("balloom_dead_4.png");
					break;

				case ONEAL_LEFT_0:
					sprite = new Sprite("oneal_left_0.png");
					break;
				case ONEAL_LEFT_1:
					sprite = new Sprite("oneal_left_1.png");
					break;
				case ONEAL_LEFT_2:
					sprite = new Sprite("oneal_left_2.png");
					break;
				case ONEAL_LEFT_3:
					sprite = new Sprite("oneal_left_3.png");
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
				case ONEAL_RIGHT_3:
					sprite = new Sprite("oneal_right_3.png");
					break;
				case ONEAL_DEAD_0:
					sprite = new Sprite("oneal_dead_0.png");
					break;
				case ONEAL_DEAD_1:
					sprite = new Sprite("oneal_dead_1.png");
					break;
				case ONEAL_DEAD_2:
					sprite = new Sprite("oneal_dead_2.png");
					break;
				case ONEAL_DEAD_3:
					sprite = new Sprite("oneal_dead_3.png");
					break;
				case ONEAL_DEAD_4:
					sprite = new Sprite("oneal_dead_4.png");
					break;

				case DOLL_LEFT_0:
					sprite = new Sprite("doll_left_0.png");
					break;
				case DOLL_LEFT_1:
					sprite = new Sprite("doll_left_1.png");
					break;
				case DOLL_LEFT_2:
					sprite = new Sprite("doll_left_2.png");
					break;
				case DOLL_LEFT_3:
					sprite = new Sprite("doll_left_3.png");
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
				case DOLL_RIGHT_3:
					sprite = new Sprite("doll_right_3.png");
					break;
				case DOLL_DEAD_0:
					sprite = new Sprite("doll_dead_0.png");
					break;
				case DOLL_DEAD_1:
					sprite = new Sprite("doll_dead_1.png");
					break;
				case DOLL_DEAD_2:
					sprite = new Sprite("doll_dead_2.png");
					break;
				case DOLL_DEAD_3:
					sprite = new Sprite("doll_dead_3.png");
					break;
				case DOLL_DEAD_4:
					sprite = new Sprite("doll_dead_4.png");
					break;

				case MINVO_LEFT_0:
					sprite = new Sprite("minvo_left_0.png");
					break;
				case MINVO_LEFT_1:
					sprite = new Sprite("minvo_left_1.png");
					break;
				case MINVO_LEFT_2:
					sprite = new Sprite("minvo_left_2.png");
					break;
				case MINVO_LEFT_3:
					sprite = new Sprite("minvo_left_3.png");
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
				case MINVO_RIGHT_3:
					sprite = new Sprite("minvo_right_3.png");
					break;
				case MINVO_DEAD_0:
					sprite = new Sprite("minvo_dead_0.png");
					break;
				case MINVO_DEAD_1:
					sprite = new Sprite("minvo_dead_1.png");
					break;
				case MINVO_DEAD_2:
					sprite = new Sprite("minvo_dead_2.png");
					break;
				case MINVO_DEAD_3:
					sprite = new Sprite("minvo_dead_3.png");
					break;
				case MINVO_DEAD_4:
					sprite = new Sprite("minvo_dead_4.png");
					break;

				case GHOST_LEFT_0:
					sprite = new Sprite("ghost_left_0.png");
					break;
				case GHOST_LEFT_1:
					sprite = new Sprite("ghost_left_1.png");
					break;
				case GHOST_LEFT_2:
					sprite = new Sprite("ghost_left_2.png");
					break;
				case GHOST_LEFT_3:
					sprite = new Sprite("ghost_left_3.png");
					break;
				case GHOST_RIGHT_0:
					sprite = new Sprite("ghost_right_0.png");
					break;
				case GHOST_RIGHT_1:
					sprite = new Sprite("ghost_right_1.png");
					break;
				case GHOST_RIGHT_2:
					sprite = new Sprite("ghost_right_2.png");
					break;
				case GHOST_RIGHT_3:
					sprite = new Sprite("ghost_right_3.png");
					break;
				case GHOST_DEAD_0:
					sprite = new Sprite("ghost_dead_0.png");
					break;
				case GHOST_DEAD_1:
					sprite = new Sprite("ghost_dead_1.png");
					break;
				case GHOST_DEAD_2:
					sprite = new Sprite("ghost_dead_2.png");
					break;
				case GHOST_DEAD_3:
					sprite = new Sprite("ghost_dead_3.png");
					break;
				case GHOST_DEAD_4:
					sprite = new Sprite("ghost_dead_4.png");
					break;

				case PLAYER1_BOMB_0:
					sprite = new Sprite("player1_bomb_0.png");
					break;
				case PLAYER1_BOMB_1:
					sprite = new Sprite("player1_bomb_1.png");
					break;
				case PLAYER1_BOMB_2:
					sprite = new Sprite("player1_bomb_2.png");
					break;

				case PLAYER2_BOMB_0:
					sprite = new Sprite("player2_bomb_0.png");
					break;
				case PLAYER2_BOMB_1:
					sprite = new Sprite("player2_bomb_1.png");
					break;
				case PLAYER2_BOMB_2:
					sprite = new Sprite("player2_bomb_2.png");
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
					sprite = new Sprite("explosion_horizontal_right_last_0.png");
					break;
				case EXPLOSION_HORIZONTAL_RIGHT_LAST_1:
					sprite = new Sprite("explosion_horizontal_right_last_1.png");
					break;
				case EXPLOSION_HORIZONTAL_RIGHT_LAST_2:
					sprite = new Sprite("explosion_horizontal_right_last_2.png");
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

				case BRICK_EXPLODED_0:
					sprite = new Sprite("brick_exploded_0.png");
					break;
				case BRICK_EXPLODED_1:
					sprite = new Sprite("brick_exploded_1.png");
					break;
				case BRICK_EXPLODED_2:
					sprite = new Sprite("brick_exploded_2.png");
					break;

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
					sprite = new Sprite("animation_null.png");
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
				BufferedImage resizedImage = resizeImage(image, SIZE, SIZE);
				resizedImage.getRGB(0, 0, SIZE, SIZE, _pixels, 0, SIZE);
			}
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
			BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight,
					BufferedImage.TYPE_INT_ARGB);
			java.awt.Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
			g.dispose();
			return resizedImage;
		}
	}
