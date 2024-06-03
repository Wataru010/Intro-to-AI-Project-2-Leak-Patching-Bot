import java.util.ArrayList;

public class CreateScene {
    public static Coordinate set_bot(int[][] ship, ArrayList<Coordinate> path){
        int bot_index = (int)(Math.random()*path.size());
        Coordinate bot = path.get(bot_index);
        ship[bot.getRow()][bot.getCol()] = 5;
        path.remove(bot_index);
        return bot;
    }

    public static Coordinate set_leak1(int[][] ship, ArrayList<Coordinate> path, Coordinate bot, int k_value){
        int leak1_index = (int)(Math.random()*path.size());
        Coordinate leak1 = path.get(leak1_index);
        if(2*k_value+1 >= ship.length){
            while (leak1.compare_coor(bot)) {
                leak1_index = (int)(Math.random()*path.size());
                leak1 = path.get(leak1_index);
            }
        }else{
            while(in_range(leak1, bot, k_value, ship)){
                leak1_index = (int)(Math.random()*path.size());
                leak1 = path.get(leak1_index);
            }
        }
        ship[leak1.getRow()][leak1.getCol()] = 8;
        path.remove(leak1_index);
        return leak1;
    }

    public static Coordinate set_leak2(int[][] ship, ArrayList<Coordinate> path, Coordinate bot, int k_value, Coordinate leak1){
        int leak2_index = (int)(Math.random()*path.size());
        Coordinate leak2 = path.get(leak2_index);
        while(in_range(leak2, bot, k_value, ship) && leak2.compare_coor(leak1)){
            leak2_index = (int)(Math.random()*path.size());
            leak2 = path.get(leak2_index);
        }
        ship[leak2.getRow()][leak2.getCol()] = 9;
        path.remove(leak2_index);
        return leak2;
    }

    public static boolean in_range(Coordinate pick_coor, Coordinate bot, int k_value, int[][] ship){
        int start_row = bot.getRow()-k_value;
        int start_col = bot.getCol()-k_value;
        int end_row = bot.getRow()+k_value;
        int end_col = bot.getCol()+k_value;

        for(int i = start_row; i <= end_row; i++){
            for(int j = start_col; j <= end_col; j++){
                if( (i >= 0 && i < ship.length) && (j >= 0 && j < ship[i].length)){
                    if(i == pick_coor.getRow() && j == pick_coor.getCol()){
                        return true;
                    }
                }
            }
        }
        return false; 
    }
}
