import processing.core.PImage;

public interface Entity
{
    PImage getCurrentImage(Object entity);
    Point getEntityPosition();
    void setEntityPosition(Point pos);
}


