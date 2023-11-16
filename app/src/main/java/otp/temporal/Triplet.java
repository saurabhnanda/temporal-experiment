package otp.temporal;

public class Triplet<A, B, C> {

    private A first;
    private B second;
    private C third;

    public Triplet() {}

    public Triplet(A a, B b, C c) {
        first = a;
        second = b;
        third  = c;
    }

    public A first() { return first; }
    public B second() { return second; }
    public C third() { return third; }

    public int hashCode() {
        return this.first.hashCode() + this.second.hashCode() + this.third.hashCode();
    }

    public boolean equals(Triplet<A,B,C> p) {
        return p.first()==first && p.second()==second && p.third()==third;
    }

    public String toString() {
        return "<" + 
            (first==null ? null : first.toString()) + ", " + 
            (second==null ? null : second.toString()) + ", " + 
            (third==null ? null : third.toString()) + ">";
    }
}
