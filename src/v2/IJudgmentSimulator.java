package v2;

import java.util.List;

public interface IJudgmentSimulator {

	public void simulate( List<ProjectJudgment> projectJudgments, int nbJugments, int nbVoters, int nbBallots );

}
