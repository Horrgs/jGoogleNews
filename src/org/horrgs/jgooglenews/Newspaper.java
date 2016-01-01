package org.horrgs.jgooglenews;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Horrgs on 12/30/2015.
 */
public class Newspaper {
    Newspaper[] newspapers = new Newspaper[5];
    Site site = new Site("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&");
    public JFrame jFrame;
    public JTextPane jTextPane;
    public JTabbedPane jTabbedPane;
    String linkColor = "#0099FF", textColor = "#000000", borderColor = "#858B85", insideColor = "#949A94";
    SimpleAttributeSet title = new SimpleAttributeSet(), link = new SimpleAttributeSet(), rest = new SimpleAttributeSet();
    StyledDocument styledDocument;
    private JTextField searchBar = new JTextField("", 22);
    private JButton searchButton = new JButton("Search");
    public void openNewspaper() {
        jFrame = new JFrame();
        jFrame.addMouseListener(new LinkListener());
        jFrame.setTitle("Newspaper");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLayout(new GridBagLayout());
        jFrame.setSize(new Dimension(968, 551));
        jFrame.setBackground(Color.decode(insideColor).darker().darker().darker());
        jTabbedPane = new JTabbedPane();
        jTabbedPane.setPreferredSize(new Dimension(892, 494));
        jTabbedPane.setMinimumSize(new Dimension(892, 494));
        jTabbedPane.setBorder(BorderFactory.createLineBorder(jFrame.getBackground()));

        
        Article[] headlines = site.getArticles("topic=", "h", 4);
        JComponent headlinesComponent = addSectionArticles(headlines);
        jTabbedPane.addTab("Headlines", headlinesComponent);
        jTabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        Article[] scienceAndTech = site.getArticles("topic=", "t", 4);
        JComponent sciAndTechComponent = addSectionArticles(scienceAndTech);
        jTabbedPane.addTab("Science & Tech", sciAndTechComponent);
        jTabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        Article[] politics = site.getArticles("topic=", "p", 4);
        JComponent politicsComponent = addSectionArticles(politics);
        jTabbedPane.addTab("Politics", politicsComponent);
        jTabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        Article[] sports = site.getArticles("topic=", "s", 4);
        JComponent sportsComponent = addSectionArticles(sports);
        jTabbedPane.addTab("Sports", sportsComponent);
        jTabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        Article[] world  = site.getArticles("topic=", "w", 4);
        JComponent worldComponent = addSectionArticles(world);
        jTabbedPane.addTab("World", worldComponent);
        jTabbedPane.setMnemonicAt(4, KeyEvent.VK_5);

        jFrame.add(jTabbedPane);
        jFrame.setVisible(true);
    }

    public JComponent addSectionArticles(Article[] articles) {
        JPanel jPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        for(int x = 0; x < articles.length; x++) {
            jTextPane = new JTextPane();
            jTextPane.setEditable(false);
            jTextPane.setBorder(BorderFactory.createLineBorder(Color.decode(borderColor), 5));
            jTextPane.setPreferredSize(new Dimension(792, 94));
            jTextPane.setMinimumSize(new Dimension(792, 94));
            jTextPane.addMouseListener(new LinkListener());
            styledDocument = jTextPane.getStyledDocument();
            StyleConstants.setBold(title, true);
            StyleConstants.setForeground(title, Color.decode(textColor));

            StyleConstants.setForeground(link, Color.decode(linkColor));
            try {
                styledDocument.insertString(styledDocument.getLength(), articles[x].getTitle(), title);
                styledDocument.insertString(styledDocument.getLength(), "\n" + articles[x].getPublisher(), rest);
                styledDocument.insertString(styledDocument.getLength(), "\n" + articles[x].getPublishedDate(), rest);
                styledDocument.insertString(styledDocument.getLength(), "\n" + articles[x].getLink(), link);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            gbc.gridy = gbc.gridy + 1;
            gbc.gridx = 0;
            jPanel.add(jTextPane, gbc);
        }
        return jPanel;
    }

    private class LinkListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() != jFrame) {
                JTextPane pane = (JTextPane) e.getSource();
                String split[] = pane.getText().split("\n");
                if(Desktop.isDesktopSupported()) {
                    try {
                        System.out.println("Attempting to open link " + split[split.length - 1]);
                        Desktop.getDesktop().browse(new URI(split[split.length - 1]));
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        System.out.println("Failed to open link " + split[split.length - 1]);
                        ex.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
