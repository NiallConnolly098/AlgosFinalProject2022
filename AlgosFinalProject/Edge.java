import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.HashSet;

public class Edge {
	public static HashMap<Integer, ArrayList<edgeNode>> adjacencies;
    public static HashMap<Integer, stops> MapDetails;
    public static void Maps() {
    	adjacencies = new HashMap<>();
    	MapDetails = new HashMap<>();
    }
    public Edge() {
    	Maps();
    }
    
    private static void readTransfers(File transfers) throws IOException{
    	BufferedReader br = new BufferedReader(new FileReader(transfers));
    	//<String[]> lineList = new ArrayList<String[]>();
    	String store;
    	int lineCount = 0;
    	int empty = -1;
    	try {
    		while((store = br.readLine()) != null) {
    			String[] line = store.split(",");
    			if(lineCount != 0) {
    				int from_stop_id=(line[0].equals("")||line[0].equals(" ")) ? empty : Integer.parseInt(line[0]);
    				int to_stop_id=(line[1].equals("")||line[1].equals(" ")) ? empty : Integer.parseInt(line[1]);
    				int transfer_type=(line[2].equals("")||line[2].equals(" ")) ? empty : Integer.parseInt(line[2]);
    				double cost;
    				switch(transfer_type) {
    				//Cost is 2 if it comes from transfers.txt with transfer type 0
    				case 0:
    					cost = 2;
    					Connect(from_stop_id, to_stop_id, cost);
    					break;
    				//Cost is minimum transfer time / 100 if it comes from transfer.txt  with transfer type 2
    				case 2:
    					double min_transfer_time = Double.parseDouble(line[3]);
    					cost = min_transfer_time / 100;
    					Connect(from_stop_id, to_stop_id, cost);
    					break;
    				default:
    					throw new Exception("Invalid transfer type");
    				}
    			}
    			lineCount = lineCount + 1;
    		}
    	}
    	catch(Exception e){
    		System.out.println(e);
    	}
    	System.out.println("File 'transfers' read, lineCount: " + lineCount);
    	br.close();
    }
    
    public static void Connect(int from_stop_id, int to_stop_id, double cost) {
        adjacencies.computeIfAbsent(from_stop_id, k -> new ArrayList<>());
        adjacencies.computeIfAbsent(to_stop_id, k -> new ArrayList<>());
        adjacencies.get(from_stop_id).add(new edgeNode(cost, to_stop_id));
    }
    
    private static void readStops(File stops) throws IOException{
    	BufferedReader br = new BufferedReader(new FileReader(stops));
    	//ArrayList<String[]> lineList = new ArrayList<String[]>();
    	String store;
    	int lineCount = 0;
    	int empty = -1;
    	double emptyDouble = -1.0;
        String emptyString = "";
        try {
        	while((store = br.readLine()) != null) {
        		String[] line = store.split(",");
        		if(lineCount != 0) {
        			int stop_id = (line[0].equals("")||line[0].equals(" ")) ? empty : Integer.parseInt(line[0]);
        			int stop_code = (line[1].equals("")||line[1].equals(" ")) ? empty : Integer.parseInt(line[1]);
        			String stop_name = (line[2].equals("")||line[2].equals(" ")) ? emptyString : moveKeywords(line[2]);
        			String stop_desc = (line[3].equals("") || line[3].equals(" ")) ? emptyString : line[3];
        			double stop_lat = (line[4].equals("") || line[4].equals(" ")) ? emptyDouble : Double.parseDouble(line[4]);
        			double stop_lon = (line[5].equals("") || line[5].equals(" ")) ? emptyDouble : Double.parseDouble(line[5]);
        			String zone_id = (line[6].equals("") || line[6].equals(" ")) ? emptyString : line[6];
        			String stop_url = (line[7].equals("") || line[7].equals(" ")) ? emptyString : line[7];
        			int location_type = (line[8].equals("") || line[8].equals(" ")) ? empty : Integer.parseInt(line[8]);
        			String parent_station = (line.length == 9) ? emptyString : line[9];
        			
        			newStop(new stops(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, 
        					zone_id, stop_url,location_type, parent_station));
        		}
        		lineCount=lineCount+1;
        	}
        }
        catch(Exception e) {
        	System.out.println(e);
        }
        System.out.println("File 'stops' read, lineCount: " + lineCount);
        br.close();
    }
    
