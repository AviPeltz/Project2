public final class Viewport
{
    private int row;
    private int col;
    private int numRows;
    private int numCols;

    public int getRow(){return this.row;}
    public int getCol(){return this.col;}
    public int getNumRows(){return this.numRows;}
    public int getNumCols(){return this.numCols;}

    public Viewport(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public Point viewportToWorld(int colVar, int rowVar) {
        return new Point(colVar + col, rowVar + row);
    }
    public Point worldToViewport(int colVar, int rowVar) {
        return new Point(colVar - col, rowVar - row);
    }
    public void shift(int colVar, int rowVar) {
        col = colVar;
        row = rowVar;
    }
    public boolean contains(Point p) {
        return p.y >= row && p.y < row + numRows
                && p.x >= col && p.x < col + numCols;
    }

}