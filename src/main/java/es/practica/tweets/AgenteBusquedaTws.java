package es.practica.tweets;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.util.ArrayList;


public class AgenteBusquedaTws extends Agent {
	
	public static final String NICKNAME = "AgenteBusquedaTws";
	private static final long serialVersionUID = 1L;
	
	private static final String CONSUMER_KEY = "7XsFLHVr8uVhffSVcBBaWWUmm";
	private static final String CONSUMER_SECRET = "AziRelj7QYxRv5yOKYFLtHWr3gDowDctFMW8wdhzRF8wplFRlD";
	private static final String ACCESS_TOKEN ="2433519700-DR7wtTCoSMfKW9MWUCr2ycexIebRq2rppdoElwv";
	private static final String ACCESS_TOKEN_SECRET = "n6l0Gb3s3BkZqElQVPLhyD7f6IaJvzX8mqNSByw4V26SP";
	
	private boolean run = false;
	private int numberOfTweets = 1;
	private String hashTag = new String();
	private AgentesMetodos agentManager = null;
	
	ArrayList<String> plainTweets;
	
	//setup agent
	protected void setup() {
		super.setup();
		this.agentManager = new AgentesMetodos();
		//register new agent
		this.agentManager.agentRegister(this, NICKNAME);
		//set search
		this.addBehaviour(new Search());
		
	}
	
	private class Search extends CyclicBehaviour{

		private static final long serialVersionUID = 1L;
	
		public void reset() {
			super.reset();
		}
		
		@Override
		public void action() {
			
			if(run) {
			Interfaz guiData = AgentMaster.window; 
			plainTweets = new ArrayList<String>();
			hashTag = guiData.getHashTag();
			
			numberOfTweets = Integer.parseInt(guiData.getnTweets());

			TweetRecopiler();
			agentManager.sendResultToAgent(myAgent, "AgenteAnalizador", listToString(plainTweets));
			
			}
			
			run = true;
			doSuspend();
			
		}
		
		//get tweets
		public void TweetRecopiler() {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			
			cb.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET)
			.setOAuthAccessToken(ACCESS_TOKEN).setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
			
			TwitterFactory tweetFactory = new TwitterFactory(cb.build());
			
			Twitter twitter = tweetFactory.getInstance();
			
			Query query = new Query();
			
			query.setLang("es");
			query.setQuery(hashTag);
			
			getTweets(query,twitter);
			
		}
		
		//fetch tweets
		private void getTweets(Query query, Twitter twitter) {
			
			try {
				QueryResult searchResult = twitter.search(query);
				
				for(int i = 0; i < numberOfTweets || searchResult.getTweets().get(i) == null; i ++) {
					Status status = searchResult.getTweets().get(i);
					plainTweets.add(status.getText());
				}
			}
			catch(TwitterException e) {
				System.err.println("ERROR CONEXION CON TWITTER: " + e);
			}
			catch(Exception e) {
				System.err.println(e);
			}
		}
		
		//list to string formatter, separator "___"
		private String listToString(ArrayList<String> strList) {
			String result = "";
			for(int i = 0; i < strList.size(); i++) {
				result+= strList.get(i);
				if(i != strList.size()-1) {
					result+= "___";
				}
			}
			
			
			return result;
		}
	
	}
	
}

