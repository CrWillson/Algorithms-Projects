package roadscholar;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int numIntersections = 0;
        int numRoads = 0;
        int numCities = 0;
        int numSigns = 0;

        float distance[][] = null;
        int predecessor[][] = null;
        String cities[] = null;
        Sign signs[] = null;

        // Parse the input
        try {
            Scanner scanner = new Scanner(new File("roadscholar/input.txt"));

            numIntersections = scanner.nextInt();
            numRoads = scanner.nextInt();
            numCities = scanner.nextInt();

            cities = new String[numIntersections];

            // Initialize an empty distance table
            distance = new float[numIntersections][numIntersections];
            predecessor = new int[numIntersections][numIntersections];
            for (int i = 0; i < numIntersections; i++) {
                for (int j = 0; j < numIntersections; j++) {
                    distance[i][j] = Float.MAX_VALUE;
                    if (i == j) {
                        distance[i][j] = 0;
                    }
                }
            }

            // Set up initial weight table
            for (int i = 0; i < numRoads; i++) {
                int i1 = scanner.nextInt();
                int i2 = scanner.nextInt();
                float dist = scanner.nextFloat();

                distance[i1][i2] = dist;
                distance[i2][i1] = dist;

                predecessor[i1][i2] = i1;
                predecessor[i2][i1] = i2;
            }

            // Get the name of all the cities
            for (int i = 0; i < numCities; i++) {
                int index = scanner.nextInt();
                String name = scanner.next();

                cities[index] = name;
            }

            // Create all of the signs
            numSigns = scanner.nextInt();
            signs = new Sign[numSigns];
            for (int i = 0; i < numSigns; i++) {
                int i1 = scanner.nextInt();
                int i2 = scanner.nextInt();
                float dist = scanner.nextFloat();

                signs[i] = new Sign(i1, i2, dist);
            }

        }
        catch (Exception e) {
            System.err.println(e);
        }

        // Run Floyd's algorithm
        for (int k = 0; k < numIntersections; k++) {
            for (int u = 0; u < numIntersections; u++) {
                for (int v = 0; v < numIntersections; v++) {
                    if ((distance[u][k] + distance[k][v]) < distance[u][v]) {
                        distance[u][v] = distance[u][k] + distance[k][v];
                        predecessor[u][v] = predecessor[k][v];
                    }
                }
            }
        }

        // Add labels to the signs
        for (int i = 0; i < numSigns; i++) {
            int startNode = signs[i].i1;
            for (int j = 0; j < numIntersections; j++) {
                if (cities[j] == null) {
                    continue;
                }
                int totalDist = Math.round(distance[startNode][j] - signs[i].dist);

                int endNode = j;
                while(endNode != startNode) {
                    if (endNode == signs[i].i2) {
                        signs[i].labels.put(totalDist, cities[j]);
                        break;
                    }
                    endNode = predecessor[startNode][endNode];
                }
            }
        }
        
        // Print out the labels
        for (int i = 0; i < numSigns; i++) {
            int numLabels = signs[i].labels.size();

            for (int j = 0; j < numLabels; j++) {
                int dist = signs[i].labels.firstKey();
                String city = signs[i].labels.get(dist);
                
                signs[i].labels.remove(dist, city);
                System.out.println(city + " " + dist);
            }

            if (numLabels != 0) {
                System.out.print("\n");
            }
        }

        /*
        DecimalFormat decFor = new DecimalFormat("#.##");
        for (int i = 0; i < numIntersections; i++) {
            for (int j = 0; j <numIntersections; j++) {
                System.out.print(decFor.format(best[i][j]) + ", ");
            }
            System.out.print("\n");
        }


        System.out.println("------------------------");

        for (int i = 0; i < numIntersections; i++) {
            for (int j = 0; j <numIntersections; j++) {
                System.out.print(predecessor[i][j] + ", ");
            }
            System.out.print("\n");
        }
        */
    }
}