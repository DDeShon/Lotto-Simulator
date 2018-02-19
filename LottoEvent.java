import javax.swing.*;
import java.awt.event.*;

public class LottoEvent implements ItemListener, ActionListener, Runnable {
    LottoSimulator gui;
    Thread playing;
    
    public LottoEvent(LottoSimulator in) {
        gui = in;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Play")) {
            startPlaying();
        }
        if (command.equals("Stop")) {
            stopPlaying();
        }
        if (command.equals("Reset")) {
            clearAllFields();
        }
    }
    
    public void startPlaying() {
        playing = new Thread(this);
        playing.start();
        gui.play.setEnabled(false);
        gui.stop.setEnabled(true);
        gui.reset.setEnabled(false);
        gui.quickpick.setEnabled(false);
        gui.personal.setEnabled(false);
    }
    
    public void stopPlaying() {
        gui.stop.setEnabled(false);
        gui.play.setEnabled(true);
        gui.reset.setEnabled(true);
        gui.quickpick.setEnabled(true);
        gui.personal.setEnabled(true);
        playing = null;
    }
    
    public void clearAllFields() {
        for (int i = 0; i < 6; i++) {
            gui.numbers[i].setText(null);
            gui.winners[i].setText(null);
        }
        gui.got3.setText("0");
        gui.got4.setText("0");
        gui.got5.setText("0");
        gui.got6.setText("0");
        gui.drawings.setText("0");
        
    }
    
    @Override
    public void itemStateChanged(ItemEvent event) {
        Object item = event.getItem();
        if (item == gui.quickpick) {
            for (int i = 0; i < 6; i++) {
                int pick;
               
                    pick = (int) Math.floor(Math.random() * 69 + 1);
                
                gui.numbers[i].setText("" + pick);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                gui.numbers[i].setText(null);
            }
        }
    }
        
    
    
    void addOneToField(JTextField field) {
        int num = Integer.parseInt("0" + field.getText());
        num++;
        field.setText("" + num);
    }
    
    boolean numberGone(int num, JTextField[] pastNums, int count) {
        for (int i = 0; i < 6; i++) {
            if (Integer.parseInt(pastNums[i].getText()) == num) {
                return true;
            }
        }
        return false;
    }
    
    boolean matchedOne(JTextField win, JTextField[] allPicks) {
        for (int i = 0; i < 6; i++) {
            String winText = win.getText();
            if (winText.equals(allPicks[i].getText())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        while (playing == thisThread) {
            addOneToField(gui.drawings);
            int draw = Integer.parseInt(gui.drawings.getText());
            
            
            int matches = 0;
            for (int i = 0; i < 6; i++) {
                int ball;
                
                    ball = (int) Math.floor(Math.random() * 69 + 1);
                
                gui.winners[i].setText("" + ball);
                if (matchedOne(gui.winners[i], gui.numbers)) {
                    matches++;
                }
            }
            switch (matches) {
                case 3:addOneToField(gui.got3);
                    stopPlaying();
                    break;
                case 4:addOneToField(gui.got4);
                    stopPlaying();
                    break;
                case 5:addOneToField(gui.got5);
                    stopPlaying();
                    break;
                case 6:addOneToField(gui.got6);
                    gui.stop.setEnabled(false);
                    gui.play.setEnabled(true);
                    playing = null;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
}