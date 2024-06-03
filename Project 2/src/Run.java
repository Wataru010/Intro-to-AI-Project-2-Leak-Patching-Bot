public class Run {
    public static void main(String args[]){
        // for(int k = 1; k < 50; k++){
        //     for(int i = 0; i < 500; i++){
        //         Ship ship = Ship.build_ship(1, k, 0.0);
        //         int bot1_action = Bot1.run_bot1(ship);
        //         System.out.println("Total Action Took: " + bot1_action);
        //         if(bot1_action < 0){
        //             System.out.println(k);
        //             System.out.println(i);
        //             break;
        //         }
        //     }
        // }

        // Ship ship = Ship.build_ship(1, 20, 0.0);
        // int bot1_action = Bot1.run_bot1_demo(ship);
        // System.out.println("Total Action Took: " + bot1_action);


        // Bot1 vs Bot2 Test Run 
        // ---------------------------------------------------------------------------------------------------------------------
        int[] bot1_actions = new int[25];
        int[] bot2_actions = new int[25];
        for(int k = 1; k < 26; k++){
            int total1 = 0;
            int total2 = 0;

            for(int i = 0; i < 300; i++){
                int k_value = k;
                Ship ship = Ship.build_ship(1, k_value, 0.0);
                int bot1_action = Bot1.run_bot1(ship);
                System.out.println("Total Action Took: " + bot1_action);
                if(bot1_action < 0){
                    System.out.println(k);
                    System.out.println(i);
                    break;
                }
                total1 += bot1_action;

                ship.reset_ship(k_value, 0.0);

                int bot2_action = Bot2.run_bot2(ship);
                System.out.println("Total Action Took: " + bot2_action);
                if(bot2_action < 0){
                    System.out.println(k);
                    System.out.println(i);
                    break;
                }
                total2 += bot2_action; 
            }
            System.out.println(total1/300);
            System.out.println(total2/300);
            bot1_actions[k-1] = total1/300;
            bot2_actions[k-1] = total2/300;
        }

        System.out.println();
        for(int a = 0; a < 3; a++){
            if(a == 0){
                System.out.print("K Val:  ");
            }else{
                System.out.print("Bot " + a + ":  ");
            }
            for(int i = 0; i < 25; i++){
                if(a == 0){
                    if(i > 9){
                        System.out.print(i+1 + "   ");
                    }else{
                        System.out.print(i+1 + "    ");
                    }
                }else if(a == 1){
                    System.out.print(bot1_actions[i]+ "  ");
                }else{
                    System.out.print(bot2_actions[i]+ "  ");
                }
            }
            System.out.println();
        }
        // ---------------------------------------------------------------------------------------------------------------------

        // Bot5 vs Bot6 Test Run 
        // ---------------------------------------------------------------------------------------------------------------------
        int[] bot5_actions = new int[25];
        int[] bot6_actions = new int[25];
        for(int k = 1; k < 26; k++){
            int total1 = 0;
            int total2 = 0;

            for(int i = 0; i < 300; i++){
                int k_value = k;
                Ship ship = Ship.build_ship(5, k_value, 0.0);
                int bot5_action = Bot5.run_bot5(ship);
                System.out.println("Total Action Took: " + bot5_action);
                if(bot5_action < 0){
                    System.out.println(k);
                    System.out.println(i);
                    break;
                }
                total1 += bot5_action;

                ship.reset_ship(k_value, 0.0);

                int bot6_action = Bot6.run_bot6(ship);
                System.out.println("Total Action Took: " + bot6_action);
                if(bot6_action < 0){
                    System.out.println(k);
                    System.out.println(i);
                    break;
                }
                total2 += bot6_action; 
            }
            System.out.println(total1/300);
            System.out.println(total2/300);
            bot5_actions[k-1] = total1/300;
            bot6_actions[k-1] = total2/300;
        }

        System.out.println();
        for(int a = 0; a < 3; a++){
            if(a == 0){
                System.out.print("K Val:  ");
            }else{
                System.out.print("Bot " + (a+4) + ":  ");
            }
            for(int i = 0; i < 25; i++){
                if(a == 0){
                    if(i > 9){
                        System.out.print(i+1 + "   ");
                    }else{
                        System.out.print(i+1 + "    ");
                    }
                }else if(a == 1){
                    System.out.print(bot5_actions[i]+ "  ");
                }else{
                    System.out.print(bot6_actions[i]+ "  ");
                }
            }
            System.out.println();
        }
        // ---------------------------------------------------------------------------------------------------------------------

        // Bot3 vs Bot4 Test Run 
        // ---------------------------------------------------------------------------------------------------------------------
        int[] bot3_actions = new int[11];
        int[] bot4_actions = new int[11];
        int count34 = 0;
        for(double a = 0.01; a < 0.11; a+=0.01){
            int total3 = 0;
            int total4 = 0;

            for(int i = 0; i < 300; i++){
                double alpha_value = a;
                Ship ship = Ship.build_ship(3, 1, alpha_value);
                int bot3_action = Bot3.run_bot3(ship);
                System.out.println("Total Action Took: " + bot3_action);
                if(bot3_action < 0){
                    System.out.println(alpha_value);
                    System.out.println(i);
                    break;
                }
                total3 += bot3_action;

                ship.reset_ship(1, alpha_value);

                int bot4_action = Bot4.run_bot4(ship);
                System.out.println("Total Action Took: " + bot4_action);
                if(bot4_action < 0){
                    System.out.println(alpha_value);
                    System.out.println(i);
                    break;
                }
                total4 += bot4_action; 
            }
            System.out.println(total3/300);
            System.out.println(total4/300);
            bot3_actions[count34] = total3/300;
            bot4_actions[count34] = total4/300;

            count34++;
        }
        
        System.out.println();
        for(int a = 0; a < 3; a++){
            if(a == 0){
                System.out.print("A Val: ");
            }else{
                System.out.print("Bot " + (a+2) + ": ");
            }
            for(int i = 0; i < 10; i++){
                if(a == 0){
                    System.out.printf("%.2f   ", 0.01*(i+1));
                }else if(a == 1){
                    System.out.print(bot3_actions[i]+ "   ");
                }else{
                    System.out.print(bot4_actions[i]+ "   ");
                }
            }
            System.out.println();
        }
        // ---------------------------------------------------------------------------------------------------------------------

        // Bot7 vs Bot8 vs Bot9 Test Run
        // ---------------------------------------------------------------------------------------------------------------------
        int[] bot7_actions = new int[11];
        int[] bot8_actions = new int[11];
        int[] bot9_actions = new int[11];
        int count789 = 0;
        for(double a = 0.01; a < 0.11; a+=0.01){
            int total7 = 0;
            int total8 = 0;
            int total9 = 0;

            for(int i = 0; i < 100; i++){
                double alpha_value = a;
                Ship ship = Ship.build_ship(7, 1, alpha_value);
                int bot7_action = Bot7.run_bot7(ship);
                System.out.println("7: Total Action Took: " + bot7_action);
                if(bot7_action < 0){
                    System.out.println(alpha_value);
                    System.out.println(i);
                    break;
                }
                total7 += bot7_action;

                ship.reset_ship(1, alpha_value);

                int bot8_action = Bot8.run_bot8(ship);
                System.out.println("8: Total Action Took: " + bot8_action);
                if(bot8_action < 0){
                    System.out.println(alpha_value);
                    System.out.println(i);
                    break;
                }
                total8 += bot8_action; 

                ship.reset_ship(1, alpha_value);

                int bot9_action = Bot9.run_bot9(ship);
                System.out.println("9: Total Action Took: " + bot9_action);
                if(bot9_action < 0){
                    System.out.println(alpha_value);
                    System.out.println(i);
                    break;
                }
                total9 += bot9_action; 
            }
            System.out.println(total7/100);
            System.out.println(total8/100);
            System.out.println(total9/100);
            bot7_actions[count789] = total7/100;
            bot8_actions[count789] = total8/100;
            bot9_actions[count789] = total9/100;    

            count789++;
        }
        
        System.out.println();
        for(int a = 0; a < 4; a++){
            if(a == 0){
                System.out.print("A Val: ");
            }else{
                System.out.print("Bot " + (a+6) + ": ");
            }
            for(int i = 0; i < 10; i++){
                if(a == 0){
                    System.out.printf("%.2f   ", 0.01*(i+1));
                }else if(a == 1){
                    System.out.print(bot7_actions[i]+ "   ");
                }else if(a == 2){
                    System.out.print(bot8_actions[i]+ "   ");
                }else{
                    System.out.print(bot9_actions[i]+ "   ");
                }
            }
            System.out.println();
        }
        // ---------------------------------------------------------------------------------------------------------------------


        // int total3 = 0;
        // for(int i = 0; i < 500; i++){
        //     Ship ship = Ship.build_ship(3, 1, 0.9);
        //     int bot3_action = Bot3.run_bot3(ship);
        //     System.out.println("Total Action Took: " + bot3_action);
        //     total3+=bot3_action;
        // }
        // System.out.println(total3/500);

        // int total7 = 0;
        // for(int i = 0; i < 500; i++){
        //     Ship ship = Ship.build_ship(7, 1, 0.9);
        //     int bot7_action = Bot7.run_bot7(ship);
        //     System.out.println("Total Action Took: " + bot7_action);
        //     total7+=bot7_action;
        // }
        // System.out.println(total7/500);

        // int total8 = 0;
        // for(int i = 0; i < 100; i++){
        //     Ship ship = Ship.build_ship(8, 1, 0.05);
        //     int bot8_action = Bot8.run_bot8(ship);
        //     System.out.println("Total Action Took: " + bot8_action);
        //     total8+=bot8_action;
        // }
        // System.out.println(total8/100);

        // int total9 = 0;
        // for(int i = 0; i < 100; i++){
        //     Ship ship = Ship.build_ship(9, 1, 0.05);
        //     int bot9_action = Bot9.run_bot9(ship);
        //     System.out.println("Total Action Took: " + bot9_action);
        //     total9+=bot9_action;
        // }
        // System.out.println(total9/100);

        // Ship ship = Ship.build_ship(1, 5, 0.0);
        // int bot1_action = Bot1.run_bot1_demo(ship);
        // System.out.println("Total Action Took: " + bot1_action);

        // ship.reset_ship(5, 0.0);
        // int bot2_action = Bot2.run_bot2_demo(ship);
        // System.out.println("Total Action Took: " + bot2_action);

        // Ship ship = Ship.build_ship(5, 5, 0);
        // int bot5_action = Bot5.run_bot5_demo(ship);
        // System.out.println("Total Action Took: " + bot5_action);

        // Ship ship = Ship.build_ship(5, 1, 0);
        // int bot6_action = Bot6.run_bot6_demo(ship);
        // System.out.println("Total Action Took: " + bot6_action);

        // Ship ship = Ship.build_ship(3, 1, 1);
        // int bot3_action = Bot3.run_bot3_demo(ship);
        // System.out.println("Total Action Took: " + bot3_action);

        // Ship ship = Ship.build_ship(4, 1, 0.1);
        // int bot4_action = Bot4.run_bot4_demo(ship);
        // System.out.println("Total Action Took: " + bot4_action);

        // Ship ship = Ship.build_ship(7, 1, 0.1);
        // int bot7_action = Bot7.run_bot7_demo(ship);
        // System.out.println("Total Action Took: " + bot7_action);


        // Ship ship = Ship.build_ship(8, 1, 0.05);
        // int bot8_action = Bot8.run_bot8_demo(ship);
        // System.out.println("Total Action Took: " + bot8_action);

        // Ship ship = Ship.build_ship(9, 1, 0.05);
        // int bot9_action = Bot9.run_bot9_demo(ship);
        // System.out.println("Total Action Took: " + bot9_action);
    }   
}
