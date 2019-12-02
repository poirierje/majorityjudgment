import java.util.Arrays;

public class ProjectJudgment {

	public  double    lovely; // Is this project to be favorably voted by a majority ? Probability from 0 (bad vote) to 1 (good vote).

	public  int       num;
	public  String    name;
	
	private int       nbProjects;
	private int       nbJudgments;
	
	public  Integer[] judgmentsAsNumbers;  // = How many votes by judgment value
	public  Double [] judgmentsAsPercents; // = How many votes by judgment value as percents

	public  int       totalJudgments;
	public  int       medianPointJudgment;
	public  double    pcMoreTh;
	public  double    pcLessTh;
	
	public ProjectJudgment(int num, double lovely, int nbProjects, int nbJudgments) {
		super();
		
		this.lovely              = lovely;

		this.num                 = num;
		this.name                = "Project_" + String.format("%0" + ((int) Math.log10(nbProjects) + 1) + "d", num);
		
		this.nbProjects          = nbProjects;
		this.nbJudgments         = nbJudgments;
		
		this.judgmentsAsNumbers  = new Integer[nbJudgments];
		for (int i = 0; i < nbJudgments; i++) {
			judgmentsAsNumbers[i] = 0;
		}

		calculatePercents();
		calculateMedianPointJudgment();
	}

	public void addJudgment( int value ) {
		judgmentsAsNumbers[value]++;
		calculatePercents();
		calculateMedianPointJudgment();
	}
	
	private void calculatePercents() {
		totalJudgments = Arrays.asList(judgmentsAsNumbers).stream().reduce(0, Integer::sum);
		judgmentsAsPercents = new Double[ nbJudgments ];
		for (int i = 0; i < nbJudgments; i++) {
			judgmentsAsPercents[i] = (judgmentsAsNumbers[i] + 0.0) / totalJudgments;
		}
	}
	
	private void calculateMedianPointJudgment() {
		double cumulatedPercents = 0.0;

		pcMoreTh = 0.0;
		pcLessTh = 0.0;
		
		for (int i = 0; i < nbJudgments; i++) {
			cumulatedPercents = cumulatedPercents + judgmentsAsPercents[i];
			if ( cumulatedPercents > 0.5) {
				medianPointJudgment = i;
				pcMoreTh = 1 - cumulatedPercents;
				break;
			} else {
				pcLessTh = cumulatedPercents;
			}
		}
	}
	
	public String toString() {
		return name + " : " + Arrays.asList(judgmentsAsPercents) + " = " + Arrays.asList(judgmentsAsNumbers);
	}
	
}
