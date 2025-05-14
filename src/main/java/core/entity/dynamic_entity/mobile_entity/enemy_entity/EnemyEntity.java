package core.entity.dynamic_entity.mobile_entity.enemy_entity;

import core.entity.dynamic_entity.mobile_entity.MobileEntity;
import core.entity.dynamic_entity.mobile_entity.Bomber;
import core.system.game.GameControl;
import core.util.Util;
import core.entity.background_entity.BackgroundEntity;
import core.entity.background_entity.Wall;
import core.entity.dynamic_entity.static_entity.StaticEntity;
import core.entity.dynamic_entity.static_entity.Brick;
import core.entity.dynamic_entity.static_entity.Bomb;
import core.sound.Sound;
import core.graphics.Sprite;
import java.util.*;

public class EnemyEntity extends MobileEntity {
    protected transient int boostedspeed = 25;
    protected transient int speed = 10;
    protected transient float moveTimer = 0;
    protected transient float directionChangeTimer = 0;
    protected transient float movementFrequencyTime = 0.02f;

    protected transient boolean usePathfinding = false;
    protected transient float pathfindingRange = 5.0f;
    protected transient float pathUpdateFrequency = 0.5f;
    protected transient float pathUpdateTimer = 0;
    protected transient List<Node> currentPath = new ArrayList<>();
    protected  boolean hasPathToBomber = false;

    public EnemyEntity(int x, int y, int imageId) {
        super(x, y, imageId);
    }

