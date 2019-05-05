package ua.edu.ukma.fi.atsaruk.chatbot.service;

public interface MessageSender {

  String sendMessage(long chatId, String message);
}
