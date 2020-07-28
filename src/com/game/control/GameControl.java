package com.game.control;

import com.game.states.PlayingState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Pattern;

import static com.game.framework.gui.WindowManager.WIDTH;
import static com.game.framework.gui.WindowManager.HEIGHT;

public class GameControl implements ActionListener {

    private PlayingState playingState;
    private JFrame frame;
    private JButton button, button1, button2;
    static JTextPane text;
    private JTextField textField;

    private JPanel panel = new JPanel(new BorderLayout());
    private JPanel mainPanel = new JPanel(new BorderLayout());

    private Queue<String> Commands = new LinkedList<>();
    private int timeUnit = 1;

    Map<String, Integer> variables = new HashMap<>();
    ArrayList<String> whileConditions = new ArrayList<>();
    static int i = 0;

    private boolean ifFlag = false;
    private boolean whileFlag = false;
    private boolean forFlag = false;

    private boolean antrasFor = false;
    private boolean antrasWhile = false;
    ArrayList<String> antrasForKomandos = new ArrayList<>();
    static int j = 0;

    private String[] statement;
    private String[] implement;
    private String[] antrasStatement;
    private String[] antrasImplement;

    static Socket s;
    static ServerSocket ss;
    static InputStreamReader isr;
    static BufferedReader br;
    static String message;

    static Socket s1;
    static ServerSocket ss1;
    static InputStreamReader isr1;
    static BufferedReader br1;
    static String message1;

    static Socket s2;
    static ServerSocket ss2;
    static InputStreamReader isr2;
    static BufferedReader br2;
    static String message2;

    StringBuffer stringBuffer;

    public GameControl(PlayingState playingState) {
        this.playingState = playingState;
        this.initFrame();
        this.initTextArea();
        this.initButton();
        this.initServer();
        this.initServer2();
        this.initServer3();
    }

