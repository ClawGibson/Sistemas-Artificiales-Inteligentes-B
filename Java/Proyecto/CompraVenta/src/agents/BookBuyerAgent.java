package agents;

import GUI.Inicio;
import jade.core.Agent;
import behaviours.RequestPerformer;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import javax.swing.JOptionPane;

public class BookBuyerAgent extends Agent {

    Inicio in = new Inicio();
    private String bookTitle;
    private AID[] sellerAgents;
    private int ticker_timer = 10000;
    private BookBuyerAgent this_agent = this;

    protected void setup() {
        in.setVisible(true);
        in.mensajesResultados("Agente comprador [" + getLocalName() + "] listo");
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            bookTitle = (String) args[0];

            addBehaviour(new TickerBehaviour(this, ticker_timer) {
                protected void onTick() {
                    in.mensajesResultados("Intentando comprar el libro: " + bookTitle);

                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        String mensaje = "Se encontraron los siguientes agentes vendedores: ";
                        String agentesVendedores = "";
                        sellerAgents = new AID[result.length];
                        for (int i = 0; i < result.length; i++) {
                            sellerAgents[i] = result[i].getName();
                            agentesVendedores = agentesVendedores + "- " + "[" + sellerAgents[i].getLocalName() + "]";
                        }
                        if (agentesVendedores.length() != 0) {
                            in.mensajesLibros(mensaje + "\n" + agentesVendedores);
                        } else {
                            in.mensajesLibros("Aún no hay vendedores disponibles.");
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    myAgent.addBehaviour(new RequestPerformer(this_agent, in));
                }
            });
        } else {
            JOptionPane.showMessageDialog(in, "No se especificó un libro", "Advertencia", JOptionPane.ERROR_MESSAGE);
            doDelete();
        }
    }

    @Override
    protected void takeDown() {
        String name = getLocalName();
        JOptionPane.showMessageDialog(in, "Agente comprador [" + name + "] finalizado");
        in.mensajesResultados("Agente comprador [" + getLocalName() + "] finalizado");
    }

    public AID[] getSellerAgents() {
        return sellerAgents;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
