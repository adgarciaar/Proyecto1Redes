package CapturaPaquetes;


import GUI.Ventanas.AnalizadorPaquetes;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import javax.swing.table.DefaultTableModel;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
import java.util.ArrayList;
import java.util.List;
import jpcap.packet.ICMPPacket;

public class PacketContents implements PacketReceiver {

    public static TCPPacket tcp;
    public static UDPPacket udp;
    public static ICMPPacket icmp;

    public static List<Object[]> rowList = new ArrayList<Object[]>();

    public void recievePacket(Packet packet) {
    }

    @Override
    public void receivePacket(Packet packet) {

        if (packet instanceof TCPPacket) {
            tcp = (TCPPacket) packet;

            Object[] row = {AnalizadorPaquetes.No, tcp.length, tcp.src_ip, tcp.dst_ip, "TCP", packet.len};

            rowList.add(new Object[]{AnalizadorPaquetes.No, tcp.length, tcp.src_ip, tcp.dst_ip, "TCP", tcp.src_port, tcp.dst_port,
                tcp.ack, tcp.ack_num, tcp.data, tcp.sequence, tcp.offset, tcp.header});

            DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
            model.addRow(row);
            AnalizadorPaquetes.No++;

        } else if (packet instanceof UDPPacket) {

            udp = (UDPPacket) packet;

            Object[] row = {AnalizadorPaquetes.No, udp.length, udp.src_ip, udp.dst_ip, "UDP",packet.len};
            rowList.add(new Object[]{AnalizadorPaquetes.No, udp.length, udp.src_ip, udp.dst_ip, "UDP", udp.src_port, udp.dst_port,
                udp.data, udp.offset, udp.header});

            DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
            model.addRow(row);
            AnalizadorPaquetes.No++;

        } else if (packet instanceof ICMPPacket) {

            icmp = (ICMPPacket) packet;

            Object[] row = {AnalizadorPaquetes.No, icmp.length, icmp.src_ip, icmp.dst_ip, "ICMP",packet.len};
            rowList.add(new Object[]{AnalizadorPaquetes.No, icmp.length, icmp.src_ip, icmp.dst_ip, "ICMP", icmp.checksum, icmp.header,
                icmp.offset, icmp.orig_timestamp, icmp.recv_timestamp, icmp.trans_timestamp, icmp.data});

            DefaultTableModel model = (DefaultTableModel) AnalizadorPaquetes.jTablePaquetes.getModel();
            model.addRow(row);
            AnalizadorPaquetes.No++;

        }
    }
}
