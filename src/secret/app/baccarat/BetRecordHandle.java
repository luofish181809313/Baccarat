package secret.app.baccarat;

import java.util.ArrayList;
import java.util.List;

public class BetRecordHandle {
	
	public static int[][] betMoney = {
		{1, 2, 3},
		{4, 5, 6},
		{7, 8, 9},
		{10, 11, 12},
		{13, 14, 15},
		{16, 17, 18},
		{19, 20, 21}
	}; 
	
	public static List<String> storedPostFlopList = new ArrayList<String>();
	public static List<String> crazyModeBinaryList = new ArrayList<String>();
	public static List<String> betRecordHistory = new ArrayList<String>();
	
	public static int winCounts;
	public static int lostCounts;
	public static int drawCOunts;

}
