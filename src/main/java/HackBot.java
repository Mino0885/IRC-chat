import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class HackBot {
    static String server = "irc.freenode.net";

    static String nick = "MINOadfad";

    static String login = "MINO_MAC";

    static String channel = "#mini123";

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(server, 6667);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //输出线程
        new Thread(() -> {
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }).start();
        // Log on to the server.
        login(writer);
        join(writer);
        sendGroupMsg(writer);
        Scanner sysScanner = new Scanner(System.in);
        String comd = sysScanner.nextLine();
        switch (comd) {
            case "group":
                sendGroupMsg(writer);
                break;
            case "user":
                System.out.println("nikename: ");
                String nickname = sysScanner.nextLine();
                sendU2Msg(writer, nickname);
                break;
            case ":exitsys":
                System.exit(0);
        }
    }

    private static void sendU2Msg(BufferedWriter writer, String nickname) {
        try {
            System.out.println("send msg to " + nickname);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String u2Msg = scanner.nextLine();
                if (":exit".equals(u2Msg)) {
                    return;
                }
                writer.write("PRIVMSG " + nickname + " :" + u2Msg + "\r\n");
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("sendU2Msg error" + e.getMessage());
        }
    }

    public static void join(BufferedWriter writer) {
        try {
            writer.write("JOIN " + channel + "\r\n");
            writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("join error" + e.getMessage());
            System.exit(0);
        }
    }

    public static void login(BufferedWriter writer) {
        try {
            writer.write("NICK " + nick + "\r\n");
            writer.write("USER " + login + " 8 * : Java IRC Hacks Bot\r\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("login error" + e.getMessage());
            System.exit(0);
        }
    }

    public static void sendGroupMsg(BufferedWriter writer) {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String groupMsg = scanner.nextLine();
                if (":exit".equals(groupMsg)) {
                    System.out.println("group chat end");
                    return;
                }
                writer.write("PRIVMSG " + channel + " :" + groupMsg + "\r\n");
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("sendGroupMsg error" + e.getMessage());
        }
    }
}