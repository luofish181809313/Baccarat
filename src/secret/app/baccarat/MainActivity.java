package secret.app.baccarat;

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
	private TextView last3rdRecord;
	private TextView last2ndRecord;
	private TextView last1stRecord;
	private TextView storedBinaryRecord;
	
	private final int bankerButtonId = R.id.buttonBetBanker;
	private final int playerButtonId = R.id.buttonBetPlayer;
	private final int drawButtonId = R.id.buttonBetDraw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		displayedWedgetInit();
	}

	private void displayedWedgetInit() {

		bankerButton = (Button) findViewById(bankerButtonId);
		bankerButton.setOnClickListener(buttonListener);
		playerButton = (Button) findViewById(playerButtonId);
		playerButton.setOnClickListener(buttonListener);
		drawButton = (Button) findViewById(drawButtonId);
		drawButton.setOnClickListener(buttonListener);

		betCount = (TextView) findViewById(R.id.textViewBetCount);
		last1stRecord = (TextView) findViewById(R.id.textViewLast1stRecord);
		last2ndRecord = (TextView) findViewById(R.id.textViewLast3rdRecord);
		storedBinaryRecord = (TextView) findViewById(R.id.textViewStoredBinaryArray);
	}
	
	private Button.OnClickListener buttonListener = new Button.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			updateDisplayBetData(v.getId());
		}
	}; 

	private void updateDisplayBetData(int id) {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
