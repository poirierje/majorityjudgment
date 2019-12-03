package v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JudgmentSimulator_WithCentroid implements IJudgmentSimulator {
	
	static final Random RND = new Random( System.currentTimeMillis() );

	// *********************************************************************************************
	// * SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE *
	// * SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE SIMULATE *
	// *********************************************************************************************
	
	public void simulate( List<ProjectJudgment> projectJudgments, int nbJugments, int nbVoters, int nbBallots ) 
	{
		for (int i = 0; i < nbVoters; i++) {
			List<ProjectJudgment> projects = new ArrayList<ProjectJudgment>( projectJudgments );
			for (int ballot = 0; ballot < nbBallots; ballot++) {
				
				// Which project to vote for ?
				ProjectJudgment votedProject = projects.get( RND.nextInt(projects.size()) );
				
				// Vote and remove this project
				double rnd 		= RND.nextDouble() * nbJugments;
				double centroid = rnd * (votedProject.lovely * 2);	
				if (centroid < 0               ) centroid = 0;
				if (centroid > nbJugments - 1) centroid = nbJugments - 1;
				
				if ( RND.nextDouble() < ( 0.33 * (1 + RND.nextDouble()) ) )
					centroid = RND.nextInt(nbJugments);
					
//				System.out.println( "Centroid = " + centroid + "		centroid rounded = " + Math.round(centroid) + "		rnd = " + rnd + "		Lovely = " + votedProject.lovely );
				
				votedProject.addJudgment( (int) Math.round(centroid) );
				projects.remove(votedProject);
				
//				System.out.println( "#" + i + " voted " + (int) Math.round(centroid) + " for " + votedProject );
			}
		}
	}

	

}
