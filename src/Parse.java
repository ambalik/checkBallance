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
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class Parse {
    
    private final String AUTH_URL = "https://sydney.sarkor.com/cbuser/auth.jsp", LOGIN = "login", PASSWORD = "password", SET_COOKIE = "set-cookie", COOKIE = "Cookie", LOCATION = "location";
    private HttpsURLConnection huc;
    private NodeList nl;
    
    public NodeList getNl() {
        return nl;
    }
    
    protected Parse(String login, String password) throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException, IOException, ParserException {
        run(login, password);
    }
    
    private void connect(String login, String password) throws MalformedURLException, IOException {
        HttpClient hc = new HttpClient();
        PostMethod pm = new PostMethod(AUTH_URL);
        NameValuePair[] nvp = {new NameValuePair(LOGIN, login), new NameValuePair(PASSWORD, password)};
        pm.setRequestBody(nvp);
        hc.executeMethod(pm);
        if (pm.getStatusCode() == HttpStatus.SC_OK) {
            System.err.println("Логин или пароль неверен " + pm.getResponseBodyAsString());
        } else if (pm.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY || pm.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY) {
            huc = (HttpsURLConnection) new URL(pm.getResponseHeader(LOCATION).getValue()).openConnection();
            huc.addRequestProperty(COOKIE, pm.getResponseHeader(SET_COOKIE).getValue());
            huc.setHostnameVerifier(new HostnameVerifier() {
                
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        }
    }
    
    private void run(String login, String password) throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException, IOException, ParserException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);
        connect(login, password);
        Parser p = new Parser(huc);
        nl = p.parse(null);
    }
    
    private static class DefaultTrustManager implements X509TrustManager {
        
        public DefaultTrustManager() {
        }
        
        @Override
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            //throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            //  throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
