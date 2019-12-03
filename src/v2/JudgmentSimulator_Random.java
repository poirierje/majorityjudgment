package v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JudgmentSimulator_Random implements IJudgmentSimulator {
	
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

				votedProject.addJudgment( RND.nextInt(nbJugments) );
				projects.remove(votedProject);
			}
		}
	}

	

}
