package v1;
import java.util.Comparator;
import java.util.Random;

public class SortByMajorityJudgment_FROM_FULL_ARTICLE implements Comparator<ProjectJudgment> {

	static final Random RND = new Random( System.currentTimeMillis() );

	public int compare(ProjectJudgment p1, ProjectJudgment p2) {
		
		return p1.majorityValue.compareTo( p2.majorityValue ) * -1;
		
	}

}
