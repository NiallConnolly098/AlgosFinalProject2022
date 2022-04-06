import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

public class FunctionalityPart2 {
	public static Tree TreeStops = new Tree();
	public static String[] columns(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String string;
		while ((string = br.readLine()) != null) {
			String[] line = string.split(",");
			br.close();
			return line;
		}
		br.close();
		return null;
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
	
	public static ArrayList<String> getName(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String string;
		ArrayList<String> name = new ArrayList<String>();
		while((string=br.readLine()) != null){
			String[] line = string.split(",");
			if(!line[2].equals("stop_name")) {
				String keywordsMoved = moveKeywords(line[2]);
				name.add(keywordsMoved);
			}
		}
		br.close();
		return name;
	}
	
	public static void addToTree(ArrayList<String> names) {
		for(String name : names) {
			TreeStops.addWord(name);
		}
	}
	
	public static void printDupes(ArrayList<String> names) {
		ArrayList<String> UniqueStops = new ArrayList<>();
		int dupes = 0;
		System.out.println("Duplicates:");
		for(String i : names) {
			if(UniqueStops.contains(i)) {
				dupes++;
				System.out.println(dupes + " " + i);
			}
			else UniqueStops.add(i);
		}
		System.out.println("Unique stop count: " + UniqueStops.size());
	}
	
	public static Map<String, ArrayList<String>> makeMap(File file) throws IOException{
		Map<String, ArrayList<String>> treeMap = new TreeMap<>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String string;
		int index = 2;
		while((string=br.readLine()) != null) {
			String[] line = string.split(",");
			if(!line[index].equals("stop_name")) {
				String keywordsMoved = moveKeywords(line[index]);
				treeMap.computeIfAbsent(keywordsMoved, k -> new ArrayList<>()).add(string);
			}
		}
		br.close();
		return treeMap;
	}
	
	public static void main(String[] args) throws IOException {
		String stops_path = "C:\\Users\\niall\\OneDrive\\Desktop\\input file\\sstops.txt";
        File stops = new File(stops_path);
        //String[] columnName = columns(stops);
        ArrayList<String> names = getName(stops);
        addToTree(names);
        String[] hastings = TreeStops.searchWord("HASTINGS");
        for(String i : hastings) {
        	System.out.println(i);
        	System.out.println("Length: " + hastings.length);
        }
    }
}
