import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class Connectors {
	public static HashMap<Integer, ArrayList<Node>> adjacencies;
    public static HashMap<Integer, BusStop> MapDetails;
    public static void Maps() {
    	adjacencies = new HashMap<>();
    	MapDetails = new HashMap<>();
    }
    public Connectors() {
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
    	System.out.println("Line count - " + lineCount);
    	br.close();
    }
    
    public static void Connect(int from_stop_id, int to_stop_id, double cost) {
        adjacencies.computeIfAbsent(from_stop_id, k -> new ArrayList<>());
        adjacencies.computeIfAbsent(to_stop_id, k -> new ArrayList<>());
        adjacencies.get(from_stop_id).add(new Node(cost, to_stop_id));
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
        			
        			newStop(new BusStop(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, 
        					zone_id, stop_url,location_type, parent_station));
        		}
        		lineCount=lineCount+1;
        	}
        }
        catch(Exception e) {
        	System.out.println(e);
        }
        System.out.println("Line count - " + lineCount);
        br.close();
    }
    
    public static String moveKeywords(String stopName) {
    	int keyLength = 2;
        int flagLength = 8;
        String temp = stopName.substring(0, keyLength).trim().toUpperCase();
        String tempFlag = stopName.substring(0, flagLength).trim().toUpperCase();
        
        if (temp.equals("WB") || temp.equals("NB") || temp.equals("SB") || temp.equals("EB")) {
            String end = stopName.substring(keyLength + 1);
            String start = stopName.substring(0, keyLength);
            String movedString = end.concat(" ").concat(start);
            return moveKeywords(movedString);
        }
        if (tempFlag.equals("FLAGSTOP")) {
            String lastPart = stopName.substring(flagLength + 1);
            String firstPart = stopName.substring(0, flagLength);
            String movedString = lastPart.concat(" ").concat(firstPart);
            return moveKeywords(movedString);
        }
        else return stopName;
    }
    
    public static void newStop(BusStop stop) {
        adjacencies.put(stop.stop_id, new ArrayList<>());
        MapDetails.put(stop.stop_id, stop);
    }
    
    public Connectors(File stops, File transfers) throws IOException{
    	Maps();
    	readTransfers(transfers);
    	readStops(stops);
    }
}
