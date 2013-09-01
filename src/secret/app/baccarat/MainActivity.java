package secret.app.baccarat;

import secret.algo.random.RandomAlgo;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button bankerButton;
	private Button playerButton;
	private Button drawButton;

	private TextView betCount;
	private TextView latest3rdRecord;
	private TextView latest2ndRecord;
	private TextView latest1stRecord;
	private TextView storedBinaryRecord;
	private TextView currentRandomBinary;

	private final int bankerButtonId = R.id.buttonBetBanker;
	private final int playerButtonId = R.id.buttonBetPlayer;
	private final int drawButtonId = R.id.buttonBetDraw;
	private final int betCountId = R.id.textViewBetCount;
	private final int latest1stRecordId = R.id.textViewLatest1stRecord;
	private final int latest2ndRecordId = R.id.textViewLatest2ndRecord;
	private final int latest3rdRecordId = R.id.textViewLatest3rdRecord;
	private final int storeBinaryRecordId = R.id.textViewStoredBinaryArray;
	private final int currentRandomRecordId = R.id.textViewCurrentRandomBinary;

	private char[] randomBinaryArray;
	private RandomAlgo random;	
	private char[] storedPostFlop;
	private char[] historyRecord;
	
	private final int SCOPE_MAX = 128;
	private final int BANK = 49;
	private final int PLAY = 48;
	private final int DRAW = 68;
	private final int MAX_RANDOM_BINARY_SCOPE = Integer.toBinaryString(SCOPE_MAX - 1).length();
	
	private final int WIN = 87;
	private final int LOSE = 76;

	private final int MIN_STORED_RANDOM_SCOPE = 10;
	
	private int betCursorInBinaryArray = 0;
	private int flopCursorInStoredArray = 0;
	private int recordCursorInHistoryArray = 0;
	private int totalBetCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		baccaratInit();
	}
	
	private void baccaratInit() {
		displayedWedgetInit();
		
		checkBinary();
		showBet();
		
		storedPostFlop = new char[MAX_RANDOM_BINARY_SCOPE];
		historyRecord = new char[MAX_RANDOM_BINARY_SCOPE];
	}

	private void displayedWedgetInit() {
		bankerButton = (Button) findViewById(bankerButtonId);
		bankerButton.setOnClickListener(buttonListener);
		playerButton = (Button) findViewById(playerButtonId);
		playerButton.setOnClickListener(buttonListener);
		drawButton = (Button) findViewById(drawButtonId);
		drawButton.setOnClickListener(buttonListener);

		betCount = (TextView) findViewById(betCountId);
		latest1stRecord = (TextView) findViewById(latest1stRecordId);
		latest2ndRecord = (TextView) findViewById(latest2ndRecordId);
		latest3rdRecord = (TextView) findViewById(latest3rdRecordId);
		storedBinaryRecord = (TextView) findViewById(storeBinaryRecordId);
		currentRandomBinary = (TextView) findViewById(currentRandomRecordId);
	}

	private Button.OnClickListener buttonListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			updateDisplayBetData(v.getId());
		}
	};

	private void updateDisplayBetData(int id) {
		// 1.Random binary create
		//  1.1 If lose more than 7th bet or win, recreate random binary; or show next bet
		//  1.2 If binary exist, show money bet via binary array; or create
		//   1.2.1 If stored binary array is not full, create via RandomAlgo and display
		//   1.2.2 If stored binary array full, choose from stored binary and display
		// 2.Store current post flop
		//  2.1 If not draw, also store into stored binary; or empty stored binary
		//  2.2 Whatever, stored into history
		// 3.Show latest 3 records
		
		totalBetCount++;
		
		int postFlopStatus = 1024;
		
		switch (id) {
			case bankerButtonId:
				postFlopStatus = BANK;
				break;
			case playerButtonId:
				postFlopStatus = PLAY;
				break;
			case drawButtonId:
				postFlopStatus = DRAW;
				break;
			default:
				// TODO: Add reminder on screen
				return;
		}
		
		if ((DRAW == postFlopStatus) || 
			(postFlopStatus == randomBinaryArray[betCursorInBinaryArray - 1])) {
			storeBetRecord(postFlopStatus, WIN);
			betCursorInBinaryArray = MAX_RANDOM_BINARY_SCOPE;
			checkBinary();
			showBet();
		} else {
			storeBetRecord(postFlopStatus, LOSE);
			if (betCursorInBinaryArray < MAX_RANDOM_BINARY_SCOPE) {
				showBet();
			} else if (betCursorInBinaryArray == MAX_RANDOM_BINARY_SCOPE){
				checkBinary();
				showBet();
				// TODO: Add reminder that you lose all your money
			}
		}

		showRecord();
	}

	private void checkBinary() {
		if ((randomBinaryArray == null) || (randomBinaryArray.length < MAX_RANDOM_BINARY_SCOPE)
				|| (betCursorInBinaryArray == MAX_RANDOM_BINARY_SCOPE)) {
			randomBinaryArray = new char[MAX_RANDOM_BINARY_SCOPE];
			random = new RandomAlgo();
			
			int storedBinaryLength = BetRecordHandle.storedPostFlopList.size();

			if (storedBinaryLength < MIN_STORED_RANDOM_SCOPE) {
				randomBinaryArray = random.getRandomBinary(SCOPE_MAX);
				betCursorInBinaryArray = 0;
			} else {
				randomBinaryArray = BetRecordHandle.storedPostFlopList.
						get(random.getRandomDecimal(MIN_STORED_RANDOM_SCOPE + 1)).toCharArray();
				BetRecordHandle.storedPostFlopList.clear();
			}
		}
	}

	private void showBet() {
		if (!(betCursorInBinaryArray > -1 | betCursorInBinaryArray < 7)) {
			//TODO: add reminder or return label for analysis
			return;
		}

		if (randomBinaryArray[betCursorInBinaryArray] == BANK) {
			bankerButton
					.setText(String
							.valueOf(BetRecordHandle.betMoney[betCursorInBinaryArray][0]));
			playerButton.setText("0");
			drawButton
					.setText(String
							.valueOf(BetRecordHandle.betMoney[betCursorInBinaryArray][2]));
		} else if (randomBinaryArray[betCursorInBinaryArray] == PLAY) {
			bankerButton.setText("0");
			playerButton
					.setText(String
							.valueOf(BetRecordHandle.betMoney[betCursorInBinaryArray][1]));
			drawButton
					.setText(String
							.valueOf(BetRecordHandle.betMoney[betCursorInBinaryArray][2]));
		}

		betCursorInBinaryArray++;
	}
	
	private void storeBetRecord(int postFlop, int winOrLose) {
		
		historyRecord[recordCursorInHistoryArray] = (char)postFlop;
		recordCursorInHistoryArray++;
		
		if (WIN == winOrLose || (betCursorInBinaryArray == MAX_RANDOM_BINARY_SCOPE)) {
			BetRecordHandle.betRecordHistory.add(String.valueOf(historyRecord));
			recordCursorInHistoryArray = 0;
		}
		
		if (DRAW != postFlop) {
			storedPostFlop[flopCursorInStoredArray] = (char)postFlop;
		} else {
			storedPostFlop = new char[MAX_RANDOM_BINARY_SCOPE];
			flopCursorInStoredArray = 0;
		}
		
		if (flopCursorInStoredArray == MAX_RANDOM_BINARY_SCOPE) {
			BetRecordHandle.storedPostFlopList.
				add(String.valueOf(storedPostFlop));
			storedPostFlop = new char[MAX_RANDOM_BINARY_SCOPE];
			flopCursorInStoredArray = 0;
		}
	}

	private void showRecord() {
		betCount.setText("Total Bet: " + String.valueOf(totalBetCount));
		
		int historySize = BetRecordHandle.betRecordHistory.size();
		if (historySize == 1) {
			latest1stRecord.setText("1st record:" + BetRecordHandle.betRecordHistory.get(0));
		} else if (historySize == 2) {
			latest1stRecord.setText("1st record:" + BetRecordHandle.betRecordHistory.get(1));
			latest2ndRecord.setText("2rd record:" + BetRecordHandle.betRecordHistory.get(0));
		} else if (historySize == 3) {
			latest1stRecord.setText("1st record:" + BetRecordHandle.betRecordHistory.get(2));
			latest2ndRecord.setText("2nd record:" + BetRecordHandle.betRecordHistory.get(1));
			latest3rdRecord.setText("3rd record:" + BetRecordHandle.betRecordHistory.get(0));
		} else if (historySize > 3) {
			latest1stRecord.setText("1st record:" + BetRecordHandle.betRecordHistory.get(historySize - 1));
			latest2ndRecord.setText("2nd record:" + BetRecordHandle.betRecordHistory.get(historySize - 2));
			latest3rdRecord.setText("3rd record:" + BetRecordHandle.betRecordHistory.get(historySize - 3));
		}
		
		storedBinaryRecord.setText("Stored Binary: " + String.valueOf(storedPostFlop));
		currentRandomBinary.setText("Random Binary: " + String.valueOf(randomBinaryArray));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
