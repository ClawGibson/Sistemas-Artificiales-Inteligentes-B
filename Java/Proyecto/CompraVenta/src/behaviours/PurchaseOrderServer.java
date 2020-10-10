package behaviours;

import agents.BookSellerAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PurchaseOrderServer extends CyclicBehaviour {

    BookSellerAgent bsAgent;

    public PurchaseOrderServer(BookSellerAgent a) {
        bsAgent = a;
    }

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = bsAgent.receive(mt);

        if (msg != null) {
            String title = msg.getContent();
            ACLMessage reply = msg.createReply();

            Integer price = (Integer) bsAgent.getCatalogue().remove(title);
            if (price != null) {
                reply.setPerformative(ACLMessage.INFORM);
                System.out.println("Libro: " + title + " vendido al agente [" + msg.getSender().getLocalName() + "]");
            } else {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("No disponible");
            }
            bsAgent.send(reply);
        } else {
            block();
        }
    }
}
