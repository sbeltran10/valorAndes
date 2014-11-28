package valorAndes.dao;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class DAOSender {

	private ConnectionFactory cf;

	private Connection c;

	private Session s;

	private Destination d;

	private MessageProducer mp;

	/**
	 * Constructor del encargado de enviar mensajes JMS.
	 */
	public DAOSender() {
		InitialContext init;
		try {
			init = new InitialContext();
			this.cf = (ConnectionFactory) init.lookup("RemoteConnectionFactory");
			this.d = (Destination) init.lookup("queue/test");
			this.c = (Connection) this.cf.createConnection("sistrans", "test");
			((javax.jms.Connection) this.c).start();
			this.s = ((javax.jms.Connection) this.c).createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.mp = this.s.createProducer(this.d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia un mensaje a JMS.
	 * @param mensaje
	 */
	public void send(String mensaje) throws Exception{
		try {
			TextMessage tm = this.s.createTextMessage(mensaje);
			this.mp.send(tm);
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
	}
}
