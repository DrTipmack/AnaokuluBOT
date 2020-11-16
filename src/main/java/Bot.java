import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Bot extends ListenerAdapter {
    private FileManager fm;
    public String prefix = "!";

    public static void main(String[] args) throws LoginException {

        try {
            JDA jda = JDABuilder
                    .createDefault(FileManager.getLine("token.txt"))
                    .addEventListeners(new Bot()) //adds an Event listener to the JDA object. This means we can get events such as messages, new users etc.
                    .setActivity(Activity.watching("you")) //Sets the Activity to "Watching you"
                    .setStatus(OnlineStatus.ONLINE) //Sets the status to "Online"
                    .build(); //Builds the object with the set params
            jda.awaitReady(); //Blocking guarantees that JDA will be completely loaded.
            System.out.println("Bot is online");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override //Override makes sure we override the onMessageReceived class from the parent extended class: Listener Adapter
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        JDA jda = event.getJDA(); //When an event is received, it comes with the JDA object set in main(). We can use this in the future
        long responseNumber = event.getResponseNumber(); //The amount of discord events that JDA has received since the last reconnect.

        User author = event.getAuthor(); //The author of the message (eg. the user)
        Message message = event.getMessage(); //The message
        MessageChannel channel = event.getChannel(); //The message channel that the message was sent through.
                                                     //Could be a TextChannel (eg #general), a PrivateChannel (DM), or a Group (Group DM)
        String msg = message.getContentDisplay(); //Gets a human readable text content of the message
        boolean bot = author.isBot(); //Checks if the Sender is a BOT or a human

        if (event .isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild(); //The Guild (Server) that this message was sent in.
            TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
            Member member = event.getMember(); //This Member that sent the message. Contains Guild specific information about the User!

            String name;

            if (message.isWebhookMessage())
                name = author.getName();    //If this is a Webhook message, then there is no Member associated
                                            // with the User, thus we default to the author for name.
            else {
                assert member != null;
                name = member.getEffectiveName(); //Uses nickname if member has one
            }

            try {
                fm  = new FileManager(guild.getName() + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (msg.startsWith(prefix) || !bot) {
                String[] messageIn = msg.split("\n");
                String command = messageIn[0].substring(1);
                System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
//                message.addReaction("❤️").queue();

                String answer = "This command doesn't exist";
                switch (command) {
                    case "pingserver":
                        String status;
                        if (pingHost("google.com", 8884, 20000))
                            answer = "ONLINE";
                        else
                            answer = "doesnt work :(";
                    break;

                    case "add":
                        StringBuilder addString = new StringBuilder();
                        for(String i : messageIn) {
                            addString.append(i);
                        }
                        try {
                            fm.add(addString + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                channel.sendMessage(answer).queue();
            }
        }
    }

    public static boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}

