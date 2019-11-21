package application;

import java.io.IOException;

import java.io.InputStream; 
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	Socket socket; // ������ �־�� ��ǻ�Ͱ� ��Ʈ��ũ �󿡼� ����� �Ҽ����ִ�.

	public Client(Socket socket) {
		this.socket = socket;
		receive();
	}

	// Ŭ���̾�Ʈ�κ��� �޽����� ���� �޴� �޼ҵ��̴�
	public void receive() {
		Runnable thread = new Runnable() {// �Ϲ������� �ϳ��� �����带 ���鶧�� ������� ���� ��� ���� �������̺귯����
											// ���������� �ݵ�� run() �Լ��� �ݵ�� �������־���Ѵ�. �� �ϳ��� �����尡 ��� ���ν� �����ϴ��� run�ȿ��� ������
											// �ؾ��Ѵ�

			@Override
			public void run() {
				try {
					while (true) { //�ݺ������� Ŭ���̾�Ʈ�κ��� � ������ ���޹������ֵ��� �ϱ�����
						InputStream in = socket.getInputStream();  //� ������ ���޹������ֵ��� input��ü�� ���
						byte[] buffer = new byte[512]; //�ѹ��� 512����Ʈ��ŭ ���޹��� �� �ֵ��� �Ѵ�.
						int length = in.read(buffer); //Ŭ���̾�Ʈ�κ��� ���޹��� ������ ���ۿ� ����ش�., length�� ��� �޽����� ũ�⸦ �ǹ�
						
						while(length == -1) throw new IOException(); // �޽����� �дٰ� ������ �߻��ϸ� �˷��ִ°��̴�.
						System.out.println("[�޽��� ���ż���]" + socket.getRemoteSocketAddress()
						+" : "+ Thread.currentThread().getName());   //getRemoteSocketAddress�� ���� ������ Ŭ���̾�Ʈ�� ip�ּҿ� ���� ���������� , currentThread().getName()�� �������� ������ ���� ���
						String message = new String(buffer, 0 , length, "UTF-8");
						for(Client client : Main.clients) { //���޹����޽����� �ٸ� Ŭ���̾�Ʈ���� ���� �Ѵ�.
							client.send(message);
							
						}
						try {
							
						}catch(Exception e) {
							try {
								System.out.println("�޽��� ���� ����"+socket.getRemoteSocketAddress() + " : " + Thread.currentThread().getName() );
							}catch(Exception e2) {
								e2.printStackTrace();
							}
							
						}
						
						
						
							
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		};
		Main.threadPool.equals(thread); // ����Ǯ�� ������� �ϳ��� �����带 ��Ͻ����ִ°��̴�.,������� �����带 ���������� �����ϱ����Ͽ� ������Ǯ�� ���
	}

	//
	public void send(String message) {
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte [] buffer = message.getBytes("utf-8");
					out.write(buffer);  //���ۿ���䳻���� �������� Ŭ���̾�Ʈ�� ����
					out.flush(); //�ݵ���ؾ��Ѵ� �׷��� ������� ���������� ������ �ߴٴ°��� �˷��ټ��մ�.
				}catch(Exception e) {
					try {
						System.out.println("[�޽��� �۽� ����]"
								+socket.getRemoteSocketAddress()
								+ " : " + Thread.currentThread().getName());
						Main.clients.remove(Client.this); //Main�� �ִ� Ŭ���̾�Ʈ�� ������ ��� �迭����  ���� �����ϴ� Ŭ���̾�Ʈ�� �����Ѵ� �� ������ �߻��ؼ� �ش� Ŭ���̾�Ʈ�� �������� ������ ���ܼ� �翬�� �����ȿ�����  �ش� Ŭ���̾�Ʈ�� ������ ������°���  ó���ϰ��ϴ°��̴�.
						socket.close(); //������ ���� Ŭ���̾�Ʈ�� ������ �ݾƹ�����
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				
			}
			
		};
		Main.threadPool.equals(thread); // ����Ǯ�� ������� �ϳ��� �����带 ��Ͻ����ִ°��̴�.,������� �����带 ���������� �����ϱ����Ͽ� ������Ǯ�� ���
		
	}
}
