package agents;

import jade.core.Agent;
import behaviours.RequestPerformer;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BookBuyerAgent extends Agent {

    private String bookTitle;
    private AID[] sellerAgents;
    private int ticker_timer = 10000;
    private BookBuyerAgent this_agent = this;

    protected void setup() {
        System.out.println("Agente comprador [" + getLocalName() + "] listo");

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            bookTitle = (String) args[0];
            System.out.println("Libro: " + bookTitle);

            addBehaviour(new TickerBehaviour(this, ticker_timer) {
                protected void onTick() {
                    System.out.println("Intentando comprar el libro: " + bookTitle);

                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Se encontraron los siguientes agentes vendedores:");
                        sellerAgents = new AID[result.length];
                        for (int i = 0; i < result.length; i++) {
                            sellerAgents[i] = result[i].getName();
                            System.out.println("[" + sellerAgents[i].getLocalName() + "]");
                        }

                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    myAgent.addBehaviour(new RequestPerformer(this_agent));
                }
            });
        } else {
            System.out.println("No se especificó un libro.");
            doDelete();
        }
    }

    protected void takeDown() {
        System.out.println("Agente comprador [" + getLocalName() + "] finalizado");
    }

    public AID[] getSellerAgents() {
        return sellerAgents;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
