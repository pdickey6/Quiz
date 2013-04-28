package groupg.quiztime;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class Launch extends Activity {
	public final static String EXTRA_NAME = "groupg.quiztime.NAME";
	
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
		EditText eText = (EditText) findViewById(R.id.nameInput);
		String name = eText.getText().toString();
		intent.putExtra(EXTRA_NAME, name);
		
		startActivity(intent);
	}

}