package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import model.math.Vec3f;
import model.object3D.Object3D;
import controller.renderer.Renderer;
import model.pipeline.programmable.FlatShader;
import model.pipeline.programmable.PhongShader;
import model.pipeline.programmable.shaderUtilities.CommonTransformations;
import model.pipeline.programmable.shaderUtilities.lighting.Light;
import model.pipeline.programmable.shaderUtilities.lighting.LightShader;

public class buttone extends JPanel implements ActionListener {

	String filePath;
	Renderer renderer;
	File file;
	private JButton importButton, addLight, clearLightButton, OKbutton;
	static JTextField red, green, blue, pointX, pointY, pointZ, d1, d2, d3;
	static JTextField xPos, yPos, zPos, lookAtX, lookAtY, lookAtz, cameraIncX, cameraIncY, cameraIncZ;
	static JTextField rotation, offsetX, offsetY, offsetZ;
	static JLabel offsetXLabel, offsetYLabel, offsetZLabel;
	static JLabel xPosLabel, yPosLabel, zPosLabel, lookAtXlabel, lookAtYlabel, lookAtZlabel, cameraIncXlabel,
			cameraIncYLabel,
			cameraZlabel, rotationLabel;
	JComboBox menulist;
	String[] menuItems = { "Default", "Toggle Chunky", "Toggle Texture", "Toggle Diffuse", "Toggle Specular",
	 "Toggle Ambient", "Toggle Spec Mode"};
	Object[] optionButton = { "Point Light", "Directional Light" };
	Object[] optionNextButton = { "Next", "Cancel" };
	static float posX, posY, posZ;
	static float lookAt1, lookAt2, lookAt3;
	static float cameraIncXFloat, cameraIncYFloat, cameraIncZFloat;
	static float roationFloat, offsetXF, offsetYF, offsetZF;
	int intTxt1, intTxt2, intTxt3;
	int testPane, showMessageDialog;
	float directionFloat1, directionFloat2, directionFloat3;
	float floatX, floatY, floatZ;

