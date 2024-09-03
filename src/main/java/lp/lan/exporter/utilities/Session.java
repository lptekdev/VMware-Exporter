/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import com.vmware.vim25.InvalidLocaleFaultMsg;
import com.vmware.vim25.InvalidLoginFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VimService;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author luisr
 */
public class Session {

    public Session() {

    }

    private String username;
    private String password;
    private String vimSdkUrl;
    private VimService vimService;
    private VimPortType vimPort;
    private ServiceContent serviceContent;
    private boolean login ; 

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getvimSdkUrl() {
        return vimSdkUrl;
    }

    public VimService getVimService() {
        return vimService;
    }

    public VimPortType getVimPort() {
        return vimPort;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setvimSdkUrl(String url) {
        this.vimSdkUrl = url;
    }

    public void setVimService(VimService vimService) {
        this.vimService = vimService;
    }

    public void setVimPort(VimPortType vimPort) {
        this.vimPort = vimPort;
    }

    public boolean login() {
        login = false;
        ManagedObjectReference SVC_INST_REF = new ManagedObjectReference();

        // DISABLE SSL VERIFICATION //
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    login = true;
                    return true;
                    
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();                      
        }
        // END DISABLE SSL VERIFICATION //

        SVC_INST_REF.setType("ServiceInstance");
        SVC_INST_REF.setValue("ServiceInstance");

        try {
            this.vimService = new VimService();
            this.vimPort = vimService.getVimPort();
            Map<String, Object> ctxt = ((BindingProvider) vimPort).getRequestContext();

            ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, vimSdkUrl);
            ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

            // Retrieve the ServiceContent object and login
            this.serviceContent = vimPort.retrieveServiceContent(SVC_INST_REF);
            vimPort.login(serviceContent.getSessionManager(), username, password, null);
            login = true;
        } catch (InvalidLocaleFaultMsg | InvalidLoginFaultMsg | RuntimeFaultFaultMsg e) {
            e.printStackTrace();
            //return login;
        }
        return login;
    }

    public ServiceContent getServiceContent() {
        return serviceContent;
    }

}