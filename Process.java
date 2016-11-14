import java.util.*;
/* Louis Guerra, Andrew Zhou, Christopher Crawford
 *
 * Process.java contains various helper/conversion methods that are used by 
 * throughout the program.
 */

public class Process
{ 
    // normalizes a vector to magnitude 1
	public static double[] normalizeVector(double[] vector)
	{
		double sumSquared = 0;
		
		for (int i = 0; i < vector.length; i++){
			sumSquared += vector[i]*vector[i];
		}
		
		sumSquared = Math.sqrt(sumSquared);
		
		double[] newVector = new double[vector.length];
		
		for (int i = 0; i < vector.length; i++) {
			newVector[i] = vector[i] / sumSquared;
		}
		
		return newVector;
	}
  
	
	
  // returns a note vector for a given frequency spectrum
  // certain frequencies are bandpassed for a more accurate note vector
  public static double[] notes(double[] input, int samps, double srate)
  {
    
    double[] notes = new double[12]; //declare our vector of notes
    
    int m = input.length; //length of input matrix
    double t = 0.01; //threshold amplitude
    
    double lowbound = 130; //near C3
    double highbound = 2100; //near C7
    int startIndex = (int)(samps * lowbound / srate);
    int lastIndex = (int)(samps * highbound / srate);
    
    //loop through each entry that falls within our bandpass
    for(int i = startIndex; i < lastIndex; i++)
    {
      double a = input[i]; //read in amplitude
      
      //if amplitude is above threshold...
      if (a > t)
      {
        //read in freq and convert it to semitones above A
        double f = i*srate/samps;
        double n = (12*(Math.log(f/440))/Math.log(2))%12;
        //round to nearest integer semitone
        int n_int = (int) Math.round(n);
        
        //if our mod function returned negative, change it to positive 
        if (n < 0)
        {
          n = n +12;
        } else if (n > 11) {
          n = n - 12;
        }
        //if we didn't have to round too much, we record this into our notes vector.
        if (Math.abs(n - n_int) < 0.3)
        {
          notes[n_int] = notes[n_int] + a;
        }
      }    
    }  
    return notes;
  }
  
  
}





