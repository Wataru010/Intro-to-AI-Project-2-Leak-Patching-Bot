public class CoordinateSet {

    private Coordinate c1;
    private Coordinate c2;
    
    public CoordinateSet(Coordinate c1, Coordinate c2){
        this.c1 = c1;
        this.c2 = c2;
    }

    public Coordinate getCoor1(){
        return c1;
    }

    public Coordinate getCoor2(){
        return c2;
    }

    
    @Override
    public boolean equals(Object o){
        if (this == o) {    
            return true;    
        }    

        if (o instanceof CoordinateSet){
            
            CoordinateSet coor = (CoordinateSet) o;

            if(this.c1.compare_coor(coor.getCoor1()) && this.c2.compare_coor(coor.getCoor2())){
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        String h_code = String.format("%d%d%d%d", c1.getRow(),c1.getCol(),c2.getRow(),c2.getCol());
        return Integer.parseInt(h_code);
    }
}
