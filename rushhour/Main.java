package rushhour;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Rush Hour puzzle solver
 * @author Joshua Cappella and Caleb Willson
 * 
 * Overview:
 *  After creating an array of vehicle objects out of the input, an initial board state
 *  is created and added to the map and the queue.
 *  While the queue is not empty or the solution is not found, it will continuously analyze
 *  the board state (which is represented by an array of vehicles) at the front of the queue.
 *  If the board state being analyzed is the end state, a string form of that board state
 *  is returned.
 *  Otherwise, the algorithm iterates through every car in the current vehicle array.
 *      First check for moves to the right or down.
 *          Then check if the vehicle is a car or truck.
 *              Depending on which vehicle type it is, there are different conditions for
 *              a move being invalid, but if it is invalid, stop checking in the right
 *              or down direction.
 *          If the move was valid, then create a deep copy of the current vehicle array
 *          and replace the current car with a new car shifted 1 unit to the right
 *          or down.
 *          If this new board state is one we haven't seen before (i.e. it isn't in the map)
 *          then add it to the map and queue.
 *              The map is set up so that key = new board state, 
 *              value = [parent board state, the move made to go from parent -> new]
 *          Repeat the process checking if the current car can move 2, 3, or 4 moves
 *          to the right or down until all moves are analyzed or an invalid move is hit.
 *      Second, check for any moves to the left or up.
 *          This process is largely the same as checking for moves to the left
 *          or down except some checks can be reduced since both cars and trucks act
 *          the same when moving left or up so there is no need to differentiate 
 *          between them.
 * Once a board state has been analyzed, all of its adjacent board states have been
 * added to both the map and queue.
 * Once the final state is found, the moves are printed by starting at the 
 * final state in the map and recursively traversing the map back to the original
 * board state (indicated by value = [null, null]) where in the move count is
 * printed. Then all the moves are printed on the way back up.
 */
