
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

import model.object3D.Object3D;
import model.renderer.Renderer;

public class buttone extends JPanel implements ActionListener {

	String filePath;
	Renderer renderer;
	File file;
	private JButton importButton, addLight, clearLightButton;
	JTextField txt1, txt2, txt3; // for rgb
	JTextField pointX, pointY, pointZ; // for point
	JTextField d1, d2, d3; // for direction
	JLabel label1, label2, label3;
	Object[] optionButton = { "Point Light", "Directional Light" };

	public buttone(Renderer renderer) {
		this.renderer = renderer;

		importButton = new JButton("Import File");
		addLight = new JButton("Add Light");
		clearLightButton = new JButton("Clear Light");

		label1 = new JLabel("red");
		label2 = new JLabel("green");
		label3 = new JLabel("blue");

		txt2 = new JTextField(4);
		txt1 = new JTextField(4);
		txt3 = new JTextField(4);

		add(label1);
		add(txt1);
		add(label2);
		add(txt2);
		add(label3);
		add(txt3);
		add(importButton);
		add(addLight);
		add(clearLightButton);

		clearLightButton.addActionListener(this);
		addLight.addActionListener(this);
		importButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("BUTTON PRESSED");
		Object src = e.getSource();
		if (src == importButton) {

			JFileChooser chooser = new JFileChooser();
			int result = chooser.showOpenDialog(null);
			if (JFileChooser.APPROVE_OPTION == result) {
				File file = chooser.getSelectedFile();
				filePath = file.getAbsolutePath();
				Object3D objModel = new Object3D(filePath);

				renderer.buttonFlag = true;
				renderer.loadModelData(objModel);

				renderer.buttonFlag = false;
			}
		}
		if (src == addLight) {

			lightColor();

		}
		if (src == clearLightButton) {
			clearLight();
		}
	}

	public String getPath() {
		return filePath;
	}

	public void lightColor() {

		int intTxt1 = Integer.parseInt(txt1.getText());
		int intTxt2 = Integer.parseInt(txt2.getText());
		int intTxt3 = Integer.parseInt(txt3.getText());

		try {
			if ((intTxt1 >= 0 && intTxt1 <= 255) && (intTxt2 >= 0 && intTxt2 <= 255)
					&& (intTxt3 >= 0 && intTxt3 <= 255)) {
				System.out.println(txt1.getText());
				System.out.println(txt2.getText());
				System.out.println(txt3.getText());

				int result = JOptionPane.showOptionDialog(null, null, "Enter a light color option",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
						optionButton, null);

				if (result == JOptionPane.YES_OPTION) {
					System.out.println("light point color chosen");
					pointX = new JTextField(4);
					pointY = new JTextField(4);
					pointZ = new JTextField(4);

					Object[] fileds = {
							"Point X", pointX,
							"Point Y", pointY,
							"Point Z", pointZ,
					};
					JOptionPane.showConfirmDialog(null, fileds, "enter 3 vals", JOptionPane.OK_OPTION);
					// TODO: call renderer
					System.out.println("point x:" + pointX.getText());
					System.out.println("point y:" + pointY.getText());
					System.out.println("point z:" + pointZ.getText());
				}
			} else {
				System.out.println("number out of range!");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input! Please enter valid numbers.");
		}
	}

	public void clearLight() {
		// TODO: call renderer
	}
}