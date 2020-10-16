package behaviours;

import GUI.Inicio;
import agents.BookBuyerAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javax.swing.JOptionPane;

public class RequestPerformer extends Behaviour {

    private AID bestSeller;
    private int bestPrice;
    private int repliesCount = 0;
    private MessageTemplate mt;
    private int step = 0;
    private BookBuyerAgent bbAgent;
    private String bookTitle;
    Inicio in;

    public RequestPerformer(BookBuyerAgent a, Inicio in) {
        bbAgent = a;
        bookTitle = a.getBookTitle();
        this.in = in;
    }

    public void action() {
        switch (step) {
            case 0:
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < bbAgent.getSellerAgents().length; i++) {
                    cfp.addReceiver(bbAgent.getSellerAgents()[i]);
                }

                cfp.setContent(bookTitle);
                cfp.setConversationId("book-trade");
                cfp.setReplyWith("cfp" + System.currentTimeMillis());
                myAgent.send(cfp);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                step = 1;
                break;

            case 1:
                ACLMessage reply = bbAgent.receive(mt);
                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.PROPOSE) {
                        int price = Integer.parseInt(reply.getContent());
                        if (bestSeller == null || price < bestPrice) {
                            bestPrice = price;
                            bestSeller = reply.getSender();
                        }
                    }
                    repliesCount++;
                    if (repliesCount >= bbAgent.getSellerAgents().length) {
                        step = 2;
                    }
                } else {
                    block();
                }
                break;

            case 2:
                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                order.addReceiver(bestSeller);
                order.setContent(bookTitle);
                order.setConversationId("book-trade");
                order.setReplyWith("Orden" + System.currentTimeMillis());
                bbAgent.send(order);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                        MessageTemplate.MatchInReplyTo(order.getReplyWith()));

                step = 3;

                break;

            case 3:
                reply = myAgent.receive(mt);
                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        JOptionPane.showMessageDialog(null, "El libro: [" + bookTitle + "] ha sido comprado con éxito al agente: [" + reply.getSender().getLocalName() + "]" + "\n" + "Precio = " + bestPrice);
                        myAgent.doDelete();
                    } else {
                        JOptionPane.showMessageDialog(null, "Intento fallido: " + "\n" + "El libro solicitado ya ha sido vendido.");
                    }
                    step = 4;
                } else {
                    block();
                }
                break;
        }
    }

    public boolean done() {
        if (step == 2 && bestSeller == null) {
            JOptionPane.showMessageDialog(null, "Intento fallido: " + " El libro [" + bookTitle + "] no está disponible para venta.");
        }
        return ((step == 2 && bestSeller == null) || step == 4);
    }
}
