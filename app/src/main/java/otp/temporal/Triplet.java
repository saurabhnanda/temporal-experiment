package otp.temporal;

public class Triplet<A, B, C> {

    private A first;
    private B second;
    private C third;

    public Triplet(A a, B b, C c) {
        first = a;
        second = b;
        third  = c;
    }

    public A first() { return first; }
    public B second() { return second; }
    public C third() { return third; }
}
