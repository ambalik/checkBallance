/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ambal
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class User {

    private NodeList nl;
    private String fio, tp, ue, traf, perTraf, ip;

    public static void main(String args[]) throws IOException, NoSuchAlgorithmException, KeyManagementException, MalformedURLException, ParserException {
        switch (args.length) {
            case 2:
                User u = new User(args[0], args[1]);
                break;
            default:
                System.out.println("Usage: java -jar /path/to/Net.jar login password");
        }
    }

    private User(String login, String password) throws IOException, NoSuchAlgorithmException, KeyManagementException, MalformedURLException, ParserException {
        Parse parse = new Parse(login, password);
        nl = parse.getNl();
        if (init()) {
            run();
        }
    }

    private boolean init() {
        setFio();
        setTp();
        setUe();
        setTraf();
        setPerTraf();
        setIp();
        return true;
    }

    private void setIp() {
        ip = nl.elementAt(3).getChildren().elementAt(3).getChildren().elementAt(5).getChildren().elementAt(11).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(16).getChildren().elementAt(2).getText().trim();
    }

    private void setFio() {
        fio = nl.elementAt(3).getChildren().elementAt(3).getChildren().elementAt(5).getChildren().elementAt(11).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(4).getText().trim();
    }

    private void setPerTraf() {
        try {
            perTraf = nl.elementAt(3).getChildren().elementAt(3).getChildren().elementAt(5).getChildren().elementAt(13).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(18).getChildren().asString();
        } catch (Exception e) {
            perTraf = "";
        }
    }

    private void setTp() {
        tp = nl.elementAt(3).getChildren().elementAt(3).getChildren().elementAt(5).getChildren().elementAt(11).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(9).getText().trim();
    }

    private void setTraf() {
        traf = nl.elementAt(3).getChildren().elementAt(3).getChildren().elementAt(5).getChildren().elementAt(13).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(15).getChildren().asString();
    }

    private void setUe() {
        ue = nl.elementAt(3).getChildren().elementAt(3).getChildren().elementAt(5).getChildren().elementAt(13).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(1).getChildren().elementAt(2).getText().trim();
    }

    private void run() throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"notify-send", "-u", "critical", "-c", "info", "-i", "/usr/share/icons/hicolor/128x128/mimetypes/libreoffice-oasis-database.png", "-h", "STRING:NAME:", "Здравствуйте, " + fio, "Тарифный план: " + tp + "\n" + "На счету осталось: " + ue + "\n" + "Осталось трафика: " + traf + "\n" + "Переходящего трафика осталось: " + perTraf + "\n" + ip});
        System.out.println("Здравствуйте, " + fio + "\n" + "Тарифный план: " + tp + "\n" + "На счету осталось: " + ue + "\n" + "Осталось трафика: " + traf + "\n" + "Переходящего трафика осталось: " + perTraf + "\n" + ip);
    }
}
