import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;




public class Server extends JFrame implements ActionListener {
	
	private static final int PORT = 9001;
	
	private static HashSet<String> names = new HashSet<String>();
	
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	
	JButton bStop;
	
	public Server()
	{
		
		setTitle("Server");
		setLayout(null);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		
		
		bStop = new JButton("Exit & Stop");
		bStop.setBounds(145, 150, 100, 50);
		add(bStop);
		bStop.addActionListener(this);
		
		setSize(400, 250);
		setLocationRelativeTo(null);
		
		
		
		
		
	
	}
	
	

	public static void main(String[] args) throws Exception {
		
		System.out.println("The chat server is running.");
		
		Server server = new Server();
		
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
		

	}
	
	 private static class Handler extends Thread {
	        private String name;
	        private Socket socket;
	        private BufferedReader in;
	        private PrintWriter out;

	        
	        public Handler(Socket socket) {
	            this.socket = socket;
	        }

	        
	        public void run() {
	            try {

	                
	                in = new BufferedReader(new InputStreamReader(
	                    socket.getInputStream()));
	                out = new PrintWriter(socket.getOutputStream(), true);

	                
	                while (true) {
	                    //out.println("SUBMITNAME");
	                    name = in.readLine();
	                    if (name == null) {
	                        return;
	                    }
	                    synchronized (names) {
	                        if (!names.contains(name)) {
	                            names.add(name);
	                            break;
	                        }
	                    }
	                }

	               
	                out.println("You are connected " + name + "!");
	                writers.add(out);

	                
	                while (true) {
	                    String input = in.readLine();
	                    if (input == null) {
	                        return;
	                    }
	                    for (PrintWriter writer : writers) {
	                        writer.println(name + ": " + input);
	                    }
	                }
	            } catch (IOException e) {
	                System.out.println(e);
	            } finally {
	                
	                if (name != null) {
	                    names.remove(name);
	                }
	                if (out != null) {
	                    writers.remove(out);
	                }
	                try {
	                    socket.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		
		
		if(source==bStop)
		{
			dispose();
			
		}
		
	}

}
