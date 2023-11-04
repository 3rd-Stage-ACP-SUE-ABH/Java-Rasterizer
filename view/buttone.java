
import java.awt.GridLayout;
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
	JTextField red, green, blue, pointX, pointY, pointZ, d1, d2, d3;
	JTextField xPos, yPos, zPos, lookAtX, lookAtY, lookAtz, cameraIncX, cameraIncY, cameraIncZ;

	JLabel xPosLabel, yPosLabel, zPosLabel, lookAtXlabel, lookAtYlabel, lookAtZlabel, cameraIncXlabel, cameraIncYLabel,
			cameraZlabel;
	Object[] optionButton = { "Point Light", "Directional Light" };

	public buttone(Renderer renderer) {

		this.renderer = renderer;

		importButton = new JButton("Import File");
		addLight = new JButton("Add Light");
		clearLightButton = new JButton("Clear Light");

		xPosLabel = new JLabel("Camera X");
		yPosLabel = new JLabel("Camera Y");
		zPosLabel = new JLabel("Camera Z");
		xPos = new JTextField(2);
		yPos = new JTextField(2);
		zPos = new JTextField(2);

		lookAtXlabel = new JLabel("Look x: ");
		lookAtYlabel = new JLabel("Look y: ");
		lookAtZlabel = new JLabel("Look z: ");
		lookAtX = new JTextField(2);
		lookAtY = new JTextField(2);
		lookAtz = new JTextField(2);

		cameraIncXlabel = new JLabel("InclinationX ");
		cameraIncYLabel = new JLabel("InclinationY ");
		cameraZlabel = new JLabel("InclinationZ ");
		cameraIncX = new JTextField(2);
		cameraIncY = new JTextField(2);
		cameraIncZ = new JTextField(2);

		add(lookAtXlabel);
		add(lookAtX);
		add(lookAtYlabel);
		add(lookAtY);
		add(lookAtZlabel);
		add(lookAtz);

		add(xPosLabel);
		add(xPos);
		add(yPosLabel);
		add(yPos);
		add(zPosLabel);
		add(zPos);

		add(cameraIncXlabel);
		add(cameraIncX);
		add(cameraIncYLabel);
		add(cameraIncY);
		add(cameraZlabel);
		add(cameraIncZ);

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
			addLight();

		}
		if (src == clearLightButton) {
			clearLight();
		}
	}

	public String getPath() {
		return filePath;
	}

	public void optionButtons() {

		int result = JOptionPane.showOptionDialog(null, null, "Enter a light color option",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				optionButton, null);

		if (result == JOptionPane.YES_OPTION) {
			System.out.println("light point color chosen");
			pointFields();
		} else {
			directions();
		}

	}

	public void pointFields() {
		pointX = new JTextField(4);
		pointY = new JTextField(4);
		pointZ = new JTextField(4);

		Object[] fileds = {
				"Point X", pointX,
				"Point Y", pointY,
				"Point Z", pointZ,
		};
		JOptionPane.showConfirmDialog(null, fileds, "Point Felds", JOptionPane.OK_OPTION);
		System.out.println("pointX: " + pointX.getText());
		System.out.println("pointY: " + pointY.getText());
		System.out.println("pointZ: " + pointZ.getText());
		// TODO: call renderer

	}

	public void directions() {
		d1 = new JTextField(4);
		d2 = new JTextField(4);
		d3 = new JTextField(4);

		Object[] directionFields = {
				"Point X ", d1,
				"Point Y ", d2,
				"Point Z ", d3,
		};
		JOptionPane.showConfirmDialog(null, directionFields, "Direction Fields", JOptionPane.OK_OPTION);
	}

	public void addLight() {
		red = new JTextField(4);
		green = new JTextField(4);
		blue = new JTextField(4);

		Object[] rgbFields = {
				"Red", red,
				"Green", green,
				"Blue", blue,
		};

		JOptionPane.showConfirmDialog(null, rgbFields, "RGB Fields", JOptionPane.NO_OPTION);
		System.out.println("red: " + red.getText());
		System.out.println("green: " + green.getText());
		System.out.println("blue: " + blue.getText());
		// checking if the values are in the range of 0 and 255
		int intTxt1 = Integer.parseInt(red.getText());
		int intTxt2 = Integer.parseInt(green.getText());
		int intTxt3 = Integer.parseInt(blue.getText());

		if ((intTxt1 >= 0 && intTxt1 <= 255) && (intTxt2 >= 0 && intTxt2 <= 255)
				&& (intTxt3 >= 0 && intTxt3 <= 255)) {
			optionButtons();
		} else {
			JOptionPane.showMessageDialog(null, "number not in the range!", "Error pop", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void clearLight() {
		// TODO: call renderer
	}

}
