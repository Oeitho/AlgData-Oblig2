import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TabellListe<T> implements Liste<T>
{
  private T[] a;
  private int antall;
  private int endringer;

  public TabellListe(int størrelse)    // konstruktør
  {
    @SuppressWarnings("unchecked")   // pga. konverteringen: Object[] -> T[]
    T[] b = (T[]) new Object[størrelse];  // oppretter tabellen

    a = b;
    antall = 0;                           // foreløpig ingen verdier
    endringer = 0;                        // foreløpig ingen endringer
  }

  public TabellListe()                    // standardkonstruktør
  {
    this(10);                             // startstørrelse på 10
  }

  public TabellListe(T[] b)               // en T-tabell som parameter
  {
    this(b.length);                       // kaller en konstruktør

    for (T verdi : b)
    {
      if (verdi != null) a[antall++] = verdi;  // hopper over null-verdier
    }
  }

  private static void fratilKontroll(int antall, int fra, int til)
  {
    if (fra < 0)                                  // fra er negativ
      throw new IndexOutOfBoundsException
        ("fra(" + fra + ") er negativ!");

    if (til > antall)                             // til er utenfor tabellen
      throw new IndexOutOfBoundsException
        ("til(" + til + ") > antall(" + antall + ")");

    if (fra > til)                                // fra er større enn til
      throw new IllegalArgumentException
        ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
  }

  public Liste<T> subliste(int fra, int til)
  {
    fratilKontroll(antall, fra, til);

    TabellListe<T> liste = new TabellListe<>(til - fra);

    for (int i = fra, j = 0; i < til; i++, j++) liste.a[j] = a[i];
    liste.antall = til - fra;

    return liste;
  }

  @Override
  public boolean leggInn(T verdi)  // inn bakerst
  {
    Objects.requireNonNull(verdi, "null er ulovlig!");

    // En full tabell utvides med 50%
    if (antall == a.length)
    {
      a = Arrays.copyOf(a,(3*antall)/2 + 1);
    }

    a[antall++] = verdi;   // setter inn ny verdi

    endringer++;

    return true;
  }

  @Override
  public void leggInn(int indeks, T verdi)
  {
    Objects.requireNonNull(verdi, "null er ulovlig!");

    indeksKontroll(indeks, true);  // true: indeks = antall er lovlig

    // En full tabell utvides med 50%
    if (antall == a.length) a = Arrays.copyOf(a,(3*antall)/2 + 1);

    // rydder plass til den nye verdien
    for (int i = antall; i > indeks; i--) a[i] = a[i-1];

    a[indeks] = verdi;     // setter inn ny verdi

    antall++;              // en ny verdi
    endringer++;           // en endring
  }

  @Override
  public int antall()
  {
    return antall;         // returnerer antallet
  }

  @Override
  public boolean tom()
  {
    return antall == 0;    // listen er tom hvis antall er 0
  }

  @Override
  public T hent(int indeks)
  {
    indeksKontroll(indeks, false);   // false: indeks = antall er ulovlig
    return a[indeks];                // returnerer er tabellelement
  }

  @Override
  public int indeksTil(T verdi)
  {
    for (int i = 0; i < antall; i++)
    {
      if (a[i].equals(verdi)) return i;   // funnet!
    }
    return -1;   // ikke funnet!
  }

  @Override
  public boolean inneholder(T verdi)
  {
    return indeksTil(verdi) != -1;
  }

  @Override
  public T oppdater(int indeks, T verdi)
  {
    Objects.requireNonNull(verdi, "null er ulovlig!");

    indeksKontroll(indeks, false);  // false: indeks = antall er ulovlig

    T gammelverdi = a[indeks];      // tar vare på den gamle verdien
    a[indeks] = verdi;              // oppdaterer

    endringer++;                    // en endring

    return gammelverdi;             // returnerer den gamle verdien
  }

  @Override
  public T fjern(int indeks)
  {
    indeksKontroll(indeks, false);  // false: indeks = antall er ulovlig
    T verdi = a[indeks];

    antall--;  // reduserer antallet

    // sletter ved å flytte verdier mot venstre
    for (int i = indeks; i < antall; i++) a[i] = a[i + 1];

    a[antall] = null;   // tilrettelegger for "søppeltømming"

    endringer++;

    return verdi;
  }

  @Override
  public boolean fjern(T verdi)
  {
    Objects.requireNonNull(verdi, "null er ulovlig!");

    for (int i = 0; i < antall; i++)
    {
      if (a[i].equals(verdi))
      {
        antall--;  // reduserer antallet

        // sletter ved å flytte verdier mot venstre
        for (int j = i; j < antall; j++) a[j] = a[j + 1];

        a[antall] = null;   // tilrettelegger for "søppeltømming"
        endringer++;

        return true;  // vellykket fjerning
      }
    }
    return false;  // ingen fjerning
  }

  @Override
  public boolean fjernHvis(Predicate<? super T> p)    // betingelsesfjerning
  {
    Objects.requireNonNull(p, "predikatet er null");  // kaster unntak

    int nyttAntall = antall;

    for (int i = 0, j = 0; j < antall; j++)
    {
      if (p.test(a[j])) nyttAntall--;   // a[j] skal fjernes
      else a[i++] = a[j];               // forskyver 
    }

    for (int i = nyttAntall; i < antall; i++)
    {
      a[i] = null;   // tilrettelegger for "søppeltømming"
    }

    boolean fjernet = nyttAntall < antall;

    if (fjernet) endringer++;

    antall = nyttAntall;

    return fjernet;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void nullstill()
  {
    if (a.length > 10)
      a = (T[])new Object[10];
    else
      for (int i = 0; i < antall; i++)
      {
        a[i] = null;
      }

    antall = 0;
    endringer++;
  }

  public String toString2()
  {
    StringBuilder s = new StringBuilder("[");

    if (!tom())
    {
      s.append(a[0]);
      for (int i = 1; i < antall; i++)
      {
        s.append(',').append(' ').append(a[i]);
      }
    }
    s.append(']');

    return s.toString();
  }

  @Override
  public String toString()
  {
    StringJoiner s = new StringJoiner(", ","[","]");

    for (int i = 0; i < antall; i++) s.add(a[i].toString());

    return s.toString();
  }

  @SuppressWarnings("unchecked")
  public <T> T[] tilTabell(T[] b)
  {
    if (b.length < antall)
      return (T[]) Arrays.copyOf(a, antall, b.getClass());
    System.arraycopy(a, 0, b, 0, antall);
    if (b.length > antall) b[antall] = null;
    return b;
  }

  @Override
  public void forEach(Consumer<? super T> action)
  {
    for (int i = 0; i < antall; i++)
    {
      action.accept(a[i]);
    }
  }

  @Override
  public Iterator<T> iterator()
  {
    return new TabellListeIterator();
  }

  private class TabellListeIterator implements Iterator<T>
  {
    private int denne;               // instansvariabel
    private boolean fjernOK;         // instansvariabel
    private int iteratorendringer;   // instansvariabel

    private TabellListeIterator()
    {
      denne = 0;
      fjernOK = false;
      iteratorendringer = endringer;
    }

    @Override
    public boolean hasNext()     // sjekker om det er flere igjen
    {
      return denne < antall;     // sjekker verdien til denne
    }

    @Override
    public T next()
    {
      if (iteratorendringer != endringer)
      {
        throw new ConcurrentModificationException("Listen er endret!");
      }

      if (!hasNext())
      {
        throw new NoSuchElementException("Tomt eller ingen verdier igjen!");
      }

      T denneverdi = a[denne];   // henter aktuell verdi
      denne++;                   // flytter indeksen
      fjernOK = true;            // nå kan remove() kalles

      return denneverdi;         // returnerer verdien
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action)
    {
      while (denne < antall)
      {
        action.accept(a[denne++]);
      }
    }

    @Override
    public void remove()
    {
      if (iteratorendringer != endringer)
        throw new ConcurrentModificationException("Listen er endret!");

      if (!fjernOK)
        throw new IllegalStateException("Ulovlig tilstand!");

      fjernOK = false;           // remove() kan ikke kalles på nytt

      // verdien i denne - 1 skal fjernes da den ble returnert i siste kall
      // på next(), verdiene fra og med denne flyttes derfor en mot venstre
      antall--;           // en verdi vil bli fjernet
      denne--;            // denne må flyttes til venstre

      for (int i = denne; i < antall; i++) a[i] = a[i + 1];  // tetter igjen

      a[antall] = null;   // verdien som lå lengst til høyre nulles

      endringer++;
      iteratorendringer++;
    }

  } // TabellListeIterator

} // TabellListe