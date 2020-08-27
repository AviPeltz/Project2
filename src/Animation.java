public class Animation implements Action{
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Animation(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        ((AnimationEntity)entity).nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity,
                    ActionFactory.createAnimationAction(entity,
                            Math.max(repeatCount - 1,
                                    0)),
                    ((AnimationEntity)entity).getAnimationPeriod());
        }
    }
}
