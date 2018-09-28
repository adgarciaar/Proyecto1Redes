package MedicionThroughput;

import java.util.TimerTask;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;
import java.util.ArrayList;

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

    private long bytesRecibidos;
    private long bytesEnviados;
    private double anchoBanda;
    private ArrayList<String> listaInterfaces;
    private int nInterface;

    public Throughput() {
    }

    public long getBytesRecibidos() {
        return bytesRecibidos;
    }

    public void setBytesRecibidos(long bytesRecibidos) {
        this.bytesRecibidos = bytesRecibidos;
    }

    public long getBytesEnviados() {
        return bytesEnviados;
    }

    public void setBytesEnviados(long bytesEnviados) {
        this.bytesEnviados = bytesEnviados;
    }

    public double getAnchoBanda() {
        return anchoBanda;
    }

    public void setAnchoBanda(long anchoBanda) {
        this.anchoBanda = anchoBanda;
    }

    public ArrayList<String> getListaInterfaces() {
        return listaInterfaces;
    }

    public void setListaInterfaces(ArrayList<String> listaInterfaces) {
        this.listaInterfaces = listaInterfaces;
    }

    public int getnInterface() {
        return this.nInterface;
    }

    public void setnInterface(int nInterface) {
        this.nInterface = nInterface;
    }

    @Override
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

        //System.out.println(this.nInterface);
        //int contador = 0;
        //while (enumVariant.hasMoreElements()) { 
        
        String BytesReceivedPerSec = null, BytesSentPerSec = null, CurrentBandwidth = null;
        
        for (int i = 0; i < this.nInterface; i++) {
            //contador +=1;
            //if(contador == this.nInterface){
            item = enumVariant.nextElement().toDispatch();
            //Dispatch.call returns a Variant which we can convert to a java form.
            BytesReceivedPerSec = Dispatch.call(item, "BytesReceivedPerSec").toString();
            BytesSentPerSec = Dispatch.call(item, "BytesSentPerSec").toString();
            CurrentBandwidth = Dispatch.call(item, "CurrentBandwidth").toString();           
        }
        this.bytesRecibidos = Integer.parseInt(BytesReceivedPerSec);
        this.bytesEnviados = Integer.parseInt(BytesSentPerSec);
        this.anchoBanda = Double.parseDouble(CurrentBandwidth);
        System.out.println(Dispatch.call(item, "Name").toString());
    }

    public void listarInterfaces() {

        String host = "localhost"; //Technically you should be able to connect to other hosts, but it takes setup
        String connectStr = String.format("winmgmts:\\\\%s\\root\\CIMV2", host);
        String query = "SELECT * FROM Win32_PerfRawData_Tcpip_NetworkInterface"; //Started = 1 means the service is running.
        ActiveXComponent axWMI = new ActiveXComponent(connectStr);
        //Execute the query
        Variant vCollection = axWMI.invoke("ExecQuery", new Variant(query));

        //Our result is a collection, so we need to work though the.
        EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
        Dispatch item = null;

        this.listaInterfaces = new ArrayList<>();

        while (enumVariant.hasMoreElements()) {

            item = enumVariant.nextElement().toDispatch();
            String nombreInterface = Dispatch.call(item, "Name").toString();
            this.listaInterfaces.add(nombreInterface);
        }

    }

}
