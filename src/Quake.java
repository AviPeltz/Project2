import processing.core.PImage;
import java.util.List;


public class Quake implements AnimationEntity, ActivityEntity{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;

    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    public Quake(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    /**
     * getEntityPosition() will return the entity's position
     * @position
     */
    public Point getEntityPosition(){ return(position); }
    public void setEntityPosition(Point pos){this.position = pos;}

    public void scheduleActions(EventScheduler scheduler, WorldModel world,
                                ImageStore imageStore)
    {
                scheduler.scheduleEvent(this,
                        ActionFactory.createActivityAction(this, world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this, ActionFactory.createAnimationAction(this,
                        QUAKE_ANIMATION_REPEAT_COUNT),
                        this.getAnimationPeriod());



    }

    public void executeActivity(WorldModel world, ImageStore imageStore,
                                     EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    public void nextImage()
    {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public PImage getCurrentImage(Object entity) {
        if (entity instanceof Background) {
            return ((Background)entity).getImages().get(
                    ((Background)entity).getImageIndex());
        }
        else if (entity instanceof Quake) {
            return ((Quake)entity).images.get(((Quake)entity).imageIndex);
        }
        else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }
}
