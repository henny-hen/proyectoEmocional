package es.practica.tweets;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgenteVisualizador extends Agent{
	
	public static final String NICKNAME = "AgenteVisualizador";
	
	private static final long serialVersionUID = 1L;
	private TelBot tb = new TelBot();
	
	private ACLMessage recMsg = null;
	private AgentesMetodos agentManager = null;
	
	protected void setup(){
		super.setup();
		
		this.agentManager = new AgentesMetodos();
		this.agentManager.agentRegister(this, NICKNAME);
		
		try {
			final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new TelBot());
			
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

		addBehaviour(new Process());
	}
	
	
	public class Process extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public void action() {
			
			recMsg = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));	

			if(recMsg != null) {
				String[] inputArgument = recMsg.getContent().split("___");
				long id = -758769403;
				for (int i = 0; i < inputArgument.length; i++) {
					String text = inputArgument[i];
					tb.sendMessageToUser(id, text);
				}
			}

		}
		
	}
}
