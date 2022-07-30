package brz.breeze.tool_utils;



import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import android.os.Handler;

public class BEmailUtils {

	private static BEmailUtils bEmails;

	private static OnEmailSendListener onListener;

	private String title = "";

	private String content = "";

	private String target = "";

	private String sendUserName = "BREEZE";

	private MimeMessage message;

	private final Session session;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendUserName() {
		return sendUserName;
	}

    public static BEmailUtils getInstance() {
		if (bEmails == null) {
			bEmails = new BEmailUtils();
		}
		return bEmails;
	}

	public BEmailUtils() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", "smtp.163.com");
		properties.setProperty("mail.smtp.auth", "true");
		session = Session.getDefaultInstance(properties);
	}

	public static void send(String title, String content, String emailAddress, OnEmailSendListener listener) {
		BEmailUtils b = BEmailUtils.getInstance();
		b.setTitle(title);
		b.setContent(content);
		b.setTarget(emailAddress);
		b.send(listener);
	}
	
	public void send(OnEmailSendListener listener) {
		onListener = listener;
		new Thread(new Runnable(){
			public void run(){
				try {
					message = new MimeMessage(session);
					message.setFrom(new InternetAddress("breeze258369@163.com", sendUserName, "UTF-8"));
					message.setRecipients(Message.RecipientType.TO, target);
					message.setSubject(title);
					message.setContent(content, "text/html;charset=UTF-8");
					message.setSentDate(new Date());
					message.saveChanges();
					Transport transport = session.getTransport();
					transport.connect("breeze258369@163.com", "IHGCUXRHKURBZZXQ");
					transport.sendMessage(message, message.getAllRecipients());
					onListener.onSuccess();
					transport.close();
				} catch (final Exception e) {
					onListener.onFailure(e);
				}
			}
		}).start();
	}
	
	public interface OnEmailSendListener {
		void onSuccess();
		void onFailure(Exception exception);
	}
	
}
