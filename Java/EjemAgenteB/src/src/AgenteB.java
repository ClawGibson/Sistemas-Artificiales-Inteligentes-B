package src;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author Gamaliel Bernal
 */
public class AgenteB extends Agent {

    protected void setup() {;
        addBehaviour(new Comportamiento());
    }

    private class Comportamiento extends Behaviour {

        int cont = 0;

        public void action() {
            System.out.println("Contador: [" + cont + "] Hola mundo de agentes yo soy el agente --> "
                    + getAID().getName());
        }

        public boolean done() {
            if (cont == 100) {
                return true;
            } else {
                cont++;
            }
            return false;
        }
    }
}
