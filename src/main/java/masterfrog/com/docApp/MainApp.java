package masterfrog.com.docApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApp {
    public static void main(String[] args) {
        final JFrame jFrame = getFrame();
        final JPanel jPanel = new JPanel();
        jFrame.add(jPanel);
        JButton jButton = new JButton("Преобразовать");
        jPanel.add(jButton);

        final JTextArea jTextArea = new JTextArea(30,65);
        Font font = new Font("Arial",Font.PLAIN,  14);
        jTextArea.setLineWrap(true);
        jTextArea.setEditable(true);
        jTextArea.setFont(font);
        jTextArea.setText("1.3 миллиона долларов\n200 тысяч евро\n 12  фунтов  стерлингов\n2,4 триллиона гривен\n2 миллиарда иен\n3 тенге\n40 юаней\n1.2 тысячи кубических метров газа\n300 кубометров леса\nтире —\n\"кавычки\"");
        jPanel.add(jTextArea);

        JScrollPane jScrollPane = new JScrollPane(jTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jPanel.add(jScrollPane);

        jFrame.setVisible(true);

        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String str = new String();
                Boolean openQuote = true;
                str = jTextArea.getText();
                String regular = "([0-9]+[.|,])?" + //0 или 1 группа из 1ой или больше цифр и . или ,
                                "([0-9]+)" + //группа из 1 или больше цифр
                                "((\\s*\\w*)?)" + //0 или одно слово с пробелом (тысяч, миллионов)
                                "(\\s*доллар\\w*|\\s*евро|\\s*фунт\\w*\\s*стерлинг\\w*|\\s*грив\\w*|\\s*иен\\w*|\\s*тенге|\\s*юан\\w*|\\s*куб\\w*\\s*метр\\w*|\\s*кубометр\\w*)";

                //1,3 миллиона долларов
                //Pattern pattern = Pattern.compile("([0-9])+((.|,)?([0-9]+))*(\\s\\w*)*(\\sдоллар\\w*|\\sевро)", Pattern.UNICODE_CHARACTER_CLASS);
                Pattern pattern = Pattern.compile(regular, Pattern.UNICODE_CHARACTER_CLASS);
                Matcher matcher = pattern.matcher(str);

                String buff = "";
                while(matcher.find()) {
                    System.out.println(matcher.group());
                    if (matcher.group().indexOf("доллар")   > 0) buff = "$";
                    if (matcher.group().indexOf("евро")     > 0) buff = "€";
                    if (matcher.group().indexOf("стерлинг") > 0) buff = "£";
                    if (matcher.group().indexOf("рубл")     > 0) buff = "₽";
                    if (matcher.group().indexOf("грив")     > 0) buff = "₴";
                    if (matcher.group().indexOf("иен")      > 0) buff = "¥";
                    if (matcher.group().indexOf("тенге")    > 0) buff = "₸";
                    if (matcher.group().indexOf("юан")      > 0) buff = "¥";
                    if (matcher.group().indexOf("куб")      > 0) buff = "";

                    //double priceValue = Double.parseDouble(matcher.group().replace(',','.').replaceAll("[^0-9|.]",""));

                    buff = buff + matcher.group().replace('.',',').replaceAll("[^0-9|,]","");

                    if (matcher.group().indexOf("тысяч")    > 0) buff = buff + " тыс";
                    if (matcher.group().indexOf("миллион")  > 0) buff = buff + " млн";
                    if (matcher.group().indexOf("миллиард") > 0) buff = buff + " млрд";
                    if (matcher.group().indexOf("триллион") > 0) buff = buff + " трлн";

                    if (matcher.group().indexOf("куб")      > 0) buff = buff + ". куб.м";

                    str = str.replace(matcher.group(), buff);
                }



                //Замена тире и кавычек
                StringBuilder sbStr = new StringBuilder(str);
                for (int i = 0; i < str.length(); i++) {
                    if (sbStr.charAt(i) == '—') sbStr.setCharAt(i, '-');
                    if (sbStr.charAt(i) == '"') {
                        if (openQuote) sbStr.setCharAt(i, '«'); else sbStr.setCharAt(i, '»');
                        openQuote = !openQuote;
                    }
                }
                jTextArea.setText(sbStr.toString());
            }
        });
    }

    static JFrame getFrame() {
        JFrame jFrame = new JFrame() {};
        //jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(850, 600);
        jFrame.setLocation(200, 200);
        jFrame.setTitle("Подготовщик текста");
        return jFrame;
    }
}
