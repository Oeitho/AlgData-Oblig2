import java.util.Comparator;
import java.util.Random;

public class Main {
    
    private static final int ITERATIONS = 1000;
    private static Random random = new Random();
    
    public static void main(String[] args) {
        Integer[] i1 = new Integer[ITERATIONS];
        Integer[] i2 = new Integer[ITERATIONS*4];
        for (int i = 0; i < ITERATIONS; i++) {
            i1[i] = random.nextInt();
        }
        for (int i = 0; i < ITERATIONS * 4; i++) {
            i2[i] = random.nextInt();
        }
        Liste<Integer> liste1 = new DobbeltLenketListe<Integer>(i1);
        Liste<Integer> liste2 = new DobbeltLenketListe<Integer>(i2);
        
        long startTime1 = System.currentTimeMillis();
        
        DobbeltLenketListe.sorter(liste1, Comparator.naturalOrder());
        
        long endTime1 = System.currentTimeMillis();
        
        long time1 = endTime1 - startTime1;
        
        long startTime2 = System.currentTimeMillis();
        
        DobbeltLenketListe.sorter(liste2, Comparator.naturalOrder());
        
        long endTime2 = System.currentTimeMillis();
        
        long time2 = endTime2 - startTime2;
        
        System.out.println((double)((double) (time2)/ (double)(time1)));
    }
}
