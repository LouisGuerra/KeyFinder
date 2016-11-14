/* Louis Guerra, Christopher Crawford, Andrew Zhou
 *
 * Fourier.java contains fft(double[] a) which will take an input of real
 * samples and will return a double array of the absolute values of the fft.
 *
 */
public class Fourier
{
    // Complex numbers are represented as length 2 double arrays
    // These constants help keeping ordering from getting mixed up
    private static final int REAL = 0;
    private static final int IMG = 1;
    
    // multiplying two complex numbers
    private static double[] complexMultiply(double[] a, double[] b)
    {
        double[] result = new double[2];
        result[REAL] = a[REAL]*b[REAL] - a[IMG]*b[IMG];
        result[IMG] = a[REAL]*b[IMG] + a[IMG]*b[REAL];
        return result;
    }
    
    // adding two complex numbers
    private static double[] complexAdd(double[] a, double[] b)
    {
        return new double[] {a[REAL] + b[REAL], a[IMG] + b[IMG]};
    }
    
    // subtracting b from a
    private static double[] complexSubtract(double[] a, double[] b)
    {
        return new double[] {a[REAL] - b[REAL], a[IMG] - b[IMG]};
    }
    
    // determining the absolute value (magnitude) of complex number a
    private static double complexMagnitude(double[] a)
    {
        return Math.sqrt(a[REAL] * a[REAL] + a[IMG] * a[IMG]);
    }
    
    // recursive complex FFT algorithm
    private static double[][] fftcomplex(double[][] input)
    {
        // default case
        if (input.length == 1) {
            return new double[][] { input[0] };
        }
        
        // create and fill arrays with even and odd terms
        double[][] even = new double[input.length/2][2];
        double[][] odd = new double[input.length/2][2];
        
        for (int i = 0; i < input.length/2; i++) {
            even[i][REAL] = input[i*2][REAL];
            even[i][IMG] = input[i*2][IMG];
            odd[i][REAL] = input[i*2+1][REAL];
            odd[i][IMG] = input[i*2+1][IMG];
        }
        
        // recursion
        double[][] evenfft = fftcomplex(even);
        double[][] oddfft = fftcomplex(odd);
        
        double[][] output = new double[input.length][2];
        
        // combine results
        for (int f = 0; f < input.length/2; f++) {
            double theta = -2 * f * Math.PI / ((double) input.length);
            double[] omega = {Math.cos(theta), Math.sin(theta)};
            output[f] = complexAdd(even[f], complexMultiply(omega, odd[f]));
            output[f + input.length/2] = complexSubtract(even[f], complexMultiply(omega, odd[f]));
        }
        
        return output;
    }
    
    
    // public wrapper method that takes real input and returns real output
    public static double[] fft(double[] input)
    {
        double[][] complexified = new double[input.length][2];
        
        // convert all real nubmers to complex numbers
        for (int i = 0; i < input.length; i++) {
            complexified[i][REAL] = input[i];
            complexified[i][IMG] = 0;
        }
        
        // perform fft
        double[][] fftresult = fftcomplex(complexified);
        
        // convert back to real numbers
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = complexMagnitude(fftresult[i]);
        }
        
        return output;
    }





}