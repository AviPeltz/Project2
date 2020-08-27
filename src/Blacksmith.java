import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Blacksmith implements Entity {

    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;

    public Blacksmith(
            String id,
            Point position,
            List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;

    }
    public Point getEntityPosition(){ return(position); }

    public void setEntityPosition(Point pos){this.position = pos;}

    public PImage getCurrentImage(Object entity) {
        if (entity instanceof Background) {
            return ((Background)entity).getImages().get(
                    ((Background)entity).getImageIndex());
        }
        else if (entity instanceof Blacksmith) {
            return ((Blacksmith)entity).images.get(((Blacksmith)entity).imageIndex);
        }
        else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }
}
