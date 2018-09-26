package CapturaPaquetes;


import GUI.Ventanas.AnalizadorPaquetes;
import static GUI.Ventanas.AnalizadorPaquetes.startTime;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import javax.swing.table.DefaultTableModel;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
import java.util.ArrayList;
import java.util.List;
import jpcap.packet.ARPPacket;
import jpcap.packet.ICMPPacket;

public class PacketContents implements PacketReceiver {

    public static TCPPacket tcp;
    public static UDPPacket udp;
    public static ICMPPacket icmp;
    public static ARPPacket arp;

    //public static List<Object[]> rowList = new ArrayList<Object[]>();
    //public static List<Packet> rowList = new ArrayList<>();
    
    @Override
    public void receivePacket(Packet packet) {
        
        //rowList.add(packet);
        DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
        double estimatedTime = (double)System.currentTimeMillis() - AnalizadorPaquetes.startTime;
        estimatedTime = estimatedTime/1000;
        
        AnalizadorPaquetes.packetList.add(packet);
        
        if (packet instanceof TCPPacket) {
            tcp = (TCPPacket) packet;

            Object[] row = {AnalizadorPaquetes.No, estimatedTime, tcp.src_ip, tcp.dst_ip, "TCP", packet.len};
            /*rowList.add(new Object[]{AnalizadorPaquetes.No, tcp.length, tcp.src_ip, tcp.dst_ip, "TCP", tcp.src_port, tcp.dst_port,
                tcp.ack, tcp.ack_num, tcp.data, tcp.sequence, tcp.offset, tcp.header, tcp.protocol});*/
            
            //rowList.add(packet);
            
            //DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
            model.addRow(row);
            AnalizadorPaquetes.No++;

        } else if (packet instanceof UDPPacket) {

            udp = (UDPPacket) packet;

            Object[] row = {AnalizadorPaquetes.No, estimatedTime, udp.src_ip, udp.dst_ip, "UDP",packet.len};
            /*rowList.add(new Object[]{AnalizadorPaquetes.No, udp.length, udp.src_ip, udp.dst_ip, "UDP", udp.src_port, udp.dst_port,
                udp.data, udp.offset, udp.header, udp.protocol});*/
            //rowList.add(packet);

            //DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
            model.addRow(row);
            AnalizadorPaquetes.No++;

        } else if (packet instanceof ICMPPacket) {

            icmp = (ICMPPacket) packet;

            Object[] row = {AnalizadorPaquetes.No, estimatedTime, icmp.src_ip, 
                icmp.dst_ip, "ICMP",packet.len};
            /*rowList.add(new Object[]{AnalizadorPaquetes.No, icmp.length, icmp.src_ip, icmp.dst_ip, "ICMP", icmp.checksum, icmp.header,
                icmp.offset, icmp.orig_timestamp, icmp.recv_timestamp, icmp.trans_timestamp, icmp.data, icmp.protocol});*/
            //rowList.add(packet);

            //DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
            model.addRow(row);
            AnalizadorPaquetes.No++;

        } else if (packet instanceof ARPPacket) {
            arp = (ARPPacket) packet;

            Object[] row = {AnalizadorPaquetes.No, estimatedTime, arp.getSenderHardwareAddress(), 
                arp.getTargetHardwareAddress(), "ARP", packet.len};
            /*rowList.add(new Object[]{AnalizadorPaquetes.No, arp.len, arp.sender_hardaddr, 
                arp.sender_protoaddr,arp.target_hardaddr, arp.target_protoaddr,"ARP", arp.header,arp.data});*/
            //rowList.add(packet);

            //DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
            model.addRow(row);
            AnalizadorPaquetes.No++;
        }
    }
}
