package com.dist.redfx;

import com.dist.redfx.service.RedisService;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.JedisPubSub;

public class connectFXMLController implements Initializable {

    @FXML
    Parent root;

    @FXML
    TextFlow appTitle;

    @FXML
    TextField redisHost;
    
    @FXML
    TextField PubIDTextField;

    @FXML
    TextField redisPort;
    
    @FXML
    TextField SubChannelIDTextfield;
    
    @FXML
    TextField channelIDTextField;

    @FXML
    Label errorMsg;

    @FXML
    Label label;

    @FXML
    ListView messageIDListView;
    
    @FXML
    Rectangle backIDRect;
    
    @FXML
    Button connectIDButton;
    
    @FXML
    Button PubIDButton;
    
    @FXML
    Button changeSUBIDButton;
    
    @FXML
    ListView channelIDListView;
    
    private int i = 0;
    private RedisService redisService;
    private JedisPubSub jedisPubSub;
        
    //----> message structure
    private String jedisChannel = "testing";
    private String code;
    private String msg;

    private static String JEDIS_SERVER;
    
    
    ArrayList<String> messageContainer = new ArrayList<String>();
    ArrayList<String> info = new ArrayList<String>();
    
    private CountDownLatch messageReceivedLatch = new CountDownLatch(1);
    private CountDownLatch publishLatch = new CountDownLatch(1);
    

    public void connectToRedis(ActionEvent event) throws IOException, InterruptedException {
        String hostname = redisHost.getText();
        JEDIS_SERVER = redisHost.getText();
        String port = redisPort.getText();
        channelIDTextField.setText("home");
        port = StringUtils.isNumeric(port) ? port : "6379";

        redisService = new RedisService(hostname, Integer.valueOf(port)); //connecting redis

        serverInfo();
        try {
            //label.setText(redisService.ping());
            if (redisService.ping() != null) {
                errorMsg.setVisible(false);
                redisHost.setDisable(true);
                redisPort.setDisable(true);
                connectIDButton.setDisable(true);
                
                
                label.setText(redisService.ping());
                //----------------->
                messageIDListView.getItems().add(i, redisService.ping());
                
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        try {
                            jedisPubSub = setupSubscriber();

                            messageReceivedLatch.await();
                            
                          //log("Got Message: %s", messageContainer.iterator().next());
                         } catch (InterruptedException ex) {
                            Logger.getLogger(connectFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
                
            }
        } catch (JedisException ex) {
            label.setText("... !");
            errorMsg.setTextFill(Color.valueOf("#D82C27"));
            errorMsg.setVisible(true);
        }
    }
    
    public void changeSub(ActionEvent event)  {
        String tmp = jedisChannel;
        
        jedisPubSub.unsubscribe(tmp);
        
        jedisChannel = SubChannelIDTextfield.getText();
        //System.out.println(PubIDTextField.getText());
        
        new Thread(new Runnable() {
                    @Override
                    public void run(){
                        try {
                            
                            jedisPubSub = setupSubscriber();

                            messageReceivedLatch.await();
                            
                         } catch (InterruptedException ex) {
                            Logger.getLogger(connectFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
        
    }
    
    public void publishToRedis(ActionEvent event) throws IOException, InterruptedException {

        final String currchannel = channelIDTextField.getText();
        final String pubMessage = PubIDTextField.getText();

        PubIDTextField.setText("");
        try {
            
                new Thread(new Runnable() {
                    @Override
                    public void run(){

                        log("Connecting");
                        Jedis jedis = new Jedis(JEDIS_SERVER);
                        
                        log("Waiting to publish");
                        
                        //publishLatch.await();
                        log("Ready to publish, waiting one sec");
                        //Thread.sleep(1000);
                        
                        log("Publishing");
                        log("Channel: %s - Message: %s", currchannel, pubMessage);
                        jedis.publish(currchannel, pubMessage);
                        //jedis.publish("testing", "hi");
                        log("punlished, closing publishing connection");
                        
                        
                        jedis.quit();
                        log("Publishing connection closed");
                        
                    }
                }, "publisherThread").start();

        } catch (JedisException ex) {
            label.setText("... !");
            errorMsg.setTextFill(Color.valueOf("#D82C27"));
            errorMsg.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTitle();
        
    }

    private void initTitle() {
        Text part1 = new Text("Welcome to ");
        Text part2 = new Text("Pub/Sub");

        part1.setFill(Color.valueOf("#555"));
        part2.setFill(Color.valueOf("#D82C27"));

        appTitle.getChildren().addAll(part1, part2);
        appTitle.setPadding(new Insets(0, 0, 10, 0));
    }
    
    /**
     *
     */
    public void serverInfo(){
        Jedis jedis = new Jedis(JEDIS_SERVER);
        info = (ArrayList<String>) jedis.pubsubChannels("*");
        for(int i=0; i < info.size(); i++){
            //log("from channe;l: %d", info.size());
            channelIDListView.getItems().add(channelIDListView.getItems().size(), info.get(i));
        }
        //jedis.getClient().sendCommand("PUBSUB CHANNELS");
        //String info = redisService.getInfo("stats");
        //infoIDTextArea.setText(info);172.17.
        
        
    }

    private JedisPubSub setupSubscriber() {

        final JedisPubSub jedisPubSub = new JedisPubSub() {

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                log("onUnsubscribe");
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                log("onSubscribe");
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
            }

            @Override
            public void onMessage(String channel, String message) {
                messageContainer.add(message);                      //saving messages into a array
                //log("Message received");
                
                for(String s : messageContainer){
                    log("Got Message %s",s);
                }

                String[] data = message.split(":", 2);
               
                if (data.length == 1) messageIDListView.getItems().add(messageIDListView.getItems().size(), message); //adding messages to listview
                
                else {
                    code = data[0];
                    msg  = data[1];
                    //log("Code:%s- Message:%s.", data[0], data[1]);

                    if (code.equalsIgnoreCase("color")) {
                        backIDRect.setFill(Color.valueOf(data[1]));         //setting color of background (a Rectangle)
                    } else if (code.equalsIgnoreCase("msg")) {
                        messageIDListView.getItems().add(messageIDListView.getItems().size(), data[1]); //adding messages to listview
                    }

                }
                messageReceivedLatch.countDown();
            }

        };
        new Thread(new Runnable()  {
            @Override
            public void run() {
                try {
                    log("Connectig");
                    Jedis jedis = new Jedis(JEDIS_SERVER);
                    log("subscribing");
                    jedis.subscribe(jedisPubSub, jedisChannel);
                    log("subscribe returned, closing down");
                    jedis.quit();
                } catch (Exception e) {
                    log(">>> Sub - " + e.getMessage());
                }
            }
            
        }, "subscriberThread").start();
        return jedisPubSub;
    }
    
    
    static final long startMillis = System.currentTimeMillis();

    private static void log(String string, Object... args) {
        long millisSinceStart = System.currentTimeMillis() - startMillis;
        System.out.printf("%20s %6d %s\n", Thread.currentThread().getName(), millisSinceStart,
                String.format(string, args));

    }
    
    
}
