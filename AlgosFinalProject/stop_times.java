public class stop_times {
	//trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled
	int trip_id;
    String arrival_time;
    String departure_time;
    int stop_id;
    int stop_sequence;
    int stop_headsign;
    int pickup_type;
    int drop_off_type;
    float shape_dist_traveled;
    
    public stop_times(int trip_id,String arrival_time,String departure_time, int stop_id, 
    		int stop_sequence, int stop_headsign, int pickup_type, int drop_off_type, float shape_dist_traveled) {
    	this.trip_id = trip_id;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.stop_id = stop_id;
        this.stop_sequence = stop_sequence;
        this.stop_headsign = stop_headsign;
        this.pickup_type = pickup_type;
        this.drop_off_type = drop_off_type;
        this.shape_dist_traveled = shape_dist_traveled;
    }
    
    public void printStopDetails() {
        System.out.println("trip_id: " + trip_id + ", arrival_time: " + arrival_time + ", departure_time: " + departure_time
                + ", stop_id: " + stop_id + ", stop_sequence: " + stop_sequence + ", stop_headsign: " + stop_headsign + 
                ", pickup_type: " + pickup_type + ", drop_off_type: " + drop_off_type + ", shape_dist_traveled: "
                + shape_dist_traveled);
    }
}
