package et.com.delivereth.Telegram.OtherUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SendSMS {
    private static final Logger logger = LoggerFactory.getLogger(SendSMS.class);
    @Async
    public void sendSMS(String phone, String text){
        final String urlTemplate = "https://portal.websprix.com/sendsms.php?id=%s&pass=%s&to=%s&txt=%s";
        String url = String.format(urlTemplate,"deliver", "12345678", phone, text);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        logger.info(result);
    }
}