    public static String moveKeywords(String stop) {
    	int keyword = 2;
        int flagstop = 8;
        String temp = stop.substring(0, keyword).trim().toUpperCase();
        String tempFlag = stop.substring(0, flagstop).trim().toUpperCase();
        
        if (temp.equals("WB") || temp.equals("NB") || temp.equals("SB") || temp.equals("EB")) {
            String end = stop.substring(keyword + 1);
            String start = stop.substring(0, keyword);
            String movedString = end.concat(" ").concat(start);
            return moveKeywords(movedString);
        }
        if (tempFlag.equals("FLAGSTOP")) {
            String lastPart = stop.substring(flagstop + 1);
            String firstPart = stop.substring(0, flagstop);
            String movedString = lastPart.concat(" ").concat(firstPart);
            return moveKeywords(movedString);
        }
        else return stop;
    }
    
    public static void newStop(stops stop) {
        adjacencies.put(stop.stop_id, new ArrayList<>());
        MapDetails.put(stop.stop_id, stop);
    }
    
    public Edge(File stops, File transfers) throws IOException{
    	Maps();
    	readTransfers(transfers);
    	readStops(stops);
    }
    
    public ArrayList<Integer> findShortest(int from_stop_id, int to_stop_id){
    	if(validStopID(from_stop_id) && validStopID(to_stop_id)){
    		if(from_stop_id == to_stop_id){
    			System.out.println("Same stop");
    			costOfShortest = Double.NEGATIVE_INFINITY;
    			return null;
    		}
    		HashSet<Integer> visited = new HashSet<>(adjacencies.size());
    		HashMap<Integer, Integer> prev = new HashMap<>(adjacencies.size());
    		HashMap<Integer, Double> dist = new HashMap<>(adjacencies.size());
    		for (int i : adjacencies.keySet()) {
    			prev.put(i, Integer.MAX_VALUE);
    			visited.add(i);
                dist.put(i, Double.POSITIVE_INFINITY);
            }
    		dist.put(from_stop_id, 0.0);
    		while (!visited.isEmpty()) {
    			int curr = Integer.MAX_VALUE;
                double min = Double.POSITIVE_INFINITY;
                for(int i : visited) {
                	double j = dist.get(i);
                	if(j<min) {
                		min = j;
                		curr = i;
                	}
                }
                if(curr == Integer.MAX_VALUE) break;
                visited.remove(curr);
                if(curr == to_stop_id) break;
                ArrayList<edgeNode> adjacent = adjacencies.get(curr);
                if(adjacent != null) {
                	for(edgeNode adj : adjacent) {
                		if(adj.cost != Double.POSITIVE_INFINITY && dist.get(curr) != null) {
                			double adjDist = dist.get(curr) + adj.cost;
                			if(dist.get(adj.stopID)> adjDist) {
                				dist.put(adj.stopID, adjDist);
                				prev.put(adj.stopID, curr);
                			}
                		}
                	}
                }
    		}
    		
    		ArrayList<Integer> shortest = new ArrayList<>();
    		int stop = to_stop_id;
    		if (prev.get(stop) != null) {
                if (prev.get(stop) != Integer.MAX_VALUE || stop == from_stop_id) {
                    while (stop != Integer.MAX_VALUE) {
                        shortest.add(0, stop);
                        stop = prev.get(stop);
                    }
                }
            }
    		if (dist.get(to_stop_id) != null)
                costOfShortest = dist.get(to_stop_id);

            return shortest;
    	}
    	if (!validStopID(from_stop_id)) {
            System.out.println(from_stop_id + " is an invalid stop id");
    	}
    	if (!validStopID(to_stop_id)) {
            System.out.println(to_stop_id +" is an invalid stop id");
    	}
    	costOfShortest = -1.0;
    	return null;
    }
    
    public ArrayList<stops> findVisitedStops(ArrayList<Integer> stopIDs){
        ArrayList<stops> StopDetails = new ArrayList<>();
        System.out.println("Visited Stops");
        for(int stop : stopIDs){
            stops stopDetails = MapDetails.get(stop);
            System.out.println("stop_id - " + stopDetails.stop_id + ", stop_name - " + stopDetails.stop_name);
            StopDetails.add(stopDetails);
        }
        return StopDetails;
    }
    
    private static double costOfShortest;
    public static double findCostOfShortest() {
    	return costOfShortest;
    }
    
    public static boolean validStopID(int stop_id) {
    	return adjacencies.keySet().contains(stop_id);
    }
}
