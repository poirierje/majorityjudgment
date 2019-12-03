import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MajorityJudgment {

	// ALGORITHM
	static final String BD_BASIC            = "basic_algo";
	static final String BD_BASIC_EGAL_PERSO = "basic_with_egality_algo";
	static final String FULL                = "full_algo";
	


	
	
	static final String ALGO  = BD_BASIC;      static final boolean ARASE_0 = false;
//	static final String ALGO  = BD_BASIC_EGAL; static final boolean ARASE_0 = false;
//	static final String ALGO  = FULL;          static final boolean ARASE_0 = true;
	
	static final int      NB_PROJECTS         =     40;
	static final int      NB_VOTERS           =  20000;
	
	static final int      NB_JUDGMENTS        =      7; // nb of values for judging
	static final int      NB_BALLOTS_BY_VOTER =     10; // nb of ballots deposed by each voter (1 ballot = 1 project judgment) 

	
	
	
	
	static final Random   RND                 = new Random( System.currentTimeMillis() );
	
	static final String   HTML_FILENAME       = "C:\\TEMP\\majority_judment_results.html";
	
	static final String[] RGB                 = new String[NB_JUDGMENTS];

	
	
	// VARIABLES
	
	List<ProjectJudgment> votes = new ArrayList<ProjectJudgment>();
	
	
	
	// *********************************************************************************************
	// * MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN *
	// * MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN MAIN *
	// *********************************************************************************************

	public static void main(String[] args) 
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

	private void run() 
	{
		// Create projects
		for (int i = 0; i < NB_PROJECTS; i++) {
			votes.add( new ProjectJudgment( i, RND.nextDouble(), NB_PROJECTS, NB_JUDGMENTS, NB_VOTERS ));
		}
		
		// --------------------------------------------------------------------------------------------------- Simulate votes - Exactly NB_BALLOTS_BY_VOTER
		for (int i = 0; i < NB_VOTERS; i++) {
			List<ProjectJudgment> projects = new ArrayList<ProjectJudgment>( votes );
			for (int ballot = 0; ballot < NB_BALLOTS_BY_VOTER; ballot++) {
				
				// Which project to vote for ?
				ProjectJudgment votedProject = projects.get( RND.nextInt(projects.size()) );
				
				// Vote and remove this project
				double rnd 		= RND.nextDouble() * NB_JUDGMENTS;
				double centroid = rnd * (votedProject.lovely * 2);	
				if (centroid < 0               ) centroid = 0;
				if (centroid > NB_JUDGMENTS - 1) centroid = NB_JUDGMENTS - 1;
				
				if ( RND.nextDouble() < ( 0.33 * (1 + RND.nextDouble()) ) )
					centroid = RND.nextInt(NB_JUDGMENTS);
					
//				System.out.println( "Centroid = " + centroid + "		centroid rounded = " + Math.round(centroid) + "		rnd = " + rnd + "		Lovely = " + votedProject.lovely );
				
				votedProject.addJudgment( (int) Math.round(centroid) );
				projects.remove(votedProject);
				
//				System.out.println( "#" + i + " voted " + (int) Math.round(centroid) + " for " + votedProject );
			}
			System.out.println( "#" + i + " judgment done !" );
		}

		// On tronque un maximum de zéros (du nombre de zéros du projet ayant le moins de zéros)
		int nbZerosToDelete = votes.get(0).judgmentsAsNumbersWith0[0];
		for (ProjectJudgment project : votes) {
			if ( project.judgmentsAsNumbersWith0[0] < nbZerosToDelete )
				nbZerosToDelete = project.judgmentsAsNumbersWith0[0];
		}

		if ( !ARASE_0 )
			nbZerosToDelete = 0;
		
		for (ProjectJudgment project : votes) {
			project.judgmentsAsNumbersWith0[0] = project.judgmentsAsNumbersWith0[0] - nbZerosToDelete;
		}

		// Updating projects
		for (ProjectJudgment project : votes) {
			project.update( nbZerosToDelete );
		}

		// Sort projects
		if ( ALGO == BD_BASIC )
			votes.sort( new SortByMajorityJudgment_FROM_BD() );
		else if ( ALGO == BD_BASIC_EGAL_PERSO )
			votes.sort( new SortByMajorityJudgment_FROM_BD_WITH_EGALITY_HANDLE() );
		else
			votes.sort( new SortByMajorityJudgment_FROM_FULL_ARTICLE() );
		
		// Final display
//		System.out.println( toString() );
		toHtmlFile();
        
	}

	// *********************************************************************************************
	// * DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY D *
	// * DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY D *
	// *********************************************************************************************
	
	public String toString() 
	{
		String str = super.toString() + " :\n";
		for (ProjectJudgment project : votes) {
			str = str + project + "\n";
		}
		return str;
	}
	
	public void toHtmlFile() 
	{
		// Precalculate RGB colors
		if ( NB_JUDGMENTS == 5) {
			RGB[0] = "#ba131a";
			RGB[1] = "#f8931d";
			RGB[2] = "#fef200";
			RGB[3] = "#8ec63f";
			RGB[4] = "#008641";
		} else if ( NB_JUDGMENTS == 6) {
			RGB[0] = "#e13620";
			RGB[1] = "#f17b01";
			RGB[2] = "#fcb100";
			RGB[3] = "#80c442";
			RGB[4] = "#01aa57";
			RGB[5] = "#0b8343";
		} else if ( NB_JUDGMENTS == 7) {
			RGB[0] = "#e13620";
			RGB[1] = "#f17b01";
			RGB[2] = "#fcb100";
			RGB[3] = "#c9da01";
			RGB[4] = "#80c442";
			RGB[5] = "#01aa57";
			RGB[6] = "#0b8343";
		}

		// Generate HTML code
		String str = "<body>";

		str = str + "<div style='width=100%; position: relative; font-family: arial; font-size: 12px'>";
		for (ProjectJudgment project : votes) {
			boolean first = true;
			str = str + "<div style='width:100%; display: flex; flex-direction: column;'>";
			str = str + 	"<div style='display: flex; border-bottom: 1px solid white; color: white'>";
			for (int i = 0; i < NB_JUDGMENTS; i++) {
				if ( false ) {
					str = str + "<div style='display: flex; justify-content: center; align-items: center; position: relative; background-color: " + RGB[i] + "; width :" + ( 100 * project.judgmentsAsPercents[i] ) + "%; border-right :1px solid white; '>&nbsp;";
//					str = str + "<span >" + ( first ? project.name + "&nbsp;" : "" )+ "</span><span style='margin:3px; z-index: 1; padding: 2px; background-color: rgba(0,0,0,0.2);'>" + (Math.round(project.judgmentsAsPercents[i] * 100.0) / 100.0)  + "%</span>";
				} else {
					str = str + "<div style='display: flex; justify-content: center; align-items: center; position: relative; background-color: " + RGB[i] + "; width :" + ( 100 * project.judgmentsAsPercentsWith0[i] ) + "%; border-right :1px solid white; '>&nbsp;";
//					str = str + "<span >" + ( first ? project.name + "&nbsp;" : "" )+ "</span><span style='padding:1px; z-index: 1; padding: 2px; background-color: rgba(0,0,0,0.2);'>" + (Math.round(project.judgmentsAsPercentsWith0[i] * 100.0) / 100.0)  + "%</span>";
				}
				str = str + "</div>";
				first = false;
			}
			str = str + 	"</div>";
//			str = str + 	"<div style='margin-bottom: 10px; '>Lovely = " + project.lovely + "&emsp;Median value = " + project.medianPointJudgment + "&emsp;Percent less than = " + project.pcLessTh + "&emsp;Percent more than = " + project.pcMoreTh + "</div>";
			str = str + "</div>";
		}
		str = str + 	"<div style='position: absolute; top: 0; left: 0; height: 100%; width: 50%; border-right: 1px solid black;'>&nbsp;</div>";
		str = str + "</div>";

		for (ProjectJudgment project : votes) {
			str = str + "<div>" + project.name + " (" + project.totalRealJudgments + " votes) --> " + project.majorityValue + "</div>";
		}

		str = str + "</body>";

		// Write HTML into file
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(HTML_FILENAME));
			writer.write( str );
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
