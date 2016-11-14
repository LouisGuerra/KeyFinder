# KeyFinder
Finds Key of Songs.


Fourier.java - uses the FFT algorithm to convert an input discrete signal (array of double) into 
its frequency domain representation. This program contains various methods for handling the complex 
numbers involved in the calculation. 
This program was adopted partially from StdAudio.java by Robert Sedgewick and Kevin Wayne 

Process.java – Contains 2 methods, notes and normalizeVector. Notes takes as input an array of doubles 
that represents the results from the FFT. The indices are changed into frequency values, which we then 
convert to semitones above or below middle A (440 hertz). This information is used to sum up all the 
amplitudes that represent each note of the chromatic scale. A 12-vector is returned; it represents the 
relative appearances of each note along the length of the song. Note that before the FFT information is 
analyzed, we effectively bandpass the song by omitting frequencies below 130hz (about C3) and above 2100 
(around C7). This improves performance time and effectiveness by focusing on the frequencies which tend to 
carry the melody of the song.  
NormalizeVector is a simple function that normalizes any given vector 
such that its magnitude will be 1.

Profilematch.java – Contains getKey which inputs the 12-vector from notes and compares it to the 24 
standard vectors that were found to best represent the profiles of the 24 different keys according 
to the research paper, “What's Key for Key? The Krumhansl-Schmuckler Key-Finding Algorithm Reconsidered” 
published by Temperley.  GetKey uses least squares matching to best match the input vector with one of 
the key profiles, and outputs the key of the song, in string format. 

Keyfinder.java – Keyfinder is the 
primary program that combines everything else. It inputs a .wav or .midi file, and converts it into an 
array of doubles, a representation of all the samples in the signal. Keyfinder than uses Fourier.java to 
convert the song into a different array of doubles, which represent the frequency (of appearance) of all acoustic 
frequencies. This array is passed into notes, and then getKey, and prints the key of the input song.  
