public class Activity implements Action{
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        if(this.entity instanceof MinerFull){
            ((MinerFull)entity).executeActivity(world, imageStore, scheduler);
        }
        else if(this.entity instanceof MinerNotFull){
            ((MinerNotFull)entity).executeActivity(world, imageStore, scheduler);
        }
        else if(this.entity instanceof Ore){
            ((Ore)entity).executeActivity(world, imageStore, scheduler);
        }
        else if(this.entity instanceof OreBlob){
            ((OreBlob)entity).executeActivity(world,imageStore,scheduler);
        }
        else if(this.entity instanceof Quake){
            ((Quake)entity).executeActivity(world, imageStore, scheduler);
        }
        else if(this.entity instanceof Vein){
            ((Vein)entity).executeActivity(world, imageStore, scheduler);
        }
        else {
            throw new UnsupportedOperationException(String.format(
                    "executeActivityAction not supported for %s", this.entity));
        }
    }

}
