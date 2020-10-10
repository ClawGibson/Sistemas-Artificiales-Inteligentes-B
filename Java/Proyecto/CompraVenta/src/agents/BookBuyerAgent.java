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

    public void setNombre(String name) {
        this_agent.getAMS().setLocalName(name);
        //this_agent.getAID().setName(name);
    }

    protected void setup() {
        in.resultados.setText("Agente comprador [" + getLocalName() + "] listo");
        //System.out.println("Agente comprador [" + getLocalName() + "] listo");

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            bookTitle = (String) args[0];
            in.libros.setText("Libro: " + bookTitle);
            //System.out.println("Libro: " + bookTitle);

            addBehaviour(new TickerBehaviour(this, ticker_timer) {
                protected void onTick() {
                    in.resultados.setText("Intentando comprar el libro: " + bookTitle);
                    //System.out.println("Intentando comprar el libro: " + bookTitle);

                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        in.libros.setText("Se encontraron los siguientes agentes vendedores: ");
                        String aux = in.libros.getText();
                        String agentesVendedores = "";
                        //System.out.println("Se encontraron los siguientes agentes vendedores:");
                        sellerAgents = new AID[result.length];
                        for (int i = 0; i < result.length; i++) {
                            sellerAgents[i] = result[i].getName();
                            agentesVendedores = agentesVendedores + " | " + "[" + sellerAgents[i].getLocalName() + "]";
                            //System.out.println("[" + sellerAgents[i].getLocalName() + "]");
                        }
                        in.libros.setText(aux + "\n" + agentesVendedores);
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    myAgent.addBehaviour(new RequestPerformer(this_agent));
                }
            });
        } else {
            JOptionPane.showMessageDialog(in, "No se especificó un libro", "Advertencia", JOptionPane.ERROR_MESSAGE);
            //System.out.println("No se especificó un libro.");
            doDelete();
        }
    }

    protected void takeDown() {
        in.resultados.setText("Agente comprador [" + getLocalName() + "] finalizado");
        //System.out.println("Agente comprador [" + getLocalName() + "] finalizado");
    }

    public AID[] getSellerAgents() {
        return sellerAgents;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
