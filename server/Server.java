package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
	private DatagramSocket socket;

	Server() {
		try {
			socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);
			System.out.println("UDP Server started");
			while (true) {
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);

				System.out.println(getClass().getName() + ">>>Discovery packet received from: "
						+ packet.getAddress().getHostAddress());
				System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));
				// See if the packet holds the right command (message)
				String message = new String(packet.getData()).trim();
				if (message.equals("BOEBOT1.0_DISCOVER")) {
					byte[] sendData = ("BOEBOT1.0_ACK_" + getSerial()).getBytes();
					// Send a response
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(),
							packet.getPort());
					socket.send(sendPacket);
					System.out.println(
							getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
    public static String getSerial()
    {
        try{
        for(String line : Files.readAllLines(Paths.get("/proc/cpuinfo"), StandardCharsets.UTF_8))
        {
            if(line.indexOf("Serial") == 0)
                return line.substring(line.lastIndexOf(" ")+1);
        }
    } catch(Exception e) { e.printStackTrace(); }
        return "deadbeef00000000";
    
    }	
}
