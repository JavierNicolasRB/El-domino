package modelo;

public record Ficha(int ladoA, int ladoB) {
    
    public Ficha voltear() {
        return new Ficha(ladoB, ladoA);
    }

    public boolean esDoble() {
        return ladoA == ladoB;
    }

    public int getPuntos() {
        return ladoA + ladoB;
    }

    @Override
    public String toString() {
        return "[" + ladoA + "|" + ladoB + "]";
    }
}