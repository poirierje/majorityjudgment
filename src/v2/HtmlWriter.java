package v2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlWriter {

	static final String   HTML_FILENAME = "C:\\TEMP\\majority_judment_results.html";
	static       String[] RGB           = null;

	private int nbJudgments;
	
	public HtmlWriter( int nbJudgments ) {
		super();
		
		this.nbJudgments = nbJudgments;

		// Precalculate RGB colors
		RGB = new String[ nbJudgments ];
		
		if ( nbJudgments == 5) 
		{
			RGB[0] = "#ba131a";
			RGB[1] = "#f8931d";
			RGB[2] = "#fef200";
			RGB[3] = "#8ec63f";
			RGB[4] = "#008641";
		} 
		else if ( nbJudgments == 6) 
		{
			RGB[0] = "#e13620";
			RGB[1] = "#f17b01";
			RGB[2] = "#fcb100";
			RGB[3] = "#80c442";
			RGB[4] = "#01aa57";
			RGB[5] = "#0b8343";
		} 
		else if ( nbJudgments == 7) 
		{
			RGB[0] = "#e13620";
			RGB[1] = "#f17b01";
			RGB[2] = "#fcb100";
			RGB[3] = "#c9da01";
			RGB[4] = "#80c442";
			RGB[5] = "#01aa57";
			RGB[6] = "#0b8343";
		}
	}

	// *********************************************************************************************
	// * DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY D *
	// * DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY DISPLAY D *
	// *********************************************************************************************
	
	public void toHtmlFile( List<ProjectJudgment> projectJudgments ) 
	{

		// Generate HTML code
		String str = "<body>";

		str = str + "<div style='width=100%; position: relative; font-family: arial; font-size: 12px'>";
		for (ProjectJudgment project : projectJudgments) {
			boolean first = true;
			str = str + "<div style='width:100%; display: flex; flex-direction: column;'>";
			str = str + 	"<div style='display: flex; border-bottom: 1px solid white; color: white'>";
			for (int i = 0; i < nbJudgments; i++) {
				str = str + "<div style='display: flex; justify-content: flex-start; align-items: center; position: relative; background-color: " + RGB[i] + "; width :" + ( 100 * project.judgmentsAsPercentsWith0[i] ) + "%; border-right :1px solid white; '>&nbsp;";
				
				str = str + "<div style='text-align:left; width:50%'>" + ( first ? project.name + "&nbsp;" : "" )+ "</div><div style='text-align:left; padding:0px 3px; z-index: 1; margin: 2px; background-color: rgba(0,0,0,0.2);'>" + (Math.round(project.judgmentsAsPercentsWith0[i] * 100.0))  + "%</div>";
				
				str = str + "</div>";
				first = false;
			}
			str = str + 	"</div>";

//			str = str + 	"<div style='margin-bottom: 10px; '>Lovely = " + project.lovely + "&emsp;Median value = " + project.medianPointJudgment + "&emsp;Percent less than = " + project.pcLessTh + "&emsp;Percent more than = " + project.pcMoreTh + "</div>";
			
			str = str + "</div>";
		}
		str = str + 	"<div style='position: absolute; top: 0; left: 0; height: 100%; width: 50%; border-right: 3px solid black;'>&nbsp;</div>";
		str = str + "</div>";

		for (ProjectJudgment project : projectJudgments) {
			str = str + "<div style='font-family:\"courier new\"'>" + project.name + " (" + project.totalRealJudgments + " votes) --> " + project.majorityValue + "</div>";
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
