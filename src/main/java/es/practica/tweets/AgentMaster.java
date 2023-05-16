package es.practica.tweets;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class AgentMaster extends Agent {


	private static final long serialVersionUID = 1L;
	protected OneShotBehaviour comportamiento;
    public static AgentController agBusqueda;
    public static AgentController agAnalizer;
    public static AgentController agTelegram;
    public static Interfaz window;

    public void setup() {
        System.out.println("- [AGENTE MAESTRO]: GENERADO -");
        comportamiento = new OneShotBehaviour(this) {

        	
			private static final long serialVersionUID = 1L;

			public void action() {
                //Get the JADE runtime interface (singleton)
                jade.core.Runtime runtime = jade.core.Runtime.instance();
               
                //Create a Profile, where the launch arguments are stored
                Profile profile = new ProfileImpl();
                profile.setParameter(Profile.CONTAINER_NAME, "Red_Agentes");
                profile.setParameter(Profile.MAIN_HOST, "localhost");
               
                //Create a non-main agent container
                ContainerController container = runtime.createAgentContainer(profile);
                try {
                   agBusqueda = container.createNewAgent("AgenteBusquedaTws","es.practica.tweets.AgenteBusquedaTws",new Object[]{});//arguments
                    
                   agAnalizer = container.createNewAgent("AgenteAnalizador","es.practica.tweets.AgenteAnalizador",new Object[]{});//arguments
                   
                   agTelegram = container.createNewAgent("AgenteVisualizador","es.practica.tweets.AgenteVisualizador",new Object[]{});//arguments

                    agBusqueda.start();
                    agAnalizer.start();
                    agTelegram.start();
                    
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                        	try {
                        		window = new Interfaz();
            					window.frameanazalizador.setVisible(true);
            					
            				} catch (Exception e) {
            					e.printStackTrace();
            				}
                        }
                    });

                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        };
        addBehaviour(comportamiento);
        

    }
    
     
}