package es.practica.tweets;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;

public class AgentesMetodos {
	//register agent to DF
	public void agentRegister(Agent agent, String servicio){

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(agent.getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType(servicio);
		sd.setName(servicio);

		dfd.addServices(sd);

		try{

			DFService.register(agent,dfd);

		}catch(FIPAException ex){
			System.err.println("AGENTE "+servicio+"NO REGISTRADO.\n[MENSAJE DE ERROR]: \n"+ex.getMessage());
			agent.doDelete(); 
		}
		System.out.println("AGENTE:["+servicio+"] REGISTRADO");
	}
	
	//Look for X agent
	@SuppressWarnings("deprecation")
	public DFAgentDescription agentLookUp(Agent agent, String service) {

		DFAgentDescription agentDFD = null;
		
		//search variables
		DFAgentDescription template=new DFAgentDescription();
		ServiceDescription templateSd=new ServiceDescription();

		//search values
		templateSd.setType(service); 
		template.addServices(templateSd);

		//search restrictions
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(new Long(1));
		
		try{
			
			//run search
			DFAgentDescription [] results = DFService.search(agent, template, sc);
			
			//check search results
			if (results.length > 0) {
				
				Boolean found = false;
				for (int i = 0; i < results.length && !found; ++i) {

					//get DFD from agent
					DFAgentDescription dfd = results[i];
						
					//get agent service
					Iterator it = dfd.getAllServices();

					//check if it is the service 
					while (it.hasNext() && !found){
						
						ServiceDescription sd = (ServiceDescription) it.next();

						if (sd.getType().equals(service)){
							agentDFD = dfd;
							found = true;
						}
					}
				}
			}

		}catch(FIPAException e){
			e.printStackTrace();
		}
		
		return agentDFD;
	}
	
	//send acl message to agent "agentRecNickName"
	public void sendResultToAgent(Agent agent, String agentRecNickName, String data) {
		
		DFAgentDescription receiverDFD = this.agentLookUp(agent, agentRecNickName);
		ACLMessage aclMsg = new ACLMessage(ACLMessage.REQUEST);
		
		aclMsg.setSender(agent.getAID());
		
		AID idReceiver = new AID(receiverDFD.getName().getName(), AID.ISGUID);
		
		aclMsg.addReceiver(idReceiver);
		
		aclMsg.setContent(data);
		
		agent.send(aclMsg);
		
	}
	
}
