import java.util.Arrays;
import java.util.Comparator;

public class Main {
    private static final int ITERATIONS = 100000;
    
    public static void main(String[] args) {
        String[] navn = {
                "Lars"
                ,
                "Anders"
                ,
                "Bodil"
                ,
                "Kari"
                ,
                "Per"
                ,
                "Berit"
                };
                Liste<String> liste1 = 
                new
                DobbeltLenketListe<>(navn);
                Liste<String> liste2 = 
                new
                TabellListe<>(navn);
                Liste<String> liste3 = 
                new
                EnkeltLenketListe<>(navn);
                DobbeltLenketListe.
                sorter
                (liste1, Comparator.naturalOrder
                ());
                DobbeltLenketListe.
                sorter
                (liste2, Comparator.naturalOrder
                ());
                DobbeltLenketListe.
                sorter
                (liste3, Comparator.naturalOrder
                ());
                System.
                out
                .println(liste1);  
                // [Anders, Berit, Bodil, Kari, Lars, Per]
                System.
                out
                .println(liste2);  
                // [Anders, Berit, Bodil, Kari, Lars, Per]
                System.
                out
                .println(liste3);  
                // [Anders, Berit, Bodil, Kari, Lars, Per]
                // Tabellen navn er upåvirket:
                System.
                out
                .println(Arrays.toString
                (navn));
                // [Lars, Anders, Bodil, Kari, Per, Berit]
    }
    
}
