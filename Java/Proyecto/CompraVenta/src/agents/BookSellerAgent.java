package agents;

import GUI.Inicio;
import GUI.NuevoVendedor;
import java.util.Hashtable;

import behaviours.OfferRequestServer;
import behaviours.PurchaseOrderServer;
import gui.BookSellerGui;
import jade.core.AID;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BookSellerAgent extends Agent {

    private Hashtable catalogue;
    private BookSellerGui gui;
    private NuevoVendedor nv;
    Inicio in = new Inicio();
    private AID name;

    public void setName(String nombre) {
        name = new AID(nombre);
    }

    public String getNameAgent() {
        return name.getLocalName();
    }

    public AID getAgent() {
        return name;
    }

    protected void setup() {
        catalogue = new Hashtable();

        gui = new BookSellerGui(getAgent());
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

        addBehaviour(new PurchaseOrderServer(this));
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        gui.dispose();
        in.resultados.setText("Agente vendedor[" + getLocalName() + "] finalizado");
        //System.out.println("Agente vendedor[" + getLocalName() + "] finalizado");
    }

    public void updateCatalogue(final String title, final int price) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                catalogue.put(title, price);
                in.libros.setText("Libro: [" + title + "] insertado con el precio de:  $" + price);
                //System.out.println("Libro: [" + title + "] insertado con el precio de:  $" + price);
            }
        });
    }

    public Hashtable getCatalogue() {
        return catalogue;
    }
}
