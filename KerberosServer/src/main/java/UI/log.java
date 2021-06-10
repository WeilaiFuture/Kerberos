package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;


public class log {
    private JPanel panel1;

    private void createUIComponents() {
        // TODO: place custom component creation code here

     //   String[] columnNames ={"明文", "密文"};
     //   JTable table = new JTable();
      //  table.addColumn(columnNames);
    }
    static public JTable createTable(){
        JFrame frame=new JFrame("报文日志");
        frame.setSize(1000,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane=frame.getContentPane();
        Object[][] tableDate=new Object[0][3];
        String[] name={"源地址","目的地址","报文类型","密文","明文"};
        JTable table=new JTable(tableDate,name);
        contentPane.add(new JScrollPane(table));
        frame.setVisible(true);
        return table;
    }
    static public void add(JTable table,LinkedList<String[]> list){
        Vector vData = new Vector();
        Vector vName = new Vector();
        Vector vRow;
        vName.add("源地址");
        vName.add("目的地址");
        vName.add("报文类型");
        vName.add("密文");
        vName.add("明文");
        for(int k=0;k<list.size();k++){
            vRow = new Vector();
            vRow.add(list.get(k)[0]);
            vRow.add(list.get(k)[1]);
            vRow.add(list.get(k)[2]);
            vRow.add(list.get(k)[3]);
            vRow.add(list.get(k)[4]);
            vData.add(vRow.clone());
        }
        DefaultTableModel model = new DefaultTableModel(vData, vName);
        table.setModel(model);
    }
        public static void main(String[] agrs) throws InterruptedException {
            JTable table=createTable();
            TimeUnit.SECONDS.sleep(1);

            String[] name={"源地址","目的地址","密文"};
            LinkedList<String[]> list=new LinkedList<String[]>();
            for(int i=0;i<10;i++){
                String []s=new String[3];
                s[0]=i+"0";
                s[1]=i+"1";
                s[2]=i+"2";
             //   s[3]=i+"3";
                list.addFirst(s);
                add(table,list);
                TimeUnit.SECONDS.sleep(1);
            }
        }
}
