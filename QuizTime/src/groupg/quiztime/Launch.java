package groupg.quiztime;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class Launch extends Activity {
	public final static String EXTRA_Q_COUNT = "groupg.quiztime.Q_COUNT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_launch);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch, menu);
		return true;
	}

	/** onClick listener for startQuizBtn **/
	public void StartQuizClick(View view){
		Intent intent = new Intent(this, QuizActivity.class);

		//get number of questions
		EditText numQs = (EditText) findViewById(R.id.textQuestionCount);
		int qCt;
		try{
			qCt = Integer.valueOf(numQs.getText().toString());
		} catch (NumberFormatException ex) {
			qCt = 10;
		}

		if(qCt < 1 || qCt > 50)
			qCt = 10;
		intent.putExtra(EXTRA_Q_COUNT, qCt);
		startActivity(intent);
	}
}
