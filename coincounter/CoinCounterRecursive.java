package coincounter;

import java.util.Scanner; 

public class CoinCounterRecursive {

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
    
    public static void main(String[] args) {
        //File inFile = new File("project2/input.txt");
        //Scanner input = new Scanner(inFile);
        Scanner input = new Scanner(System.in);

        int numDenoms = input.nextInt();

        int denoms[] = new int[numDenoms];
        for (int i = 0; i < numDenoms; i++) {
            denoms[i] = input.nextInt();
        }

        int numTests = input.nextInt();
        
        for (int i = 0; i < numTests; i++) {
            int inputNum = input.nextInt();
            int memo[][] = new int[inputNum + 1][numDenoms];
            int coins[] = mc_recursive(inputNum, denoms, memo);
            printCoins(inputNum, denoms, coins);
        }
        
        input.close();

    }
}