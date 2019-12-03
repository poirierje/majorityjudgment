package v2;
import java.util.Comparator;
import java.util.Random;

public class SortByMajorityJudgment_40_WITH_MEDIAN_WITH_EGAL_VALEUR_MAJORITAIRE implements Comparator<ProjectJudgment> {

	static final Random RND = new Random( System.currentTimeMillis() );

	public int compare(ProjectJudgment p1, ProjectJudgment p2) {
		
		return p1.majorityValue.compareTo( p2.majorityValue ) * -1;
		
	}

}
