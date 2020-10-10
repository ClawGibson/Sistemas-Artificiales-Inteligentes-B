package comprador;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 *
 * @author Gamaliel Bernal
 */
public class Comprador extends Agent {

    private String titulo;

    protected void setup() {
        System.out.println("Soy el agente comprador [" + getLocalName() + "]");

        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            titulo = (String) args[0];
            System.out.println("Vamos a intentar comprar el libro: " + titulo);
            addBehaviour(new TickerBehaviour(this, 10000) {
                protected void onTick() {
                    System.out.println("[" + getLocalName() + "] está enviando petición a posibles vendedores...");
                }
            });
        } else {
            System.out.println("No se ha especificado un libro a comprar.");
            doDelete();
        }
    }
}
