
public class Main {
    private static final int ITERATIONS = 100000;
    
    public static void main(String[] args) {
        long totalTime = 0;
        DobbeltLenketListe<Integer> liste = new DobbeltLenketListe<>();
        for (int i = 0; i < ITERATIONS; i++) {
            for (int j = 0; j < 10000; j++) {
                liste.leggInn(j);
            }
            long start = System.nanoTime();
            liste.nullstill();
            long end = System.nanoTime();
            totalTime += end - start;
        }
        System.out.println("nanosekunder: " + totalTime/ITERATIONS + " ns.");
    }
    
}
