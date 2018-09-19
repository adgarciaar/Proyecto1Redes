package MedicionThroughput;


import java.util.TimerTask;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adrian
 */
public class Throughput extends TimerTask {
    
    private int bytesRecibidos;
    private int bytesEnviados;

    public Throughput() {
    }  

    public int getBytesRecibidos() {
        return bytesRecibidos;
    }

    public void setBytesRecibidos(int bytesRecibidos) {
        this.bytesRecibidos = bytesRecibidos;
    }

    public int getBytesEnviados() {
        return bytesEnviados;
    }

    public void setBytesEnviados(int bytesEnviados) {
        this.bytesEnviados = bytesEnviados;
    }
    
    public void run() {
        String host = "localhost"; //Technically you should be able to connect to other hosts, but it takes setup
        String connectStr = String.format("winmgmts:\\\\%s\\root\\CIMV2", host);
	String query = "SELECT * FROM Win32_PerfRawData_Tcpip_NetworkInterface"; //Started = 1 means the service is running.
	ActiveXComponent axWMI = new ActiveXComponent(connectStr); 
	//Execute the query
	Variant vCollection = axWMI.invoke("ExecQuery", new Variant(query));
		
	//Our result is a collection, so we need to work though the.
	EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
	Dispatch item = null;
        
	while (enumVariant.hasMoreElements()) { 
            
            item = enumVariant.nextElement().toDispatch();
            //Dispatch.call returns a Variant which we can convert to a java form.
            String BytesReceivedPerSec = Dispatch.call(item,"BytesReceivedPerSec").toString();
            String BytesSentPerSec = Dispatch.call(item,"BytesSentPerSec").toString();
            
            //System.out.println("BytesReceivedPerSec = " + BytesReceivedPerSec);
            //System.out.println("BytesSentPerSec = " + BytesSentPerSec);
            
            this.bytesRecibidos = Integer.parseInt(BytesReceivedPerSec);
            this.bytesEnviados = Integer.parseInt(BytesSentPerSec);
                        
	}       
    }
    
}