	public buttone(Renderer renderer) {

		this.renderer = renderer;

		importButton = new JButton("Import File");
		addLight = new JButton("Add Light");
		clearLightButton = new JButton("Clear Light");
		OKbutton = new JButton("OK");
		menulist = new JComboBox<>(menuItems);

		rotation = new JTextField(2);
		offsetX = new JTextField(2);
		offsetY = new JTextField(2);
		offsetZ = new JTextField(2);
		offsetXLabel = new JLabel("Offset X");
		offsetYLabel = new JLabel("Offset Y");
		offsetZLabel = new JLabel("Offset Z");
		restrictToDigitsAndBackspace(offsetX);
		restrictToDigitsAndBackspace(offsetY);
		restrictToDigitsAndBackspace(offsetZ);

		rotationLabel = new JLabel("Roation");

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

		add(rotationLabel);
		add(rotation);

		add(offsetXLabel);
		add(offsetX);
		add(offsetYLabel);
		add(offsetY);
		add(offsetZLabel);
		add(offsetZ);

		add(menulist);
		add(OKbutton);

		menulist.addActionListener(this);
		clearLightButton.addActionListener(this);
		addLight.addActionListener(this);
		importButton.addActionListener(this);
		OKbutton.addActionListener(this);
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
			addLightGUI();
		}
		if (src == clearLightButton) {

			clearLight();
		}
		if (src == menulist) {
			// { "Default", "Toggle Chunky", "Toggle Texture", 
			// "Toggle Diffuse", "Toggle Specular",
			// "Toggle Ambient", "Toggle Spec Mode"}
		 	switch (menulist.getSelectedIndex()) {
				case 0:
					LightShader.setDefaults();
					break;
				case 1:
					LightShader.toggleChunky();
					break;
				case 2:
					LightShader.toggleTex();
					break;
				case 3:
					LightShader.toggleDiffuse();
					break;
				case 4:
					LightShader.toggleSpec();
					break;
				case 5:
					LightShader.toggleAmbient();
					break;
				case 6:
					LightShader.toggleSpecMode();
				default:
					break;
			} 
		}
		if (src == OKbutton)
			updateInput();
	}

	public String getPath() {
		return filePath;
	}

	public void optionButtons() {

		int result = JOptionPane.showOptionDialog(this, null, "Enter a light color option",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				optionButton, null);

		if (result == JOptionPane.YES_OPTION) {
			System.out.println("light point color chosen");
			addPointLight();
		} else if (result == JOptionPane.NO_OPTION) {
			directions();
		}

	}

	public static void updateInput() {

		posX = xPos.getText().length() > 0 ? Float.parseFloat(xPos.getText()) : 0;
		posY = yPos.getText().length() > 0 ? Float.parseFloat(yPos.getText()) : 0;
		posZ = zPos.getText().length() > 0 ? Float.parseFloat(zPos.getText()) : 2;

		CommonTransformations.camPos = new Vec3f(posX, posY, posZ);

		lookAt1 = lookAtX.getText().length() > 0 ? Float.parseFloat(lookAtX.getText()) : 0;
		lookAt2 = lookAtY.getText().length() > 0 ? Float.parseFloat(lookAtY.getText()) : 0;
		lookAt3 = lookAtz.getText().length() > 0 ? Float.parseFloat(lookAtz.getText()) : 0;

		CommonTransformations.lookAt = new Vec3f(lookAt1, lookAt2, lookAt3);

		cameraIncXFloat = cameraIncX.getText().length() > 0 ? Float.parseFloat(cameraIncX.getText()) : 0;
		cameraIncYFloat = cameraIncY.getText().length() > 0 ? Float.parseFloat(cameraIncY.getText()) : 1;
		cameraIncZFloat = cameraIncZ.getText().length() > 0 ? Float.parseFloat(cameraIncZ.getText()) : 0;

		CommonTransformations.cameraUp = new Vec3f(cameraIncXFloat, cameraIncYFloat, cameraIncZFloat);

		roationFloat = rotation.getText().length() > 0 ? Float.parseFloat(rotation.getText()) : 0;

		offsetXF = offsetX.getText().length() > 0 ? Float.parseFloat(offsetX.getText()) : 0;
		offsetYF = offsetY.getText().length() > 0 ? Float.parseFloat(offsetY.getText()) : 0;
		offsetZF = offsetZ.getText().length() > 0 ? Float.parseFloat(offsetZ.getText()) : 0;

		CommonTransformations.rotationAngle = roationFloat;
		CommonTransformations.offset = new Vec3f(offsetXF, offsetYF, offsetZF);

	}

	public void addPointLight() {
		tempLight.direction = null;
		pointX = new JTextField(4);
		pointY = new JTextField(4);
		pointZ = new JTextField(4);

		Object[] fileds = {
				"Point X", pointX,
				"Point Y", pointY,
				"Point Z", pointZ,
		};
		restrictToDigitsAndBackspace(pointX);
		restrictToDigitsAndBackspace(pointY);
		restrictToDigitsAndBackspace(pointZ);
		boolean flag = false;

		while (!flag) {

			testPane = JOptionPane.showOptionDialog(this, fileds, "title", JOptionPane.YES_NO_OPTION,
					JOptionPane.DEFAULT_OPTION,
					null,
					optionNextButton,
					optionNextButton[0]);
			if (testPane == JOptionPane.YES_OPTION) {
				if ((pointX.getText().length() != 0) && (pointY.getText().length() != 0)
						&& (pointZ.getText().length() != 0)) {
					floatX = Float.parseFloat(pointX.getText());
					floatY = Float.parseFloat(pointY.getText());
					floatZ = Float.parseFloat(pointZ.getText());

				} else {
					int option = JOptionPane.showConfirmDialog(this,
							"please input some values", "Warning", JOptionPane.YES_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						continue;
					} else {
						break;
					}
				}
			} else {
				break;
			}
			flag = true;
		}

		tempLight.position = new Vec3f(floatX, floatY, floatZ);
		System.out.println(tempLight.position);
		LightShader.addLight(tempLight);
	}

	public void directions() {
		tempLight.position = null;
		d1 = new JTextField(4);
		d2 = new JTextField(4);
		d3 = new JTextField(4);

		Object[] directionFields = {
				"Point X ", d1,
				"Point Y ", d2,
				"Point Z ", d3,
		};
		restrictToDigitsAndBackspace(d1);
		restrictToDigitsAndBackspace(d2);
		restrictToDigitsAndBackspace(d3);
		boolean flag = false;

		while (!flag) {

			testPane = JOptionPane.showOptionDialog(this, directionFields, "title", JOptionPane.YES_NO_OPTION,
					JOptionPane.DEFAULT_OPTION,
					null,
					optionNextButton,
					optionNextButton[0]);
			if (testPane == JOptionPane.YES_OPTION) {
				if ((d1.getText().length() != 0) && (d2.getText().length() != 0) && (d3.getText().length() != 0)) {
					directionFloat1 = Float.parseFloat(d1.getText());
					directionFloat2 = Float.parseFloat(d2.getText());
					directionFloat3 = Float.parseFloat(d3.getText());
				} else {
					int option = JOptionPane.showConfirmDialog(this,
							"please input some values", "Warning", JOptionPane.YES_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						continue;
					} else {
						break;
					}
				}
			} else {
				break;
			}
			flag = true;
		}

		tempLight.direction = new Vec3f(directionFloat1, directionFloat2, directionFloat3);
		System.out.println(tempLight.direction);
		LightShader.addLight(tempLight);
	}

	Light tempLight;

	public void addLightGUI() {
		boolean flag = false;
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
		while (!flag) {

			testPane = JOptionPane.showOptionDialog(this, rgbFields, "title", JOptionPane.YES_NO_OPTION,
					JOptionPane.DEFAULT_OPTION,
					null,
					optionNextButton,
					optionNextButton[0]);
			if (testPane == JOptionPane.YES_OPTION) {
				if ((red.getText().length() != 0) && (green.getText().length() != 0)
						&& (blue.getText().length() != 0)) {
					intTxt1 = Integer.parseInt(red.getText());
					intTxt2 = Integer.parseInt(green.getText());
					intTxt3 = Integer.parseInt(blue.getText());
				} else {
					int option = JOptionPane.showConfirmDialog(this,
							"please input some values", "Warning", JOptionPane.YES_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						continue;
					} else {
						break;
					}
				}
			} else {
				break;
			}
			flag = true;

			if ((intTxt1 >= 0 && intTxt1 <= 255) && (intTxt2 >= 0 && intTxt2 <= 255)
					&& (intTxt3 >= 0 && intTxt3 <= 255)) {
				tempLight = new Light();
				System.out.println(intTxt1 + " " + intTxt2 + " " + intTxt3);
				tempLight.lightColor = new Color(intTxt1, intTxt2, intTxt3);
				optionButtons();
			} else {
				JOptionPane.showMessageDialog(this, "number not in the range!", "Error pop", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void clearLight() {
		LightShader.clearLights();
	}

	public static void restrictToDigitsAndBackspace(JTextField textField) {
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				char keyChar = ke.getKeyChar();
				if ((keyChar >= '0' && keyChar <= '9') || (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE)
						|| (ke.getKeyCode() == KeyEvent.VK_PERIOD) || (ke.getKeyCode() == KeyEvent.VK_MINUS)) {
					textField.setEditable(true);
				} else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					updateInput();
				} else {
					textField.setEditable(false);
				}
			}
		});
	}
}

// old jOption pane model
// old option pane...
// JOptionPane.showConfirmDialog(null, rgbFields, "RGB Fields",
// JOptionPane.DEFAULT_OPTION);
// checking if the values are in the range of 0 and 255