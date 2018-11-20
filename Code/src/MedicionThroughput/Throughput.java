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

    private long bytesRecibidos; //guardar bytes recibidos actualmente por interface
    private long bytesEnviados; //guardar bytes recibidos actualmente por interface
    private double anchoBanda; //guardar ancho de banda de la interface
    private ArrayList<String> listaInterfaces; //guarda las interfaces de red detectadas
    private int nInterface; //guarda interface de red seleccionada para medición
    
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
    //se ejecuta cada segundo desde medirTrafico() en MedicionThroughput
    //para extraer toda la información necesaria
    @Override
    public void run() {
        //se hace un query sobre localhost a las interfaces de red para extraer
        //ancho de banda y bytes recibidos y enviados
        String host = "localhost"; 
        String connectStr = String.format("winmgmts:\\\\%s\\root\\CIMV2", host);
        String query = "SELECT * FROM Win32_PerfRawData_Tcpip_NetworkInterface"; 
        ActiveXComponent axWMI = new ActiveXComponent(connectStr);
        //Execute the query
        Variant vCollection = axWMI.invoke("ExecQuery", new Variant(query));
        //se guardan las interfaces de red
        EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
        Dispatch item = null;
        //inicializar variables auxiliares
        String BytesReceivedPerSec = null, BytesSentPerSec = null, CurrentBandwidth = null;
        //mediante el ciclo se llega hasta la interface seleccionada y se obtienen datos
        for (int i = 0; i < this.nInterface; i++) {            
            item = enumVariant.nextElement().toDispatch();
            //Dispatch.call returns a Variant which we can convert to a java form.
            BytesReceivedPerSec = Dispatch.call(item, "BytesReceivedPerSec").toString();
            BytesSentPerSec = Dispatch.call(item, "BytesSentPerSec").toString();
            CurrentBandwidth = Dispatch.call(item, "CurrentBandwidth").toString();           
        }
        //se guardan los datos obtenidos en los atributos de la clase
        this.bytesRecibidos = Long.parseLong(BytesReceivedPerSec);
        this.bytesEnviados = Long.parseLong(BytesSentPerSec);
        this.anchoBanda = Double.parseDouble(CurrentBandwidth);        
    }
    //se ejecuta en constructor de MedicionThroughput para listar interfaces
    //de red en el combobox
    public void listarInterfaces() {
        //se ejecuta query sobre localhost para obtener interfaces de red
        String host = "localhost"; 
        String connectStr = String.format("winmgmts:\\\\%s\\root\\CIMV2", host);
        String query = "SELECT * FROM Win32_PerfRawData_Tcpip_NetworkInterface"; 
        ActiveXComponent axWMI = new ActiveXComponent(connectStr);
        //Execute the query
        Variant vCollection = axWMI.invoke("ExecQuery", new Variant(query));
        //se guardan las interfaces de red
        EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
        Dispatch item = null;
        //se inicializa la lista
        this.listaInterfaces = new ArrayList<>();
        //se guarda el nombre de todas las interfaces en la lista
        while (enumVariant.hasMoreElements()) {
            item = enumVariant.nextElement().toDispatch();
            String nombreInterface = Dispatch.call(item, "Name").toString();
            this.listaInterfaces.add(nombreInterface);
        }
    }
}
