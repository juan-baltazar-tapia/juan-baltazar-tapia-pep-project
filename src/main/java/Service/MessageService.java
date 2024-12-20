package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;


public class MessageService {
   private MessageDAO MessageDAO;
  
   public MessageService(){
       MessageDAO = new MessageDAO();
   }
   /**
    * Constructor for a MessageService when a MessageDAO is provided.
    * This is used for when a mock MessageDAO that exhibits mock behavior is used in the test cases.
    * This would allow the testing of MessageService independently of MessageDAO.
    * There is no need to modify this constructor.
    * @param MessageDAO
    */
   public MessageService(MessageDAO MessageDAO){
    this.MessageDAO = MessageDAO;
   }
  
   public Message addMessage(Message message) {
    return MessageDAO.insertMessage(message);
   }

   public List<Message> getAllMessages() {
    return MessageDAO.getAllMessages();
   }
 
}

