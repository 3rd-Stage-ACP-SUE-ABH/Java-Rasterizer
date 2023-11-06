
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.*;

import model.object3D.Object3D;
import controller.renderer.Renderer;

public class buttone extends JPanel implements ActionListener {

	String filePath;
	Renderer renderer;
	File file;
	private JButton importButton, addLight, clearLightButton;
	JTextField red, green, blue, pointX, pointY, pointZ, d1, d2, d3;
	JTextField xPos, yPos, zPos, lookAtX, lookAtY, lookAtz, cameraIncX, cameraIncY, cameraIncZ;
	JTextField rotation, offset;

	JLabel xPosLabel, yPosLabel, zPosLabel, lookAtXlabel, lookAtYlabel, lookAtZlabel, cameraIncXlabel, cameraIncYLabel,
			cameraZlabel, rotationLabel, offsetLabel;
	JComboBox menulist;
	String[] menuItems = { "Option1", "Option2" };
	Object[] optionButton = { "Point Light", "Directional Light" };
	float posX, posY, posZ;
	float lookAt1, lookAt2, lookAt3;
	float cameraIncXFloat, cameraIncYFloat, cameraIncZFloat;
	float roationFloat, offsetFloat;

	public buttone(Renderer renderer) {
		this.renderer = renderer;

		importButton = new JButton("Import File");
		addLight = new JButton("Add Light");
		clearLightButton = new JButton("Clear Light");

		menulist = new JComboBox<>(menuItems);

		rotation = new JTextField(2);
		offset = new JTextField(2);
		rotationLabel = new JLabel("Roation");
		offsetLabel = new JLabel("Offset");

		xPosLabel = new JLabel("Camera X");
		yPosLabel = new JLabel("Camera Y");
		zPosLabel = new JLabel("Camera Z");
		xPos = new JTextField(2);
		yPos = new JTextField(2);
		zPos = new JTextField(2);
		restrictToDigitsAndBackspace(xPos);
		restrictToDigitsAndBackspace(yPos);
		restrictToDigitsAndBackspace(zPos);

		lookAtXlabel = new JLabel("Look x: ");
		lookAtYlabel = new JLabel("Look y: ");
		lookAtZlabel = new JLabel("Look z: ");
		lookAtX = new JTextField(2);
		lookAtY = new JTextField(2);
		lookAtz = new JTextField(2);
		restrictToDigitsAndBackspace(lookAtX);
		restrictToDigitsAndBackspace(lookAtY);
		restrictToDigitsAndBackspace(lookAtz);

		cameraIncXlabel = new JLabel("orientationX ");
		cameraIncYLabel = new JLabel("orientationY ");
		cameraZlabel = new JLabel("orientationZ ");
		cameraIncX = new JTextField(2);
		cameraIncY = new JTextField(2);
		cameraIncZ = new JTextField(2);
		restrictToDigitsAndBackspace(cameraIncX);
		restrictToDigitsAndBackspace(cameraIncY);
		restrictToDigitsAndBackspace(cameraIncZ);

		add(lookAtXlabel);
		add(lookAtX);
		add(lookAtYlabel);
		add(lookAtY);
		add(lookAtZlabel);
		add(lookAtz);
		// posX = Float.parseFloat(xPos.getText());
		// posY = Float.parseFloat(yPos.getText());
		// posZ = Float.parseFloat(zPos.getText());

		add(xPosLabel);
		add(xPos);
		add(yPosLabel);
		add(yPos);
		add(zPosLabel);
		add(zPos);
		// lookAtX1 = Float.parseFloat(lookAtX.getText());
		// lookAt2 = Float.parseFloat(lookAtY.getText());
		// lookAt3 = Float.parseFloat(lookAtz.getText());

		add(cameraIncXlabel);
		add(cameraIncX);
		add(cameraIncYLabel);
		add(cameraIncY);
		add(cameraZlabel);
		add(cameraIncZ);
		// cameraIncXFloat = Float.parseFloat(cameraIncX.getText());
		// cameraIncYFloat = Float.parseFloat(cameraIncY.getText());
		// cameraIncZFloat = Float.parseFloat(cameraIncZ.getText());

		add(importButton);
		add(addLight);
		add(clearLightButton);

		add(rotationLabel);
		add(rotation);

		add(offsetLabel);
		add(offset);

		add(menulist);

		menulist.addActionListener(this);
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
		if (src == menulist) {

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
			addPointLight();
		} else {
			System.out.println("direction chose");
			directions();
		}

	}

