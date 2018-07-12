import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.06 13:43.
 */
public class TestAuthClient {
    private static final String port = "9081";
    private static final String serverIp = "192.168.1.110";

    public static void main(String[] args) {
        testgetAuthList();
    }

    private static void testgetAuthList() {
        int i = 1;
        int startIndex = 0,pageSize = 40;

        while( i !=0) {
            String msg = "{\"carrierIata\":\"ZH\",\"MsgTm\":\"2017-01-25 03:26:03\",\"startIndex\":\"0\",\"pageSize\":\"0\"}";
            String msg1 = clientSend("http://" + serverIp + ":" + port + "/services/charging/getAuthList.json", msg, MediaType.APPLICATION_JSON_TYPE);

            startIndex += pageSize;
            i = 0;
        }

    }



    private static String clientSend(String url, String msg, MediaType mediaType) {
        System.out.println("Registering user via " + url);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Entity<String> entity = Entity.entity(msg, mediaType);
        Response response = target.request().post(Entity.entity(msg, mediaType));
        String s = null;
        try {
            if (response.getStatus() != 200 && response.getStatus() != 204) {
                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
            }

            s = response.readEntity(String.class);
            System.err.println("============: " + s);
        } finally {
            response.close();
            client.close();
        }
        return s;
    }
}

