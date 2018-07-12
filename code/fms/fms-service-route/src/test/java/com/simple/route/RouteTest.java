package com.simple.route;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * upLinkMsgStateNoticeExc,rejectMsgNoticeExc,upLinkMsgStateNotice,downLinkMsgNoticeExc,rejectMsgNotice,downLinkMsgNotice
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.07 15:40.
 */
public class RouteTest {
    private static final String port = "9091";
    private static final String serverIp = "192.168.1.110";
/*
    private static final String port = "9091";
    private static final String serverIp = "192.168.1.110";
*/

    public static void main(String[] args) {

        testHadAuth();
        //testNotice();
         //testNotices();
        //testPwi();
        // testFpn();
    }

    //测试无授权服务
    private static void testNoAuth() {
        String msg = "{\"Channel\":\"ACARS2\",\"MsgTm\":\"2017-01-25 03:26:03\",\"MsgType\":\"REQFPN\",\"OrigMsg\":\"2017-01-25 03:26:03\\n QU SHAQPHO\\n.BJSXCXA 250326\\n FML\\nFI HO1179/AN B-8407\\nDT BJS HRB 250326 F11A\\n-  REQFPN1493\",\"content\":{\"AcrNo\":\"B-8407\"}}";
        sendMsg("http://"+serverIp+":" + port + "/services/analyze/downLinkMsgNotice.json",msg, MediaType.APPLICATION_JSON_TYPE);
    }

    private static void testHadAuth() {
        String msg = "{\"AcReg\":\"B6571\",\"Channel\":\"ACARS2\",\"MsgTm\":\"2017-03-27 17:40:03\",\"MsgType\":\"REQFPN\",\"OrigMsg\":\"2017-01-25 03:26:03\\n QU SHAQPHO\\n.BJSXCXA 250326\\n FML\\nFI HO1179/AN B-8407\\nDT BJS HRB 250326 F11A\\n-  REQFPN1493\",\"content\":{\"AcrNo\":\"B-8407\"}}";
        sendMsg("http://"+serverIp+":" + port + "/services/analyze/downLinkMsgNotice.json",msg, MediaType.APPLICATION_JSON_TYPE);
    }



    private static void testFpn() {
        String msg = "{\"AcReg\":\"B5615\",\"Channel\":\"ACARS2\",\"MsgTm\":\"2017-03-22 10:40:03\",\"MsgType\":\"REQFPN\",\"OrigMsg\":\"2017-01-25 03:26:03\\n QU SHAQPHO\\n.BJSXCXA 250326\\n FML\\nFI HO1179/AN B-8407\\nDT BJS HRB 250326 F11A\\n-  REQFPN1493\",\"content\":{\"AcrNo\":\"B-8407\"}}";
        String msg1 = sendAndNoticeMsg("http://" + serverIp + ":" + port + "/services/analyze/downLinkMsgNotice.json", msg, MediaType.APPLICATION_JSON_TYPE);


    }


    private static void testPwi() {
        String msg = "{\"Channel\":\"ACARS1\",\"MsgTm\":\"2017-03-22 15:50:10\",\"MsgType\":\"REQPWI\",\"OrigMsg\":\"2017-03-21 07:50:10\\n QU SHAQPHO\\n.BJSXCXA 250326\\n FML\\nFI HO1179/AN B6565\\nDT BJS HRB 250326 F11A\\n-  REQPWI/CQ331/WQ331:GORPI.SAGUT.IDVEL.XDX.TAO.XIVID.FD.TEKAM.IGDEG.NIXEP.TANIB.MAKNO.UDETI.VENOS.CHI.NODAL.ISKEM.BIDIB.NUBKI.LEMOT/DQ331/WRZYHB.ZYTX201\\n\",\"acReg\":\"B5615\",\"content\":{\"alternateAirportList\":[\"ZYHB\",\"ZYTX\"],\"climbAltitudeList\":[\"331\"],\"cruiseLevelAltitudeList\":[\"331\"],\"cruiseLevelWaypointList\":[\"GORPI\",\"SAGUT\",\"IDVEL\",\"XDX\",\"TAO\",\"XIVID\",\"FD\",\"TEKAM\",\"IGDEG\",\"NIXEP\",\"TANIB\",\"MAKNO\",\"UDETI\",\"VENOS\",\"CHI\",\"NODAL\",\"ISKEM\",\"BIDIB\",\"NUBKI\",\"LEMOT\"],\"descentAltitudeList\":[\"331\"]}}";
        String msg1 = sendAndNoticeMsg("http://" + serverIp + ":" + port + "/services/analyze/downLinkMsgNotice.json", msg, MediaType.APPLICATION_JSON_TYPE);
    }


