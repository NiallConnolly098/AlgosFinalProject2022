import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FunctionalityPart1 {
    public static Edge paths;
    public static Route trips;

    public static void Graph(File stops, File stop_times, File transfers) throws IOException {
        System.out.println("Setting up Graph");

        paths = new Edge(stops, transfers);
        trips = new Route(stop_times);

        for (int i = 1; i < trips.Data.size(); i++) {
            stop_times trip1 = trips.Data.get(i - 1);
            stop_times trip2 = trips.Data.get(i);
            int cost = 1;
            if (trip1.trip_id == trip2.trip_id) {
                paths.Connect(trip1.stop_id, trip2.stop_id, cost);
            }
        }
        System.out.println("Finishing setting graph");
    }

    public static void ShortestPrint(int fromStopID, int toStopID) {
        ArrayList<Integer> shortestPath = paths.findShortest(fromStopID, toStopID);
        double shortestCost = paths.findCostOfShortest();
        if (shortestCost == Double.POSITIVE_INFINITY) {
            System.out.println("No route from from " + fromStopID + " to " + toStopID);
        } 
        else if (shortestCost == Double.NEGATIVE_INFINITY) {
            System.out.println("both are same");
        } 
        else if (shortestCost == -1.0) {
            System.out.println("Invalid input");
        } 
        else {
            System.out.println("Cost from " + fromStopID + " to " + toStopID + " is: " + shortestCost);
            ArrayList<stops> details = paths.findVisitedStops(shortestPath);
            for(int i = 0; i < shortestPath.size();i++){
                System.out.print(shortestPath.get(i));
                if(i != shortestPath.size() - 1){
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        String stops_times_path = "C:\\Users\\niall\\OneDrive\\Desktop\\input files\\stop_times.txt";
        File stop_times = new File(stops_times_path);
        String stops_path = "C:\\Users\\niall\\OneDrive\\Desktop\\input file\\sstops.txt";
        File stops = new File(stops_path);
        String transfers_path = "C:\\Users\\niall\\OneDrive\\Desktop\\input files\\transfers.txt";
        File transfers = new File(transfers_path);
        Graph(stops, stop_times, transfers);
        int fromStopID = 71;
        int toStopID = 646;
        ShortestPrint(fromStopID, toStopID);
    }
}
