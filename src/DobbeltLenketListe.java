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
        Node<T> node;
        if (indeks < antall/2) {
            node = hode;
            for (int i = 0; i < indeks; i++) {
                node = node.neste;
            }
        } else {
            node = hale;
            for (int i = antall - 1; i > indeks; i--) {
                node = node.forrige;
            }
        }
        
        return node;
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
        fratilKontroll(antall, fra, til);
        Liste<T> liste = new DobbeltLenketListe<>();
        
        Node<T> node = finnNode(fra);
        for (int i = fra; i < til; i++) {
            liste.leggInn(node.verdi);
            node = node.neste;
        }
        
        return liste;
    }
    
    private static void fratilKontroll(int antall, int fra, int til) {
        if (fra < 0)
            throw new IndexOutOfBoundsException("fra(" + fra + ") er negativ!");
        
        if (til > antall)
            throw new IndexOutOfBoundsException("til(" + til + ") > antall(" + antall + ")");
        
        if (fra > til)
          throw new IllegalArgumentException("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
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
        indeksKontroll(indeks, true);
        
        Node<T> nyNode = new Node<>(verdi);
        
        if (tom()) {
            hode = hale = nyNode;
        } else if (indeks == 0) {
            nyNode.neste = hode;
            hode.forrige = nyNode;
            hode = nyNode;
        } else if (indeks == antall) {
            hale.neste = nyNode;
            nyNode.forrige = hale;
            hale = nyNode;
        } else {
            Node<T> gammelNodeMedIndeks = finnNode(indeks);
            nyNode.forrige = gammelNodeMedIndeks.forrige;
            nyNode.neste = gammelNodeMedIndeks;
            gammelNodeMedIndeks.forrige.neste = nyNode;
            gammelNodeMedIndeks.forrige = nyNode;
        }
        antall++;
        endringer++;
    }
    
    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
    }
    
    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false);
        return finnNode(indeks).verdi;
    }
    
    @Override
    public int indeksTil(T verdi) {
        if (verdi == null) {
            return -1;
        }
        
        int indeks = 0;
        Node node = hode;
        while (node != null) {
            if (node.verdi.equals(verdi)) {
                return indeks;
            }
            indeks++;
            node = node.neste;
        }
        
        return -1;
    }
    
    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi);
        indeksKontroll(indeks, false);
        
        Node<T> node = finnNode(indeks);
        T gammelverdi = node.verdi;
        finnNode(indeks).verdi = nyverdi;
        
        endringer++;
        
        return gammelverdi;
    }
    
    @Override
    public boolean fjern(T verdi) {
        Node<T> node = hode;
        while (node != null) {
            if (node.verdi.equals(verdi)) {
                if (node == hode) hode = hode.neste;
                if (node == hale) hale = hale.forrige;
                if (node.forrige != null) node.forrige.neste = node.neste;
                if (node.neste != null) node.neste.forrige = node.forrige;
                antall--;
                endringer++;
                return true;
            }
            node = node.neste;
        }
        return false;
    }
    
    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks, false);
        Node<T> node = null;
        T verdi;
        
        if (indeks == 0) {
            node = hode;
            hode = hode.neste;
        }
        if (indeks == antall - 1) {
            node = hale;
            hale = hale.forrige;
        }
        if (indeks > 0 && indeks < antall/2) {
            node = hode;
            for (int i = 0; i < indeks; i++) {
                node = node.neste;
            }
        } else if (indeks < antall - 1){
            node = hale;
            for (int i = antall - 1; i > indeks; i--) {
                node = node.forrige;
            }
        }
        verdi = node.verdi;
        if (node.forrige != null) node.forrige.neste = node.neste;
        if (node.neste != null) node.neste.forrige = node.forrige;
        antall--;
        endringer++;
        return verdi;
    }
    
    @Override
    public void nullstill() {
        Node<T> node = hode;
        while (node != null) {
            Node<T> neste = node.neste;
            node.forrige = null;
            node.neste = null;
            node = neste;
        }
        hode = null;
        hale = null;
        antall = 0;
        endringer++;
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