    private static void testNotices() {
        String[] acRegs = getAcRegs();
        for (String acReg : acRegs) {
            testNotice(acReg.trim());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void testNotice(String acReg) {
        String msg = "{\"AcReg\":\""+acReg+"\",\"Channel\":\"ACARS2\",\"MsgTm\":\"2017-03-15 18:10:03\",\"MsgType\":\"REQFPN\",\"OrigMsg\":\"2017-01-25 03:26:03\\n QU SHAQPHO\\n.BJSXCXA 250326\\n FML\\nFI HO1179/AN B-8407\\nDT BJS HRB 250326 F11A\\n-  REQFPN1493\",\"content\":{\"AcrNo\":\"B-8407\"}}";
        String msg1 = sendAndNoticeMsg("http://" + serverIp + ":" + port + "/services/analyze/downLinkMsgNotice.json", msg, MediaType.APPLICATION_JSON_TYPE);

        noticeMsg("http://"+serverIp+":" + port + "/services/analyze/upLinkMsgStateNotice.json",msg1, MediaType.APPLICATION_JSON_TYPE);
    }

    private static String[] getAcRegs() {
       String[] arr = new String[]{"B5615 ","B9909 ","B1940 ","B5411 ","B6159 ","B6567 ","B6153 ","B1938 ","B1842 ","B5105 ","B5612 ","B1711 ","B1517 ","B1941 ","B5860 ","B6807 ","B6316 ","B6856 ","B5187 ","B6939 ","B5616 ","B5073 ","B5402 ","B1937 ","B1939 ","B5109 ","B5441 ","B5440 ","B6615 ","B5079 ","B6749 ","B5103 ","B6853 ","B5673 ","B9979 ","B1713 ","B5776 ","B5606 ","B6647 ","B5618 ","B5075 ","B5378 ","B5360 ","B2669 ","B6359 ","B2667 ","B1755 ","B6571 ","B6563 ","B9938 ","B9939 ","B6570 ","B6781 ","B5671 ","B1757 ","B1518 ","B1972 ","B9980 ","B6937 ","B6358 ","B5362 ","B1936 ","B5186 ","B6352 ","B6780 ","B5401 ","B6650 ","B5670 ","B1712 ","B6550 ","B6740 ","B6357 ","B6613 ","B6855 ","B6351 ","B6750 ","B1758 ","B5381 ","B9908 ","B6315 ","B5365 ","B5357 ","B1602 ","B5025 ","B6722 ","B6360 ","B5773 ","B1759 ","B6648 ","B1841 ","B5379 ","B6566 ","B9910 ","B6377 ","B2692 ","B6312 ","B5361 ","B6286 ","B6721 ","B6392 ","B5690 ","B1993 ","B5410 ","B6938 ","B6692 ","B1710 ","B5412 ","B6720 ","B6197 ","B5617 ","B1935 ","B6565 ","B5400 ","B5380 ","B6835 ","B5736 ","B1756 ","B6649 ","B6806 ","B6313 ","B6196 ","B6857 ","B6690 ","B5078 ","B6691 ","B6165 ","B5737 ","B5772 ","B6568 ","B5613 ","B5619 ","B6297 ","B6935 ","B2691"};
       // String[] arr = new String[]{"B5615"};
        return arr;
    }


    /**
     * 发送报文
     *
     * @param url       连接
     * @param msg       报文
     * @param mediaType 请求类型
     */
    private static void sendMsg(String url, String msg, MediaType mediaType) {
        System.out.println("Registering user via " + url);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().post(Entity.entity(msg, mediaType));
        try {
            if (response.getStatus() != 200 && response.getStatus() != 204) {
                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
            }
            System.err.println("Successfully got result: " + response.readEntity(String.class));
        } finally {
            response.close();
            client.close();
        }
    }

    private static String sendAndNoticeMsg(String url, String msg, MediaType mediaType) {
        System.out.println("Registering user via " + url);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
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

    private static void noticeMsg(String url, String msg, MediaType mediaType) {
        System.out.println("Registering user via " + url);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        //Response response = target.request().post(Entity.entity(msg, mediaType));
        Response response = target.request().post(Entity.entity(msg, mediaType));
        try {
            if (response.getStatus() != 200 && response.getStatus() != 204) {
                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
            }
            String s = response.readEntity(String.class);
            System.err.println("Successfully got result: " + response.readEntity(String.class));
        } finally {
            response.close();
            client.close();
        }
    }


}
