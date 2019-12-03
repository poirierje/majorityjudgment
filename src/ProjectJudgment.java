import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProjectJudgment {

	public  double        lovely; // Is this project to be favorably voted by a majority ? Probability from 0 (bad vote) to 1 (good vote).

	public  int           num;
	public  String        name;
	
	private int           nbProjects;
	private int           nbJudgments;
	private int           nbVoters;
	
	public  Integer[]     judgmentsAsNumbers;  // = How many real judgments by judgment value
	public  Double []     judgmentsAsPercents; // = How many real judgments by judgment value as percents

	public  Integer[]     judgmentsAsNumbersWith0;  // = How many real judgments by judgment value with added 0 for every abstention
	public  Double []     judgmentsAsPercentsWith0; // = How many real judgments by judgment value as percents with added 0 for every abstention

	public  int           totalRealJudgments;
	
	public  int           medianPointJudgment;
	public  double        pcMoreTh;
	public  double        pcLessTh;
	
	public  String        majorityValue = "";
	
	// *********************************************************************************************
	// * CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONST *
	// * CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONST *
	// *********************************************************************************************
	
	public ProjectJudgment(int num, double lovely, int nbProjects, int nbJudgments, int nbVoters) {
		super();
		
		this.lovely                  = lovely;

		this.num                     = num;
		this.name                    = "Project_" + String.format("%0" + ((int) Math.log10(nbProjects) + 1) + "d", num);
		
		this.nbProjects              = nbProjects;
		this.nbJudgments             = nbJudgments;
		this.nbVoters                = nbVoters;
		
		this.judgmentsAsNumbers      = new Integer[nbJudgments];
		this.judgmentsAsNumbersWith0 = new Integer[nbJudgments];
		for (int i = 0; i < nbJudgments; i++) {
			judgmentsAsNumbers[i]      = 0;
			judgmentsAsNumbersWith0[i] = 0;
		}

		update( 0 );
	}

	// *********************************************************************************************
	// * ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD A *
	// * ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD ADD A *
	// *********************************************************************************************
	
	public void addJudgment( int value ) {
		
		// Add judgment
		judgmentsAsNumbers[value]++;

		// Add abstention judgments (with 0 value)
		for (int i = 0; i < nbJudgments; i++) {
			judgmentsAsNumbersWith0[i] = judgmentsAsNumbers[i];
		}
		int totalJudgments = Arrays.asList(judgmentsAsNumbers).stream().reduce(0, Integer::sum);
		judgmentsAsNumbersWith0[0] = judgmentsAsNumbersWith0[0] + ( nbVoters - totalJudgments );		

//		update();
	}
	
	// *********************************************************************************************
	// * CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE *
	// * CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE CALCULATE *
	// *********************************************************************************************
	
	public void update( int nbDeletedO ) {
		System.out.print ("Updating " + this.name + "... ");
		calculatePercents( nbDeletedO );
		calculateMedianPointJudgment();
		calculateMajoritoryRanking();
		System.out.println ("done.");
	}
	
	private void calculatePercents( int nbDeletedO ) {

		// Without abstention
		totalRealJudgments = Arrays.asList(judgmentsAsNumbers).stream().reduce(0, Integer::sum);
		judgmentsAsPercents = new Double[ nbJudgments ];
		for (int i = 0; i < nbJudgments; i++) {
			judgmentsAsPercents[i] = (judgmentsAsNumbers[i] + 0.0) / totalRealJudgments;
		}
		
		// With abstention
		judgmentsAsPercentsWith0 = new Double[ nbJudgments ];
		for (int i = 0; i < nbJudgments; i++) {
			judgmentsAsPercentsWith0[i] = (judgmentsAsNumbersWith0[i] + 0.0) / (nbVoters - nbDeletedO);
		}

	}
	
	/**
	 * Calculate the following values (example with 09-12-14-15-15-16-16-16-16-17 ie 9=10%, 12=10%, 14=10%, 15=20%, 16=40%, 17=10%) : 
	 * - Median judgment : the first note of more (or exactly) than 50% of people = 15      for 50%
	 * - Percent of people who judge less than median judgment                    = 9+12+14 for 30%
	 * - Percent of people who judge more than median judgment                    = 16+17   for 50%
	 * 
	 * ==> Basic method described in BD from https://old-v1.lechoixcommun.fr/ressources/BD-lechoixcommun-FR.pdf, p 113-114
	 */
	private void calculateMedianPointJudgment() {
		double cumulatedPercents = 0.0;

		pcMoreTh = 0.0;
		pcLessTh = 0.0;
		
		for (int i = 0; i < nbJudgments; i++) {
			cumulatedPercents = cumulatedPercents + judgmentsAsPercentsWith0[i];
			
			if ( cumulatedPercents > 0.5) {
				medianPointJudgment = i;
				pcMoreTh = 1 - cumulatedPercents;
				break;
			} else {
				pcLessTh = cumulatedPercents;
			}

			
		}
	}
	
	/**
	 * Calculate a string composed by median judgment, then second media judgment when deleting the previous, and so on.
	 * Example with 07-11-13-13-13-14-14-14 :
	 * 	07 11 13 13 13 14 14 14 --> 13
	 * 	07 11 13    13 14 14 14 --> 1313
	 * 	07 11 13       14 14 14 --> 131313
	 * 	07 11          14 14 14 --> 13131314
	 * 	07 11             14 14 --> 1313131411
	 * 	07                14 14 --> 131313141114
	 * 	07                   14 --> 13131314111407
	 * 	                     14 --> 1313131411140714
	 * The comparable value is 1313131411140714.
	 * 
	 * ==> Precise method described in page 3 of "Le jugement majoritaire :  description détaillée" by Michel Balinski Rida Laraki / 13 avril 2007 / Cahier n° 2007-06
	 */
	private void calculateMajoritoryRanking() {
		majorityValue = "";
		
		// Get individual judgments
		List<Integer> tempIndividualJudgmentsWith0 = new ArrayList<Integer>();
		for (int i = 0; i < nbJudgments; i++) {
			for (int j = 0; j < judgmentsAsNumbersWith0[i]; j++) {
				tempIndividualJudgmentsWith0.add( i );
			}
		}
		
		// Sort judgments
		Collections.sort( tempIndividualJudgmentsWith0 );


		// Calculate final majority value
		while ( tempIndividualJudgmentsWith0.size() > 0 ) {
			int index;
			if ( tempIndividualJudgmentsWith0.size() % 2 == 0 ) { 
				index = (int) ((tempIndividualJudgmentsWith0.size() - 1) / 2 );
			} else {
				index = (int) ((tempIndividualJudgmentsWith0.size() - 0) / 2 );
			}
			majorityValue = majorityValue + tempIndividualJudgmentsWith0.get( index ) + "";
			tempIndividualJudgmentsWith0.remove(index);
		}
	}
	
	// *********************************************************************************************
	// * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS *
	// * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS *
	// *********************************************************************************************
	
	public String toString() {
		return name + " : " + Arrays.asList(judgmentsAsPercents) + " = " + Arrays.asList(judgmentsAsNumbers);
	}
	
}
