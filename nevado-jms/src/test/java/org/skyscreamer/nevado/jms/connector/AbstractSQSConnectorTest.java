package org.skyscreamer.nevado.jms.connector;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.nevado.jms.connector.mock.MockSQSConnector;
import org.skyscreamer.nevado.jms.message.NevadoMessage;
import org.skyscreamer.nevado.jms.message.NevadoTextMessage;

import javax.jms.JMSException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AbstractSQSConnectorTest {

    private AbstractSQSConnector sqsConnector;

    @Before
    public void setUp() {
        sqsConnector = new MockSQSConnector();
    }

    @Test
    public void shouldSerializeMessage() throws JMSException {
        //arrange
        NevadoTextMessage message = new NevadoTextMessage();
        message.setText("fooBarBuzz");

        //act
        String serializeMessage = sqsConnector.serializeMessage(message);

        //assert
        assertThat(serializeMessage.startsWith("{"), is(false));
        assertThat(serializeMessage.endsWith("}"), is(false));

        //act
        NevadoMessage deserializeMessage = sqsConnector.deserializeMessage(serializeMessage);

        //assert
        assertThat(deserializeMessage, is(instanceOf(NevadoTextMessage.class)));
        assertThat(((NevadoTextMessage)deserializeMessage).getText(), is("fooBarBuzz"));
    }

    @Test
    public void jsonIsNotSerializedAgain() throws JMSException {
        //arrange
        NevadoTextMessage message = new NevadoTextMessage();
        String json = "\n  \t \n{\"foo\":\"bar\"}";
        message.setText(json);

        //act
        String serializeMessage = sqsConnector.serializeMessage(message);

        //assert
        assertThat(serializeMessage, is(json));

        //act
        NevadoMessage deserializeMessage = sqsConnector.deserializeMessage(serializeMessage);

        //assert
        assertThat(deserializeMessage, is(instanceOf(NevadoTextMessage.class)));
        assertThat(((NevadoTextMessage)deserializeMessage).getText(), is(json));
    }
}