import processing.core.PImage;

import java.util.*;

public final class WorldModel
{
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    private static final int ORE_REACH = 1;
    private static final int PROPERTY_KEY = 0;
    private static final String BGND_KEY = "background";
    private static final String VEIN_KEY = "vein";
    private static final String ORE_KEY = "ore";
    private static final String OBSTACLE_KEY = "obstacle";
    private static final String SMITH_KEY = "blacksmith";
    private static final String MINER_KEY = "miner";

    public int getNumCols() { return numCols; }
    public int getNumRows(){return numRows;}
    public Set<Entity> getEntities(){return entities;}


    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public Entity getOccupancyCell(Point pos) {

        return occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(Point pos, Entity entity)
    {
        occupancy[pos.y][pos.x] = entity;
    }

    public void setBackgroundCell(Point pos, Background backgroundVar)
    {
        background[pos.y][pos.x] = backgroundVar;
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows && pos.x >= 0
                && pos.x < numCols;
    }

    public Background getBackgroundCell(Point pos) {
        return background[pos.y][pos.x];
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (withinBounds(pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage(getBackgroundCell(pos)));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (this.withinBounds(newPt) && !this.isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }


    public Optional<Entity> findNearest(Point pos, Class nameClass)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : entities) {
            if (entity.getClass() == nameClass) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    public void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.getEntityPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }
        this.addEntity(entity);
    }

    //edit here to make sure this is fine
    public void addEntity(Entity entity) {
        if (this.withinBounds(entity.getEntityPosition())) {
            this.setOccupancyCell(entity.getEntityPosition(), entity);
            entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getEntityPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);
            entity.setEntityPosition(pos);
        }
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAt(entity.getEntityPosition());
    }

    public static int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public void removeEntityAt(Point pos) {
        if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setEntityPosition( new Point(-1, -1));
            entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public boolean isOccupied(Point pos) {
        return this.withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getEntityPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getEntityPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public void setBackground(Point pos, Background background)
    {
        if (this.withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell( pos));
        }
        else {
            return Optional.empty();
        }
    }

    public void load(
            Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    public boolean processLine(
            String line, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return ParseFactory.parseBackground(properties, this, imageStore);
                case MINER_KEY:
                    return ParseFactory.parseMiner(properties, this, imageStore);
                case OBSTACLE_KEY:
                    return ParseFactory.parseObstacle(properties, this, imageStore);
                case ORE_KEY:
                    return ParseFactory.parseOre(properties, this, imageStore);
                case SMITH_KEY:
                    return ParseFactory.parseSmith(properties, this, imageStore);
                case VEIN_KEY:
                    return ParseFactory.parseVein(properties, this, imageStore);
            }
        }

        return false;
    }

}