    private void initServer3() {
        SwingWorker<Void, Void> sw = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    ss2 = new ServerSocket(9001); // nuotrauka
                    while (true) {
                        s2 = ss2.accept();
                        InputStream inputStream = s2.getInputStream();

                        System.out.println("Reading: " + System.currentTimeMillis());

                        byte[] sizeAr = new byte[4];
                        inputStream.read(sizeAr);
                        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
                        System.out.println(size);
                        byte[] imageAr = new byte[size];

                        DataInputStream dataInputStream = new DataInputStream(inputStream);
                        dataInputStream.readFully(imageAr);

                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

                        System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());

                        FilePath filePath = new FilePath();
                        String file = filePath.getFilePath();

                        ImageIO.write(image, "png", new File(file));

                        Image dimg = image.getScaledInstance(image.getWidth() / 3, image.getHeight() / 3, Image.SCALE_SMOOTH);

                        ImageIcon imageIcon = new ImageIcon(dimg);
                        text.insertIcon(imageIcon);
                    }
                    // ss2.close();


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        };

        sw.execute();
    }

    private void initServer2() {

        SwingWorker<Void, Void> sw = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    ss1 = new ServerSocket(9000); // tekstas
                    while (true) {
                        s1 = ss1.accept();
                        isr1 = new InputStreamReader(s1.getInputStream());
                        br1 = new BufferedReader(isr1);
                        stringBuffer = new StringBuffer();
                        text.setText(text.getText() + "Android: ");
                        while ((message1 = br1.readLine()) != null) {
                            stringBuffer.append(message1);
                            text.setText(text.getText() + message1 + "\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        sw.execute();
    }

    public void initServer() {
        SwingWorker<Void, Void> sw = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    ss = new ServerSocket(4000); // gauna
                    while (true) {
                        s = ss.accept();
                        isr = new InputStreamReader(s.getInputStream());
                        br = new BufferedReader(isr);
                        stringBuffer = new StringBuffer();
                        text.setText(text.getText() + "Android komanda: ");
                        while ((message = br.readLine()) != null) {
                            stringBuffer.append(message);
                            text.setText(text.getText() + message + "\n");
                        }

//                        while (Commands.size() != 0) {
//                            System.out.println(Commands.peek());
//                            Commands.remove();
//                        }

                        Commands = splitText();
                        if (Commands.size() != 0) {
                            checkDictionary(Commands);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        sw.execute();
    }

    private void initFrame() {
        this.frame = new JFrame("Game");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        this.frame.setBounds(40, 40, WIDTH / 2, HEIGHT);
        this.frame.add(mainPanel);
    }

    private void initTextArea() {
        this.text = new JTextPane();
        this.text.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        this.mainPanel.add(new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        this.textField = new JTextField();
        this.mainPanel.add(textField, BorderLayout.NORTH);
    }

    private void initButton() {
        this.button = new JButton("Siųsti žinutę");
        this.button.setBackground(new Color(130, 159, 217));
        this.button.setForeground(Color.BLACK);
        this.button.addActionListener(this);
        this.button.setActionCommand("tekstas");
        this.panel.add(button, BorderLayout.SOUTH);

        button1 = new JButton("Siųsti komandą");
        button2 = new JButton("Siųsti nuotrauką");

        button1.setBackground(new Color(130, 159, 217));
        button1.setForeground(Color.BLACK);
        button1.addActionListener(this);
        button1.setActionCommand("komanda");
        panel.add(button1, BorderLayout.CENTER);


        button2.setBackground(new Color(130, 159, 217));
        button2.setForeground(Color.BLACK);
        button2.addActionListener(this);
        button2.setActionCommand("nuotrauka");
        panel.add(button2, BorderLayout.NORTH);

        this.mainPanel.add(panel, BorderLayout.SOUTH);
    }

    private Queue<String> splitText() {

        Commands.clear();
        whileConditions.clear();
        antrasForKomandos.clear();
        variables.clear();
        i = 0;
        j = 0;
        whileFlag = false;
        forFlag = false;
        antrasFor = false;
        antrasWhile = false;

        ArrayList<String> commands = new ArrayList<String>();
        String[] lines = stringBuffer.toString().split("[\n|\r]");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("}[\t ]*[;]+", "}");
            lines[i] = lines[i].replaceAll("\\)[\t ]*[;]+", ");");

            String[] command = lines[i].split("(?<=;)+");
            Collections.addAll(commands, command);
        }
        for (int i = 0; i < commands.size(); i++) {
            String[] naujas = commands.get(i).split("(?<=[(])|(?=[)])|(?<=[{])|(?=[}])|(?=[{])|(?<=[}])");
            commands.remove(i);

            for (int j = 0; j < naujas.length; j++) {
                commands.add(i + j, naujas[j]);
            }
            commands.set(i, commands.get(i).replaceAll("^[\t ]+|[\t ]+$", ""));
        }
        Commands.addAll(commands);

        return Commands;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "tekstas":
                try {
                    Socket s = new Socket("192.168.0.104", 9000);
                    PrintWriter pw = new PrintWriter(s.getOutputStream());
                    pw.write(textField.getText());
                    pw.flush();
                    pw.close();
                    s.close();
                    text.setText(text.getText() + "Vartotojas: " + textField.getText() + "\n");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;

            case "komanda":
                text.setText(text.getText() + "Vartotojo komanda: " + textField.getText() + "\n");
                message = textField.getText();
                stringBuffer = new StringBuffer();
                stringBuffer.append(message);
                Commands = splitText();
                if (Commands.size() != 0) {
                    checkDictionary(Commands);
                }
                break;

            case "nuotrauka":

                FilePath filePath = new FilePath();
                String file = filePath.getFilePath();

                if (file != null) {
                    try {
                        Socket s = new Socket("192.168.0.104", 9005);
                        DataOutputStream os = new DataOutputStream(s.getOutputStream());

                        File input_file = new File(file);
                        BufferedImage image = ImageIO.read(input_file);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);

                        os.writeInt(baos.toByteArray().length);
                        os.write(baos.toByteArray());
                        os.flush();

                        ImageIcon imageIcon = new ImageIcon(file);
                        text.insertIcon(imageIcon);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

        }
    }

    ThreadListener listener = new ThreadListener() {
        @Override
        public void threadFinished() {
            if (antrasFor) {
                System.out.println("i reiksme: " + antrasForKomandos.size());
                if (antrasForKomandos.size() != i) {
                    System.out.println("PRADEDAAAAM");
                    executeWhile(antrasForKomandos, statement);
                } else {
                    addVariable(antrasImplement);
                    System.out.println("Nauja reiksme2: " + variables.get("j"));
                    System.out.println("STATEMENT2: " + isTrue(antrasStatement));
                    i = 0;
                    if (isTrue(antrasStatement)) {
                        executeWhile(antrasForKomandos, antrasStatement);
                    } else {
                        System.out.println("IS NAUJO PRADEDAMAS PIRMAS FOR");
                        antrasForKomandos.clear();
                        antrasFor = false;
                        i = j;
                        executeWhile(whileConditions, antrasStatement);
                    }
                }
            } else if (antrasWhile) {
                System.out.println("Antros komandos i: " + i);
                System.out.println("Antros komandos size: " + antrasForKomandos.size());
                if (antrasForKomandos.size() != i) {
                    executeWhile(antrasForKomandos, statement);
                } else {
                    i = 0;
                    System.out.println("AR TRUE AR FALSE: " + isTrue(antrasStatement));
                    if (isTrue(antrasStatement)) {
                        System.out.println("ATEJAU");
                        executeWhile(antrasForKomandos, antrasStatement);
                    } else {
                        antrasForKomandos.clear();
                        antrasWhile = false;
                        i = j;
                        executeWhile(whileConditions, antrasStatement);
                    }
                }
            } else if (forFlag) {
                if (whileConditions.size() != i) {
                    executeWhile(whileConditions, statement);
                } else {
                    addVariable(implement);
                    System.out.println("Nauja reiksme: " + variables.get("i"));
                    if (isTrue(statement)) {
                        System.out.println("STATEMENT: " + isTrue(statement));
                        i = 0;
                        executeWhile(whileConditions, statement);
                    } else {
                        forFlag = false;
                        if (Commands.size() != 0) {
                            checkDictionary(Commands);
                        }
                    }
                }
            } else if (!whileFlag) {
                if (Commands.size() != 0) {
                    System.out.println("IEJO");
                    timeUnit = 1;
                    checkDictionary(Commands);
                }
            } else {
                if (whileConditions.size() != i) {
                    executeWhile(whileConditions, statement);
                } else {
                    if (isTrue(statement)) {
                        i = 0;
                        executeWhile(whileConditions, statement);
                    } else if (Commands.size() != 0) {
                        whileFlag = false;
                        checkDictionary(Commands);
                    }
                }
            }
        }
    };

    private void checkDictionary(Queue<String> Commands) {

        if (Pattern.compile("eitiK[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Pattern.compile("[0-9]+").matcher(Commands.peek()).matches()) {
                timeUnit = Integer.parseInt(Commands.peek());
                System.out.println("PASALINO2: " + Commands.peek());
                Commands.remove();
            } else if (Commands.size() != 0 && Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(Commands.peek()).matches()) {
                if (variables.containsKey(Commands.peek()) && variables.get(Commands.peek()) >= 0) {
                    timeUnit = variables.get(Commands.peek());
                    System.out.println("YRA TOKS KINTAMASIS " + Commands.peek());
                    Commands.remove();
                } else {
                    new IllegalCode(Commands.peek());
                    return;
                }
            }
            if (Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread left = new Thread(() -> {
                    try {
                        playingState.player.setMovingLeft(true);
                        System.out.println("kaire started");
                        Thread.sleep(220 * timeUnit);
                        System.out.println("kaire ended");
                        playingState.player.setMovingLeft(false);
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                left.start();
            } else {
                new IllegalCode(Commands.peek());
            }


        } else if (Pattern.compile("eitiD[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Pattern.compile("[0-9]+").matcher(Commands.peek()).matches()) {
                timeUnit = Integer.parseInt(Commands.peek());
                System.out.println("PASALINO2: " + Commands.peek());
                Commands.remove();
            } else if (Commands.size() != 0 && Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(Commands.peek()).matches()) {
                if (variables.containsKey(Commands.peek())) {
                    timeUnit = variables.get(Commands.peek());
                    System.out.println("YRA TOKS KINTAMASIS " + Commands.peek());
                    Commands.remove();
                } else {
                    new IllegalCode(Commands.peek());
                    return;
                }
            }
            if (Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread right = new Thread(() -> {
                    try {
                        playingState.player.setMovingRight(true);
                        System.out.println("desine started");
                        Thread.sleep(220 * timeUnit);
                        System.out.println("desine ended");
                        playingState.player.setMovingRight(false);
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                right.start();
            } else {
                new IllegalCode(Commands.peek());
            }


        } else if (Pattern.compile("eitiA[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Pattern.compile("[0-9]+").matcher(Commands.peek()).matches()) {
                timeUnit = Integer.parseInt(Commands.peek());
                System.out.println("PASALINO2: " + Commands.peek());
                Commands.remove();
            } else if (Commands.size() != 0 && Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(Commands.peek()).matches()) {
                if (variables.containsKey(Commands.peek())) {
                    timeUnit = variables.get(Commands.peek());
                    System.out.println("YRA TOKS KINTAMASIS " + Commands.peek());
                    Commands.remove();
                } else {
                    new IllegalCode(Commands.peek());
                    return;
                }
            }
            if (Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread up = new Thread(() -> {
                    try {
                        playingState.player.setMovingUp(true);
                        System.out.println("up started");
                        Thread.sleep(220 * timeUnit);
                        System.out.println("up ended");
                        playingState.player.setMovingUp(false);
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                up.start();
            } else {
                new IllegalCode(Commands.peek());
            }


        } else if (Pattern.compile("eitiZ[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Pattern.compile("[0-9]+").matcher(Commands.peek()).matches()) {
                timeUnit = Integer.parseInt(Commands.peek());
                System.out.println("PASALINO2: " + Commands.peek());
                Commands.remove();
            } else if (Commands.size() != 0 && Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(Commands.peek()).matches()) {
                if (variables.containsKey(Commands.peek())) {
                    timeUnit = variables.get(Commands.peek());
                    System.out.println("YRA TOKS KINTAMASIS " + Commands.peek());
                    Commands.remove();
                } else {
                    new IllegalCode(Commands.peek());
                    return;
                }
            }
            if (Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread down = new Thread(() -> {
                    try {
                        playingState.player.setMovingDown(true);
                        System.out.println("up started");
                        Thread.sleep(220 * timeUnit);
                        System.out.println("up ended");
                        playingState.player.setMovingDown(false);
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                down.start();
            } else {
                new IllegalCode(Commands.peek());
            }

        } else if (Pattern.compile("isNaujo[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread restart = new Thread(() -> {
                    try {
                        playingState.generateLevel();
                        System.out.println("restart started");
                        Thread.sleep(500);
                        System.out.println("restart ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                restart.start();
            } else {
                new IllegalCode(Commands.peek());
            }

        } else if (Pattern.compile("pulti[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread attack = new Thread(() -> {
                    try {
                        playingState.player.attack();
                        System.out.println("attack started");
                        Thread.sleep(500);
                        System.out.println("attack ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                attack.start();
            } else {
                new IllegalCode(Commands.peek());
            }

        } else if (Pattern.compile("sunaikinti[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("KAS PER NESAMONE");
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread killAll = new Thread(() -> {
                    try {
                        playingState.deleteEnemies();
                        System.out.println("delete started");
                        Thread.sleep(500);
                        System.out.println("delete ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                killAll.start();
            } else {
                new IllegalCode(Commands.peek());
            }

        } else if (Pattern.compile("atgaivinti[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread regenerate = new Thread(() -> {
                    try {
                        playingState.player.regenerateHealth();
                        System.out.println("regenerate started");
                        Thread.sleep(500);
                        System.out.println("dregenerate ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                regenerate.start();
            } else {
                new IllegalCode(Commands.peek());
            }

        } else if (Pattern.compile("pridetiApsauga[ \t]*\\(").matcher(Commands.peek()).matches()) {
            System.out.println("PASALINO1: " + Commands.peek());
            Commands.remove();
            if (Commands.size() != 0 && Pattern.compile("[0-9]+").matcher(Commands.peek()).matches()) {
                timeUnit = Integer.parseInt(Commands.peek());
                System.out.println("PASALINO2: " + Commands.peek());
                Commands.remove();
            } else if (Commands.size() != 0 && Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(Commands.peek()).matches()) {
                if (variables.containsKey(Commands.peek())) {
                    timeUnit = variables.get(Commands.peek());
                    System.out.println("YRA TOKS KINTAMASIS " + Commands.peek());
                    Commands.remove();
                } else {
                    new IllegalCode(Commands.peek());
                    return;
                }
            }
            if (Commands.size() != 0 && Pattern.compile("\\);").matcher(Commands.peek()).matches()) {
                System.out.println("PASALINO69: " + Commands.peek());
                Commands.remove();
                Thread addArmor = new Thread(() -> {
                    try {
                        playingState.player.addArmor(timeUnit);
                        System.out.println("addArmor started");
                        Thread.sleep(500);
                        System.out.println("AddArmor ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                addArmor.start();
            } else {
                new IllegalCode(Commands.peek());
            }

        } else if (Pattern.compile("(int)[ \t]+[a-zA-Z][a-zA-Z0-9]*[ \t]*(=[ \t]*(-?[1-9]\\d*|0)[ \t]*)?;").matcher(Commands.peek()).matches()) {
            addVariable(Commands.peek().split("\\s+"));
            Commands.remove();
            listener.threadFinished();

        } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*[ \t]*(=[ \t]*(-?[1-9]\\d*|0)[ \t]*)?;").matcher(Commands.peek()).matches()) {
            addVariable(Commands.peek().split("\\s+"));
            Commands.remove();
            listener.threadFinished();


        } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*[+]{2};").matcher(Commands.peek()).matches()) {
            addVariable(Commands.peek().split("\\s+"));
            Commands.remove();
            listener.threadFinished();
        } else if (Pattern.compile("if[ \t]*\\(").matcher(Commands.peek()).matches()) {
            Commands.remove();
            if (isTrue(Commands.peek().split("\\s+"))) {
                Commands.remove();
                if (Commands.size() != 0 && Pattern.compile("\\)").matcher(Commands.peek()).matches()) {
                    Commands.remove();
                    if (Commands.size() != 0 && Pattern.compile("\\{").matcher(Commands.peek()).matches()) {
                        Commands.remove();
                        ifFlag = false;
                        listener.threadFinished();
                    }
                }
            } else {
                while (Commands.peek() != null && !Commands.peek().equals("}")) {
                    Commands.remove();
                }
                ifFlag = true;
                listener.threadFinished();
            }
        } else if (Commands.size() != 0 && Pattern.compile("}").matcher(Commands.peek()).matches()) {
            Commands.remove();
            listener.threadFinished();
        } else if (Pattern.compile("else").matcher(Commands.peek()).matches()) {
            if (ifFlag) {
                Commands.remove();
                if (Commands.size() != 0 && Pattern.compile("\\{").matcher(Commands.peek()).matches()) {
                    Commands.remove();
                    ifFlag = false;
                    listener.threadFinished();
                }
            } else {
                while (Commands.peek() != null && !Commands.peek().equals("}")) {
                    Commands.remove();
                }
                ifFlag = false;
                listener.threadFinished();
            }

        } else if (Pattern.compile("for[ \t]*\\(").matcher(Commands.peek()).matches()) {
            Commands.remove();
            addVariable(Commands.peek().split("\\s+"));
            Commands.remove();
            statement = Commands.peek().split("\\s+");
            Commands.remove();
            if (isTrue(statement)) {
                implement = Commands.peek().split("\\s+");
                Commands.remove();
                Commands.remove();
                if (Commands.size() != 0 && Pattern.compile("\\{").matcher(Commands.peek()).matches()) {
                    Commands.remove();
                    while (!Commands.peek().equals("}")) {
                        if (Commands.peek().equals("{")) {
                            while (!Commands.peek().equals("}")) {
                                whileConditions.add(Commands.peek());
                                Commands.remove();
                            }
                        }
                        whileConditions.add(Commands.peek());
                        Commands.remove();
                    }
                    whileConditions.add(Commands.peek());
                    Commands.remove();
                    forFlag = true;
                    executeWhile(whileConditions, statement);
                    listener.threadFinished();
                }
            }
            while (Commands.peek() != null && !Commands.peek().equals("}"))
                Commands.remove();

            ifFlag = false;
            listener.threadFinished();
        } else if (Pattern.compile("while[ \t]*\\(").matcher(Commands.peek()).matches()) {
            Commands.remove();
            statement = Commands.peek().split("\\s+");
            if (isTrue(statement)) {
                System.out.println(isTrue(statement));
                Commands.remove();
                Commands.remove();
                if (Commands.size() != 0 && Pattern.compile("\\{").matcher(Commands.peek()).matches()) {
                    Commands.remove();
                    while (!Commands.peek().equals("}")) {
                        if (Commands.peek().equals("{")) {
                            while (!Commands.peek().equals("}")) {
                                whileConditions.add(Commands.peek());
                                Commands.remove();
                            }
                        }
                        whileConditions.add(Commands.peek());
                        Commands.remove();
                    }
                    whileFlag = true;
                    executeWhile(whileConditions, statement);
                    listener.threadFinished();
                }
            }
            while (Commands.peek() != null && !Commands.peek().equals("}"))
                Commands.remove();

            ifFlag = false;
            listener.threadFinished();

        } else {
            if (Commands.size() != 0) {
                Commands.remove();
            }

            if (Commands.size() != 0) {
                listener.threadFinished();
            }
        }
    }

    public void addVariable(String[] line) {
        switch (line.length) {
            case 1:
                if (line[0].substring(line[0].length() - 1).equals(";")) {
                    variables.replace(line[0].replaceAll("[+]{2}?;", ""), (variables.get(line[0].replaceAll("[+]{2}?;", ""))) + 1);
                } else {
                    variables.replace(line[0].replaceAll("[+]{2}?", ""), (variables.get(line[0].replaceAll("[+]{2}?", ""))) + 1);
                }
                break;

            case 2:
                System.out.println(line[1].replaceAll(";", ""));
                variables.put(line[1].replaceAll(";", ""), 0);
                break;

            case 3:
                System.out.println(line[0] + " = " + Integer.parseInt(line[2].replaceAll(";", "")));
                variables.put(line[0], Integer.parseInt(line[2].replaceAll(";", "")));
                break;

            case 4:

            case 5:
                System.out.println(line[1] + " = " + Integer.parseInt(line[3].replaceAll(";", "")));
                variables.put(line[1], Integer.parseInt(line[3].replaceAll(";", "")));
                break;
        }
    }

    public boolean isTrue(String[] line) {
        if (line.length == 3) {
            if ((Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(line[0]).matches() && Pattern.compile("[-0-9]+").matcher(line[2]).matches())) {
                switch (line[1]) {
                    case ">":
                        if (isGreater(line[0], Integer.parseInt(line[2]))) return ifFlag = true;
                        else return ifFlag = false;
                    case ">=":
                        if (isGreaterEqual(line[0], Integer.parseInt(line[2]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "<":
                        if (isLess(line[0], Integer.parseInt(line[2]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "<=":
                        if (isLessEqual(line[0], Integer.parseInt(line[2]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "!=":
                        if (isNotEqual(line[0], Integer.parseInt(line[2]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "==":
                        if (isEqual(line[0], Integer.parseInt(line[2]))) return ifFlag = true;
                        else return ifFlag = false;
                    default:
                        return ifFlag = false;
                }

            } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(line[2]).matches() && Pattern.compile("[-0-9]+").matcher(line[0]).matches()) {
                switch (line[1]) {
                    case ">":
                        if (isLess(line[2], Integer.parseInt(line[0]))) return ifFlag = true;
                        else return ifFlag = false;
                    case ">=":
                        if (isLessEqual(line[2], Integer.parseInt(line[0]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "<":
                        if (isGreater(line[2], Integer.parseInt(line[0]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "<=":
                        if (isGreaterEqual(line[2], Integer.parseInt(line[0]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "!=":
                        if (isNotEqual(line[2], Integer.parseInt(line[0]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "==":
                        if (isEqual(line[2], Integer.parseInt(line[0]))) return ifFlag = true;
                        else return ifFlag = false;
                    default:
                        return ifFlag = false;
                }
            } else if (Pattern.compile("[-0-9]+").matcher(line[0]).matches() && Pattern.compile("[-0-9]+").matcher(line[2]).matches()) {
                switch (line[1]) {
                    case ">":
                        if (isGreater(Integer.parseInt(line[0]), Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    case ">=":
                        if (isGreaterEqual(Integer.parseInt(line[0]), Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    case "<":
                        if (isLess(Integer.parseInt(line[0]), Integer.parseInt(line[2]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "<=":
                        if (isLessEqual(Integer.parseInt(line[0]), Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    case "!=":
                        if (isNotEqual(Integer.parseInt(line[0]), Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    case "==":
                        if (isEqual(Integer.parseInt(line[0]), Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    default:
                        return ifFlag = false;
                }
            } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(line[0]).matches() && Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(line[2]).matches()) {
                switch (line[1]) {
                    case ">":
                        if (isGreater(line[0], line[2])) return ifFlag = true;
                        else return ifFlag = false;
                    case ">=":
                        if (isGreaterEqual(line[0], line[2])) return ifFlag = true;
                        else return ifFlag = false;
                    case "<":
                        if (isLess(line[0], line[2])) return ifFlag = true;
                        else return ifFlag = false;
                    case "<=":
                        if (isLessEqual(line[0], line[2])) return ifFlag = true;
                        else return ifFlag = false;
                    case "!=":
                        if (isNotEqual(line[0], line[2])) return ifFlag = true;
                        else return ifFlag = false;
                    case "==":
                        if (isEqual(line[0], line[2])) return ifFlag = true;
                        else return ifFlag = false;
                    default:
                        return ifFlag = false;
                }
            } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(line[0]).matches() && Pattern.compile("[-0-9]+;").matcher(line[2]).matches()) {
                line[2] = line[2].replaceAll(";", "");
                switch (line[1]) {
                    case ">":
                        if (isGreater(line[0], Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    case ">=":
                        if (isGreaterEqual(line[0], Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    case "<":
                        if (isLess(line[0], Integer.parseInt(line[2]))) return ifFlag = true;
                        else return ifFlag = false;
                    case "<=":
                        if (isLessEqual(line[0], Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    case "!=":
                        if (isNotEqual(line[0], Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    case "==":
                        if (isEqual(line[0], Integer.parseInt(line[2])))
                            return ifFlag = true;
                        else return ifFlag = false;
                    default:
                        return ifFlag = false;
                }
            }
        }
        return ifFlag = false;
    }

    public boolean isGreater(String var, int num) {
        if (variables.containsKey(var)) {
            if (variables.get(var) > num) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }

    public boolean isGreater(int num1, int num2) {
        if (num1 > num2) return ifFlag = true;
        else return ifFlag = false;
    }

    public boolean isGreater(String var1, String var2) {
        System.out.println("WOW");
        if (variables.containsKey(var1) && variables.containsKey(var2)) {
            if (variables.get(var1) > variables.get(var2)) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }


    public boolean isGreaterEqual(String var, int num) {
        if (variables.containsKey(var)) {
            if (variables.get(var) >= num) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }

    public boolean isGreaterEqual(int num1, int num2) {
        if (num1 >= num2) return ifFlag = true;
        else return ifFlag = false;
    }

    public boolean isGreaterEqual(String var1, String var2) {
        if (variables.containsKey(var1) && variables.containsKey(var2)) {
            if (variables.get(var1) >= variables.get(var2)) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }


    public boolean isLess(String var, int num) {
        if (variables.containsKey(var)) {
            if (variables.get(var) < num) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }

    public boolean isLess(int num1, int num2) {
        if (num1 < num2) return ifFlag = true;
        else return ifFlag = false;
    }

    public boolean isLess(String var1, String var2) {
        if (variables.containsKey(var1) && variables.containsKey(var2)) {
            if (variables.get(var1) < variables.get(var2)) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }


    public boolean isLessEqual(String var, int num) {
        if (variables.containsKey(var)) {
            if (variables.get(var) <= num) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }

    public boolean isLessEqual(int num1, int num2) {
        if (num1 < num2) return ifFlag = true;
        else return ifFlag = false;
    }

    public boolean isLessEqual(String var1, String var2) {
        if (variables.containsKey(var1) && variables.containsKey(var2)) {
            if (variables.get(var1) <= variables.get(var2)) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }


    public boolean isEqual(String var, int num) {
        if (variables.containsKey(var)) {
            if (variables.get(var) == num) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }

    public boolean isEqual(int num1, int num2) {
        if (num1 == num2) return ifFlag = true;
        else return ifFlag = false;
    }

    public boolean isEqual(String var1, String var2) {
        if (variables.containsKey(var1) && variables.containsKey(var2)) {
            if (variables.get(var1).equals(variables.get(var2))) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }


    public boolean isNotEqual(String var, int num) {
        if (variables.containsKey(var)) {
            if (variables.get(var) != num) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }

    public boolean isNotEqual(int num1, int num2) {
        if (num1 != num2) return ifFlag = true;
        else return ifFlag = false;
    }

    public boolean isNotEqual(String var1, String var2) {
        if (variables.containsKey(var1) && variables.containsKey(var2)) {
            if (!variables.get(var1).equals(variables.get(var2))) return ifFlag = true;
            else return ifFlag = false;
        }
        return false;
    }

    private void executeWhile(ArrayList<String> whileConditions, String[] statement) {

        if (Pattern.compile("eitiK[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("[0-9]+").matcher(whileConditions.get(i)).matches()) {
                timeUnit = Integer.parseInt(whileConditions.get(i));
                System.out.println("PASALINO2: " + whileConditions.get(i));
                i++;
            } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(whileConditions.get(i)).matches()) {
                if (variables.containsKey(whileConditions.get(i)) && variables.get(whileConditions.get(i)) >= 0) {
                    timeUnit = variables.get(whileConditions.get(i));
                    System.out.println("YRA TOKS KINTAMASIS " + whileConditions.get(i));
                    i++;
                } else {
                    new IllegalCode(whileConditions.get(i));
                    return;
                }
            }
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread left = new Thread(() -> {
                    try {
                        playingState.player.setMovingLeft(true);
                        System.out.println("kaire2 started");
                        Thread.sleep(220 * timeUnit);
                        System.out.println("kaire2 ended");
                        playingState.player.setMovingLeft(false);
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                left.start();
                try {
                    left.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }


        } else if (Pattern.compile("eitiD[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("[0-9]+").matcher(whileConditions.get(i)).matches()) {
                timeUnit = Integer.parseInt(whileConditions.get(i));
                System.out.println("PASALINO2: " + whileConditions.get(i));
                i++;
            } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(whileConditions.get(i)).matches()) {
                if (variables.containsKey(whileConditions.get(i))) {
                    timeUnit = variables.get(whileConditions.get(i));
                    System.out.println("YRA TOKS KINTAMASIS " + whileConditions.get(i));
                    i++;
                } else {
                    new IllegalCode(whileConditions.get(i));
                    return;
                }
            }
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread right = new Thread(() -> {
                    try {
                        playingState.player.setMovingRight(true);
                        System.out.println("desine started");
                        Thread.sleep(220 * timeUnit);
                        System.out.println("desine ended");
                        playingState.player.setMovingRight(false);
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                right.start();
                try {
                    right.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }


        } else if (Pattern.compile("eitiA[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("[0-9]+").matcher(whileConditions.get(i)).matches()) {
                timeUnit = Integer.parseInt(whileConditions.get(i));
                System.out.println("PASALINO2: " + whileConditions.get(i));
                i++;
            } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(whileConditions.get(i)).matches()) {
                if (variables.containsKey(whileConditions.get(i))) {
                    timeUnit = variables.get(whileConditions.get(i));
                    System.out.println("YRA TOKS KINTAMASIS " + whileConditions.get(i));
                    i++;
                } else {
                    new IllegalCode(whileConditions.get(i));
                    return;
                }
            }
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread up = new Thread(() -> {
                    try {
                        playingState.player.setMovingUp(true);
                        System.out.println("up started");
                        Thread.sleep(220 * timeUnit);
                        System.out.println("up ended");
                        playingState.player.setMovingUp(false);
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                up.start();
                try {
                    up.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }


        } else if (Pattern.compile("eitiZ[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("[0-9]+").matcher(whileConditions.get(i)).matches()) {
                timeUnit = Integer.parseInt(whileConditions.get(i));
                System.out.println("PASALINO2: " + whileConditions.get(i));
                i++;
            } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(whileConditions.get(i)).matches()) {
                if (variables.containsKey(whileConditions.get(i))) {
                    timeUnit = variables.get(whileConditions.get(i));
                    System.out.println("YRA TOKS KINTAMASIS " + whileConditions.get(i));
                    i++;
                } else {
                    new IllegalCode(whileConditions.get(i));
                    return;
                }
            }
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread down = new Thread(() -> {
                    try {
                        playingState.player.setMovingDown(true);
                        System.out.println("up started");
                        Thread.sleep(220 * timeUnit);
                        System.out.println("up ended");
                        playingState.player.setMovingDown(false);
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                down.start();
                try {
                    down.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }

        } else if (Pattern.compile("isNaujo[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread restart = new Thread(() -> {
                    try {
                        playingState.generateLevel();
                        System.out.println("restart started");
                        Thread.sleep(500);
                        System.out.println("restart ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                restart.start();
                try {
                    restart.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }

        } else if (Pattern.compile("pulti[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread attack = new Thread(() -> {
                    try {
                        playingState.player.attack();
                        System.out.println("attack started");
                        Thread.sleep(500);
                        System.out.println("attack ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                attack.start();
                try {
                    attack.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }

        } else if (Pattern.compile("sunaikinti[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("KAS PER NESAMONE");
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread killAll = new Thread(() -> {
                    try {
                        playingState.deleteEnemies();
                        System.out.println("delete started");
                        Thread.sleep(500);
                        System.out.println("delete ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                killAll.start();
                try {
                    killAll.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }

        } else if (Pattern.compile("atgaivinti[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread regenerate = new Thread(() -> {
                    try {
                        playingState.player.regenerateHealth();
                        System.out.println("regenerate started");
                        Thread.sleep(500);
                        System.out.println("dregenerate ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                regenerate.start();
                try {
                    regenerate.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }

        } else if (Pattern.compile("pridetiApsauga[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            System.out.println("PASALINO1: " + whileConditions.get(i));
            i++;
            if (Pattern.compile("[0-9]+").matcher(whileConditions.get(i)).matches()) {
                timeUnit = Integer.parseInt(whileConditions.get(i));
                System.out.println("PASALINO2: " + whileConditions.get(i));
                i++;
            } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(whileConditions.get(i)).matches()) {
                if (variables.containsKey(whileConditions.get(i))) {
                    timeUnit = variables.get(whileConditions.get(i));
                    System.out.println("YRA TOKS KINTAMASIS " + whileConditions.get(i));
                    i++;
                } else {
                    new IllegalCode(whileConditions.get(i));
                    return;
                }
            }
            if (Pattern.compile("\\);").matcher(whileConditions.get(i)).matches()) {
                System.out.println("PASALINO69: " + whileConditions.get(i));
                i++;
                Thread addArmor = new Thread(() -> {
                    try {
                        playingState.player.addArmor(timeUnit);
                        System.out.println("addArmor started");
                        Thread.sleep(500);
                        System.out.println("AddArmor ended");
                        listener.threadFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                addArmor.start();
                try {
                    addArmor.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                new IllegalCode(whileConditions.get(i));
            }

        } else if (Pattern.compile("(int)[ \t]+[a-zA-Z][a-zA-Z0-9]*[ \t]*(=[ \t]*(-?[1-9]\\d*|0)[ \t]*)?;").matcher(whileConditions.get(i)).matches()) {
            addVariable(whileConditions.get(i).split("\\s+"));
            i++;
            listener.threadFinished();
        } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*[ \t]*(=[ \t]*(-?[1-9]\\d*|0)[ \t]*)?;").matcher(whileConditions.get(i)).matches()) {
            addVariable(whileConditions.get(i).split("\\s+"));
            i++;
            listener.threadFinished();
        } else if (Pattern.compile("[a-zA-Z][a-zA-Z0-9]*[+]{2};").matcher(whileConditions.get(i)).matches()) {
            addVariable(whileConditions.get(i).split("\\s+"));
            i++;
            listener.threadFinished();
        } else if (Pattern.compile("}").matcher(whileConditions.get(i)).matches() || Pattern.compile("\\{").matcher(whileConditions.get(i)).matches()) {
            i++;
            listener.threadFinished();
        } else if (Pattern.compile("if[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            i++;
            if (isTrue(whileConditions.get(i).split("\\s+"))) {
                i++;
                if (Pattern.compile("\\)").matcher(whileConditions.get(i)).matches()) {
                    i++;
                    if (Pattern.compile("\\{").matcher(whileConditions.get(i)).matches()) {
                        ifFlag = false;
                        listener.threadFinished();
                    }
                }
            } else {
                while (!whileConditions.get(i).equals("}")) {
                    i++;
                }
                ifFlag = true;
                listener.threadFinished();
            }
        } else if (Pattern.compile("else").matcher(whileConditions.get(i)).matches()) {
            if (ifFlag) {
                i++;
                if (Pattern.compile("\\{").matcher(whileConditions.get(i)).matches()) {
                    i++;
                    ifFlag = false;
                    listener.threadFinished();
                }
            } else {
                while (!whileConditions.get(i).equals("}")) {
                    i++;
                }
                ifFlag = false;
                listener.threadFinished();
            }
        } else if (Pattern.compile("for[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            if (antrasFor) {
                i = 0;
                listener.threadFinished();
            }
            i++;
            addVariable(whileConditions.get(i).split("\\s+"));
            i++;
            antrasStatement = whileConditions.get(i).split("\\s+");
            i++;
            if (isTrue(antrasStatement)) {
                antrasImplement = whileConditions.get(i).split("\\s+");
                i++;
                i++;
                if (Pattern.compile("\\{").matcher(whileConditions.get(i)).matches()) {
                    i++;
                    while (!whileConditions.get(i).equals("}")) {
                        if (whileConditions.get(i).equals("{")) {
                            while (!whileConditions.get(i).equals("}")) {
                                antrasForKomandos.add(whileConditions.get(i));
                                i++;
                            }
                        }
                        antrasForKomandos.add(whileConditions.get(i));
                        i++;
                    }
                    antrasFor = true;
                    j = i;
                    i = 0;
                    listener.threadFinished();
                }
            }
            i = j;
            while (!whileConditions.get(i).equals("}"))
                i++;
            ifFlag = false;
            listener.threadFinished();
        } else if (Pattern.compile("while[ \t]*\\(").matcher(whileConditions.get(i)).matches()) {
            if (antrasWhile) {
                i = 0;
                listener.threadFinished();
            }
            i++;
            antrasStatement = whileConditions.get(i).split("\\s+");
            i++;
            if (isTrue(antrasStatement)) {
                i++;
                if (Pattern.compile("\\{").matcher(whileConditions.get(i)).matches()) {
                    i++;
                    while (!whileConditions.get(i).equals("}")) {
                        if (whileConditions.get(i).equals("{")) {
                            while (!whileConditions.get(i).equals("}")) {
                                antrasForKomandos.add(whileConditions.get(i));
                                i++;
                            }
                        }
                        antrasForKomandos.add(whileConditions.get(i));
                        i++;
                    }
                    antrasWhile = true;
                    j = i;
                    i = 0;
                    listener.threadFinished();
                }
            }
            i = j;
            while (!whileConditions.get(i).equals("}"))
                i++;
            ifFlag = false;
            listener.threadFinished();
        }

    }
}