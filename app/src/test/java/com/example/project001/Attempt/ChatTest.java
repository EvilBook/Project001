package com.example.project001.Attempt;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChatTest {


    Chat chat = new Chat("Maria", "Claudia", "why?");

    private String expectedSender;
    private String actualSender;
    private String expectedReceiver;
    private String actualReceiver;
    private String expectedMSG;
    private String actualMSG;

    @Test
    public void getSender() {
        expectedSender = "Laura";
        actualSender = chat.getSender();
        Assert.assertEquals(expectedSender, actualSender);
    }

    @Test
    public void setSender() {
        //chat.setSender("Cassandra");
        actualSender = chat.getSender();
        expectedSender = "Cassandra";
        Assert.assertEquals(expectedSender, actualSender);
    }

    @Test
    public void getReceiver() {
        expectedReceiver = "Persephona";
        actualReceiver = chat.getReceiver();
        Assert.assertEquals(expectedReceiver, actualReceiver);
    }

    @Test
    public void setReceiver() {
        //chat.setReceiver("Virginia");
        actualReceiver = chat.getReceiver();
        expectedReceiver = "Virginia";
        Assert.assertEquals(expectedReceiver, actualReceiver);
    }

}