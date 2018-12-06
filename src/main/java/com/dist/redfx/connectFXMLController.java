package com.dist.redfx;

import com.dist.redfx.service.RedisService;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.input.MouseEvent;
import static javafx.scene.paint.Color.GREEN;
import javafx.stage.Stage;
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
    
    @FXML
    public ComboBox colorIDComboBox;
    
    @FXML
    Button ID1;
    @FXML
    Button ID2;
    @FXML
    Button ID3;
    @FXML
    Button ID4;
    @FXML
    Button ID5;
    @FXML
    Button ID6;
    @FXML
    Button ID7;
    @FXML
    Button ID8;
    @FXML
    Button ID9;
    @FXML
    Button ID10;
    @FXML
    Button ID11;
    @FXML
    Button ID12;
    @FXML
    Button ID13;
    @FXML
    Button ID14;
    @FXML
    Button ID15;
    @FXML
    Button ID16;
    @FXML
    Button ID17;
    @FXML
    Button ID18;
    @FXML
    Button ID19;
    @FXML
    Button ID20;
    @FXML
    Button ID21;
    @FXML
    Button ID22;
    @FXML
    Button ID23;
    @FXML
    Button ID24;
    @FXML
    Button ID25;
    @FXML
    Button ID26;
    @FXML
    Button ID27;
    @FXML
    Button ID28;
    @FXML
    Button ID29;
    @FXML
    Button ID30;
    @FXML
    Button ID31;
    @FXML
    Button ID32;
    @FXML
    Button ID33;
    @FXML
    Button ID34;
    @FXML
    Button ID35;
    @FXML
    Button ID36;
    @FXML
    Button ID37;
    @FXML
    Button ID38;
    @FXML
    Button ID39;
    @FXML
    Button ID40;
    @FXML
    Button ID41;
    @FXML
    Button ID42;
    @FXML
    Button ID43;
    @FXML
    Button ID44;
    @FXML
    Button ID45;
    @FXML
    Button ID46;
    @FXML
    Button ID47;
    @FXML
    Button ID48;
    @FXML
    Button ID49;
    @FXML
    Button ID50;
    @FXML
    Button ID51;
    @FXML
    Button ID52;
    @FXML
    Button ID53;
    @FXML
    Button ID54;
    
    private Map<String, Button> button;

    
    
    private int i = 0;
    private RedisService redisService;
    private JedisPubSub jedisPubSub;
        
    //----> message structure
    private String jedisChannel = "testing";
    private String code;
    private String msg;
    private String color;
    
    
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
    
    
    
    public void buttonClick(ActionEvent event) {
        Button b = (Button) event.getSource();
        String bMessage = "button:" + b.getId() + ":" + colorIDComboBox.getSelectionModel().getSelectedItem().toString();
        //label.setText(bMessage);
        
        final String currchannel = channelIDTextField.getText();
        final String pubMessage = bMessage;

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
                        //----------> PUBLISH HAPPENES
                        
                        jedis.publish(currchannel, pubMessage);
                        //jedis.publish("testing", "hi");
                        log("punlished, closing publishing connection");
                        
                        
                        jedis.quit();
                        log("Publishing connection closed");
                        
                    }
                }, "publisherThread").start();

        } catch (JedisException ex) {
            label.setText("error on button 11");
            errorMsg.setTextFill(Color.valueOf("#D82C27"));
            //errorMsg.setVisible(true);
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
                        //----------> PUBLISH HAPPENES
                        
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
        
        colorIDComboBox.getItems().addAll(
                "blue",
                "green",
                "red",
                "black",
                "white");
        
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

        final JedisPubSub jedisPubSub;
        jedisPubSub = new JedisPubSub() {
            
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

                String[] data = message.split(":", 3);
               
                if (data.length == 1) messageIDListView.getItems().add(messageIDListView.getItems().size(), message); //adding messages to listview
                
                else {
                    code = data[0];
                    msg  = data[1];
                    //log("Code:%s- Message:%s.", data[0], data[1]);

                    if (code.equalsIgnoreCase("color")) {
                        backIDRect.setFill(Color.valueOf(data[1]));         //setting color of background (a Rectangle)
                    } else if (code.equalsIgnoreCase("msg")) {
                        messageIDListView.getItems().add(messageIDListView.getItems().size(), data[1]); //adding messages to listview
                    } else if (code.equalsIgnoreCase("button")) {
                        color =  data[2];
                        
                        //button.get(msg).getStyleClass().add(color);
                        
                        if (msg.equals("ID1")){
                            ID1.getStyleClass().add(color);
                        }
                        if (msg.equals("ID2")){
                            ID2.getStyleClass().add(color);
                        }
                        if (msg.equals("ID3")){
                            ID3.getStyleClass().add(color);
                        }
                        if (msg.equals("ID4")){
                            ID4.getStyleClass().add(color);
                        }
                        if (msg.equals("ID5")){
                            ID5.getStyleClass().add(color);
                        }
                        if (msg.equals("ID6")){
                            ID6.getStyleClass().add(color);
                        }
                        if (msg.equals("ID7")){
                            ID7.getStyleClass().add(color);
                        }
                        if (msg.equals("ID8")){
                            ID8.getStyleClass().add(color);
                        }
                        if (msg.equals("ID9")){
                            ID9.getStyleClass().add(color);
                        }
                        if (msg.equals("ID10")){
                            ID10.getStyleClass().add(color);
                        }
                        if (msg.equals("ID11")){
                            ID11.getStyleClass().add(color);
                        }
                        if (msg.equals("ID12")){
                            ID12.getStyleClass().add(color);
                        }
                        if (msg.equals("ID13")){
                            ID13.getStyleClass().add(color);
                        }
                        if (msg.equals("ID14")){
                            ID14.getStyleClass().add(color);
                        }
                        if (msg.equals("ID15")){
                            ID15.getStyleClass().add(color);
                        }
                        if (msg.equals("ID16")){
                            ID16.getStyleClass().add(color);
                        }
                        if (msg.equals("ID17")){
                            ID17.getStyleClass().add(color);
                        }
                        if (msg.equals("ID18")){
                            ID18.getStyleClass().add(color);
                        }
                        if (msg.equals("ID19")){
                            ID19.getStyleClass().add(color);
                        }
                        if (msg.equals("ID120")){
                            ID20.getStyleClass().add(color);
                        }
                        if (msg.equals("ID21")){
                            ID21.getStyleClass().add(color);
                        }
                        if (msg.equals("ID22")){
                            ID22.getStyleClass().add(color);
                        }
                        if (msg.equals("ID23")){
                            ID23.getStyleClass().add(color);
                        }
                        if (msg.equals("ID24")){
                            ID24.getStyleClass().add(color);
                        }
                        if (msg.equals("ID25")){
                            ID25.getStyleClass().add(color);
                        }
                        if (msg.equals("ID26")){
                            ID26.getStyleClass().add(color);
                        }
                        if (msg.equals("ID27")){
                            ID27.getStyleClass().add(color);
                        }
                        if (msg.equals("ID28")){
                            ID28.getStyleClass().add(color);
                        }
                        if (msg.equals("ID29")){
                            ID29.getStyleClass().add(color);
                        }
                        if (msg.equals("ID30")){
                            ID30.getStyleClass().add(color);
                        }
                        if (msg.equals("ID31")){
                            ID31.getStyleClass().add(color);
                        }
                        if (msg.equals("ID32")){
                            ID32.getStyleClass().add(color);
                        }
                        if (msg.equals("ID33")){
                            ID33.getStyleClass().add(color);
                        }
                        if (msg.equals("ID34")){
                            ID34.getStyleClass().add(color);
                        }
                        if (msg.equals("ID35")){
                            ID35.getStyleClass().add(color);
                        }
                        if (msg.equals("ID36")){
                            ID36.getStyleClass().add(color);
                        }
                        if (msg.equals("ID37")){
                            ID37.getStyleClass().add(color);
                        }
                        if (msg.equals("ID38")){
                            ID38.getStyleClass().add(color);
                        }
                        if (msg.equals("ID39")){
                            ID39.getStyleClass().add(color);
                        }
                        if (msg.equals("ID40")){
                            ID40.getStyleClass().add(color);
                        }
                        if (msg.equals("ID41")){
                            ID41.getStyleClass().add(color);
                        }
                        if (msg.equals("ID42")){
                            ID42.getStyleClass().add(color);
                        }
                        if (msg.equals("ID43")){
                            ID43.getStyleClass().add(color);
                        }
                        if (msg.equals("ID44")){
                            ID44.getStyleClass().add(color);
                        }
                        if (msg.equals("ID45")){
                            ID45.getStyleClass().add(color);
                        }
                        if (msg.equals("ID46")){
                            ID46.getStyleClass().add(color);
                        }
                        if (msg.equals("ID47")){
                            ID47.getStyleClass().add(color);
                        }
                        if (msg.equals("ID48")){
                            ID48.getStyleClass().add(color);
                        }
                        if (msg.equals("ID49")){
                            ID49.getStyleClass().add(color);
                        }
                        if (msg.equals("ID50")){
                            ID50.getStyleClass().add(color);
                        }
                        if (msg.equals("ID51")){
                            ID51.getStyleClass().add(color);
                        }
                        if (msg.equals("ID52")){
                            ID52.getStyleClass().add(color);
                        }
                        if (msg.equals("ID53")){
                            ID53.getStyleClass().add(color);
                        }
                        if (msg.equals("ID54")){
                            ID54.getStyleClass().add(color);
                        }
                        
                        
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
