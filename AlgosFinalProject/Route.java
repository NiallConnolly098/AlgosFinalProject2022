import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;


public class Route {
	public ArrayList<stop_times> Data;
	public Route(File stop_times) throws IOException{
		Data = new ArrayList<>();
		readStopTimes(stop_times);
	}
	public void readStopTimes(File stop_times) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(stop_times));
		String store;
    	int lineCount = 0;
    	float emptyFloat = -1;
        int emptyInt = -1;
		try {
			while((store = br.readLine()) != null) {
				String[] line = store.split(",");
				if(lineCount != 0) {
					int trip_id = emptyInt;
				    int stop_id = emptyInt;
				    String arrival_time = line[1];
				    String departure_time = line[2];
				    int stop_sequence = emptyInt;
				    int stop_headsign = emptyInt;
				    int pickup_type = emptyInt;
				    int drop_off_type = emptyInt;
				    float shape_dist_traveled = emptyFloat;
				    
				    
				    if(!line[0].equals("")) {
				    	trip_id = Integer.parseInt(line[0]);
				    }
				    if(!line[3].equals("")) {
				    	stop_id = Integer.parseInt(line[3]);
				    }
				    if(!line[4].equals("")) {
				    	stop_sequence = Integer.parseInt(line[4]);
				    }
				    if(!line[5].equals("")) {
				    	stop_headsign = Integer.parseInt(line[5]);
				    }
				    if(!line[6].equals("")) {
				    	pickup_type = Integer.parseInt(line[6]);
				    }
				    if(!line[7].equals("")) {
				    	drop_off_type = Integer.parseInt(line[7]);
				    }
				    if((line.length==9) && !line[8].equals("")) {
				    	shape_dist_traveled = Float.parseFloat(line[8]);
				    }
				    if(validTime(arrival_time) && validTime(departure_time)) {
				    	Data.add(new stop_times(trip_id, arrival_time, departure_time, stop_id, 
				    			stop_sequence, stop_headsign, pickup_type, drop_off_type, shape_dist_traveled));
				    }
				}
				lineCount = lineCount + 1;
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		System.out.println("File 'stop_times' read, lineCount: " + lineCount);
		br.close();
	}
	
	public static boolean validTime(String time) {
		final int MAX_HOUR = 23;
		final int MAX_MIN = 59;
		final int MAX_SEC = 59;
		String[] seperatedTime = time.replaceAll("\\s", "").split(":");
		int hour, min, sec;
		try {
			hour = Integer.parseInt(seperatedTime[0]);
			min = Integer.parseInt(seperatedTime[1]);
			sec = Integer.parseInt(seperatedTime[2]);
		}
		catch(Exception e) {
			System.out.println(e);
			return false;
		}
		if((hour<=MAX_HOUR)&&(min<=MAX_MIN)&&(sec<=MAX_SEC)) {
			return true;
		}
		else return false;
	}
}
