import java.util.*;

public class ObligSBinTre<T> implements Beholder<T> {
    private static final class Node<T>{
        private T verdi; // nodens verdi
        private Node<T> venstre, høyre; // venstre og høyre barn
        private Node<T> forelder; // forelder

        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder){
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder) {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString(){
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot; // peker til rotnoden
    private int antall; // antall noder
    private int endringer; // antall endringer
    private final Comparator<? super T> comp; // komparator

    public ObligSBinTre(Comparator<? super T> c){
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public final boolean leggInn(T verdi){
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot;
        Node<T> q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null){       // fortsetter til p er ute av treet
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        p = new Node<>(verdi, q);                   // oppretter en ny node

        if (q == null) {
            rot = p;                  // p blir rotnode
        } else if (cmp < 0) {
            q.venstre = p;         // venstre barn til q
        }else {
            q.høyre = p;                        // høyre barn til q
        }

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null) {
            return false;
        }

        Node<T> p = rot;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) {
                p = p.venstre;
            } else if (cmp > 0){
                p = p.høyre;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi) {
        Deque<Node> stack = new ArrayDeque<>();
        Node<T> p = rot;
        int verdiAntall = 0;

        stack.addFirst(p);

        while(!stack.isEmpty()){
            Node<T> current = stack.removeFirst();

            if(current.verdi == verdi){
                verdiAntall++;
            }

            if(current.høyre != null){
                stack.addFirst(current.høyre);
            }

            if(current.venstre != null){
                stack.addFirst(current.venstre);
            }


        }
        return verdiAntall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {
        Objects.requireNonNull(p);

        if (p.høyre != null){
            p = p.høyre;
            while (p.venstre != null){
                p = p.venstre;
            }
        }else{
            while (p.forelder != null && p == p.forelder.høyre){
                p = p.forelder;
            }
            p = p.forelder;
        }

        return p;
    }

    @Override
    public String toString() {
        if (antall == 0){
            return "[]";
        }
        Node p = rot;

        while (p.venstre != null){
            p = p.venstre;
        }

        StringBuilder s = new StringBuilder("[").append(p.verdi);
        p = nesteInorden(p);

        for (int i = 0; i < antall-1; i++){
            s.append(", ").append(p.verdi);
            p = nesteInorden(p);
        }
        s.append("]");
        return s.toString();
    }

    public String omvendtString() {
        java.util.Deque<Node> stack = new java.util.ArrayDeque<>();

        //Lett første node på stacken
        stack.addFirst(rot);

        //Loop så lenge stacken ikke er tom
        while (!stack.isEmpty()) {
            //Skriv ut stackens innhold
            System.out.println("Stackens innhold: " + stack);

            //Hent ut node fra stacken
            Node current = stack.removeFirst();

            //Skriv ut preorden her
            System.out.print(current.verdi + ", ");

            //Legg høyre barn på stacken
            if (current.høyre != null) {
                stack.addFirst(current.høyre);
            }


            //Skriv ut inorden her
            //System.out.print(current.value + ", ");

            //Legg venstre barn på stacken
            if (current.venstre != null) {
                stack.addFirst(current.venstre);
            }

            //Skriv ut postorden her
            //System.out.print(current.value + ", ");

        }
        return null;
    }

    public String høyreGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T> {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator(){
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext() {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }
    }
}
