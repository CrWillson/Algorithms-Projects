package coincounter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner; 

public class CoinCounter {

    public static int coinSum(int coins[]) {
        int sum = 0;
        for (int i = 0; i < coins.length; i++) {
            sum += coins[i];
        }
        return sum;
    }

    public static boolean isEmpty(int coins[]) {
        for (int i = 0; i < coins.length; i++) {
            if (coins[i] != 0) { return false; }
        }
        return true;
    }

    public static void printCoins(int n, int denoms[], int coins[]) {
        System.out.print(n + " cents =");
        for (int i = denoms.length - 1; i >= 0; i--) {
            if (coins[i] != 0) {
                System.out.print(" " + denoms[i] + ":" + coins[i]);
            }
        }
        System.out.print("\n");
    }

    public static int[] mc_recursive(int n, int denoms[], int memo[][]) {
        int numDenom = denoms.length;
        if (n < 2) { 
            int returnArr[] = new int[numDenom];
            returnArr[0] = n;
            memo[n] = returnArr;
            return returnArr; 
        }

        int best = Integer.MAX_VALUE;
        int bestCoins[] = null;
        for (int k = 0; k < numDenom; k++) {
            int size = n - denoms[k];
            if (size < 0) { continue; }
            int coins[] = mc_recursive(size, denoms, memo).clone();
            coins[k]++;
            if ((coinSum(coins) < best)) { 
                best = coinSum(coins);
                bestCoins = coins; 
            }
        }

        if (isEmpty(memo[n])) { memo[n] = bestCoins; }
        return bestCoins;
    }

    public static int[] mc_memo(int n, int denoms[], int memo[][]) {
        int numDenom = denoms.length;
        if (n < 2) { 
            int returnArr[] = new int[numDenom];
            returnArr[0] = n;
            memo[n] = returnArr;
            return returnArr; 
        }
        if (!isEmpty(memo[n])) { return memo[n]; }

        int best = Integer.MAX_VALUE;
        int bestCoins[] = null;
        for (int k = 0; k < numDenom; k++) {
            int size = n - denoms[k];
            if (size < 0) { continue; }
            int coins[] = mc_memo(size, denoms, memo).clone();
            coins[k]++;
            if ((coinSum(coins) < best)) { 
                best = coinSum(coins);
                bestCoins = coins; 
            }
        }

        if (isEmpty(memo[n])) { memo[n] = bestCoins; }
        return bestCoins;
    }

    public static int[] mc_iter(int n, int denoms[], int memo[][]) {
        int numDenom = denoms.length;
        int bestCoins[] = null;

        // initialize the 1 cent case (the 0 cent case is already initialized)
        memo[1][0] = 1;

        for (int i = 2; i <= n; i++) {
            int best = Integer.MAX_VALUE;
            bestCoins = null;

            for (int k = 0; k < numDenom; k++) {
                int size = i - denoms[k];
                if (size < 0) { continue; }
                int coins[] = memo[size].clone();
                coins[k]++;
                if ((coinSum(coins) < best)) { 
                    best = coinSum(coins);
                    bestCoins = coins; 
                }
            }
            memo[i] = bestCoins;
        }

        return bestCoins;
    }
    public static void main(String[] args) {
        /*
        try {
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        */

        try {
            File inFile = new File("project2/input.txt");
            Scanner input = new Scanner(inFile);
            //Scanner input = new Scanner(System.in);

            int numDenoms = input.nextInt();

            int denoms[] = new int[numDenoms];
            for (int i = 0; i < numDenoms; i++) {
                denoms[i] = input.nextInt();
            }

            int numTests = input.nextInt();
            
            for (int i = 0; i < numTests; i++) {
                int inputNum = input.nextInt();
                int memo[][] = new int[inputNum + 1][numDenoms];

                long start = System.nanoTime();
                //int coins[] = mc_iter(inputNum, denoms, memo);
                int coins[] = mc_recursive(inputNum, denoms, memo);
                //int coins[] = mc_memo(inputNum, denoms, memo);
                long finish = System.nanoTime();

                long time = finish - start;
                
                printCoins(inputNum, denoms, coins);
                System.out.println("Execution time: " + time + "ns");
            }

            
            input.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}