	public void updateInput() {

		posX = xPos.getText().length() > 0 ? Float.parseFloat(xPos.getText()) : 0;
		posY = yPos.getText().length() > 0 ? Float.parseFloat(yPos.getText()) : 0;
		posZ = zPos.getText().length() > 0 ? Float.parseFloat(zPos.getText()) : 0;

		lookAt1 = lookAtX.getText().length() > 0 ? Float.parseFloat(lookAtX.getText()) : 0;
		lookAt2 = lookAtY.getText().length() > 0 ? Float.parseFloat(lookAtY.getText()) : 0;
		lookAt3 = lookAtz.getText().length() > 0 ? Float.parseFloat(lookAtz.getText()) : 0;

		cameraIncXFloat = cameraIncX.getText().length() > 0 ? Float.parseFloat(cameraIncX.getText()) : 0;
		cameraIncYFloat = cameraIncY.getText().length() > 0 ? Float.parseFloat(cameraIncY.getText()) : 0;
		cameraIncZFloat = cameraIncZ.getText().length() > 0 ? Float.parseFloat(cameraIncZ.getText()) : 0;

		roationFloat = rotation.getText().length() > 0 ? Float.parseFloat(rotation.getText()) : 0;
		offsetFloat = offset.getText().length() > 0 ? Float.parseFloat(offset.getText()) : 0;

	}

	public void addPointLight() {
		pointX = new JTextField(4);
		pointY = new JTextField(4);
		pointZ = new JTextField(4);

		Object[] fileds = {
				"Point X", pointX,
				"Point Y", pointY,
				"Point Z", pointZ,
		};
		JOptionPane.showConfirmDialog(null, fileds, "Point Felds", JOptionPane.OK_OPTION);
		float floatX = Float.parseFloat(pointX.getText());
		float floatY = Float.parseFloat(pointY.getText());
		float floatZ = Float.parseFloat(pointZ.getText());

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
		float directionFloat1 = Float.parseFloat(d1.getText());
		float directionFloat2 = Float.parseFloat(d2.getText());
		float directionFloat3 = Float.parseFloat(d3.getText());
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
		restrictToDigitsAndBackspace(red);
		restrictToDigitsAndBackspace(green);
		restrictToDigitsAndBackspace(blue);
		JOptionPane.showConfirmDialog(null, rgbFields, "RGB Fields", JOptionPane.NO_OPTION);

		// checking if the values are in the range of 0 and 255
		int intTxt1 = Integer.parseInt(red.getText());
		int intTxt2 = Integer.parseInt(green.getText());
		int intTxt3 = Integer.parseInt(blue.getText());

		if ((intTxt1 >= 0 && intTxt1 <= 255) && (intTxt2 >= 0 && intTxt2 <= 255)
				&& (intTxt3 >= 0 && intTxt3 <= 255)) {
			System.out.println(intTxt1);
			optionButtons();
		} else {
			JOptionPane.showMessageDialog(null, "number not in the range!", "Error pop", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void clearLight() {
		// TODO: call renderer
	}

	public static void restrictToDigitsAndBackspace(JTextField textField) {
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				char keyChar = ke.getKeyChar();
				if ((keyChar >= '0' && keyChar <= '9') || (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE)
						|| (ke.getKeyCode() == KeyEvent.VK_PERIOD) || (ke.getKeyCode() == KeyEvent.VK_MINUS)) {
					textField.setEditable(true);
				} else {
					textField.setEditable(false);
				}
			}
		});
	}

}
