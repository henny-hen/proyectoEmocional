package es.practica.tweets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.practica.tweets.*;
import jade.wrapper.StaleProxyException;
import java.awt.Toolkit;
import javax.swing.UIManager;

public class Interfaz {
	public String tweets;
	public String hashtag;
	private JTextField campoT;
	private JComboBox<String> comboBox;

	public JFrame frameanazalizador;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interfaz window = new Interfaz();
					window.frameanazalizador.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Interfaz() {
		initialize();
	}


	private void initialize() {
		frameanazalizador = new JFrame();
		frameanazalizador.setResizable(false);
		frameanazalizador.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagenes/twitterlogo.png")));
		frameanazalizador.setTitle("Analizador tweets");
		frameanazalizador.setBounds(100, 100, 450, 300);
		frameanazalizador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frameanazalizador.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel label1 = new JLabel("ANALIZADOR de Hashtags en Twitter");
		label1.setBackground(UIManager.getColor("Button.focus"));
		label1.setForeground(Color.WHITE);
		label1.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		label1.setBounds(48, 0, 346, 80);
		panel.add(label1);
		
		JLabel labelHash = new JLabel("Número de tweets:");
		labelHash.setBackground(new Color(0, 0, 0));
		labelHash.setFont(new Font("Dubai", Font.BOLD, 16));
		labelHash.setForeground(Color.WHITE);
		labelHash.setBounds(61, 108, 174, 15);
		panel.add(labelHash);
		
		JLabel lblSetHashtag = new JLabel("Hashtag:");
		lblSetHashtag.setFont(new Font("Dubai", Font.BOLD, 16));
		lblSetHashtag.setForeground(Color.WHITE);
		lblSetHashtag.setBounds(132, 144, 88, 15);
		panel.add(lblSetHashtag);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(232, 104, 44, 24);
		comboBox.setMaximumRowCount(1);
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"}));
		panel.add(comboBox);
		
		campoT = new JTextField();
		campoT.setBounds(232, 140, 114, 19);
		panel.add(campoT);
		campoT.setColumns(10);
		
		JButton botonAn = new JButton("Analizar");
		botonAn.setBackground(new Color(255, 255, 255));
		botonAn.setFont(new Font("Dubai Medium", Font.BOLD, 17));
		botonAn.setBounds(154, 208, 122, 42);
		botonAn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				tweets = new String(comboBox.getSelectedItem().toString()); 
				hashtag = new String(campoT.getText());

		        if (tweets == "") {
		            JOptionPane.showMessageDialog(null, "Indique el # de tweets");
		        } else {
		            
		            if (hashtag.isEmpty()) {
		                JOptionPane.showMessageDialog(null, "Indique Hastag");
		            }else{
		            	
			            try {
			            	
			                AgentMaster.agBusqueda.activate();
			            } catch (StaleProxyException ex) {
			                Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
			            }
			            
		            }

			}
				
			}

		});
		panel.add(botonAn);
		
		JLabel fondo = new JLabel("New label");
		fondo.setForeground(Color.WHITE);
		fondo.setIcon(new ImageIcon(Interfaz.class.getResource("/Imagenes/twitter5.png")));
		fondo.setBounds(0, -100, 500, 500);
		panel.add(fondo);
	}

    public String getHashTag() {
        return hashtag;
    }

    public String getnTweets() {
        return tweets;
    }
	}


