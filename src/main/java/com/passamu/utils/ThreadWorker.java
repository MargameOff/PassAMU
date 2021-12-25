package com.passamu.utils;

import javax.swing.SwingWorker;

/**
 * Thread utile pour toutes les actions lourdes en fonc
 */
public abstract class ThreadWorker extends SwingWorker<Void, Void> implements Runnable{

    public boolean finish;
    public ThreadWorker() {}

    /**
     * Fonction se déclenchant à la fin du thread
     */
    @Override
    protected void done() {
        super.done();
        try {
            get();
        } catch (Exception e) {
            showErrorMessage(e);
        }

    }

    
    /** 
     * @param e
     * Gestion de l'exception permettant l'affichage du message d'erreur.
     */
    protected void showErrorMessage(final Exception e) {
        String message;
        if (e.getCause() != null) {
            message = e.getCause().getMessage();
        } else {
            message = e.getMessage();
        }
        UIFunction.openErrorDialog(message);
    }

}
