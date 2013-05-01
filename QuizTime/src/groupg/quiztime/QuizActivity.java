package groupg.quiztime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;

public class QuizActivity extends Activity {
	private Question[] questions;
	private int[] questionStatus;
	private int questionCount;
	private int currentQ;
	private int correctIndex;
	private TextView stub;
	private RadioGroup answerGroup;
	private RadioButton radio0;
	private RadioButton radio1;
	private RadioButton radio2;
	private RadioButton radio3;
	private Button backBtn;
	private Button skipBtn;
	private Button answerBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String name = intent.getStringExtra(Launch.EXTRA_NAME);
		setTitle("Quiz: " + name);

		questionCount = 10; //TODO: Make dynamic through combobox one Launch

		//create array to hold statuses of all questions
		//-1:notAttempted, 0:answeredWrong, 1:answeredCorrect
		questionStatus = new int[questionCount];
		for(int i = 0; i < questionCount; i++){			
			questionStatus[i] = -1; //set statuses all to notAttempted			
		}		

		setContentView(R.layout.activity_quiz);
		// Show the Up button in the action bar.
		setupActionBar();

		//getGUIObjects
		stub = (TextView) findViewById(R.id.stubText);
		answerGroup = (RadioGroup) findViewById(R.id.answerRadioGroup);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		radio3 = (RadioButton) findViewById(R.id.radio3);
		backBtn = (Button) findViewById(R.id.backBtn);
		skipBtn = (Button) findViewById(R.id.skipBtn);
		answerBtn = (Button) findViewById(R.id.answerBtn);

		//fill Question[] with questions from file
		questions = importQuestions("scienceQuestions.txt", questionCount);

		//Add listener to enable answer button when
		answerGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged (RadioGroup group, int checkedId) {
				answerBtn.setEnabled(true);
			}
		});

		//Setup Initial Question
		currentQ = 0;		
		correctIndex = updateQuestion();
		resetRadios();
		backBtn.setEnabled(false);
		answerBtn.setEnabled(false);
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
	 * Produces an array of Questions containing questionCount number of random
	 *  unique questions read from the given filename.
	 **/
	private Question[] importQuestions(String fileName, int questionCount) {
		//TODO: Currently reads in questionsInFile number of question from file and returns them all. 
		//This needs to read in the entire file (50 Q's), then return questionCount number of random questions in array

		int questionsInFile = 13;
		Question[] qs = new Question[questionsInFile];		

		AssetManager aManager = getAssets();
		InputStream iStream = null;
		try {
			iStream = aManager.open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
		String line;			

		for(int ctr = 0; ctr < questionsInFile; ctr++){
			Question question = new Question();

			//Stub
			try {
				line = bReader.readLine();
				question.setStub(line.toString().substring(3));
			} catch (IOException e) {
				e.printStackTrace();
			}

			String[] ans = new String[4];
			//C
			try {
				line = bReader.readLine();
				ans[0] = (line.toString().substring(3));
			} catch (IOException e) {
				e.printStackTrace();
			}

			//D1
			try {
				line = bReader.readLine();
				ans[1] = (line.toString().substring(3));
			} catch (IOException e) {
				e.printStackTrace();
			}

			//D2
			try {
				line = bReader.readLine();
				ans[2] = (line.toString().substring(3));
			} catch (IOException e) {
				e.printStackTrace();
			}

			//D3
			try {
				line = bReader.readLine();
				ans[3] = (line.toString().substring(3));	
			} catch (IOException e) {
				e.printStackTrace();
			}

			question.setAnswers(ans[1], ans[2], ans[3], ans[0]);

			qs[ctr] = question;
		}

		//TODO: loop through qs here and fill a new array of size questionCount with random Q's from qs
		return qs;
	}
	
	/**
	 * Updates the GUI to show the question at index currentQ in questions[]
	 * Modifies answers[] to no longer have the leading "(c)" infront of correct answer
	 * @return Radio index of correct answer
	 */

	private int updateQuestion() {
		int correctIndex = -1;
		String[] answers = questions[currentQ].getAnswers();
		stub.setText((currentQ + 1) + ") " + questions[currentQ].getStub());

		if(answers[0].startsWith("(c)")){
			correctIndex = 0;
			answers[0] = answers[0].substring(3);
		} else if(answers[1].startsWith("(c)")){
			correctIndex = 1;
			answers[1] = answers[1].substring(3);
		} else if(answers[2].startsWith("(c)")){
			correctIndex = 2;
			answers[2] = answers[2].substring(3);
		} else if(answers[3].startsWith("(c)")){
			correctIndex = 3;
			answers[3] = answers[3].substring(3);
		}

		radio0.setText(answers[0]);
		radio1.setText(answers[1]);
		radio2.setText(answers[2]);
		radio3.setText(answers[3]);
		
		//Check radio that user already answered
		resetRadios();
		if(questionStatus[currentQ] != -1){
			int userAnsIndex = questions[currentQ].getUserAnswer();
			RadioButton userAnswer = (RadioButton) answerGroup.getChildAt(userAnsIndex);
			userAnswer.setChecked(true);
			answerBtn.setEnabled(false);
		}

		return correctIndex;
	}

	private int getSelectedAnsIndex() {
		int index = -1;
		if(radio0.isChecked())
			index = 0;
		else if(radio1.isChecked())
			index = 1;
		else if(radio2.isChecked())
			index = 2;
		else if(radio3.isChecked())
			index = 3;

		return index;
	}

	private void resetRadios() {
		RadioGroup radios = ((RadioGroup) findViewById(R.id.answerRadioGroup)) ;
		radios.clearCheck();
		answerBtn.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	/** onClick listener for backBtn **/
	public void backClick(View view){
		//Mark Q as not answered and load previous Q
		if(currentQ == questionCount -1){
			//enable skip btn, not on last one anymore
			skipBtn.setEnabled(true);
		}

		//Setup Q unless at end
		currentQ--;		
		correctIndex = updateQuestion();

		if(currentQ == 0){
			//enable backBtn
			backBtn.setEnabled(false);
		}		
	}

	/** onClick listener for skipBtn **/
	public void skipClick(View view){

		//Mark Q as not answered and load next Q
		if(currentQ == 0){
			//enable backBtn
			backBtn.setEnabled(true);
		}
		//Setup Q uless at end
		currentQ++;		
		correctIndex = updateQuestion();

		if(currentQ == questionCount -1){
			//disable skip btn, end of question list
			skipBtn.setEnabled(false);
		}
	}

	/** onClick listener for answerBtn **/
	public void answerClick(View view){
		//Show next Q, save ans
		int userAns = getSelectedAnsIndex(); 
		questions[currentQ].setUserAnswer(userAns);

		resetRadios();
		answerBtn.setEnabled(false);

		if( userAns == correctIndex){
			//user answered correctly
			questionStatus[currentQ] = 1;
		} else {
			//user answered wrong
			questionStatus[currentQ] = 0;
		}

		if(currentQ == 0){
			//enable backBtn
			backBtn.setEnabled(true);
		}

		if(currentQ == questionCount -2){
			//disable skip btn, end of question list
			skipBtn.setEnabled(false);
		}

		if(currentQ == questionCount -1){
			//answered final question, sent to results
			Intent intent = new Intent(this, ResultsActivity.class);
			startActivity(intent);

			return;
		}

		//Setup Q uless at end
		currentQ++;		
		correctIndex = updateQuestion();

	}

	/** onClick listener for endQuizBtn **/
	public void endQuizClick(View view){
		resetRadios();		
		Intent intent = new Intent(this, ResultsActivity.class);

		startActivity(intent);
	}
}
