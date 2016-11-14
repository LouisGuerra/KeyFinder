import javax.sound.sampled.*;
import java.io.*;
import java.util.*;
/* Christopher Crawford, Louis Guerra, Andrew Zhou
 *
 *
 *
 * KeyFinder.java contains the program that will determine the key of a
 * song given as input.
 * 
 * USAGE: java KeyFinder filename
 *
 * filename must be the filename/filepath to a 16bit wav file
 * 
 * Using -Xmx2g or another method to increase the memory alloted to the JVM
 * may be nessesary.
* 
*/

public class KeyFinder {
    // will take the first channel if the audio is stereo or more
    // adopted partially from StdAudio.java by Robert Sedgewick and Kevin Wayne.
    private static double[] convertToDoubles(byte[] bytes, AudioFormat format) throws Exception
    {
        int bytesPerSample = format.getFrameSize();
        int channels = format.getChannels(); 
        
        if (bytesPerSample/channels != 2 ||
            format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED ||
            format.isBigEndian()) {
            System.err.println(format);
            throw new Exception("Audio must be 16bit, PCM_SIGNED, Little endian");
        }
    
        float sampleRate = format.getSampleRate();
        
        int N = bytes.length;
    
        double[] output = new double[N/bytesPerSample];
        
    
        int mult = bytesPerSample/channels;
        for (int i = 0; i < output.length; i++) {
            output[i] = ((short) (((bytes[bytesPerSample*i+1] & 0xFF) << 8) + (bytes[bytesPerSample*i] & 0xFF))) / ((double) Short.MAX_VALUE);
        }
    
        return output;
    }


    // main program
    public static void main(String args[])
    {
        // Must be power of 2. This is how many samples are processed by the 
        // FFT at once. 
        int windowsize = 16384;
        
        if (args.length < 1) {
            System.err.println("Run with argument: filename");
            System.exit(0);
        }
    
        try {
            //open the file
            File file = new File(args[0]);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            int size = audioStream.available();
            
            //convert file to array of double samples
            byte[] bytes = new byte[size];
            audioStream.read(bytes);
            double[] songData = KeyFinder.convertToDoubles(bytes, audioStream.getFormat());
            double samplerate = audioStream.getFormat().getSampleRate();
            
            //test
            //StdAudio.play(songData);
            
            // run fft
            int samples = songData.length;
            double[] ffts = new double[windowsize];
            double[] sig = new double[windowsize];
            
            for (int i = 0; i < samples/windowsize; i++) {
                for (int j = 0; j < windowsize; j++) {
                    sig[j] = songData[i*windowsize + j];
                }
                double[] temp = Fourier.fft(sig);
                for (int j = 0; j < windowsize; j++) {
                    ffts[j] += temp[j];
                }
            }
            
            // normalize fft
            ffts = Process.normalizeVector(ffts);
            
            // determine song note profile
            double[] songprofile = Process.notes(ffts, windowsize, samplerate);
            
            // estimate best fit for the key of the song
            String key = ProfileMatch.getKey(songprofile);
            
            // print results
            System.out.println("The key of the song is " + key);
            
            
            // catch exceptions
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    
    }
}