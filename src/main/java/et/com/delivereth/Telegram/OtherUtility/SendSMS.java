package et.com.delivereth.Telegram.OtherUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class SendSMS {
    private static final Logger logger = LoggerFactory.getLogger(SendSMS.class);

    @Async
    public void sendSMS(String phone, String text) {
        final String urlTemplate = "https://portal.websprix.com/sendsms.php?id=%s&pass=%s&to=%s&txt=%s";
        String url = String.format(urlTemplate, "deliver", "12345678", phone, text);
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(url, String.class);
//        logger.info(result);

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                logger.info("Message Sent For {}", phone);
                logger.info("Response {}", response);
            }
            in.close();
        } catch (Exception e) {
            logger.error("FAILED TO SEND SMS");
        }

    }
}
