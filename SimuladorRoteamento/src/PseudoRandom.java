public class PseudoRandom {
    
    private final long a = 127;
    private final long c = 89;
    private final long M = 2934824;

    private long lastPseudo;
    private App app;

    public PseudoRandom (long seed, App app){
        lastPseudo = seed;
        this.app = app;
    }

    public double nextRandom() {
        app.decCount();
        lastPseudo = (a * lastPseudo + c) % M; 
        return (double) lastPseudo / M;    
    }
}
