package naturalwriting;
import java.awt.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class MyPanel extends JPanel{
    String message, path;
    Image img = null;
    BufferedImage b[];
    ImageIcon im = null;
    Graphics gg;
    ArrayList<Integer> list = new ArrayList<Integer>();
    
    public MyPanel(String path, String message) {
        this.message = message;
        this.path = path;
        setBorder(BorderFactory.createLineBorder(Color.black));
        Image image = null;
        try{
            image = ImageIO.read(new File(path));
        }catch(IOException e){
            System.out.println("Error!"+ e+ " File"+path);
        }
        
        for (int i=0;i<message.length();i++){
                System.out.println(message.charAt(i)+" "+(int)message.charAt(i));
            if ((int)message.charAt(i)>=65 && (int)message.charAt(i) <=90){
                list.add(((int)message.charAt(i)-65)+26);
            }else if ((int)message.charAt(i)>=97 && (int)message.charAt(i) <=122){
                list.add((int)message.charAt(i)-97);
            }else if ((int)message.charAt(i)==32){
                list.add(-1);
            }
        }
        
        System.out.println(list);
        //image = createImage(new FilteredImageSource(bi.getSource(),new CropImageFilter(600, 600, 20, 20)));
        BufferedImage buffered = (BufferedImage) image;
        //bi = buffered.getSubimage(0, 0, 100, 100);
        b = new BufferedImage[message.length()];
        for (int i=0;i<list.size();i++){
            if (list.get(i)!=-1)
            b[i] = buffered.getSubimage((list.get(i)%5)*100, (list.get(i)/5)*100, 100, 100);
        }
        //b[0] = buffered.getSubimage(0, 0, 100, 100);
    }

    public Dimension getPreferredSize() {
        return new Dimension(500,400);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);   
        gg = g.create();
        // Draw Text
        int sumX=0, sumY=0;
        for (int i=0;i<list.size();i++){
            if (list.get(i)!=-1){
                g.drawImage(b[i], sumX, sumY, null);
                sumX+=100;
            }else{
                sumY+=100;
                sumX=0;
            }
        }
    }
    
}

class Output{
    Output(String path, String message){
        JFrame jf = new JFrame("Output");
        jf.setLayout(new BorderLayout());
        jf.add(new MyPanel(path, message));
        jf.pack();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
public class NaturalWriting{
    
    JPanel jp = new JPanel();
    final JPanel TextPanel = new JPanel();
    JMenuBar jmb = new JMenuBar();
    JMenu[] jm = new JMenu[2];
    JMenuItem[] jmi = new JMenuItem[5];
    GridBagConstraints gbc = new GridBagConstraints();
    final JTextField jtf = new JTextField(30);
    JButton btn = new JButton("Add");
    final JLabel jlab = new JLabel("Select a new Image from the File menu and type your content above");
    String absPath = null;
    final JFileChooser chooser = new JFileChooser();
    MyPanel mp = null;
    
    NaturalWriting() throws IOException{
        final JFrame jf = new JFrame("Natural Handwriting");
        jf.setLayout(new BorderLayout());
        jm[0] = new JMenu("File");
        jm[1] = new JMenu("Help");
        
        jmb.add(jm[0]);
        jmb.add(jm[1]);
        
        jmi[0] = new JMenuItem("Open Image");
        jmi[1] = new JMenuItem("Save");
        jmi[2] = new JMenuItem("Exit");
        jmi[3] = new JMenuItem("How to use");
        jmi[4] = new JMenuItem("About");
        
        jm[0].add(jmi[0]);
        jm[0].add(jmi[1]);
        jm[0].add(new JSeparator());
        jm[0].add(jmi[2]);
        
        jm[1].add(jmi[3]);
        jm[1].add(new JSeparator());
        jm[1].add(jmi[4]);
        
        jf.add(jmb, BorderLayout.NORTH);
        
        jp.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        jp.add(new JLabel("Enter your text"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        jp.add(jtf, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        jp.add(jlab, gbc);
        
        TextPanel.setLayout(new FlowLayout());
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        jp.add(TextPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        jp.add(btn, gbc);
         
        jf.add(jp, BorderLayout.CENTER);
        
        jmi[0].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                int option = chooser.showOpenDialog(jf);
                if (option == JFileChooser.APPROVE_OPTION){
                    File selectedFile = chooser.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    moveOut(path);
                    jlab.setText("Image Loaded now type your text above and click view");
                }
            }
        });
        
        btn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                String message = jtf.getText();
                //jf.dispose();
                new Output(absPath, message);
            }        
        });
        
        jmi[1].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                JPanel jp1 = new JPanel();
                jp1.setLayout(new FlowLayout());
                jp1.add(mp);
                BufferedImage image;
                try {
                    image = new Robot().createScreenCapture(new Rectangle(jp1.getLocationOnScreen().x, jp1.getLocationOnScreen().y, jp1.getWidth(), jp1.getHeight()));
                    try {
                        ImageIO.write(image, "png", new File("C://sample"));
                    } catch (IOException ex) {
                        Logger.getLogger(NaturalWriting.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (AWTException ex) {
                    Logger.getLogger(NaturalWriting.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
        jf.pack();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
    
    void moveOut(String s){
        absPath = s;
    }
    
    void showDescPane(String path, String message){
        mp = new MyPanel(path, message);
        TextPanel.add(mp);
        TextPanel.repaint();
    }

    public static void main(String[] args) throws IOException {
        new NaturalWriting();
    }
}
