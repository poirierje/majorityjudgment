package v2;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MajorityJudgment {

	// --------------------------------------------------------------------------------------------------------------------------
		
	static final String   SIMUL               = "JudgmentSimulator_Random";      
//	static final String   SIMUL               = "JudgmentSimulator_WithCentroid";      

//	static final String   ALGO                = "SortByMajorityJudgment_10_WITH_MEDIAN_WITHOUT_EGAL";      
//	static final String   ALGO                = "SortByMajorityJudgment_20_WITH_MEDIAN_WITH_EGAL_NEXT_JUDGMENT";      
//	static final String   ALGO                = "SortByMajorityJudgment_30_WITH_MEDIAN_WITH_EGAL_NEXT_JUDGMENT_AND_JPO";      
	static final String   ALGO                = "SortByMajorityJudgment_40_WITH_MEDIAN_WITH_EGAL_VALEUR_MAJORITAIRE";      

	static final boolean  ARASE               = false;
//	static final boolean  ARASE               = true;
	
	static final int      NB_PROJECTS         =    50;
	static final int      NB_VOTERS           =   100;
	static final int      NB_JUDGMENTS        =    7; // nb of values for judging
	static final int      NB_BALLOTS_BY_VOTER =    9; // nb of ballots deposed by each voter (1 ballot = 1 project judgment) 

	// --------------------------------------------------------------------------------------------------------------------------
	
	static final Random   RND                 = new Random( System.currentTimeMillis() );

	// VARIABLES
	
	List<ProjectJudgment> projectJudgments = new ArrayList<ProjectJudgment>();
	
	// *********************************************************************************************
	// * MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN *
	// * MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN *
	// *********************************************************************************************

	public static void main(String[] args) throws Exception
	{
		if ( NB_BALLOTS_BY_VOTER > NB_PROJECTS )
		{
			throw new RuntimeException("More ballots number than projects !");
		}
		new MajorityJudgment().run();
	}
		
	// *********************************************************************************************
	// * RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN R *
	// * RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN R *
	// *********************************************************************************************

	private void run() throws Exception
	{
		// Create projects
		for (int i = 0; i < NB_PROJECTS; i++) {
			projectJudgments.add( new ProjectJudgment( i, RND.nextDouble(), NB_PROJECTS, NB_JUDGMENTS, NB_VOTERS, NB_BALLOTS_BY_VOTER ));
		}
		
		// ----------------------------------------------------------------------------------------------------- Simulate projectJudgments
		IJudgmentSimulator simulator = ( IJudgmentSimulator ) Class.forName( "v2." + SIMUL ).newInstance();

		simulator.simulate( projectJudgments, NB_JUDGMENTS, NB_VOTERS, NB_BALLOTS_BY_VOTER );

		// ----------------------------------------------------------------------------------------------------- Arasing and updating projects

		// Adding abstention
		for (ProjectJudgment project : projectJudgments) {
			project.addAbstention( NB_VOTERS );
		}

		// Calculating number of "0" to arase
		int nbZerosToDelete = 0;
		if ( ARASE )
		{
			nbZerosToDelete = projectJudgments.get(0).judgmentsAsNumbersWith0[0];
			for (ProjectJudgment project : projectJudgments) {
				if ( project.judgmentsAsNumbersWith0[0] < nbZerosToDelete )
					nbZerosToDelete = project.judgmentsAsNumbersWith0[0];
			}
		}

		// Updating projects
		for (ProjectJudgment project : projectJudgments) {
			project.update( nbZerosToDelete );
		}

		// ----------------------------------------------------------------------------------------------------- Sort projects 
		
		Comparator<ProjectJudgment> algo = ( Comparator<ProjectJudgment> ) Class.forName( "v2." + ALGO ).newInstance();
		
		System.out.println ("Algorithm = " + algo.getClass().getName() );
		
		projectJudgments.sort( algo );
		
		// ----------------------------------------------------------------------------------------------------- Generate OUTPUTS
		
		System.out.println( "FINAL RANKING :\n" + this.toString() );
		
		HtmlWriter htmlWriter = new HtmlWriter( NB_JUDGMENTS );
		htmlWriter.toHtmlFile( projectJudgments );
        
	}

	// *********************************************************************************************
	// * DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY D *
	// * DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY D *
	// *********************************************************************************************
	
	public String toString() 
	{
		String str = super.toString() + " :\n";
		for (ProjectJudgment project : projectJudgments) {
			str = str + project + "\n";
		}
		return str;
	}
	
}
