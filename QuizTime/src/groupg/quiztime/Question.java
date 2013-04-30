package groupg.quiztime;

import java.util.Random;

public class Question {
	
	/** Instance Variables */
	private String _stub;
	private String[] _answers;
	
	/** Constructors */
	//Default Constructor
	public Question (){
		_stub = null;
		_answers = null;
	}
	
	//Complete Constructor
	public Question (String stub, String dummy1, String dummy2, String dummy3, String correct){
		_stub = stub;
		_answers = getRandAnswerArray(dummy1, dummy2, dummy3, correct);
	}
	
	/** Setters */
	public void setStub(String stub){
		_stub = stub;
	}
	
	public void setAnswers(String dummy1, String dummy2, String dummy3, String correct){
		_answers = getRandAnswerArray(dummy1, dummy2, dummy3, correct);
	}
	
	/** Getters */
	public String getStub(){
		return _stub;
	}
	
	public String[] getAnswers(){
		return _answers;
	}
	
	private String[] getRandAnswerArray( String d1, String d2, String d3, String c) {
		
		//create and null answers
		String[] answers = new String[4];
		for(int i = 0; i < 4; i++){
			answers[i] = null;
		}
		
		/**randomize 3 dummy answers and correct answer into answers[] */
		//add _dummy1 to random spot
		int radNum = (int) (Math.random() * 4);
		answers[radNum] = d1;
		
		//add _dummy2 to random empty spot
		radNum = (int) (Math.random() * 4);//get next random
		if(answers[radNum] == null)
			answers[radNum] = d2;
		else { //spot in array already used, move to next spot
			answers[(radNum + 1)%4] = d2;
		}
		
		//add _dummy3 to first empty spot
		for(int i = 0; i < 4; i++){
			if(answers[i] == null){
				answers[i] = d3;
				break;
			}
		}
		
		//add _correct to remaining empty spot
		for(int i = 0; i < 4; i++){
			if(answers[i] == null){
				answers[i] = "(c)" + c; //mark correct ans with a leading '(c)'
				break;
			}
		}
		
		return answers;
	}

}