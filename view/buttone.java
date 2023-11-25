package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;

import connection.ConnectionManager;
import connection.ControllerHelpers;

public class buttone extends JPanel implements ActionListener {
	
	private int intTxt1, intTxt2, intTxt3;
	
	private float posX, posY, posZ;
	private float lookAt1, lookAt2, lookAt3;
	private float cameraIncXFloat, cameraIncYFloat, cameraIncZFloat;
	private float roationFloat, offsetXF, offsetYF, offsetZF;
	private float directionFloat1, directionFloat2, directionFloat3;
	private float floatX, floatY, floatZ;

	private Map<String, String> loadModelMap;
	private Map<String, String> clearLightMap;
	private Map<String, String> addLightMap;
	private Map<String, String> menuListMap;
	private Map<String, String> inputFieldsMap;

	private String clearLightString;

	private String filePath;
	private int[] colorArray;
	private ConnectionManager connection;

	private ImageDisplay img;
	private int width;
	private int height;
	
	private JButton importButton, addLight, clearLightButton, OKbutton;
	private JTextField red, green, blue, pointX, pointY, pointZ, d1, d2, d3;
	private JTextField xPos, yPos, zPos, lookAtX, lookAtY, lookAtz, cameraIncX, cameraIncY, cameraIncZ;
	private JTextField rotation, offsetX, offsetY, offsetZ;
	private JLabel offsetXLabel, offsetYLabel, offsetZLabel;
	private JLabel xPosLabel, yPosLabel, zPosLabel, lookAtXlabel, lookAtYlabel, lookAtZlabel, 
				   cameraIncXlabel, cameraIncYLabel, cameraZlabel, rotationLabel;
	private JComboBox<String> menulist;
	private final String[] menuItems = { "Default", "Toggle Chunky", "Toggle Texture",
	 "Toggle Diffuse", "Toggle Specular", "Toggle Ambient", "Toggle Spec Mode"};
	private final Object[] optionButton = { "Point Light", "Directional Light" };
	private final Object[] optionNextButton = { "Next", "Cancel" };
	

	public buttone(ConnectionManager connection, ImageDisplay img) {

		this.connection = connection;
		this.img = img;

		initMaps();

		setLayout(new GridLayout(20, 1, 5, 10));

		importButton = new JButton("Import File");
		addLight = new JButton("Add Light");
		clearLightButton = new JButton("Clear Light");
		OKbutton = new JButton("OK");
		menulist = new JComboBox<String>(menuItems);

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

		add(rotationLabel);
		add(rotation);

		add(offsetXLabel);
		add(offsetX);
		add(offsetYLabel);
		add(offsetY);
		add(offsetZLabel);
		add(offsetZ);
		add(menulist);

		add(importButton);
		add(addLight);
		add(clearLightButton);

		add(OKbutton);

		menulist.addActionListener(this);
		clearLightButton.addActionListener(this);
		addLight.addActionListener(this);
		importButton.addActionListener(this);
		OKbutton.addActionListener(this);
	}

	private void initMaps() {
		// load modal map
		this.loadModelMap = new HashMap<>();
		this.loadModelMap.put("fileName", null);
		this.loadModelMap.put("loadModel", null);

		// clear light map
		this.clearLightMap = new HashMap<>();
		this.clearLightMap.put("clearLight(s)", "");
		this.clearLightString = ControllerHelpers.mapToString(this.clearLightMap);

		// add light map
		this.addLightMap = new HashMap<>();
		this.addLightMap.put("addLight", null);
		this.addLightMap.put("intColorR", null);
		this.addLightMap.put("intColorG", null);
		this.addLightMap.put("intColorB", null);
		this.addLightMap.put("floatX", null);
		this.addLightMap.put("floatY", null);
		this.addLightMap.put("floatZ", null);
		this.addLightMap.put("lightDirection1", null);
		this.addLightMap.put("lightDirection2", null);
		this.addLightMap.put("lightDirection3", null);

		// menu list map
		this.menuListMap = new HashMap<>();
		this.menuListMap.put("lightShader", null);

		// input fields map
		this.inputFieldsMap = new HashMap<>();
		this.inputFieldsMap.put("lookAtX", null);
		this.inputFieldsMap.put("lookAtY", null);
		this.inputFieldsMap.put("lookAtZ", null);
		this.inputFieldsMap.put("posX", null);
		this.inputFieldsMap.put("posY", null);
		this.inputFieldsMap.put("posZ", null);
		this.inputFieldsMap.put("cameraIncX", null);
		this.inputFieldsMap.put("cameraIncY", null);
		this.inputFieldsMap.put("cameraIncZ", null);
		this.inputFieldsMap.put("rotation", null);
		this.inputFieldsMap.put("offsetX", null);
		this.inputFieldsMap.put("offsetY", null);
		this.inputFieldsMap.put("offsetZ", null);
	}

