public class Activity implements Action{
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;


    public Activity(Entity entity, WorldModel world, ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;

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
