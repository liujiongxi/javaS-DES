import des.DecryptCreator;
import des.EncryptCreator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Window extends JFrame implements ActionListener {
    ForcelyAttack f = new ForcelyAttack();
    JTextField textPlain,textKey,textCipher,textA;//视图
    JTextArea showArea;//视图
    JButton EncryptButton;//控制器
    JButton BruteForceButton;//控制器
    JButton CleanButton;//控制器
    Window () {
        init();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    void init () {
        textPlain = new JTextField(7);
        textKey = new JTextField(7);
        textCipher = new JTextField(7);
        showArea = new JTextArea();
        EncryptButton = new JButton("加密/解密");
        BruteForceButton = new JButton("暴力破解");
        CleanButton = new JButton("重新输入");
        JPanel pNorth = new JPanel();
        pNorth.add(new JLabel("明文"));
        pNorth.add(textPlain);
        pNorth.add(new JLabel("密文"));
        pNorth.add(textCipher);
        pNorth.add(new JLabel("密钥"));
        pNorth.add(textKey);
        pNorth.add(EncryptButton);
        pNorth.add(BruteForceButton);
        pNorth.add(CleanButton);
        EncryptButton.addActionListener(this);
        BruteForceButton.addActionListener(this);
        CleanButton.addActionListener(this);
        add(pNorth,BorderLayout.NORTH);
        add(new JScrollPane(showArea), BorderLayout.CENTER);
        showArea.append("       这是S-DES加密算法，在明文或密文输入框内可以输入一个8bit的二进制字符串，或者位数不大于8位的ASCII码。" +
                 "\n" +   "       根据输入方式的不同，加密/解密或暴力破解的结果会显示在下方。" + "\n" +
                "       密文输入框中必须输入10bit的二进制字符串。" + "\n");
        showArea.append("\n");
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == EncryptButton) {
            try {
                String plainText = textPlain.getText();
                String keyText = textKey.getText();
                String cipherText = textCipher.getText();
                if ((!cipherText.isEmpty()) && (plainText.isEmpty()) && (!keyText.isEmpty())) {
                    methodD(cipherText, keyText);
                }
                else if ((cipherText.isEmpty()) && (!plainText.isEmpty()) && (!keyText.isEmpty())) {
                    methodE(plainText, keyText);
                }
                else if ((!cipherText.isEmpty()) && (!plainText.isEmpty()) && (keyText.isEmpty())) {
                    showArea.append("错误！如果您想进行加密或解密，请删除明文或密文中的一个； "
                            + "\n" +
                            "           如果您想进行暴力破解，请点击暴力破解按钮。"+ "\n");
                }
                else if ((!cipherText.isEmpty()) && (!plainText.isEmpty()) && (!keyText.isEmpty())) {
                    showArea.append("错误！请检查您的输入。"+ "\n");
                }
                else {
                    showArea.append("错误！明文或密文为空"+ "\n");
                }
                //判定输入内容是否为空，来决定采用的的方法
            } catch (Exception ex) {
                showArea.append("错误！请检查输入格式。" + "\n" + ex + "\n");
            }
        } else if (e.getSource() == BruteForceButton) {
            try {
                String plainText = textPlain.getText();
                String cipherText = textCipher.getText();
                String keyText = textKey.getText();
                if ((!cipherText.isEmpty()) && (!plainText.isEmpty()) && (keyText.isEmpty())) {
                    methodBF(plainText, cipherText);
                }
            }
            catch (Exception ex) {
                showArea.append("错误！" + "\n" + ex + "\n");
            }
        } else if (e.getSource() == CleanButton) {
            textPlain.setText("");
            textKey.setText("");
            textCipher.setText("");
        }
    }
    public void methodE(String strP, String strK ){
        String p = strP;
        String key = strK;
        EncryptCreator dir = new EncryptCreator(p,key);
        String c = dir.encrypt();
        showArea.append("已加密："+"\n");
        showArea.append("明文：" + p + "   " + "密钥：" + key + "\n");
        showArea.append("密文：");
        showArea.append(c+"\n");
    }
    public void methodD(String strC, String strK ){
        String c = strC;
        String key = strK;
        DecryptCreator dir = new DecryptCreator(c, key);
        String newp = dir.decrypt();
        showArea.append("已解密："+"\n");
        showArea.append("密文：" + c + "   " + "密钥：" + key + "\n");
        showArea.append("明文：");
        showArea.append(newp+"\n");
    }
    public void methodBF(String strP,String strC) {
        String p = strP;
        String c = strC;
//        ForcelyAttack f = new ForcelyAttack();
        long timestamp1 = System.currentTimeMillis();
//        showArea.append(timestamp1+"\n");
        String keys = f.run(p,c);
        long timestamp2 = System.currentTimeMillis();
//        showArea.append(timestamp2+"\n");
        String elapsedTime = String.valueOf(timestamp2 - timestamp1);
        showArea.append("已用时间： "+elapsedTime+"ms"+"\n");
        String key = f.find();
        showArea.append(keys);
        showArea.append("\n");
        showArea.append(key);
        showArea.append("\n");
    }
}
