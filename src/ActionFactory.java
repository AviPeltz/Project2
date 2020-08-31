public class ActionFactory  {

    public static Activity createActivityAction(Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }

    public static Animation createAnimationAction(Entity entity, int repeatCount) {
        return new Animation( entity, repeatCount);
    }
}