public class Main {
    /**
     * Convert a 2d Vehicle board into a unique string representing it
     * @param board
     * @return
     */
    public static String boardToString(Vehicle[][] board) {
        String returnString = "";

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    returnString += " ";
                }
                else {
                    returnString += board[i][j].id;
                }
            }
        }

        return returnString;
    }

    /**
     * Convert an array of vehicles into a 2d board array
     * @param vehicles
     * @return
     */
    public static Vehicle[][] vehiclesToBoard(Vehicle[] vehicles) {
        Vehicle newBoard[][] = new Vehicle[6][6];

        for (int i = 0; i < vehicles.length; i++) {
            newBoard[vehicles[i].row][vehicles[i].col] = vehicles[i];
            if (vehicles[i].orientation == 'h') {
                newBoard[vehicles[i].row][vehicles[i].col + 1] = vehicles[i];
                if (vehicles[i].type.equals("truck")) newBoard[vehicles[i].row][vehicles[i].col + 2] = vehicles[i];
            }
            else {
                newBoard[vehicles[i].row + 1][vehicles[i].col] = vehicles[i];
                if (vehicles[i].type.equals("truck")) newBoard[vehicles[i].row + 2][vehicles[i].col] = vehicles[i];
            }
        }

        return newBoard;
    }

    /**
     * Convert an array of vehicles into a unique string representing them
     * @param vehicles
     * @return
     */
    public static String vehiclesToString(Vehicle[] vehicles) {
        return boardToString(vehiclesToBoard(vehicles));
    }

    /**
     * Make a deep copy of an array of vehicles
     * @param vehicles
     * @return
     */
    public static Vehicle[] cloneVehicles(Vehicle[] vehicles) {
        Vehicle newArr[] = new Vehicle[vehicles.length];
        for (int i = 0; i < vehicles.length; i++) {
            newArr[i] = vehicles[i].clone();
        }
        return newArr;
    } 

    /**
     * Analyze a given board state
     * @param map - Map<String, String[]>. Used to keep track of which states have been visited
     * @param queue - Queue<Vehicle[]>. Used to keep track of what states need analyzed
     * @param vehicles - Vehicle[]. The board state to be analyzed
     * @return Null if the final state wasn't found. If the final state was found
     *         return  a unique string representing the final state.
     */
    public static String analyzeBoardState(Map<String, String[]> map, Queue<Vehicle[]> queue, Vehicle[] vehicles) {
        // check for final board state
        if (vehicles[0].col == 4) {
            return vehiclesToString(vehicles);
        }

        Vehicle currBoard[][] = vehiclesToBoard(vehicles);

        // check each car's possible moves
        for (int i = 0; i < vehicles.length; i++) {
            Vehicle currVehicle = vehicles[i];

            // check for moves to the right and down
            for (int j = 1; j <= 4; j++) {
                String move = vehicles[i].color + " " + j;
                Vehicle newVehicles[] = cloneVehicles(vehicles);

                // check horizontal
                if (currVehicle.orientation == 'h') {
                    // check horizontal cars
                    if (currVehicle.type.equals("car")) {
                        if ((currVehicle.col + j > 4) ||
                            (currBoard[currVehicle.row][currVehicle.col + j + 1] != null)) {
                            break;
                        }
                    }
                    // check horizontal trucks
                    else {
                        if ((currVehicle.col + j > 3) ||
                            (currBoard[currVehicle.row][currVehicle.col + j + 2] != null)) {
                            break;
                        }
                    }

                    // replace the current vehicle in the new vehicle array with a shifted one
                    newVehicles[i] = vehicles[i].clone(0, j);
                    move += " R";
                }
                // check vertical
                else {
                    // check vertical cars
                    if (currVehicle.type.equals("car")) {
                        if ((currVehicle.row + j > 4) ||
                            (currBoard[currVehicle.row + j + 1][currVehicle.col] != null)) {
                            break;
                        }
                    }
                    // check vertical trucks
                    else {
                        if ((currVehicle.row + j > 3) ||
                            (currBoard[currVehicle.row + j + 2][currVehicle.col] != null)) {
                            break;
                        }
                    }

                    // replace the current vehicle in the new vehicle array with a shifted one
                    newVehicles[i] = vehicles[i].clone(j, 0);
                    move += " D";
                }

                if (!map.containsKey(vehiclesToString(newVehicles))) {
                    queue.add(newVehicles);
                    map.put(vehiclesToString(newVehicles), new String[]{vehiclesToString(vehicles), move});
                }
            }

            // check for moves to the left and up
            for (int j = -1; j >= -4; j--) {
                String move = vehicles[i].color + " " + -j;
                Vehicle newVehicles[] = cloneVehicles(vehicles);

                // check horizontal
                if (currVehicle.orientation == 'h') {
                    // check horizontal cars and trucks
                    if ((currVehicle.col + j < 0) ||
                        (currBoard[currVehicle.row][currVehicle.col + j] != null)) {
                        break;
                    }   

                    // replace the current vehicle in the new vehicle array with a shifted one
                    newVehicles[i] = vehicles[i].clone(0, j);
                    move += " L";
                }
                // check vertical
                else {
                    // check vertical cars and trucks
                    if ((currVehicle.row + j < 0) ||
                        (currBoard[currVehicle.row + j][currVehicle.col] != null)) {
                        break;
                    }

                    // replace the current vehicle in the new vehicle array with a shifted one
                    newVehicles[i] = vehicles[i].clone(j, 0);
                    move += " U";
                }

                if (!map.containsKey(vehiclesToString(newVehicles))) {
                    queue.add(newVehicles);
                    map.put(vehiclesToString(newVehicles), new String[]{vehiclesToString(vehicles), move});
                }
            }
        }

        return null;
    }

    /**
     * Print out the required moves to solve the puzzle.
     * @param map
     * @param currBoard - String. The final board state returned by analyzeBoardState
     * @param moveCount - int. Initially 0. Used for recursion.
     */
    public static void printMoves(Map<String, String[]> map, String currBoard, int moveCount) {
        if (map.get(currBoard)[0] == null) {
            if (moveCount == 1) {
                System.out.println(moveCount + "move");
            }
            System.out.println(moveCount + " moves");
            return;
        }

        printMoves(map, map.get(currBoard)[0], ++moveCount);
        System.out.println(map.get(currBoard)[1]);
    }

    /**
     * The main function for getting the input and processing it.
     * @param args
     */
    public static void main(String[] args) {
        int numVehicles = 0;
        Vehicle vehicleArr[] = null;
        Map<String, String[]> boardStatesMap = new HashMap<String,String[]>();
        Queue<Vehicle[]> boardQueue = new LinkedList<Vehicle[]>();
        int moveCount = 0;

        //try {
        //    File inFile = new File("rushhour/input4.txt");
        //    Scanner inScanner = new Scanner(inFile);
            Scanner inScanner = new Scanner(System.in);

            numVehicles = inScanner.nextInt();
            vehicleArr = new Vehicle[numVehicles];

            for (int i = 0; i < numVehicles; i++) {
                inScanner.nextLine();
                String carType = inScanner.nextLine();
                String color = inScanner.nextLine();
                char orien = (inScanner.nextLine()).charAt(0);
                int row = inScanner.nextInt() - 1;
                int col = inScanner.nextInt() - 1;

                vehicleArr[i] = new Vehicle(i, carType, color, orien, row, col);
            }

            inScanner.close();

            boardQueue.add(vehicleArr);
            boardStatesMap.put(vehiclesToString(vehicleArr), new String[]{null, null});
        //}
        //catch (FileNotFoundException e) {
        //    System.out.println("An error occurred.");
        //    e.printStackTrace();
        //}


        String finalBoard = null;
        while (!boardQueue.isEmpty() && finalBoard == null) {
           finalBoard = analyzeBoardState(boardStatesMap, boardQueue, boardQueue.remove());
        }

        printMoves(boardStatesMap, finalBoard, moveCount);

    }
}