package v2;
import java.util.Comparator;
import java.util.Random;

public class SortByMajorityJudgment_10_WITH_MEDIAN_WITHOUT_EGAL implements Comparator<ProjectJudgment> {

	static final Random RND = new Random( System.currentTimeMillis() );

	public int compare(ProjectJudgment p1, ProjectJudgment p2) {

		System.out.println ("---------------------------- " + this.getClass().getCanonicalName());
		System.out.println ("Comparing :");
		System.out.println (p1);
		System.out.println ("and");
		System.out.println (p2);

		// Cas où au moins un des deux candidats n'a reçu aucun jugement
		if ( p1.totalRealJudgments + p2.totalRealJudgments == 0 ) 
		{
			System.out.println ("p1 and p2 have no judgments : p1 = p2 ");
			return  0;
		}
		
		if ( p1.totalRealJudgments == 0 ) 
		{
			System.out.println ("p1 has no judgment, p2 does : p1 < p2 ");
			return 1;
		}

		if ( p2.totalRealJudgments == 0 )
		{
			System.out.println ("p1 has judgment, p2 does not : p1 > p2 ");
			return -1;
		}

//		System.out.println (  p1.medianPointJudgment  + " - " + p2.medianPointJudgment );
		
		if ( p1.medianPointJudgment == p2.medianPointJudgment )
		{
			System.out.println ("p1.median = " + p1.medianPointJudgment + "   =   p2.median = " + p2.medianPointJudgment);
			return 0;
		}
		else
		{
			// Le point médian est suffisant pour détartager
			if ( p1.medianPointJudgment < p2.medianPointJudgment )
			{
				System.out.println ("p1.median = " + p1.medianPointJudgment + "   <   p2.median = " + p2.medianPointJudgment);
				return 1;
			}
			else
			{
				System.out.println ("p1.median = " + p1.medianPointJudgment + "   >   p2.median = " + p2.medianPointJudgment);
				return -1;
			}
		}
		
		
	}

}
