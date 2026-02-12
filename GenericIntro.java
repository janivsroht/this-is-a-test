class Gen<t,v> {
    t var1;
    v var2;

    public Gen(t v1, v v2) {
        var1 = v1;
        var2 = v2;
    }

    t addThem() {
      return var1 + var2;
    }
}

public class GenericIntro {

    public static void main(String[] args) {
        System.out.println("Output");

        Gen<Float,Float> gen1 = new Gen<Float,Float>(0.1f, 0.9f);
        Gen<Integer> gen2 = new Gen<Integer>(10, 13);

        gen1.addThem();
        gen2.addThem().getClass().getName();
    }
}
