/* Andrew Zhou, Louis Guerra, Christopher Crawford
 *
 * ProfileMatch.java contains the 'getKey' method which matches a given song
 * profile to a key using least-squares matching.
 * 
 * 
 */
public class ProfileMatch
{
	private static double squareError (double [] freq, double [] trend) {
		double squareError = 0.0;
		
		for (int i = 0; i < freq.length; i++) {
			squareError += (freq[i]-trend[i]) * (freq[i]-trend[i]);
		}
		
		return squareError;
	}
	
	private static double[] bestFitTrend (double [] freq, double[][] trends) {
		double leastError;
		double[] bestFitTrend;
		double newError;
		leastError = squareError(freq, trends[0]);
		bestFitTrend = trends[0];
		for (int i = 1; i < trends.length; i++) {
			newError = squareError(freq, trends[i]);
			if (newError < leastError) {
				leastError = newError;
				bestFitTrend = trends[i];
			}
		}
		return bestFitTrend;
	}
    
  public static String getKey (double[] input)
  {
    double[][] keypro = new double[12][24];
    
    //profile vector
    double[] major = {6.35, 2.23, 3.48, 2.33, 4.38, 4.09, 2.52, 5.19, 2.39, 3.66, 2.29, 2.88};
    double[] minor = {6.33, 2.68, 3.52, 5.38, 2.60, 3.53, 2.54, 4.75, 3.98, 2.69, 3.34, 3.17};
    //strings of root notes
    String[] root = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    
    //load our profile vectors into 2D array
    for(int j = 0; j < 12; j++)
    {
      for(int i = 0; i < 24; i++)
      {
        int k = (j+i)%12;
        
        if (j < 12)
        {
          keypro[j][i] = major[k];
        }
        else
        {
          keypro[j][i] = minor[i];
        }
      }
    }  
    //retrieve best fit vector
    double[] match = bestFitTrend(input, keypro);
    
    //find and print our corresponding key
    
    String output = "";
    
    for(int i = 0; i < 12; i++)
    {
      if (match[i] == 6.33)
      {
        output = root[i] + " Minor";      
      }
      if (match[i] == 6.35)
      {
        output = root[i] + " Major"; 
      }        
    }
    
    return output;
  }
}