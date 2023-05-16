package es.practica.tweets;

import java.util.HashMap;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelBot extends TelegramLongPollingBot{

	public static HashMap<Long,Object> pending;

	public TelBot() {
		pending = new HashMap<Long, Object>();
	}

	@Override
	public String getBotUsername() {
		return "traductor_practicabot";
	}

	@Override
	public String getBotToken() {
		return "6180517265:AAGG8024cNDFOVoGjoo4Vewl1dvgnKHGTRI";
	}


	public void sendMessageToUser(long id, String text) {
		try {
			Long chatid = 1167263631L;
			SendMessage message = new SendMessage((Long.toString(chatid)), text);
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		
	}



}
