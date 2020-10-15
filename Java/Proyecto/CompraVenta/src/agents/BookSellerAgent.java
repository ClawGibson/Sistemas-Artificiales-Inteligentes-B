package agents;

import GUI.Inicio;
import java.util.Hashtable;

import behaviours.OfferRequestServer;
import behaviours.PurchaseOrderServer;
import gui.BookSellerGui;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BookSellerAgent extends Agent {

    private Hashtable catalogue;
    private BookSellerGui gui;
    Inicio in;

    public void setInicio(Inicio in) {
        this.in = in;
    }

    protected void setup() {
        catalogue = new Hashtable();

        gui = new BookSellerGui(this, in);
        gui.showGui();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("book-selling");
        sd.setName("book-trading");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new OfferRequestServer(this));

        addBehaviour(new PurchaseOrderServer(this, in));
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        gui.dispose();
        String name = getLocalName();
        in.mensajesResultados("Agente vendedor[" + name + "] finalizado");
    }

    public void updateCatalogue(final String title, final int price) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                catalogue.put(title, price);
                String titulo = title;
                int precio = price;
                in.mensajesLibros("Libro: [" + titulo + "] insertado con el precio de:  $" + precio);
            }
        });
    }

    public Hashtable getCatalogue() {
        return catalogue;
    }
}
