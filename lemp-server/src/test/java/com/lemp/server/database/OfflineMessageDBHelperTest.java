package com.lemp.server.database;

import com.google.gson.Gson;
import com.lemp.packet.Message;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class OfflineMessageDBHelperTest {

    @BeforeClass
    public static void setup() throws Exception {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, "lemp_test");
    }

    @Test
    public void offlineMessageTest() throws Exception {
        Message message = new Message();
        message.setId("id");
        message.setS("sender");
        message.setR("receiver");
        message.setSt(System.currentTimeMillis());
        message.setT(Message.Type.text.getKey());
        message.setC("Hello");
        OfflineMessageDBHelper.getInstance().insertOfflineMessage(message);
        List<Message> messageList = OfflineMessageDBHelper.getInstance().getOfflineMessages("receiver", false);
        Assert.assertEquals(1, messageList.size());

        List<String> messageStrList = OfflineMessageDBHelper.getInstance().getOfflineMessagesAsString("receiver", false);
        Assert.assertEquals(1, messageStrList.size());
        String messageStr = messageStrList.get(0);
        Assert.assertEquals(new Gson().toJson(message), messageStr);

        OfflineMessageDBHelper.getInstance().deleteOfflineMessages("receiver");
        messageList = OfflineMessageDBHelper.getInstance().getOfflineMessages("receiver", false);
        Assert.assertEquals(0, messageList.size());
    }

}
