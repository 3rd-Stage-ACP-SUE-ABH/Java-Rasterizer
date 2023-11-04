package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.object3D.Object3D;
import controller.renderer.Renderer;

public  class  buttone extends JPanel implements ActionListener {
	
		String filePath;
		Renderer renderer;
		File file;
		private JButton button;
	public buttone(Renderer renderer) {
		this.renderer = renderer;
		button = new JButton("Import File");
		add(button);
		button.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("BUTTON PRESSED");
		Object src = e.getSource(); 
		if(src == button) {
			 JFileChooser chooser = new JFileChooser();
		        int result = chooser.showOpenDialog(null);
		        if (JFileChooser.APPROVE_OPTION == result){
					File file = chooser.getSelectedFile();
					filePath = file.getAbsolutePath();
					Object3D objModel = new Object3D(filePath);

					renderer.buttonFlag=true;
					renderer.loadModelData(objModel);
					renderer.buttonFlag=false;
		        }
		}
	}
	public String getPath() {
		return filePath;
	}
}