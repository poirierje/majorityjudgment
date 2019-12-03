import java.util.Comparator;
import java.util.Random;

public class SortByMajorityJudgment_FROM_BD implements Comparator<ProjectJudgment> {

	static final Random RND = new Random( System.currentTimeMillis() );

	public int compare(ProjectJudgment p1, ProjectJudgment p2) {
		
		// Cas où au moins un des deux candidats n'a reçu aucun jugement
		if ( p1.totalRealJudgments + p2.totalRealJudgments == 0 ) return  0;
		if ( p1.totalRealJudgments                         == 0 ) return -1;
		if (                     p2.totalRealJudgments     == 0 ) return  1;
		
//		System.out.println (  p1.medianPointJudgment  + " - " + p2.medianPointJudgment );
		
		if ( p1.medianPointJudgment == p2.medianPointJudgment )
		{
			// Départage suite à point médian identique
			
			double maxOf4 = Math.max( Math.max( p1.pcLessTh, p1.pcMoreTh ), Math.max( p2.pcLessTh, p2.pcMoreTh ) ); 

			// Cette méthode n'est pas suffisante car elle ne départage pas les cas de percentThan égaux
			if ( (p1.pcLessTh == maxOf4) || (p2.pcMoreTh ==  maxOf4) ) 
			{
				return 1;			
			}
			else
			{
				return -1;			
			}
			
		}
		else
		{
			// Le point médian est suffisant pour détartager
			return (p1.medianPointJudgment < p2.medianPointJudgment) ? 1 : -1;
		}
		
		
	}

}
