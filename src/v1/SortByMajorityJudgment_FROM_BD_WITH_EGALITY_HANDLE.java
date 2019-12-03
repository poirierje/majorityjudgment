package v1;
import java.util.Comparator;
import java.util.Random;

public class SortByMajorityJudgment_FROM_BD_WITH_EGALITY_HANDLE implements Comparator<ProjectJudgment> {

	static final Random RND = new Random( System.currentTimeMillis() );

	public int compare(ProjectJudgment p1, ProjectJudgment p2) {
		
		// Cas où au moins un des deux candidats n'a reçu aucun jugement
		if ( p1.totalRealJudgments + p2.totalRealJudgments == 0 ) return  0;
		if ( p1.totalRealJudgments                         == 0 ) return -1;
		if (                     p2.totalRealJudgments     == 0 ) return  1;
		
		if ( p1.medianPointJudgment == p2.medianPointJudgment )
		{
			// Départage suite à point médian identique
			
			double maxOf4 = Math.max( Math.max( p1.pcLessTh, p1.pcMoreTh ), Math.max( p2.pcLessTh, p2.pcMoreTh ) ); 

			// On va tester différents cas d'égalités éventuels des percentThan, dans un ordre bien défini
			
			// Cas où l'on a un seul percentMax (V = Victoire) :
			//			
			// 				+---+---+--+				+--+---+---+				+--+----+--+				+--+-----+-+
			// p1 -->		| 3 |   | 2|				| 2|   | 3 | V				| 2|    | 2| V 				| 2|     |1|
			// 				+---+---+--+				+--+---+---+				+--+----+--+				+--+-----+-+
			// 				+-+-----+--+				+-+-----+--+				+---+---+--+				+--+---+---+
			// p2 -->		|1|     | 2| V				|1|     | 2|				| 3 |   | 2|				| 2|   | 3 | V
			// 				+-+-----+--+				+-+-----+--+				+---+---+--+				+--+---+---+
			//			
			if ( p1.pcLessTh == maxOf4 && p1.pcMoreTh <  maxOf4 && p2.pcLessTh <  maxOf4 && p2.pcMoreTh <  maxOf4 ) return  1; // p2 wins !
			if ( p1.pcLessTh <  maxOf4 && p1.pcMoreTh == maxOf4 && p2.pcLessTh <  maxOf4 && p2.pcMoreTh <  maxOf4 ) return -1; // p1 wins !
			if ( p1.pcLessTh <  maxOf4 && p1.pcMoreTh <  maxOf4 && p2.pcLessTh == maxOf4 && p2.pcMoreTh <  maxOf4 ) return -1; // p1 wins !
			if ( p1.pcLessTh <  maxOf4 && p1.pcMoreTh <  maxOf4 && p2.pcLessTh <  maxOf4 && p2.pcMoreTh == maxOf4 ) return  1; // p2 wins !
			
			// Cas où l'on a 2 percentMax sur des intentions et des projets différents. On a forcément un percentMax sur l'intention more, qui est gagnant :
			//			
			// 				+---+---+--+				+--+---+---+
			// p1 -->		| 3 |   | 2|				| 2|   | 3 | V
			// 				+---+---+--+				+--+---+---+
			// 				+-+----+---+				+---+----+-+
			// p2 -->		|1|    | 3 | V				| 3 |    |1| 
			// 				+-+----+---+				+---+----+-+
			//			
			if ( p1.pcLessTh == maxOf4 && p1.pcMoreTh <  maxOf4 && p2.pcLessTh <  maxOf4 && p2.pcMoreTh == maxOf4 ) return  1; // p2 wins !
			if ( p1.pcLessTh <  maxOf4 && p1.pcMoreTh == maxOf4 && p2.pcLessTh == maxOf4 && p2.pcMoreTh <  maxOf4 ) return -1; // p1 wins !
			
			// Cas où l'on a au moins 2 percentMax sur les deux intentions d'un même projet. On supprime ce projet, et on compare les intentions de l'autre projet (qui peuvent être égales également...) :
			//			
			// 				+-+-----+--+				+--+-----+-+				+---+--+---+				+---+--+---+
			// p1 -->		|1|     | 2| V				| 2|     |1| 				| 3 |  | 3 |  				| 3 |  | 3 | V
			// 				+-+-----+--+				+--+-----+-+				+---+--+---+				+---+--+---+
			// 				+---+--+---+				+---+--+---+				+-+-----+--+				+--+-----+-+
			// p2 -->		| 3 |  | 3 |				| 3 |  | 3 | V				|1|     | 2| V				| 2|     |1| 
			// 				+---+--+---+				+---+--+---+				+-+-----+--+				+--+-----+-+
			//			
			if (                                                   p2.pcLessTh == maxOf4 && p2.pcMoreTh == maxOf4 ) return (p1.pcLessTh == p1.pcMoreTh) ? 0 : ((p1.pcLessTh < p1.pcMoreTh) ? -1 :  1);
			if ( p1.pcLessTh == maxOf4 && p1.pcMoreTh == maxOf4                                                   ) return (p2.pcLessTh == p2.pcMoreTh) ? 0 : ((p2.pcLessTh < p2.pcMoreTh) ?  1 : -1);

			// Cas où l'on a 2 percentMax sur la même intention entre les deux projets (on considère alors l'intention autre) :
			//			
			// 				+-+----+---+				+--+---+---+				+---+----+-+				+---+---+--+
			// p1 -->		|1|    | 3 | V				| 2|   | 3 |				| 3 |    |1|				| 3 |   | 2| V
			// 				+-+----+---+				+--+---+---+				+---+----+-+				+---+---+--+
			// 				+--+---+---+				+-+----+---+				+---+---+--+				+---+----+-+
			// p2 -->		| 2|   | 3 |				|1|    | 3 | V				| 3 |   | 2| V				| 3 |    |1| 
			// 				+--+---+---+				+-+----+---+				+---+---+--+				+---+----+-+
			//			
			if ( p1.pcLessTh <  maxOf4 && p1.pcMoreTh == maxOf4 && p2.pcLessTh <  maxOf4 && p2.pcMoreTh == maxOf4 ) return (p1.pcLessTh == p1.pcLessTh) ? 0 : ((p1.pcLessTh < p1.pcLessTh) ? -1 :  1);
			if ( p1.pcLessTh == maxOf4 && p1.pcMoreTh <  maxOf4 && p2.pcLessTh == maxOf4 && p2.pcMoreTh <  maxOf4 ) return (p1.pcMoreTh == p1.pcMoreTh) ? 0 : ((p1.pcMoreTh < p1.pcMoreTh) ? -1 :  1);

			//      /!\ Certains cas continuent à poser problème, par exemple en cas d'égalité dans les intentions et les sous-intentions de niveau 1 : on ne tient pas compte des intentions de niveau 2.
			
		}
		else
		{
			// Le point médian est suffisant pour détartager
			return (p1.medianPointJudgment < p2.medianPointJudgment) ? 1 : -1;
		}
		
		throw new RuntimeException("Should not reach this line !");
		
	}

}
