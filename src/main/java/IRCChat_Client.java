import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class IRCChat_Client {
    static String server = "irc.freenode.net";

    static String nick = "MINO0885_JavaBot";

    static String login = "MINO_MAC";


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
                System.err.println(e.getMessage());
                login(writer);
            }

        }).start();

        login(writer);
        System.err.println("================ connect " + server + " success ================");
        Scanner sysScanner = new Scanner(System.in);
        while (true) {
            System.err.println(" type <start> to the menu ");
            String comd = sysScanner.nextLine();
            if (!"start".equals(comd)) {
                System.err.println("error input , type <start> to the menu");
                comd = "";
                continue;
            }
            comd = "";
            System.err.println("================ <group> for group chat ================");
            System.err.println("================ <user> for user chat ================");
            System.err.println("================ <:exitsys> for exit the system ================");
            System.err.println("================ else to send commd to the IRC server ================");
            comd = sysScanner.nextLine();
            switch (comd) {
                case "group":
                    System.err.println("================ channelname: ");
                    String channelname = sysScanner.nextLine();
                    join(writer, "#" + channelname);
                    sendGroupMsg(writer, "#" + channelname);
                    comd = "";
                    break;
                case "user":
                    System.err.println("================ nikename: ");
                    String nickname = sysScanner.nextLine();
                    sendU2Msg(writer, nickname);
                    comd = "";
                    break;
                case ":exitsys":
                    System.exit(0);
                default:
                    writeCommd(writer, comd);
                    comd = "";
                    break;
            }
        }
    }

    private static void writeCommd(BufferedWriter writer, String commd) {
        try {
            System.err.println("================ start commd mode ================");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if ("".equals(commd)) {
                    commd = scanner.nextLine();
                }
                if (":exit".equals(commd)) {
                    System.err.println("================ exit commd mode ================");
                    break;
                }
                writer.write(commd + "\r\n");
                writer.flush();
                commd = "";
            }
        } catch (Exception e) {
            System.err.println("write commd error");
        }
    }

    private static void sendU2Msg(BufferedWriter writer, String nickname) {
        try {
            System.err.println("================ send msg to " + nickname);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String u2Msg = scanner.nextLine();
                if (":exit".equals(u2Msg)) {
                    break;
                }
                writer.write("PRIVMSG " + nickname + " :" + u2Msg + "\r\n");
                writer.flush();
            }
        } catch (Exception e) {
            System.err.println("================ sendU2Msg error " + e.getMessage());
            login(writer);

        }
    }

    public static void join(BufferedWriter writer, String channelname) {
        try {
            writer.write("JOIN " + channelname + "\r\n");
            writer.write("PRIVMSG " + channelname + " :I got pinged!\r\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("================ join error " + e.getMessage());
            login(writer);
        }
    }

    public static void login(BufferedWriter writer) {
        try {
            writer.write("NICK " + nick + "\r\n");
            writer.write("USER " + login + " MINOPC * : MINO0885\r\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("================ login error " + e.getMessage());
            System.exit(0);
        }
    }

    public static void sendGroupMsg(BufferedWriter writer, String channelname) {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String groupMsg = scanner.nextLine();
                if (":exit".equals(groupMsg)) {
                    writer.write("PART " + channelname + " be right back ~ ~\r\n");
                    writer.flush();
                    System.err.println("================ group chat end ");
                    break;
                }
                writer.write("PRIVMSG " + channelname + " :" + groupMsg + "\r\n");
                writer.flush();
            }
        } catch (Exception e) {
            System.err.println("================ sendGroupMsg error " + e.getMessage());
            login(writer);
        }
    }
}