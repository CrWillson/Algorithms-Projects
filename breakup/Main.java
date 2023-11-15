package breakup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    static public int numSets = 0;

    static public Location findSet(Location x) {
        if (x != x.parent) {
            x.parent = findSet(x.parent);
        }
        return x.parent;
    }

    static public void union(Location x, Location y) {
        unionByRank(findSet(x), findSet(y));
        numSets--;
    }

    static public void unionByRank(Location x, Location y) {
        if (x.rank > y.rank) {
            y.parent = x;
        }
        else {
            x.parent = y;
            if (x.rank == y.rank) {
                y.rank++;
            }
        }
    }

    static public void main(String[] args) {
        String filePath = "breakup/input.txt";
        Stack<int[]> inputs = new Stack<>();
        int monthsSplit = 0;
        int N = 0, M = 0, K = 0, L = 0;

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            // grab the dimensions of the galaxy as well as the number of months to run for
            N = scanner.nextInt();
            M = scanner.nextInt();
            K = scanner.nextInt();
            L = scanner.nextInt();

            // add each month onto the stack
            for(int i = 0; i < L; i++) {
                int numIn = scanner.nextInt();
                int[] inputSet = new int[numIn];
                for(int j = 0; j < numIn; j++) {
                    inputSet[j] = scanner.nextInt();
                }
                inputs.push(inputSet);
            } 

            scanner.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        }
        
        // create the galaxy
        Location[][][] galaxy = new Location[N][M][K];

        // empty the stack and slowly fill the galaxy up
        while(!inputs.empty()) {
            int[] inputSet = inputs.pop();
            int numIn = inputSet.length;

            // process all the dominions that joined the empire this month
            for(int i = 0; i < numIn; i++) {
                int x = inputSet[i] % N;
                int y = (inputSet[i] / N) % M;
                int z = inputSet[i] / (N*M);

                galaxy[x][y][z] = new Location(inputSet[i]);
                numSets++;

                //check +n neighbor
                if ((x < N-1) && 
                    (galaxy[x+1][y][z] != null) &&
                    (findSet(galaxy[x+1][y][z]) != findSet(galaxy[x][y][z]))) 
                    {
                        union(galaxy[x+1][y][z], galaxy[x][y][z]);
                    }
                //check -n neighbor
                if ((x > 0) && 
                    (galaxy[x-1][y][z] != null) &&
                    (findSet(galaxy[x-1][y][z]) != findSet(galaxy[x][y][z]))) 
                    {
                        union(galaxy[x-1][y][z], galaxy[x][y][z]);
                    }
                //check +m neighbor
                if ((y < M-1) && 
                    (galaxy[x][y+1][z] != null) &&
                    (findSet(galaxy[x][y+1][z]) != findSet(galaxy[x][y][z]))) 
                    {
                        union(galaxy[x][y+1][z], galaxy[x][y][z]);
                    }
                //check -m neighbor
                if ((y > 0) && 
                    (galaxy[x][y-1][z] != null) &&
                    (findSet(galaxy[x][y-1][z]) != findSet(galaxy[x][y][z]))) 
                    {
                        union(galaxy[x][y-1][z], galaxy[x][y][z]);
                    }
                //check +k neighbor
                if ((z < K-1) && 
                    (galaxy[x][y][z+1] != null) &&
                    (findSet(galaxy[x][y][z+1]) != findSet(galaxy[x][y][z]))) 
                    {
                        union(galaxy[x][y][z+1], galaxy[x][y][z]);
                    }
                //check -k neighbor
                if ((z > 0) && 
                    (galaxy[x][y][z-1] != null) &&
                    (findSet(galaxy[x][y][z-1]) != findSet(galaxy[x][y][z]))) 
                    {
                        union(galaxy[x][y][z-1], galaxy[x][y][z]);
                    }

            }
            
            // at the end of the month, check if the galaxy is broken up
            if (numSets != 1) {
                monthsSplit++;
            }
        }
        System.out.println(monthsSplit);
    }
}