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

    // A* pathfinding properties
    protected transient boolean usePathfinding = false;
    protected transient float pathfindingRange = 0.001f;
    protected transient float pathUpdateFrequency = 1.0f;
    protected transient float pathUpdateTimer = 0;
    protected transient List<Node> currentPath = new ArrayList<>();
    protected transient boolean hasPathToBomber = false;

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
                // Nếu không thể di chuyển, thử các hướng khác
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
        Sound.playEffect("enemy_death");
        GameControl.removeEntity(this);
    }

    // A* pathfinding Node class
    protected class Node {
        int x, y;
        Node parent;
        int gCost; // cost from start to this node
        int hCost; // heuristic cost to goal
        int fCost; // g + h

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

    // Manhattan distance heuristic
    protected int calculateHeuristic(int startX, int startY, int targetX, int targetY) {
        return Math.abs(startX - targetX) + Math.abs(startY - targetY);
    }

    // Check if a position is walkable
    protected boolean isWalkable(int x, int y) {
        // Check if out of map bounds
        if (x < 0 || y < 0 || x >= GameControl.getWidth() || y >= GameControl.getHeight()) {
            return false;
        }

        // Check collision with static entities (Brick, Bomb, etc.)
        for (StaticEntity entity : GameControl.getStaticEntities()) {
            if (entity.getXTile() == x && entity.getYTile() == y) {
                // Explicitly check if it's a brick
                if (entity instanceof Brick && !brickPass) {
                    return false;
                }
                // For bombs, check if the enemy can pass bombs
                if (entity instanceof Bomb) {
                    // Nếu có bom, tìm đường đi khác
                    return false;
                }
            }
        }

        // Check collision with background entities (Walls)
        for (BackgroundEntity entity : GameControl.getBackgroundEntities()) {
            if (entity.getXTile() == x && entity.getYTile() == y) {
                if (entity instanceof Wall) {
                    return false;
                }
            }
        }

        return true;
    }

    // A* pathfinding algorithm
    protected List<Node> findPath(int startX, int startY, int targetX, int targetY) {
        if (startX == targetX && startY == targetY) {
            return new ArrayList<>();
        }

        // Check if target is walkable (if not, find closest walkable position)
        if (!isWalkable(targetX, targetY)) {
            // Try to find a nearby walkable cell
            int[][] directions = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };
            boolean found = false;

            // Search in expanding rings (1-3 tiles away)
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

            // If no walkable position found near target, return empty path
            if (!found) {
                return new ArrayList<>();
            }
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        Map<String, Node> openSetMap = new HashMap<>(); // For quick lookups
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

            // Check all 4 adjacent nodes
            int[][] directions = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } }; // Up, Right, Down, Left

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

        // No path found
        return new ArrayList<>();
    }

    // Retrace path from end node back to start
    private List<Node> retracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;

        while (currentNode != startNode) {
            path.add(0, currentNode);
            currentNode = currentNode.parent;
        }

        return path;
    }

    // Intelligent movement using A* pathfinding
    protected void intelligentMove(float deltaTime) {
        if (!usePathfinding) {
            defaultMove(deltaTime);
            return;
        }

        // Check for enemy collision with bomber
        enemyCollision();

        // Update path at regular intervals
        pathUpdateTimer += deltaTime;
        if (pathUpdateTimer >= pathUpdateFrequency || currentPath.isEmpty()) {
            pathUpdateTimer = 0;

            // Find closest Bomber
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

            // If Bomber is within range, update path
            if (closestBomber != null && minDistance <= pathfindingRange && closestBomber.isAlive()) {
                speed = boostedspeed;
                

                currentPath = findPath(getXTile(), getYTile(),
                        closestBomber.getXTile(), closestBomber.getYTile());

                if (currentPath.isEmpty()) {
                    // Không tìm được đường → di chuyển ngẫu nhiên
                    speed = 15;
                    
                    hasPathToBomber = false;
                    defaultMove(deltaTime);
                    return;
                } else {
                    hasPathToBomber = true;
                }
            } else {
                currentPath.clear();
                speed = 15;
                hasPathToBomber = false;
                defaultMove(deltaTime);
                return;
            }
        }

        // Follow the path if available
        moveTimer += deltaTime;
        if (moveTimer >= movementFrequencyTime) {
            moveTimer = 0;

            if (!currentPath.isEmpty()) {
                Node nextNode = currentPath.get(0);

                // Kiểm tra xem node tiếp theo có an toàn không
                if (!isWalkable(nextNode.x, nextNode.y)) {
                    // Nếu node không an toàn, tìm đường đi mới và di chuyển ngẫu nhiên
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
                    // Tìm đường mới ngay lập tức khi đến node
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
                    // Nếu không thể di chuyển, tìm đường mới và di chuyển ngẫu nhiên
                    currentPath.clear();
                    pathUpdateTimer = pathUpdateFrequency;
                    hasPathToBomber = false;
                    defaultMove(deltaTime);
                }
            } else if (hasPathToBomber) {
                // Nếu có dấu !? nhưng không có đường đi, tìm đường mới ngay lập tức
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
