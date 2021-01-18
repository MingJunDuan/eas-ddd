package xyz.zhangyi.ddd.eas.valueadded.trainingcontext.acl.adapter.clients;

import org.springframework.stereotype.Component;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.acl.port.clients.NotificationClient;
import xyz.zhangyi.ddd.eas.valueadded.trainingcontext.domain.notification.Notification;

@Component
public class NotificationClientImpl implements NotificationClient {
    @Override
    public void send(Notification notification) {
        System.out.println("send the notification");
    }
}