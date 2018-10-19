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
        if (verdi == null) {
            return false;
        }

        Node<T> p = rot, q = null;

        while (p != null){
            int cmp = comp.compare(verdi,p.verdi);
            if (cmp < 0) {
                q = p;
                p = p.venstre;
            }
            else if (cmp > 0) {
                q = p;
                p = p.høyre;
            }
            else {
                break;
            }
        }

        if (p == null) {
            return false;
        }

        if (p.venstre == null || p.høyre == null)   {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;
            if (p == rot){
                rot = b;
            }
            else if (p == q.venstre) {
                q.venstre = b;
                if (b != null) {
                    b.forelder = q;
                }
            }
            else {
                q.høyre = b;
                if (b != null) {
                    b.forelder = q;
                }
            }
        }
        else {
            Node<T> s = null, r = p.høyre;
            while (r.venstre != null) {
                r = r.venstre;
            }

            s = r.forelder;
            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p){
                s.venstre = r.høyre;
                if (r.høyre != null) {
                    r.høyre.forelder = s.venstre;
                }
            }
            else{
                s.høyre = r.høyre;
                if (r.høyre != null) {
                    r.høyre.forelder = s.høyre;
                }
            }
        }

        antall--;   // det er nå én node mindre i treet
        return true;

    }

    public int fjernAlle(T verdi) {
        int antallFjernet = 0;
        while (fjern(verdi)){
            antallFjernet++;
        }
        return antallFjernet;
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
        Node fjern = rot;
        Node temp = null;
        while (!tom()){
            if (nesteInorden(fjern) != null){
                fjern = nesteInorden(fjern);
            }else{
                fjern = rot;
                while (fjern.venstre != null){
                    fjern = fjern.venstre;
                }
            }

            if (fjern.venstre == null && fjern.høyre == null){
                if (fjern == rot){
                    rot = null;
                    antall--;
                    break;
                }
                temp = fjern.forelder;
                fjern.forelder = null;
                if (temp.venstre == fjern){
                    temp.venstre = null;
                }else {
                    temp.høyre = null;
                }
                fjern.verdi = null;
                fjern = temp;
                antall--;
            }
        }
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
        Deque<Node> stack = new ArrayDeque<>();
        Node p = rot;
        StringJoiner s = new StringJoiner(", ", "[","]");

        //Loop så lenge stacken ikke er tom
        while (p != null || !stack.isEmpty())
        {

            while (p !=  null)
            {
                stack.push(p);
                p = p.høyre;
            }

            p = stack.pop();

            s.add(p.verdi.toString());

            p = p.venstre;
        }
        return s.toString();
    }


    public String høyreGren() {
        Deque<Node> stack = new ArrayDeque<>();
        Node p = rot;

        while (p != null || !stack.isEmpty()) {

            while (p !=  null) {
                stack.push(p);
                p = p.høyre;
            }

            p = stack.pop();
            if (p.venstre == null && p.høyre == null){
                break;
            }
            p = p.venstre;

        }

        Deque<Node> forelderStack = new ArrayDeque<>();

        while (p != null){
            forelderStack.push(p);
            p = p.forelder;
        }

        StringJoiner s = new StringJoiner(", ", "[","]");
        while (!forelderStack.isEmpty()){
            p = forelderStack.pop();
            s.add(p.verdi.toString());
        }
        return s.toString();
    }

    public String lengstGren() {
        if (antall == 0){
            return "[]";
        }
        String [] strings = grener();
        String lengsteString = strings[0];
        int lengsteLength = 0;
        if (strings.length == 1){
            return lengsteString;
        }else {

            for (String counter:lengsteString.split(",")) {
                lengsteLength++;
            }
        }

        for (int i = 1; i < strings.length; i++){
            String string = strings[i];
            int stringLength = 0;
            for (String count:string.split(",")) {
                stringLength++;
            }
            if (stringLength >= lengsteLength){
                lengsteLength = stringLength;
                lengsteString = string;
            }
        }
        return lengsteString;
    }

    public String[] grener() {
        Deque<Node> stack = new ArrayDeque<>();
        Deque<Node> bladStack = new ArrayDeque<>();
        Deque<Node> hjelpeBladStack = new ArrayDeque<>();
        Node p = rot;
        int i = 0;
        int j = 0;

        while (p != null || !stack.isEmpty()){
            while (p !=  null){
                if (p.venstre == null && p.høyre == null){
                    bladStack.push(p);
                    i++;
                }
                stack.push(p);
                p = p.venstre;
            }
            p = stack.pop();
            p = p.høyre;
        }

        String[] strings = new String[i];

        while (!bladStack.isEmpty()){
            p = bladStack.pop();
            while (p != null){
                hjelpeBladStack.push(p);
                p = p.forelder;
            }

            StringJoiner s = new StringJoiner(", ", "[", "]");
            while (!hjelpeBladStack.isEmpty()){
                p = hjelpeBladStack.pop();
                s.add(p.verdi.toString());
            }

            strings[j] = s.toString();
            j++;
        }
        return strings;
    }

    public String bladnodeverdier() {
        if(antall == 0){
            return "[]";
        }

        StringJoiner s = new StringJoiner(", ", "[", "]");
        Node p = rot;
        traverserBlad(p, s);

        return s.toString();
    }

    public void traverserBlad(Node p, StringJoiner s){
        if(p.venstre != null){
            traverserBlad(p.venstre, s);
        }
        if(p.venstre == null &&  p.høyre == null){
            s.add(p.verdi.toString());
        }


        if(p.høyre != null){
            traverserBlad(p.høyre, s);
        }
    }

    public Node traverserBladNext(Node p){
        if(p.venstre != null){
            traverserBladNext(p.venstre);
        }
        if(p.venstre == null &&  p.høyre == null){
            return p;
        }

        if(p.høyre != null){
            traverserBladNext(p.høyre);
        }

        return p;
    }

    public String postString() {
        if(antall == 0){
            return "[]";
        }
        Deque<Node> stack = new ArrayDeque<>();
        Node p = rot;
        StringJoiner s = new StringJoiner(", ", "[","]");

        stack.push(p);
        Node prev = null;
        while (!stack.isEmpty()) {
            Node current = stack.peek();

            if (prev == null || prev.venstre == current || prev.høyre == current) {
                if (current.venstre != null)
                    stack.push(current.venstre);
                else if (current.høyre != null)
                    stack.push(current.høyre);
                else {
                    stack.pop();
                    s.add(current.verdi.toString());
                }

            }
            else if (current.venstre == prev) {
                if (current.høyre != null)
                    stack.push(current.høyre);
                else {
                    stack.pop();
                    s.add(current.verdi.toString());
                }
            }
            else if (current.høyre == prev)
            {
                stack.pop();
                s.add(current.verdi.toString());
            }

            prev = current;
        }
        return s.toString();
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
            p = rot;
            if(p == null){
                return;
            }
            while(p.høyre != null || p.venstre != null){
                q = p;
                if(p.venstre != null){
                    p = p.venstre;
                }
                if(p.høyre != null){
                    p = p.høyre;
                }
            }


        }

        @Override
        public boolean hasNext() {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next() {
            if (endringer != iteratorendringer) {
                throw new ConcurrentModificationException("Listen er endret!");
            }

            if (!hasNext()) {
                throw new NoSuchElementException("Tomt eller ingen verdier igjen!");
            }
            q = p;

            removeOK = true;

            while ((p.høyre != null && p.venstre != null) || p == q){
                p = nesteInorden(p);
            }

            return p.verdi;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }
    }
}
