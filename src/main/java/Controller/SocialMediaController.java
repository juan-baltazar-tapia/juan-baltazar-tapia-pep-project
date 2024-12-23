package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Message;
import Model.Account;
import Service.MessageService;
import Service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController (){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/messages", this::postMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{id}", this::getMessage);
        app.delete("messages/{messageId}", this::deleteMessage);
        app.get("/accounts/{accountId}/messages", this::getAllMessagesByAccountId);
        app.patch("/messages/{id}", this::updateMessage);
        app.post("/login", this::login);
        //POST localhost:8080/register
        app.post("/register", this::register);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    private void login(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account accountInfo = mapper.readValue(ctx.body(), Account.class);
        Account result = accountService.login(accountInfo);
        if (result != null) {
            ctx.json(mapper.writeValueAsString(result));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    private void register(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account accountInfo = mapper.readValue(ctx.body(), Account.class);
        Account result = accountService.register(accountInfo);
        if (result != null) {
            ctx.json(mapper.writeValueAsString(result));
            ctx.status(200);
        } else {
            ctx.status(400);
        }

    }

    private void postMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
            if(addedMessage!=null){
                ctx.json(mapper.writeValueAsString(addedMessage));
                ctx.status(200);
            }else{
                ctx.status(400);
            }
    }

    private void updateMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message updatedMessage = messageService.updateMessage(id, message);
            if(updatedMessage!=null){
                ctx.json(updatedMessage);
                ctx.status(200);
            }else{
                ctx.status(400);
            }
    }

    private void getAllMessages(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessage(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message message = messageService.getMessage(id);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
        }
    }

    private void getAllMessagesByAccountId(Context ctx) throws JsonProcessingException {
        int accountId = Integer.parseInt(ctx.pathParam("accountId"));
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        ctx.json(messages);
    }

    private void deleteMessage(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("messageId"));
        Message deletedMessage = messageService.deleteMessageById(messageId);
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
            ctx.status(200);
        } else {
            ctx.status(200);
        }
    }

}