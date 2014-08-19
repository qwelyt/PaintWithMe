import java.io.*;
import java.net.*;

public class Link implements Runnable{
	private Canvas c;
	private String host;
	private int myPort;
	private int yourPort;
	private Thread t;

	public Link(Canvas can, String[] args){
		c = can;
		t = new Thread(this);
		if(args.length != 3){
			System.err.println("You need to specify your port, host, other port.\n\tjava PaintWithMe <myPort> <host> <otherPort>");
			System.exit(1);
		}
		else{
			if(isInteger(args[0]))
				myPort = Integer.parseInt(args[0]);
			else{
				System.err.println("Own port not specified.");
				System.exit(2);
			}
			if(isInteger(args[2]))
				yourPort = Integer.parseInt(args[2]);
			else{
				System.err.println("Other port not specified.");
				System.exit(3);
			}
			host = args[1];
		}

		t.start();
	}

	public void send(String str){
		try{
			byte[] b = str.getBytes();
			InetAddress ip = null;
			try{
				ip = InetAddress.getByName(host);
			}catch(UnknownHostException uhe){
				System.err.println("The host [ " + host + " ] could not be resolved.");
			}catch(SecurityException ipSecEx){
				System.err.println("The security manager does not allow address lookup.");
			}
			
			DatagramPacket dp = new DatagramPacket(b, b.length, ip, yourPort);
			DatagramSocket ds = new DatagramSocket();
			ds.send(dp);
		}catch(SocketException se){
			System.err.println("Could not send to " + host +":"+yourPort);
		}catch(SecurityException secex){
			System.err.println("The security manager does not binding UDP-socket to " + host + ":"+ yourPort);
		}catch(IOException ioe){
			//System.err.println("Could not send datagram.");
		}
	}

	public void run(){
		try{
			DatagramSocket ds = new DatagramSocket(myPort);
			while(true){
				byte[] b = new byte[4096];
				DatagramPacket dp = new DatagramPacket(b, b.length);
				ds.receive(dp);
				String str = new String(dp.getData(), 0, dp.getLength());
				c.addPoint(str);
			}
		}catch(SocketException se){
			System.err.println("Could not create socket on port " + myPort);
			System.exit(200);
		}catch(SecurityException secex){
			System.err.println("The security manager does not allow local bind of port " + myPort);
			System.exit(201);
		}catch(IOException ioe){
			System.err.println("Could not receive datagram");
			System.exit(202);
		}
	}

	// Simple helper to check if a string is an integer.
	private boolean isInteger(String s){
		try{
			Integer.parseInt(s);
		}catch(NumberFormatException nfe){
			return false;
		}
		return true;
	}
}
