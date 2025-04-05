public class PseudoRandom {
    
    private final long a = 127;
    private final long c = 89;
    private final long M = 2934824;

    private long lastPseudo;

    public PseudoRandom (long seed){
        lastPseudo = seed;
    }

    public double nextRandom() {
        lastPseudo = (a * lastPseudo + c) % M; 
        return (double) lastPseudo / M;    
    }
}
