package otp.temporal;

public class Pair<A, B> {

    private A first;
    private B second;

    public Pair() {}

    public Pair(A a, B b) {
        first = a;
        second = b;
    }

    public int hashCode() {
        return this.first.hashCode() + this.second.hashCode();
    }

    public boolean equals(Pair<A,B> p) {
        return p.first()==first && p.second()==second;
    }

    public String toString() {
        return "<" + first.toString() + ", " + second.toString() + ">";
    }

    public A first() { return first; }
    public B second() { return second; }
}
