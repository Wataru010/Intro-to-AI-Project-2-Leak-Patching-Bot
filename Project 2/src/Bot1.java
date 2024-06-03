import java.util.concurrent.TimeUnit;
// import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Bot1 {

    // Build probability table of the ship
    public static double[][] init_leak_prob_table(Ship ship){
        int[][] ship_layout = ship.getShipArray();
        double[][] prob_table = new double[ship.getShipArray().length][ship.getShipArray().length];
        for(int i = 0; i < ship_layout.length; i++){
            for(int j = 0; j < ship_layout[i].length; j++){
                if(ship_layout[i][j] == 0){
                    prob_table[i][j] = -1.0;
                }else{
                    if(!(ship.getBotCoor().getRow() == i && ship.getBotCoor().getCol() == j)){
                        prob_table[i][j] = 0.5;
                    }else{
                        prob_table[i][j] = 0.0;
                    }
                }
            }
        }
        return prob_table;
    }

    // Scan the detection square
    public static boolean scan(Ship ship){
        Coordinate bot_coor = ship.getBotCoor();
        Coordinate leak1_coor = ship.getLeak1Coor();

        int k_value = ship.getK_Value();
        int start_row = bot_coor.getRow()-k_value;
        int start_col = bot_coor.getCol()-k_value;
        int end_row = bot_coor.getRow()+k_value;
        int end_col = bot_coor.getCol()+k_value;
        
        for(int i = 0; i < ship.getShipArray().length; i++){
            for(int j = 0; j < ship.getShipArray()[i].length; j++){
                if(((i >= start_row && i <= end_row) && (j >= start_col && j <= end_col))){
                    if(i == leak1_coor.getRow() && j == leak1_coor.getCol()){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // update probability after scan
    public static void update_table(Ship ship, double[][] prob_table, boolean scan_result){
        // Gather info of the bot and k_value
        Coordinate bot_coor = ship.getBotCoor();
        int k_value = ship.getK_Value();

        // set range for update range
        int start_row = bot_coor.getRow()-k_value;
        int start_col = bot_coor.getCol()-k_value;
        int end_row = bot_coor.getRow()+k_value;
        int end_col = bot_coor.getCol()+k_value;
        
        // update accordingly
        if(scan_result == true){
            for(int i = 0; i < prob_table.length; i++){
                for(int j = 0; j < prob_table[i].length; j++){
                    // check if it is a valid opened cell
                    if(prob_table[i][j] > -1.0){
                        // turn the cell outside of detection to 0%
                        if((!((i >= start_row && i <= end_row) && (j >= start_col && j <= end_col)))){
                            prob_table[i][j] = 0.0;
                        }
                    }
                }
            }
            // if entered cell is not the leak cell, mark it as not containing the leak
            prob_table[bot_coor.getRow()][bot_coor.getCol()] = 0.0;

        }else{ // scan_result == false
            for(int i = 0; i < prob_table.length; i++){
                for(int j = 0; j < prob_table[i].length; j++){
                    // check if it is a valid opened cell
                    if(prob_table[i][j] > -1.0){
                        // turn the cell inside of detection to 0%
                        if(((i >= start_row && i <= end_row) && (j >= start_col && j <= end_col))){
                            prob_table[i][j] = 0.0;
                        }
                    }
                }
            }
        }
    }

    // find the nearest cell that might contain the leak
    public static Coordinate BFS(Ship ship, double[][] prob_table){
        Coordinate bot_coor = ship.getBotCoor();
        Queue<Coordinate> queue = new LinkedList<>();
        boolean[][] visited = new boolean[ship.getShipArray().length][ship.getShipArray().length];

        // add initial node to queue
        queue.add(new Coordinate(bot_coor, null, 0));

        while(queue.peek() != null){
            // pop the head (Coordinate) of the queue
            Coordinate curr = queue.poll();

            // the nearest cell that might contain the leak
            if(prob_table[curr.getRow()][curr.getCol()] > 0.0){
                // System.out.println("("+ curr.getRow() + "," + curr.getCol()+ ")");
                return curr;
            }

            // check if the curr cell is in close set, if not then explore it childs
            if(visited[curr.getRow()][curr.getCol()] == false){
                // if curr is not the nearest cell that might contain the leak, add more childs
                add_candidate_child(ship, prob_table, curr, queue);
                
                // add curr to visited coor
                visited[curr.getRow()][curr.getCol()] = true;
            }
        }
        return null;
    }

    // add child to the queue
    public static void add_candidate_child(Ship ship, double[][] prob_table, Coordinate parent, Queue<Coordinate> queue){
        // used to randomize the order these cell child are added to the queue
        // ArrayList<Coordinate> list = new ArrayList<>();
        
        // top cell
        if(parent.getRow()-1 > -1){
            Coordinate top_cell = new Coordinate(new Coordinate(parent.getRow()-1, parent.getCol()), parent, parent.getLength()+1);
            if(ship.getHashPath().containsKey(String.format("%d%d", top_cell.getRow(), top_cell.getCol())) && prob_table[top_cell.getRow()][top_cell.getCol()] > -1.0){
                queue.add(top_cell);
                // list.add(top_cell);
            }   
        }
         
        // boottom cell
        if(parent.getRow()+1 < prob_table.length){
            Coordinate bottom_cell = new Coordinate(new Coordinate(parent.getRow()+1, parent.getCol()), parent, parent.getLength()+1);
            if(ship.getHashPath().containsKey(String.format("%d%d", bottom_cell.getRow(), bottom_cell.getCol())) && prob_table[bottom_cell.getRow()][bottom_cell.getCol()] > -1.0){
                queue.add(bottom_cell);
                // list.add(bottom_cell);
            }  
            
        }

        // left cell
        if(parent.getCol()-1 > -1){
            Coordinate left_cell = new Coordinate(new Coordinate(parent.getRow(), parent.getCol()-1), parent, parent.getLength()+1);
            if(ship.getHashPath().containsKey(String.format("%d%d", left_cell.getRow(), left_cell.getCol())) && prob_table[left_cell.getRow()][left_cell.getCol()] > -1.0){
                queue.add(left_cell);
                // list.add(left_cell);
            }  
        }

        // right cell
        if(parent.getCol()+1 < prob_table[parent.getRow()].length){
            Coordinate right_cell = new Coordinate(new Coordinate(parent.getRow(), parent.getCol()+1), parent, parent.getLength()+1);
            if(ship.getHashPath().containsKey(String.format("%d%d", right_cell.getRow(), right_cell.getCol())) && prob_table[right_cell.getRow()][right_cell.getCol()] > -1.0){
                queue.add(right_cell);
                // list.add(right_cell);
            }  
        }

        // while(list.size() > 0){
        //     int index = (int)(Math.random()*list.size());
        //     queue.add(list.get(index));
        //     list.remove(index);
        // }
    }
    
    public static int run_bot1(Ship ship){
        // get probablity of containing leak for each cell
        double[][] prob_table = init_leak_prob_table(ship);

        // counter for number of actions
        int action_num = 0;

        // do scan and then move till the bot found the leak
        while(!ship.getBotCoor().compare_coor(ship.getLeak1Coor())){
            // Scan current detection square
            boolean scan_result = scan(ship);
            // System.out.println(scan_result);
            action_num++;

            // update probabilities
            update_table(ship, prob_table, scan_result);

            // Move to next location where is the nearest cell that might contain the leak
            Coordinate next_loc = BFS(ship, prob_table);
            if(next_loc != null){
                // Move
                ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 1;
                ship.setBotCoor(next_loc);
                ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 5;

                // add distance to action number
                action_num += next_loc.getLength();

            }else{
                System.out.println("ERROR: Debug Please, run_bot1 after Coordinate next_loc!");
                return -1;
            }
            next_loc = null;
        }
        
        ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
        
        System.out.println("Mission Complete!");
        return action_num;
    }

    // demostration version
    public static int run_bot1_demo(Ship ship){
        // get probablity of containing leak for each cell
        double[][] prob_table = init_leak_prob_table(ship);

        // counter for number of actions
        int action_num = 0;

        // do scan and then move till the bot found the leak
        while(!ship.getBotCoor().compare_coor(ship.getLeak1Coor())){
            // Scan current detection square
            boolean scan_result = scan(ship);
            action_num++;

            // update probabilities
            update_table(ship, prob_table, scan_result);

            // Move to next location where is the nearest cell that might contain the leak
            Coordinate next_loc = BFS(ship, prob_table);
            if(next_loc != null){
                // Move
                ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 1;
                ship.setBotCoor(next_loc);
                ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 5;

                // print ship layout
                Ship.print_layout(ship.getShipArray());
                
                // add distance to action number
                action_num += next_loc.getLength();

                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch(Exception error){
                    System.out.println("Error occur!");
                    System.out.println(error);
                }
            }else{
                System.out.println("ERROR: Debug Please, run_bot1 after Coordinate next_loc!");
            }
        }

        ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;

        // print ship layout
        Ship.print_layout(ship.getShipArray());

        System.out.println("Mission Complete!");

        try{
            TimeUnit.SECONDS.sleep(1);
        }catch(Exception error){
            System.out.println("Error occur!");
            System.out.println(error);
        }

        return action_num;
    }
}
