import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class DobbeltLenketListe<T> implements Liste<T> {
    
    private static final class Node<T> { // en indre nodeklasse
    
        // instansvariabler
        private T verdi;
        private Node<T> forrige, neste;
        
        private Node(T verdi, Node<T> forrige, Node<T> neste) { // konstruktør
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }
        
        protected Node(T verdi) { // konstruktør
            this(verdi, null, null);
        }
        
    }// Node
    
    // instansvariabler
    private Node<T> hode; // peker til den første i listen
    private Node<T> hale; // peker til den siste i listen
    private int antall; // antall noder i listen
    private int endringer; // antall endringer i listen
    
    // hjelpemetode
    private Node<T> finnNode(int indeks) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    // konstruktør
    public DobbeltLenketListe() {
        hode = hale = null;
        antall = 0;
        endringer = 0;
    }
    
    // konstruktør
    public DobbeltLenketListe(T[] a) {
        if (a == null)
            throw new NullPointerException("Tabellen a er null!");
        
        for(T element: a) {
            if (element != null) {
                Node<T> node = new Node<>(element);
                if (hode == null) {
                    hode = node;
                    hale = node;
                } else {
                    node.forrige = hale;
                    hale.neste = node;
                    hale = node;
                }
                antall++;
            }
        }
    }
    
    public Liste<T> subliste(int fra, int til) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public int antall() {
        return antall;
    }
    
    @Override
    public boolean tom() {
        return antall == 0;
    }
    
    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi);
        
        Node<T> node = new Node<>(verdi);
        if (tom()) {
            hode = hale = node;
        } else {
            hale.neste = node;
            node.forrige = hale;
            hale = node;
        }
        antall++;
        endringer++;
        return true;
    }
    
    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi);
        
        Node<T> node = new Node<>(verdi);
        
        if (tom()) {
            hode = hale = node;
        } else {
            hale.neste = node;
            node.forrige = hale;
            hale = node;
        }
        
        endringer++;
        antall++;
    }
    
    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public T hent(int indeks) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public T oppdater(int indeks, T nyverdi) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public void nullstill() {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public String toString() {
        StringBuilder setningsBygger = new StringBuilder("[");
        if (hode != null) {
            Node<T> gjeldendeNode = hode;
            setningsBygger.append(hode.verdi);
            while (gjeldendeNode.neste != null) {
                gjeldendeNode = gjeldendeNode.neste;
                setningsBygger.append(", " + gjeldendeNode.verdi);
            }
        }
        setningsBygger.append("]");
        return setningsBygger.toString();
    }
    
    public String omvendtString() {
        StringBuilder setningsBygger = new StringBuilder("[");
        if (hale != null) {
            Node<T> gjeldendeNode = hale;
            setningsBygger.append(hale.verdi);
            while (gjeldendeNode.forrige != null) {
                gjeldendeNode = gjeldendeNode.forrige;
                setningsBygger.append(", " + gjeldendeNode.verdi);
            }
        }
        setningsBygger.append("]");
        return setningsBygger.toString();
    }
    
    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }
    
    private class DobbeltLenketListeIterator implements Iterator<T> {
        
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;
        private DobbeltLenketListeIterator() {
            denne = hode; // denne starter på den første i listen
            fjernOK = false; // blir sann når next() kalles
            iteratorendringer = endringer; // teller endringer
        }
        
        private DobbeltLenketListeIterator(int indeks) {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }
        
        @Override
        public boolean hasNext() {
            return denne != null; // denne koden skal ikke endres!
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }
    } // DobbeltLenketListeIterator
} // DobbeltLenketListe 