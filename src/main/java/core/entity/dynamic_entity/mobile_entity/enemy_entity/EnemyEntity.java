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
    protected transient int boostedspeed = 30;
    protected transient int speed = 15;
    protected transient float moveTimer = 0;
    protected transient float directionChangeTimer = 0;
    protected transient float movementFrequencyTime = 0.01f;

    // A* pathfinding properties
    protected transient boolean usePathfinding = false;
    protected transient float pathfindingRange = 0.001f;
    protected transient float pathUpdateFrequency = 1.0f;
    protected transient float pathUpdateTimer = 0;
    protected transient List<Node> currentPath = new ArrayList<>();

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
                direction = Util.randomDirection();
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
                if (entity instanceof Brick) {
                    return false;
                }
                // For bombs, check if the enemy can pass bombs
                if (entity instanceof Bomb && !bombPass) {
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
        Node targetNode = new Node(targetX, targetY);

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
                float distance = calculateHeuristic(getXTile(), getYTile(), bomber.getXTile(), bomber.getYTile());
                if (distance < minDistance) {
                    minDistance = distance;
                    closestBomber = bomber;
                }
            }

            // If Bomber is within range, update path
            if (closestBomber != null && minDistance <= pathfindingRange && closestBomber.isAlive()) {
                
                currentPath = findPath(getXTile(), getYTile(),
                        closestBomber.getXTile(), closestBomber.getYTile());
            } else {
                
                currentPath.clear();
                defaultMove(deltaTime);
            }
        }

        // Follow the path if available
        moveTimer += deltaTime;
        if (moveTimer >= movementFrequencyTime) {
            moveTimer = 0;

            if (!currentPath.isEmpty()) {
                // Get next node in path
                Node nextNode = currentPath.get(0);

                // Double check that the next node is walkable
                if (!isWalkable(nextNode.x, nextNode.y)) {
                    // If not walkable anymore, clear path and recalculate next time
                    currentPath.clear();
                    defaultMove(deltaTime);
                    return;
                }

                // Calculate distance to node center
                float nodeX = nextNode.x * Sprite.DEFAULT_SIZE;
                float nodeY = nextNode.y * Sprite.DEFAULT_SIZE;
                float distX = Math.abs(x - nodeX);
                float distY = Math.abs(y - nodeY);

                // If closely aligned to the grid, ensure enemy is exactly on a tile boundary
                boolean isCloseToNodeX = distX < speed * deltaTime * 10;
                boolean isCloseToNodeY = distY < speed * deltaTime * 10;

                // First align with the grid if needed
                if (isCloseToNodeX && isCloseToNodeY) {
                    // We've reached this node, align precisely and move to next node
                    x = nodeX;
                    y = nodeY;

                    // Remove current node and get next node if available
                    currentPath.remove(0);
                    if (currentPath.isEmpty()) {
                        defaultMove(deltaTime);
                        return;
                    }

                    // Update to next node
                    nextNode = currentPath.get(0);
                }

                // Determine direction to the next node
                // Only change direction when at a tile boundary to prevent zigzagging
                if (Math.abs(x % Sprite.DEFAULT_SIZE) < 2 && Math.abs(y % Sprite.DEFAULT_SIZE) < 2) {
                    if (nextNode.x > getXTile()) {
                        direction = RIGHT_MOVING;
                    } else if (nextNode.x < getXTile()) {
                        direction = LEFT_MOVING;
                    } else if (nextNode.y > getYTile()) {
                        direction = DOWN_MOVING;
                    } else if (nextNode.y < getYTile()) {
                        direction = UP_MOVING;
                    }
                }

                // Move in the current direction
                if (!move(direction, speed, deltaTime)) {
                    // If we can't move in the desired direction, realign to grid and recalculate
                    // path
                    x = getXTile() * Sprite.DEFAULT_SIZE;
                    y = getYTile() * Sprite.DEFAULT_SIZE;
                    currentPath.clear();
                }
            } else {
                // No path, use default movement
                defaultMove(deltaTime);
            }
        }
    }
    
   
}