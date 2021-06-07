package com.github.marcos.tulio.view.fragment;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.github.marcos.tulio.model.Config;

/**
 *
 * @author Marcos Santos
 */
public final class ProgressBar extends JProgressBar {

    private Thread tLoading = null;
    private Runnable handler = null;

    private boolean isInfinity = false;

    public ProgressBar() {
        initProperties();
    }

    /**
     * Define as propriedades iniciais para esta barra de progresso.
     */
    private void initProperties() {
        setStringPainted(true);
        setString(Config.TITLE);
        setForeground(Config.LIME_GREEN);
        setBackground(Config.MOSTLY_WHITE);
        setBorder(null);
        setFocusable(false);
        setUI(new BasicProgressBarUI() {
            @Override
            protected Color getSelectionBackground() {
                return Color.DARK_GRAY;
            }

            @Override
            protected Color getSelectionForeground() {
                return Config.MOSTLY_WHITE;
            }
        });
    }

    /**
     * Inicia a thread da barra de progresso. Inicia apenas se já não estiver
     * rodando.
     */
    public void init() {
        // Se a thread de carregamento não estiver rodando cria uma nova
        if (tLoading == null || !tLoading.isAlive()) {
            tLoading = new Thread(handlerProgress());
            tLoading.start();
        }
    }

    /**
     * Inicia a thread da barra de progresso.Inicia apenas se já não estiver
     * rodando.
     *
     * @param txt texto para setar na barra
     */
    public void init(String txt) {
        init();
        setString(txt);
    }

    /**
     * Finaliza a thread da barra de progresso.
     */
    public void end() {
        //if (tLoading != null && tLoading.isAlive())
        setValue(getMaximum());
    }

    /**
     * Finaliza a thread da barra de progresso.
     *
     * @param txt texto para setar na barra
     */
    public void end(String txt) {
        end();
        setString(txt);
    }

    @Override
    public void setValue(int n) {
        if (n > getMaximum()) n = getMaximum();
        super.setValue(n);
    }

    public void setInfinity(boolean value){ this.isInfinity = value; }

    private Runnable handlerProgress() {
        if (handler == null)
            handler = () -> {
                
                int value = 0;
                while (getValue() < getMaximum()) {
                    if (isInfinity){
                        value = getValue() + (int) (getMaximum() / 20);

                        if (value == getMaximum()) 
                            value = 0;

                        setValue(value);
                    }
                    
                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try {
                    Thread.sleep(2000);
                    setValue(0);
                    setString(Config.TITLE);
                    isInfinity = false;

                } catch (InterruptedException ex) {
                    Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
                }
            };

        return handler;
    }
}
