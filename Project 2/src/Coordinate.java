import java.util.ArrayList;

public class Coordinate {
    // Coordinate object instance variable
    private int row;
    private int col;
    private Coordinate parent;
    private int length;
    private int priority;

    // constructor to create a Coordinate object
    public Coordinate(int row, int col){
        this.row = row;
        this.col = col;
    }

    // overloaded constructor: for current existing coor to store its parent
    public Coordinate(Coordinate curr, Coordinate parent, int length){
        this.row = curr.getRow();
        this.col = curr.getCol();
        this.parent = parent;
        this.length = length;
    }

    // overloaded constructor: Coordinate Object for priority
    public Coordinate (Coordinate curr, Coordinate parent, int length, int priority){
        this.row = curr.getRow();
        this.col = curr.getCol();
        this.parent = parent;
        this.length = length;
        this.priority = priority;
    }

    // Setters and Getters
    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Coordinate getParent(){
        return parent;
    }

    public int getLength(){
        return length;
    }

    public int getPriority(){
        return priority;
    }

    // compare two coordinate is the same
    public boolean compare_coor(Coordinate target){
        if(this.getRow() == target.getRow() && this.getCol() == target.getCol()){
            return true;
        }
        return false;
    }

    // return the index positon of a coordinate in the arraylist
    public static int index_of_coor(ArrayList<Coordinate> list, Coordinate coor){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getRow() == coor.getRow() && list.get(i).getCol() == coor.getCol()){
                return i;
            }
        }
        return -1;
    }

    // check existence of a Coordinate in a given ArrayList
    public static boolean compare_coor_with_list(ArrayList<Coordinate> list, Coordinate coor){
        for(Coordinate obj : list){
            if(obj.getRow() == coor.getRow() && obj.getCol() == coor.getCol()){
                return true;
            }
        }
        return false;
    }

    // check existence of a Coordinate in a given ArrayList
    public static boolean compare_coor_with_list(ArrayList<Coordinate> list, int row, int col){
        for(Coordinate coor : list){
            if(coor.getRow() == row && coor.getCol() == col){
                return true;
            }
        }
        return false;
    }
}
