package v2;
import java.util.Comparator;
import java.util.Random;

public class SortByMajorityJudgment_20_WITH_MEDIAN_WITH_EGAL_NEXT_JUDGMENT implements Comparator<ProjectJudgment> {

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
			// Départage suite à point médian identique
			System.out.println ("p1.median = " + p1.medianPointJudgment + "   =   p2.median = " + p2.medianPointJudgment);
			
			double maxOf4 = Math.max( Math.max( p1.pcLessTh, p1.pcMoreTh ), Math.max( p2.pcLessTh, p2.pcMoreTh ) ); 

			System.out.println ("maxOf4 ( p1.less=" + p1.pcLessTh + " p1.more=" + p1.pcMoreTh + " p2.less=" + p1.pcLessTh + " p2.more=" + p1.pcMoreTh + " ) = " + maxOf4 );
			
			// Cette méthode n'est pas suffisante car elle ne départage pas les cas de percentThan égaux
			if ( (p1.pcLessTh == maxOf4) || (p2.pcMoreTh ==  maxOf4) ) 
			{
				System.out.println (" --> p1 < p2   (compare returns 1)");
				return 1;			
			}
			else
			{
				System.out.println (" --> p1 > p2   (compare returns -1)");
				return -1;			
			}
			
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