    protected void defaultMove(float deltaTime) {
        moveTimer += deltaTime;
        directionChangeTimer += deltaTime;

        if (directionChangeTimer >= 2.0) {
            direction = Util.randomDirection();
            directionChangeTimer = 0;
        }

        if (moveTimer >= movementFrequencyTime) {
            moveTimer = 0;
            if (!move(direction, speed, deltaTime)) {
                int[] directions = { UP_MOVING, DOWN_MOVING, LEFT_MOVING, RIGHT_MOVING };
                for (int newDir : directions) {
                    if (newDir != direction) {
                        direction = newDir;
                        if (move(direction, speed, deltaTime)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected boolean enemyCollision() {
        for (Bomber bomber : GameControl.getBomberEntities()) {
            if (bomber.getXTile() == this.getXTile() && bomber.getYTile() == this.getYTile()) {
                if (!bomber.isInvincible()) {
                    bomber.decreaseHealth();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void remove() {
        Sound.playEffect(Sound.ENEMY_DEAD);
        GameControl.removeEntity(this);
    }

    protected class Node {
        int x, y;
        Node parent;
        int gCost;
        int hCost;
        int fCost;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int calculateFCost() {
            fCost = gCost + hCost;
            return fCost;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    protected int calculateHeuristic(int startX, int startY, int targetX, int targetY) {
        return Math.abs(startX - targetX) + Math.abs(startY - targetY);
    }

    protected boolean isWalkable(int x, int y) {
        if (x < 0 || y < 0 || x >= GameControl.getWidth() || y >= GameControl.getHeight()) {
            return false;
        }

        for (StaticEntity entity : GameControl.getStaticEntities()) {
            if (entity.getXTile() == x && entity.getYTile() == y) {

                if (entity instanceof Brick && !brickPass) {
                    return false;
                }
                if (entity instanceof Bomb) {
                    return false;
                }
            }
        }

        for (BackgroundEntity entity : GameControl.getBackgroundEntities()) {
            if (entity.getXTile() == x && entity.getYTile() == y) {
                if (entity instanceof Wall) {
                    return false;
                }
            }
        }

        return true;
    }

    protected List<Node> findPath(int startX, int startY, int targetX, int targetY) {
        if (startX == targetX && startY == targetY) {
            return new ArrayList<>();
        }

        if (!isWalkable(targetX, targetY)) {
            int[][] directions = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };
            boolean found = false;

            for (int distance = 1; distance <= 3 && !found; distance++) {
                for (int[] dir : directions) {
                    int newTargetX = targetX + dir[0] * distance;
                    int newTargetY = targetY + dir[1] * distance;

                    if (isWalkable(newTargetX, newTargetY)) {
                        targetX = newTargetX;
                        targetY = newTargetY;
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                return new ArrayList<>();
            }
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        Map<String, Node> openSetMap = new HashMap<>();
        Map<String, Node> closedSet = new HashMap<>();

        Node startNode = new Node(startX, startY);

        startNode.gCost = 0;
        startNode.hCost = calculateHeuristic(startX, startY, targetX, targetY);
        startNode.calculateFCost();

        openSet.add(startNode);
        openSetMap.put(startX + "," + startY, startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            openSetMap.remove(currentNode.x + "," + currentNode.y);

            if (currentNode.x == targetX && currentNode.y == targetY) {
                return retracePath(startNode, currentNode);
            }

            closedSet.put(currentNode.x + "," + currentNode.y, currentNode);

            int[][] directions = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };

            for (int[] dir : directions) {
                int nx = currentNode.x + dir[0];
                int ny = currentNode.y + dir[1];
                String key = nx + "," + ny;

                if (!isWalkable(nx, ny) || closedSet.containsKey(key)) {
                    continue;
                }

                int tentativeGCost = currentNode.gCost + 1;
                Node neighborNode;

                if (openSetMap.containsKey(key)) {
                    neighborNode = openSetMap.get(key);
                    if (tentativeGCost >= neighborNode.gCost) {
                        continue;
                    }
                } else {
                    neighborNode = new Node(nx, ny);
                    neighborNode.hCost = calculateHeuristic(nx, ny, targetX, targetY);
                }

                neighborNode.parent = currentNode;
                neighborNode.gCost = tentativeGCost;
                neighborNode.calculateFCost();

                if (!openSetMap.containsKey(key)) {
                    openSet.add(neighborNode);
                    openSetMap.put(key, neighborNode);
                }
            }
        }

        return new ArrayList<>();
    }

    private List<Node> retracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;

        while (currentNode != startNode) {
            path.add(0, currentNode);
            currentNode = currentNode.parent;
        }

        return path;
    }

    protected void intelligentMove(float deltaTime) {
        if (!usePathfinding) {
            defaultMove(deltaTime);
            return;
        }

        enemyCollision();

        pathUpdateTimer += deltaTime;
        if (pathUpdateTimer >= pathUpdateFrequency || currentPath.isEmpty()) {
            pathUpdateTimer = 0;

            Bomber closestBomber = null;
            float minDistance = Float.MAX_VALUE;

            for (Bomber bomber : GameControl.getBomberEntities()) {
                if (!bomber.isAlive()) {
                    continue;
                }
                float distance = calculateHeuristic(getXTile(), getYTile(), bomber.getXTile(), bomber.getYTile());
                if (distance < minDistance) {
                    minDistance = distance;
                    closestBomber = bomber;
                }
            }

            if (closestBomber != null && minDistance <= pathfindingRange && closestBomber.isAlive()) {
                if (!(this instanceof Minvo)) {
                    speed = boostedspeed;
                }

                currentPath = findPath(getXTile(), getYTile(),
                        closestBomber.getXTile(), closestBomber.getYTile());

                if (currentPath.isEmpty()) {
                    if (!(this instanceof Minvo)) {
                    speed = 15;
                    }
                    hasPathToBomber = false;
                    defaultMove(deltaTime);
                    return;
                } else {
                    hasPathToBomber = true;
                }
            } else {
                currentPath.clear();
                if (!(this instanceof Minvo)) {
                    speed = 15;
                }
                hasPathToBomber = false;
                defaultMove(deltaTime);
                return;
            }
        }

        moveTimer += deltaTime;
        if (moveTimer >= movementFrequencyTime) {
            moveTimer = 0;

            if (!currentPath.isEmpty()) {
                Node nextNode = currentPath.get(0);

                if (!isWalkable(nextNode.x, nextNode.y)) {
                    currentPath.clear();
                    pathUpdateTimer = pathUpdateFrequency;
                    hasPathToBomber = false;
                    defaultMove(deltaTime);
                    return;
                }

                float nodeX = nextNode.x * Sprite.DEFAULT_SIZE;
                float nodeY = nextNode.y * Sprite.DEFAULT_SIZE;
                float distX = Math.abs(x - nodeX);
                float distY = Math.abs(y - nodeY);

                boolean isCloseToNodeX = distX < speed * deltaTime * 10;
                boolean isCloseToNodeY = distY < speed * deltaTime * 10;

                if (isCloseToNodeX && isCloseToNodeY) {
                    x = nodeX;
                    y = nodeY;
                    currentPath.remove(0);
                    pathUpdateTimer = pathUpdateFrequency;
                    return;
                }

                if (Math.abs(x % Sprite.DEFAULT_SIZE) < 2 && Math.abs(y % Sprite.DEFAULT_SIZE) < 2) {
                    if (nextNode.x > getXTile())
                        direction = RIGHT_MOVING;
                    else if (nextNode.x < getXTile())
                        direction = LEFT_MOVING;
                    else if (nextNode.y > getYTile())
                        direction = DOWN_MOVING;
                    else if (nextNode.y < getYTile())
                        direction = UP_MOVING;
                }

                if (!move(direction, speed, deltaTime)) {
                    currentPath.clear();
                    pathUpdateTimer = pathUpdateFrequency;
                    hasPathToBomber = false;
                    defaultMove(deltaTime);
                }
            } else if (hasPathToBomber) {
                pathUpdateTimer = pathUpdateFrequency;
            }
        }
    }

    @Override
    public void render(javafx.scene.canvas.GraphicsContext gc) {
        super.render(gc);
        if (hasPathToBomber) {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.setFont(new javafx.scene.text.Font("Arial Bold", 35));
            gc.fillText("!?", x + Sprite.DEFAULT_SIZE + 2, y);
        }
    }
}
