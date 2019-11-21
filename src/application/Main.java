package application;
	
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application { 
	public static ExecutorService threadPool;  //�پ��� Ŭ���̾�Ʈ�� ����������  ��������� ȿ�������� �����ϱ� ���Ϥ��� ������Ǯ�� �����
	//ExecutorService�� �������� �����带 ȿ�������� �����ϱ� ���� ����ϴ� ���̺귯���̴�. ������ �÷�
	//�����带 ó���ϰԵǸ� �⺻������ �������� ���� ������ �α⶧���� ���۽����� Ŭ���̾�Ʈ�� ���� �����ϴ���
	//�������� ���ڿ��� ������ �ֱ⶧���� ������ �������ϸ� ���� �� �ִ�.
	

	public static Vector<Client> clients =new Vector<Client>();//������ Ŭ���̾�Ʈ�� �����Ѵ�.
	ServerSocket serverSocket;
	
	//������ �������Ѽ� Ŭ���̾�Ʈ�� ������ ��ٸ��� �޼ҵ��Դϴ�.
	public void startServer(String IP,int port) {  //��� ip�� , ��� ��Ʈ�� ��� Ŭ���̾�Ʈ�� ����� �Ұ������� ���°��̴�.
		try {
			serverSocket = new ServerSocket(); //���ϰ�ü����
			serverSocket.bind(new InetSocketAddress(IP,port)); //����Ʈ�� ����ؼ�  ������ǻ�Ϳ����� �����ϴ� �� ��ǻ�Ͱ� �ڽ��� ip�ּ� , ��Ʈ��ȣ�� Ư�� Ŭ���̾�Ʈ�� ������ ��ٸ��� �Ҽ��ִ�. 
		}catch(Exception e) { //������ ���� 
			e.printStackTrace();
			if(!serverSocket.isClosed()) {//���������� �����ִ� ���°� �ƴ϶�� 
				stopServer(); //���� ���� 
				
			}
			return;
		}
		
		//���԰� �ȳ��� Ŭ���̾�Ʈ�� �����Ҷ��� ��� ��ٸ��� ��������
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				while(true) { //����ؼ� ���ο� Ŭ���̾�Ʈ�� �����Ҽ� �ֵ���  ������ش�
					try {
						Socket socket = serverSocket.accept();//  ���ο� Ŭ���̾�Ʈ�� �����Ҽ� �ֵ���  ������ش�
						clients.add(new Client(socket)); //Ŭ���̾�Ʈ�� ������ �ߴٸ�  Ŭ���̾�Ʈ �迭�� ���Ӱ� ������ Ŭ���̾�Ʈ��  �߰�
						System.out.println("[Ŭ���̾�Ʈ ����]" 
						+ socket.getRemoteSocketAddress()
						+ " : " + Thread.currentThread().getName()); //�α����
					}catch(Exception e) {
						if(!serverSocket.isClosed()) { //���� ���Ͽ� ������ �߻��Ѱ��
							stopServer(); //������ �۵� ���� 
							
						}
						break;
					}
				}
				
			}
			
		};
		threadPool = Executors.newCachedThreadPool(); //������ �ʱ�ȭ
		threadPool.submit(thread);  //���� ���̾�Ʈ�� �����Ҷ��� ��� ��ٸ��� �����带 �־��ش�.
		
	}
	//������ �۵��� ������Ű�� �޼ҵ��Դϴ�.
	public void stopServer() {
		try {
			// ���� �۵� ���� ��� ���� �ݱ�
			Iterator<Client> iterator = clients.iterator(); 
			while(iterator.hasNext()) { //�Ѹ� �Ѹ� Ŭ���̾�Ʈ�� ������ �Ѵ�.
				Client client =iterator.next(); // Ư�� Ŭ���̾�Ʈ�� ����
				client.socket.close();  // �ش� Ŭ���̾�Ʈ�� ������ ����
				iterator.remove();  //������ ���� Ŭ���̾�Ʈ�� ��
				
			}
			//���� ���� ��ü �ݱ�
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			//������ Ǯ �����ϱ� 
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();  //�ڿ� �Ҵ� ���� 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//Ui�� �����ϰ�, ���������� ���α׷��� ���۽�Ű�� �޼ҵ�
	@Override
	public void start(Stage primaryStage) {
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//���α׷��� �������̴�.
	public static void main(String[] args) {
		launch(args);
	}
}
