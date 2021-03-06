package groupg.quiztime;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ResultsActivity extends Activity {
	public final static String EXTRA_Q_COUNT = "groupg.quiztime.Q_COUNT";
	private int countQuestions;
	private int countCorrect;
	private int countWrong;
	private int countSkipped;
	private double percentGrade;
	private String letterGrade;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		int[] results = intent.getIntArrayExtra(QuizActivity.EXTRA_RESULTS);

		setContentView(R.layout.activity_results);
		// Show the Up button in the action bar.
		setupActionBar();

		setTitle("Results");

		//Interpret results array 
		InterpretResults(results);
		ShowResults();

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * Runs through the raw results array and updates countQuestions, countSkipped,
	 * countWrong, countCorrect, percentGrade, and letterGrade
	 */
	private void InterpretResults(int[] results) {
		countQuestions = results.length;

		for(int i = 0; i < countQuestions; i++){
			switch (results[i]) {
			case -1 :
				countSkipped ++;
				break;
			case 0 :
				countWrong ++;
				break;
			case 1 :
				countCorrect ++;
				break;				
			}			
		}		
		percentGrade = Math.round(100.00 * ((double)countCorrect/countQuestions));
		letterGrade = CalcLetterGrade(percentGrade);
	}

	/**
	 * Updates UI to show the results stored in the current local variables:
	 * letterGrade, percentGrade, countCorrect, countQuestions as well as updating attempted
	 */
	private void ShowResults() {

		//Set Letter Grade
		TextView textGrade = (TextView) findViewById(R.id.TextGrade);
		textGrade.setText("Grade: " + letterGrade);

		//Set Percent
		TextView textPercent = (TextView) findViewById(R.id.TextPercent);
		textPercent.setText("Percent: " + percentGrade + "%");

		//Set Correct
		TextView textCorrect = (TextView) findViewById(R.id.TextCorrect);
		textCorrect.setText("Correct: " + countCorrect);

		//Set Questions
		TextView textQuestions = (TextView) findViewById(R.id.TextQuestions);
		textQuestions.setText("Questions: " + countQuestions);

		//Set Attempts
		TextView textAttempted = (TextView) findViewById(R.id.TextAttempted);
		textAttempted.setText("Attempted: " + (countQuestions - countSkipped));
	}

	/**
	 * Finds letter grade given a grade in percent
	 * @param grade (%).
	 * @return a +- Letter grade representing the given grade%
	 */
	private String CalcLetterGrade(double grade) {
		String lGrade = "";
		if(grade >= 97.00){
			lGrade = "A+";
		} else if (grade >= 94.00) {
			lGrade = "A";					
		} else if (grade >= 90.00) {
			lGrade = "A-";					
		} else if (grade >= 87.00) {
			lGrade = "B+";
		} else if (grade >= 84.00) {
			lGrade = "B";					
		} else if (grade >= 80.00) {
			lGrade = "B-";					
		} else if (grade >= 77.00) {
			lGrade = "C+";
		} else if (grade >= 74.00) {
			lGrade = "C";					
		} else if (grade >= 70.00) {
			lGrade = "C-";					
		} else if (grade >= 60.00) {
			lGrade = "D";
		} else {
			lGrade = "F";
		}

		return lGrade;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void NewQuizClick(View view){
		Intent intent = new Intent(this, QuizActivity.class);
		intent.putExtra(EXTRA_Q_COUNT, countQuestions);
		startActivity(intent);
	}
}
