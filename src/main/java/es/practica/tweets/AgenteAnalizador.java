package es.practica.tweets;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

// AGENTE ENCARGADO DEL ANALISIS DE SENTIMIENTOS.
public class AgenteAnalizador extends Agent{
	
	

	private	String key = "AIzaSyBJ6i8JdjP7fHfJGLPU5AVf1tDYQEZMMHg";

	private static boolean MAKE_IT_RAIN = true;
	
	private String [] listaaTraducir; 
	private String [] textoTraducidoLista; 
	
	private ListaSentimientos[] originalTxtSentimentsNodes; 
	private ListaSentimientos[] translatedTxtSentimentsNodes; 
	
	private String analysisResult; 
	public static final String NICKNAME = "AgenteAnalizador"; 
	private static final long serialVersionUID = 1L;
	private AgentesMetodos agentManager = null;
	
	
	protected void setup() {
		
		super.setup();
		//register agent
		this.agentManager = new AgentesMetodos();
		this.agentManager.agentRegister(this, NICKNAME);
		
		addBehaviour(new TranslatorBehaivour());
		
	}

	
	private void analyzeResults() {
		
		double acumScore = 0, acumScoreTranslated = 0; 

		for (int i = 0;  i < listaaTraducir.length; i++) {
			acumScore = (double)(acumScore + originalTxtSentimentsNodes[i].getScore()*1);
			acumScoreTranslated = (double)(acumScoreTranslated + translatedTxtSentimentsNodes[i].getScore()*1);
			
		}
		
		analysisResult += "Efectividad del sistema : "  + String.valueOf( Math.abs(acumScore - acumScoreTranslated)) + "\n" 
											+ "Acumulado  Original = " + String.valueOf(acumScore) + "\n" 
											+ "Acumulado Traducido = " + String.valueOf(acumScoreTranslated) + "\n";
		
		for(int i=0; i<listaaTraducir.length;i++ )
			analysisResult += "\n" + "Tweet #" + Integer.toString(i)  + "\n" 
					+ "L.Original = " + String.valueOf(originalTxtSentimentsNodes[i].getScore()*1) + "\n" 
					+ "L.Traducido = " + String.valueOf(translatedTxtSentimentsNodes[i].getScore()*1) + "\n" 
					+ "Efectividad local = " + String.valueOf(Math.abs(translatedTxtSentimentsNodes[i].getScore()-originalTxtSentimentsNodes[i].getScore())*1) + "\n" 
					+ listaaTraducir[i] + "\n--\n";
			
	}
	
	private class TranslatorBehaivour extends OneShotBehaviour {

		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			
			String plainTranslatedTxt;
			Translate translate;
			Translation translation;
			
			ACLMessage recvMsg = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
			
			listaaTraducir = ((String)recvMsg.getContent()).split("___");
			textoTraducidoLista = new String[listaaTraducir.length];
			
			String translateTo = "en";
	        
			if (MAKE_IT_RAIN) {
			    for (int i = 0; i < listaaTraducir.length; i++) {
	
					translate = TranslateOptions.newBuilder().setApiKey(key).build().getService();
					translation = translate.translate(listaaTraducir[i], TranslateOption.targetLanguage(translateTo));
					plainTranslatedTxt = translation.getTranslatedText();
	
					textoTraducidoLista[i] = plainTranslatedTxt;
			    }
			}else{
				System.out.println("DUMMY: TRADUCCION TWEETS");
			}
		
		addBehaviour(new AnalyzerBehaivour());	
			
		}
		
	}


	private class AnalyzerBehaivour extends OneShotBehaviour {


		private static final long serialVersionUID = 1L;

		public void action() {

			analysisResult = new String();
			originalTxtSentimentsNodes = new ListaSentimientos[listaaTraducir.length];
			translatedTxtSentimentsNodes = new ListaSentimientos[listaaTraducir.length];
		
			ListaSentimientos node = new ListaSentimientos();
			
			for (int i = 0; i < listaaTraducir.length; i++) {
		      
				
		      if (MAKE_IT_RAIN) {
		    	  try (LanguageServiceClient language = LanguageServiceClient.create()) {
					
				      Document doc = Document.newBuilder().setContent(listaaTraducir[i].toString()).setType(Type.PLAIN_TEXT).build();
				      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
				      
				      node = new ListaSentimientos();
				      node.setScore(sentiment.getScore());
				      node.setMagnitude(sentiment.getMagnitude());
					  
				      originalTxtSentimentsNodes[i] = node;
				      
				      Document doc_translated = Document.newBuilder().setContent(textoTraducidoLista[i].toString()).setType(Type.PLAIN_TEXT).build();
				      Sentiment sentiment_translated = language.analyzeSentiment(doc_translated).getDocumentSentiment();
				      
				      node = new ListaSentimientos();
				      node.setScore(sentiment_translated.getScore());
				      node.setMagnitude(sentiment_translated.getMagnitude());
				      
				      translatedTxtSentimentsNodes[i] = node;
			      
					} catch (IOException e) {
						System.out.println("[ERROR]: CALCULO SENTIMIENTOS: TEXTO ORIGINAL");
						e.printStackTrace();
					}
		      }else{
		    	  System.out.print("DUMMY: ANALIZANDO TWEET: " + textoTraducidoLista[i].toString());
		      }
		      
			}
			
			analyzeResults();
			agentManager.sendResultToAgent(myAgent,  "AgenteVisualizador", analysisResult);
			addBehaviour(new TranslatorBehaivour());	
		}
		
	}
	
}
