package org.skyscreamer.nevado.jms.connector;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.nevado.jms.connector.mock.MockSQSConnector;
import org.skyscreamer.nevado.jms.message.NevadoBytesMessage;
import org.skyscreamer.nevado.jms.message.NevadoMessage;
import org.skyscreamer.nevado.jms.message.NevadoObjectMessage;
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
        NevadoObjectMessage message = new NevadoObjectMessage();
        message.setObject("fooBarBuzz");

        //act
        String serializeMessage = sqsConnector.serializeMessage(message);

        //assert
        assertThat(serializeMessage.contains("foo"), is(false));

        //act
        NevadoMessage deserializeMessage = sqsConnector.deserializeMessage(serializeMessage);

        //assert
        assertThat(deserializeMessage, is(instanceOf(NevadoObjectMessage.class)));
        assertThat(((NevadoObjectMessage) deserializeMessage).getObject().toString(), is("fooBarBuzz"));
    }

    @Test
    public void textIsNotSerialized() throws JMSException {
        //arrange
        NevadoTextMessage message = new NevadoTextMessage();
        String text = "something";
        message.setText(text);

        //act
        String serializeMessage = sqsConnector.serializeMessage(message);

        //assert
        assertThat(serializeMessage, is(text));

        //act
        NevadoMessage deserializeMessage = sqsConnector.deserializeMessage(serializeMessage);

        //assert
        assertThat(deserializeMessage, is(instanceOf(NevadoTextMessage.class)));
        assertThat(((NevadoTextMessage)deserializeMessage).getText(), is(text));
    }
}