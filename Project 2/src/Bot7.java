import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Bot7 {
    
    // Build probability table of the ship
    public static double[][] init_leak_prob_table(Ship ship){
        int[][] ship_layout = ship.getShipArray();
        int num_of_path = ship.getHashPath().size();
        double[][] prob_table = new double[ship.getShipArray().length][ship.getShipArray().length];
        for(int i = 0; i < ship_layout.length; i++){
            for(int j = 0; j < ship_layout[i].length; j++){
                if(ship_layout[i][j] == 0){
                    prob_table[i][j] = -1.0;
                }else{
                    // equal probabiltiy for every open cell
                    prob_table[i][j] = 1.0/(num_of_path);
                }
            }
        }
        return prob_table;
    }

    // update probability table when bot entered a new cell
    public static void bot_enters_cell_probability_update(Ship ship, double[][] prob_table){
        // set the current bot location to probability 0.0 because it is not a leak
        prob_table[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 0.0;

        // total probability after (100%) after current bot location probability drop down to 0%
        double prob_leak_in_cell_k_AND_leak_not_in_bot_loc = 0.0;

        // sum all the probability (the new 100% because current bot location probability drop down to 0%)
        for(Map.Entry<String, Coordinate> entry : ship.getHashPath().entrySet()){
            Coordinate value = entry.getValue();
            prob_leak_in_cell_k_AND_leak_not_in_bot_loc += prob_table[value.getRow()][value.getCol()];
        }

        // update new probability for each open cell with the new 100% (probability for each open cell beside the current bot location will increase, since current bot location probability drop down to 0%)
        for(Map.Entry<String, Coordinate> entry : ship.getHashPath().entrySet()){
            Coordinate value = entry.getValue();
            prob_table[value.getRow()][value.getCol()] = prob_table[value.getRow()][value.getCol()] / (prob_leak_in_cell_k_AND_leak_not_in_bot_loc);

        }
    }

    // telling the bot if it get a beep from the leak
    public static boolean beep(Ship ship){
        // get the shortest path to the leak
        Coordinate leak1_coor = BFS_FIND_LEAK_FOR_BEEP(ship, ship.getLeak1Coor());
        Coordinate leak2_coor = BFS_FIND_LEAK_FOR_BEEP(ship, ship.getLeak2Coor());

        // -a*(distance(i, leak) - 1)
        double exp1 = (-1)*ship.getAlpha()*(leak1_coor.getLength()-1);
        double exp2 = (-1)*ship.getAlpha()*(leak2_coor.getLength()-1);

        // check if "BEEP" is caused by either leak1 or leak2, or both
        return (Math.random() <= Math.exp(exp1) || Math.random() <= Math.exp(exp2));
    }

    // find shortest distance to the leak
    public static Coordinate BFS_FIND_LEAK_FOR_BEEP(Ship ship, Coordinate leak_cell){
        Coordinate bot_coor = ship.getBotCoor();
        Queue<Coordinate> queue = new LinkedList<>();
        boolean[][] visited = new boolean[ship.getShipArray().length][ship.getShipArray().length];

        // add initial node to queue
        queue.add(new Coordinate(bot_coor, null, 0));

        while(queue.peek() != null){
            // pop the head (Coordinate) of the queue
            Coordinate curr = queue.poll();

            // check if the curr cell is the leak cell (goal)
            if(curr.compare_coor(leak_cell)){
                return curr;
            }

            // check if the curr cell is in close set, if not then explore it childs
            if(visited[curr.getRow()][curr.getCol()] == false){
                // if curr is not the leak cell, add more childs
                add_candidate_child(ship, curr, queue);
                
                // add curr to visited coor
                visited[curr.getRow()][curr.getCol()] = true;
            }
        }
        return null;
    }

    // Overload method for add_candidate_child, used by BFS_FIND_LEAK_FOR_BEEP, get_distance_from_bot_table
    public static void add_candidate_child(Ship ship, Coordinate parent, Queue<Coordinate> queue){
        // used to randomize the order these cell child are added to the queue
        // ArrayList<Coordinate> list = new ArrayList<>();
        
        // top cell
        if(parent.getRow()-1 > -1){
            Coordinate top_cell = new Coordinate(new Coordinate(parent.getRow()-1, parent.getCol()), parent, parent.getLength()+1);
            if(ship.getHashPath().containsKey(String.format("%d%d", top_cell.getRow(), top_cell.getCol())) && ship.getShipArray()[top_cell.getRow()][top_cell.getCol()] != 0){
                queue.add(top_cell);
                // list.add(top_cell);
            }   
        }
         
        // boottom cell
        if(parent.getRow()+1 < ship.getShipArray().length){
            Coordinate bottom_cell = new Coordinate(new Coordinate(parent.getRow()+1, parent.getCol()), parent, parent.getLength()+1);
            if(ship.getHashPath().containsKey(String.format("%d%d", bottom_cell.getRow(), bottom_cell.getCol())) && ship.getShipArray()[bottom_cell.getRow()][bottom_cell.getCol()] != 0){
                queue.add(bottom_cell);
                // list.add(bottom_cell);
            }  
            
        }

        // left cell
        if(parent.getCol()-1 > -1){
            Coordinate left_cell = new Coordinate(new Coordinate(parent.getRow(), parent.getCol()-1), parent, parent.getLength()+1);
            if(ship.getHashPath().containsKey(String.format("%d%d", left_cell.getRow(), left_cell.getCol())) && ship.getShipArray()[left_cell.getRow()][left_cell.getCol()] != 0){
                queue.add(left_cell);
                // list.add(left_cell);
            }  
        }

        // right cell
        if(parent.getCol()+1 < ship.getShipArray()[parent.getRow()].length){
            Coordinate right_cell = new Coordinate(new Coordinate(parent.getRow(), parent.getCol()+1), parent, parent.getLength()+1);
            if(ship.getHashPath().containsKey(String.format("%d%d", right_cell.getRow(), right_cell.getCol())) && ship.getShipArray()[right_cell.getRow()][right_cell.getCol()] != 0){
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

    // make up a distance table for updating probability with or without heared a beep
    public static int[][] get_distance_from_bot_table(Ship ship){
        int[][] distance_table = new int[ship.getShipArray().length][ship.getShipArray().length];

        Coordinate bot_coor = ship.getBotCoor();
        Queue<Coordinate> queue = new LinkedList<>();
        boolean[][] visited = new boolean[ship.getShipArray().length][ship.getShipArray().length];

        // add initial node to queue
        queue.add(new Coordinate(bot_coor, null, 0));

        while(queue.peek() != null){
            // pop the head (Coordinate) of the queue
            Coordinate curr = queue.poll();

            // check if the curr cell is in close set, if not then explore it childs
            if(visited[curr.getRow()][curr.getCol()] == false){
                // add more childs
                add_candidate_child(ship, curr, queue);

                // set the current cell distanct to the distance table
                distance_table[curr.getRow()][curr.getCol()] = curr.getLength();
                
                // add curr to visited coor
                visited[curr.getRow()][curr.getCol()] = true;
            }
        }

        return distance_table;
    }

    // denominator of formula with or without heard a beep
    public static double beep_in_i(Ship ship, double[][] prob_table, int[][] distance_table){
        double prob_beep_in_i = 0.0;
         for(Map.Entry<String, Coordinate> entry : ship.getHashPath().entrySet()){
            Coordinate value = entry.getValue();
            if(!value.compare_coor(ship.getBotCoor())){
                prob_beep_in_i +=
                    prob_table[value.getRow()][value.getCol()] * Math.exp((-1)*ship.getAlpha()*(distance_table[value.getRow()][value.getCol()]-1)) ;
            }
        }
        return prob_beep_in_i;
    }

    // update probability table after heard a beep
    public static void with_beep_probability_update(Ship ship, double[][] prob_table, int[][] distance_table){
        double beep_in_i = beep_in_i(ship, prob_table, distance_table);
        for(Map.Entry<String, Coordinate> entry : ship.getHashPath().entrySet()){
            Coordinate value = entry.getValue();
            if(!value.compare_coor(ship.getBotCoor())){
                prob_table[value.getRow()][value.getCol()] = 
                (prob_table[value.getRow()][value.getCol()] * Math.exp((-1)*ship.getAlpha()*(distance_table[value.getRow()][value.getCol()]-1))) 
                / beep_in_i;
            }
        }
    }

    // update probability table after heard no beep
    public static void without_beep_probability_update(Ship ship, double[][] prob_table, int[][] distance_table){
        double beep_in_i = beep_in_i(ship, prob_table, distance_table);
        for(Map.Entry<String, Coordinate> entry : ship.getHashPath().entrySet()){
            Coordinate value = entry.getValue();
            if(!value.compare_coor(ship.getBotCoor())){
                prob_table[value.getRow()][value.getCol()] = 
                (prob_table[value.getRow()][value.getCol()] * (1 - Math.exp((-1)*ship.getAlpha()*(distance_table[value.getRow()][value.getCol()]-1)))) 
                / (1.0-beep_in_i);
            }
        }
    }

    // get the max probability coordinate (the next location to go)
    public static Coordinate get_location_of_max_probability(Ship ship, double[][] prob_table){
        return BFS_FIND_MAX_PROBABILITY(ship, prob_table);
    }

    // find the coordinate of the shortest path of the probility table that contains the max probability in the probability table
    public static Coordinate BFS_FIND_MAX_PROBABILITY(Ship ship, double[][] prob_table){
        Coordinate bot_coor = ship.getBotCoor();
        Queue<Coordinate> queue = new LinkedList<>();
        boolean[][] visited = new boolean[ship.getShipArray().length][ship.getShipArray().length];

        // add initial node to queue
        queue.add(new Coordinate(bot_coor, null, 0));

        // set the current max prob to the prob of the current bot location cell and its coordinate
        double max_prob = prob_table[bot_coor.getRow()][bot_coor.getCol()];
        Coordinate max_prob_coor = bot_coor;

        while(queue.peek() != null){
            // pop the head (Coordinate) of the queue
            Coordinate curr = queue.poll();

            // change the max prob and coordinate if found a new one that is bigger than the previous one
            if(prob_table[curr.getRow()][curr.getCol()] > max_prob){
                max_prob = prob_table[curr.getRow()][curr.getCol()];
                max_prob_coor = curr;
            }

            // check if the curr cell is in close set, if not then explore it childs
            if(visited[curr.getRow()][curr.getCol()] == false){
                // add more childs
                add_candidate_child(ship, prob_table, curr, queue);
                
                // add curr to visited coor
                visited[curr.getRow()][curr.getCol()] = true;
            }
        }
        // give back the max probility cell the bot should go next
        return max_prob_coor;
    }

    // Overload method for add_candidate_child, used by BFS_FIND_MAX_PROBABILITY
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

    // Retrive the path to goal note by back tracking its parent, so on and so forth
    public static ArrayList<Coordinate> retrive_path(Coordinate goal_note){
        ArrayList<Coordinate> path_to_goal = new ArrayList<>();
        Coordinate temp = goal_note;
        while(temp.getParent() != null){
            path_to_goal.add(0, new Coordinate(temp.getRow(), temp.getCol()));
            temp = temp.getParent();
        }
        return path_to_goal;
    }

    public static int run_bot7(Ship ship){
        // get probablity of containing leak for each cell
        double[][] prob_table = init_leak_prob_table(ship);

        // counter for number of actions
        int action_num = 0;

        // array used to check both leaks are visited or not
        boolean[] visited_leak = new boolean[2];

        // while both leaks are not plugged keep running
        while (!(visited_leak[0] == true && visited_leak[1] == true)) {

            // if the current cell is leak1 or leak2 set visited (plugged) to true
            if(ship.getBotCoor().compare_coor(ship.getLeak1Coor())){
                ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
                visited_leak[0] = true;
            }else{
                if(ship.getBotCoor().compare_coor(ship.getLeak2Coor())){
                    ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
                    visited_leak[1] = true;
                }
            }

            // update the probability table after the bot entered a new cell that may or may not a leak (bot 7 only)
            bot_enters_cell_probability_update(ship, prob_table);

            // if both leaks are visited break from the loop
            if((visited_leak[0] == true && visited_leak[1] == true)){
                break;
            }

            // check a "BEEP" is heared from either leak1 or leak2, or both
            boolean heard_beep = beep(ship);
            action_num++;

            // create a distance from current bot location to other cell
            // used for later calculation in methods "with_beep_probability_update" and "without_beep_probability_update"
            int[][] distance_table = get_distance_from_bot_table(ship);

            // react to "BEEP" accordingly
            if(heard_beep == true){
                with_beep_probability_update(ship, prob_table, distance_table);
            }else{ // heard_beep == false
                without_beep_probability_update(ship, prob_table, distance_table);
            }

            // get the next location to go where had the highest probability in the probability table
            Coordinate next_loc = get_location_of_max_probability(ship, prob_table);

            // gather the path of each individual cells toward the next location with the highest probability in the probability table
            ArrayList<Coordinate> path_to_next_loc = retrive_path(next_loc);

            // for each coordinate the bot had to go through update visited leaks accordingly
            // every time move into a new cell update the probability table
            for(Coordinate coor : path_to_next_loc){
                // if the current cell is leak1 or leak2 set visited (plugged) to true
                if(ship.getBotCoor().compare_coor(ship.getLeak1Coor())){
                    ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
                    visited_leak[0] = true;
                }else{
                    if(ship.getBotCoor().compare_coor(ship.getLeak2Coor())){
                        ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
                        visited_leak[1] = true;
                    }else{
                        ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 1;
                    }
                }

                // Move
                ship.setBotCoor(coor);
                ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 5;
                action_num++;

                // update the probability table after the bot entered a new cell that may or may not a leak (bot 7 only)
                bot_enters_cell_probability_update(ship, prob_table);
            }
        }

        // after done with plugging the leaks return the action number the bot took
        return action_num;
    }

    public static int run_bot7_demo(Ship ship){
        // get probablity of containing leak for each cell
        double[][] prob_table = init_leak_prob_table(ship);

        // counter for number of actions
        int action_num = 0;

        // array used to check both leaks are visited or not
        boolean[] visited_leak = new boolean[2];

        // while both leaks are not plugged keep running
        while (!(visited_leak[0] == true && visited_leak[1] == true)) {

            // if the current cell is leak1 or leak2 set visited (plugged) to true
            if(ship.getBotCoor().compare_coor(ship.getLeak1Coor())){
                ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
                visited_leak[0] = true;
            }else{
                if(ship.getBotCoor().compare_coor(ship.getLeak2Coor())){
                    ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
                    visited_leak[1] = true;
                }
            }

            // update the probability table after the bot entered a new cell that may or may not a leak (bot 7 only)
            bot_enters_cell_probability_update(ship, prob_table);

            // if both leaks are visited break from the loop
            if((visited_leak[0] == true && visited_leak[1] == true)){
                // print the ship layout and other information
                Ship.print_layout(ship.getShipArray());
                break;
            }

            // check a "BEEP" is heared from either leak1 or leak2, or both
            boolean heard_beep = beep(ship);
            action_num++;

            // create a distance from current bot location to other cell
            // used for later calculation in methods "with_beep_probability_update" and "without_beep_probability_update"
            int[][] distance_table = get_distance_from_bot_table(ship);

            // react to "BEEP" accordingly
            if(heard_beep == true){
                System.out.println("BEEP");
                with_beep_probability_update(ship, prob_table, distance_table);
            }else{ // heard_beep == false
                without_beep_probability_update(ship, prob_table, distance_table);
            }

            // get the next location to go where had the highest probability in the probability table
            Coordinate next_loc = get_location_of_max_probability(ship, prob_table);

            // gather the path of each individual cells toward the next location with the highest probability in the probability table
            ArrayList<Coordinate> path_to_next_loc = retrive_path(next_loc);

            // for each coordinate the bot had to go through update visited leaks accordingly
            // every time move into a new cell update the probability table
            for(Coordinate coor : path_to_next_loc){
                // if the current cell is leak1 or leak2 set visited (plugged) to true
                if(ship.getBotCoor().compare_coor(ship.getLeak1Coor())){
                    ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
                    visited_leak[0] = true;
                }else{
                    if(ship.getBotCoor().compare_coor(ship.getLeak2Coor())){
                        ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 4;
                        visited_leak[1] = true;
                    }else{
                        ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 1;
                    }
                }

                // Move
                ship.setBotCoor(coor);
                ship.getShipArray()[ship.getBotCoor().getRow()][ship.getBotCoor().getCol()] = 5;
                action_num++;

                // print the ship layout and other information
                Ship.print_layout(ship.getShipArray());
                
                // update the probability table after the bot entered a new cell that may or may not a leak (bot 7 only)
                bot_enters_cell_probability_update(ship, prob_table);

                // Pause for 1 second to see the graphic
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch(Exception error){
                    System.out.println("Error occur!");
                    System.out.println(error);
                }
            }
        }

        return action_num;
    }
}