	private void clearMap(Map<String, String> map) {
		for (Entry<String, String> entrySet : map.entrySet()) {
			map.put(entrySet.getKey(), null);
		}
	}

	public void update(String mapStr) {
		// send request to the server
		this.connection.sendData(mapStr);
		
		// recieve and repaint
		System.out.println("Data sent");
		String arrStr = this.connection.receiveData();
		System.out.println("Data recieved\nData:");
		System.out.println(arrStr);
		this.colorArray = ControllerHelpers.getIntArray(arrStr);
		this.img.image.setRGB(0, 0, width, height, this.colorArray, 0, width);
		this.img.imagePanel.repaint();
		System.out.println("panel repainted");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("BUTTON PRESSED");
		Object src = e.getSource();
		if (src == importButton) {
			importFile();
			return;
		}
		if (src == addLight) {
			addLightGUI();
			return;
		}
		if (src == clearLightButton) {
			clearLight();
			return;
		}
		if (src == menulist) {
			setShader();
			return;
		}
		if (src == OKbutton) {
			updateInput();
		}
	}

	public void importFile() {
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == result) {
			File file = chooser.getSelectedFile();
			// TEST :: just pass the .getAbsolutePath() to the .readFile()
			filePath = file.getAbsolutePath();
			String fileContent = ControllerHelpers.readFile(filePath);
			String fileName = file.getName();
				
			// this.loadModelMap.put("fileName", file.getName());
			// this.loadModelMap.put("loadModel", fileContent);
			// this.update(ControllerHelpers.mapToString(this.loadModelMap));

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("[:]");
			stringBuilder.insert(1, fileName);
			
			int indexOfColon = stringBuilder.indexOf(":");
			stringBuilder.insert(indexOfColon + 1, fileContent);
				
			this.update(stringBuilder.toString());
			// this.clearMap(this.loadModelMap);
		}
	}

	public void setShader() {
		// { "Default", "Toggle Chunky", "Toggle Texture", 
		// "Toggle Diffuse", "Toggle Specular",
		// "Toggle Ambient", "Toggle Spec Mode"}
		switch (menulist.getSelectedIndex()) {
			case 0:
				// LightShader.setDefaults();
				this.menuListMap.put("shader", "0");
				break;
			case 1:
				// LightShader.toggleChunky();
				this.menuListMap.put("shader", "1");
				break;
			case 2:
				// LightShader.toggleTex();
				this.menuListMap.put("shader", "2");
				break;
			case 3:
				// LightShader.toggleDiffuse();
				this.menuListMap.put("shader", "3");
				break;
			case 4:
				// LightShader.toggleSpec();
				this.menuListMap.put("shader", "4");
				break;
			case 5:
				// LightShader.toggleAmbient();
				this.menuListMap.put("shader", "5");
				break;
			case 6:
				// LightShader.toggleSpecMode();
				this.menuListMap.put("shader", "6");
				break;
			default:
				break;
		}

		this.update(ControllerHelpers.mapToString(this.menuListMap));
		this.clearMap(this.menuListMap);
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
			directions();
		}

	}

	public void updateInput() {

		lookAt1 = lookAtX.getText().length() > 0 ? Float.parseFloat(lookAtX.getText()) : 0;
		lookAt2 = lookAtY.getText().length() > 0 ? Float.parseFloat(lookAtY.getText()) : 0;
		lookAt3 = lookAtz.getText().length() > 0 ? Float.parseFloat(lookAtz.getText()) : 0;

		// CommonTransformations.lookAt = new Vec3f(lookAt1, lookAt2, lookAt3);
		this.inputFieldsMap.put("lookAtX", Float.toString(lookAt1));
		this.inputFieldsMap.put("lookAtY", Float.toString(lookAt2));
		this.inputFieldsMap.put("lookAtZ", Float.toString(lookAt3));


		posX = xPos.getText().length() > 0 ? Float.parseFloat(xPos.getText()) : 0;
		posY = yPos.getText().length() > 0 ? Float.parseFloat(yPos.getText()) : 0;
		posZ = zPos.getText().length() > 0 ? Float.parseFloat(zPos.getText()) : 2;

		// CommonTransformations.camPos = new Vec3f(posX, posY, posZ);
		this.inputFieldsMap.put("posX", Float.toString(posX));
		this.inputFieldsMap.put("posY", Float.toString(posY));
		this.inputFieldsMap.put("posZ", Float.toString(posZ));


		cameraIncXFloat = cameraIncX.getText().length() > 0 ? Float.parseFloat(cameraIncX.getText()) : 0;
		cameraIncYFloat = cameraIncY.getText().length() > 0 ? Float.parseFloat(cameraIncY.getText()) : 1;
		cameraIncZFloat = cameraIncZ.getText().length() > 0 ? Float.parseFloat(cameraIncZ.getText()) : 0;

		// CommonTransformations.cameraUp = new Vec3f(cameraIncXFloat, cameraIncYFloat, cameraIncZFloat);
		this.inputFieldsMap.put("cameraIncX", Float.toString(cameraIncXFloat));
		this.inputFieldsMap.put("cameraIncY", Float.toString(cameraIncYFloat));
		this.inputFieldsMap.put("cameraIncZ", Float.toString(cameraIncZFloat));


		roationFloat = rotation.getText().length() > 0 ? Float.parseFloat(rotation.getText()) : 0;
		
		// CommonTransformations.rotationAngle = roationFloat;
		this.inputFieldsMap.put("rotation", Float.toString(roationFloat));

		offsetXF = offsetX.getText().length() > 0 ? Float.parseFloat(offsetX.getText()) : 0;
		offsetYF = offsetY.getText().length() > 0 ? Float.parseFloat(offsetY.getText()) : 0;
		offsetZF = offsetZ.getText().length() > 0 ? Float.parseFloat(offsetZ.getText()) : 0;

		// CommonTransformations.offset = new Vec3f(offsetXF, offsetYF, offsetZF);
		this.inputFieldsMap.put("offsetX", Float.toString(offsetXF));
		this.inputFieldsMap.put("offsetY", Float.toString(offsetYF));
		this.inputFieldsMap.put("offsetZ", Float.toString(offsetZF));

		this.update(ControllerHelpers.mapToString(this.inputFieldsMap));
		this.clearMap(this.inputFieldsMap);
	}

	public void addPointLight() {
		// tempLight.direction = null;
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
		JOptionPane.showConfirmDialog(null, fileds, "Point Felds", JOptionPane.OK_OPTION);
		floatX = Float.parseFloat(pointX.getText());
		floatY = Float.parseFloat(pointY.getText());
		floatZ = Float.parseFloat(pointZ.getText());

		// tempLight.position = new Vec3f(floatX, floatY, floatZ);
		// System.out.println(tempLight.position);
		// LightShader.addLight(tempLight);

		this.addLightMap.put("addLight", "pointLight");
		this.addLightMap.put("floatX", Float.toString(floatX));
		this.addLightMap.put("floatY", Float.toString(floatY));
		this.addLightMap.put("floatZ", Float.toString(floatZ));

		this.update(ControllerHelpers.mapToString(this.addLightMap));
		this.clearMap(this.addLightMap);
	}

	public void directions() {
		// tempLight.position = null;
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
		JOptionPane.showConfirmDialog(null, directionFields, "Direction Fields", JOptionPane.OK_OPTION);
		directionFloat1 = Float.parseFloat(d1.getText());
		directionFloat2 = Float.parseFloat(d2.getText());
		directionFloat3 = Float.parseFloat(d3.getText());

		// tempLight.direction = new Vec3f(directionFloat1, directionFloat2, directionFloat3);
		// System.out.println(tempLight.direction);
		// LightShader.addLight(tempLight);

		this.addLightMap.put("lightType", "directionalLight");
		this.addLightMap.put("lightDirection1", Float.toString(directionFloat1));
		this.addLightMap.put("lightDirection2", Float.toString(directionFloat2));
		this.addLightMap.put("lightDirection3", Float.toString(directionFloat3));
		
		this.update(ControllerHelpers.mapToString(this.addLightMap));
		this.clearMap(this.addLightMap);
	}

	public void addLightGUI() {
		boolean flag = false;
		while (!flag) {

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

			JOptionPane.showOptionDialog(this, rgbFields, "title", JOptionPane.YES_NO_OPTION,
					JOptionPane.DEFAULT_OPTION,
					null,
					optionNextButton,
					optionNextButton[0]);

			if ((red.getText().length() != 0) && (green.getText().length() != 0) && (blue.getText().length() != 0)) {
				intTxt1 = Integer.parseInt(red.getText());
				intTxt2 = Integer.parseInt(green.getText());
				intTxt3 = Integer.parseInt(blue.getText());
			} else {
				JOptionPane.showMessageDialog(null, "Please fill the forms");
				// JOptionPane.showConfirmDialog(this, "Empty Fields", "title frameaka",
				// JOptionPane.YES_NO_OPTION);

			}

			if ((intTxt1 >= 0 && intTxt1 <= 255) && (intTxt2 >= 0 && intTxt2 <= 255)
					&& (intTxt3 >= 0 && intTxt3 <= 255)) {
				// tempLight = new Light();
				// System.out.println(intTxt1 + " " + intTxt2 + " " + intTxt3);
				// tempLight.lightColor = new Color(intTxt1, intTxt2, intTxt3);

				this.addLightMap.put("intColorR", Integer.toString(intTxt1));
				this.addLightMap.put("intColorG", Integer.toString(intTxt2));
				this.addLightMap.put("intColorB", Integer.toString(intTxt3));

				flag = true;
				optionButtons();
			} else {
				JOptionPane.showMessageDialog(null, "number not in the range!", "Error pop", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void clearLight() {
		// LightShader.clearLights();
		this.update(clearLightString);
	}

	public void restrictToDigitsAndBackspace(JTextField textField) {
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