import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColorConverterApp extends JFrame {
    private JTextField rgbRed, rgbGreen, rgbBlue;
    private JSlider redSlider, greenSlider, blueSlider;
    private JTextField xyzX, xyzY, xyzZ;
    private JTextField cmykC, cmykM, cmykY, cmykK;

    public ColorConverterApp() {
        setTitle("Color Converter");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 3));

        // RGB Inputs
        add(new JLabel("RGB:"));
        rgbRed = createTextField();
        rgbGreen = createTextField();
        rgbBlue = createTextField();
        add(rgbRed);
        add(rgbGreen);
        add(rgbBlue);

        // RGB Sliders
        redSlider = createSlider();
        greenSlider = createSlider();
        blueSlider = createSlider();
        add(redSlider);
        add(greenSlider);
        add(blueSlider);

        // XYZ Inputs
        add(new JLabel("XYZ:"));
        xyzX = createTextField();
        xyzY = createTextField();
        xyzZ = createTextField();
        add(xyzX);
        add(xyzY);
        add(xyzZ);

        // CMYK Inputs
        add(new JLabel("CMYK:"));
        cmykC = createTextField();
        cmykM = createTextField();
        cmykY = createTextField();
        cmykK = createTextField();
        add(cmykC);
        add(cmykM);
        add(cmykY);
        add(cmykK);

        // Action Listeners
        ActionListener listener = e -> updateColors();
        rgbRed.addActionListener(listener);
        rgbGreen.addActionListener(listener);
        rgbBlue.addActionListener(listener);
        redSlider.addChangeListener(e -> {
            rgbRed.setText(String.valueOf(redSlider.getValue()));
            updateColors();
        });
        greenSlider.addChangeListener(e -> {
            rgbGreen.setText(String.valueOf(greenSlider.getValue()));
            updateColors();
        });
        blueSlider.addChangeListener(e -> {
            rgbBlue.setText(String.valueOf(blueSlider.getValue()));
            updateColors();
        });

        xyzX.addActionListener(listener);
        xyzY.addActionListener(listener);
        xyzZ.addActionListener(listener);
        cmykC.addActionListener(listener);
        cmykM.addActionListener(listener);
        cmykY.addActionListener(listener);
        cmykK.addActionListener(listener);
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        return textField;
    }

    private JSlider createSlider() {
        JSlider slider = new JSlider(0, 255);
        slider.setMajorTickSpacing(51);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private void updateColors() {
        int r = parseColorValue(rgbRed.getText());
        int g = parseColorValue(rgbGreen.getText());
        int b = parseColorValue(rgbBlue.getText());

        float[] xyz = rgbToXyz(r, g, b);
        xyzX.setText(String.format("%.2f", xyz[0]));
        xyzY.setText(String.format("%.2f", xyz[1]));
        xyzZ.setText(String.format("%.2f", xyz[2]));

        float[] cmyk = rgbToCmyk(r, g, b);
        cmykC.setText(String.format("%.2f", cmyk[0]));
        cmykM.setText(String.format("%.2f", cmyk[1]));
        cmykY.setText(String.format("%.2f", cmyk[2]));
        cmykK.setText(String.format("%.2f", cmyk[3]));
    }

    private int parseColorValue(String value) {
        try {
            return Math.max(0, Math.min(255, Integer.parseInt(value)));
        } catch (NumberFormatException e) {
            return 0; // Default to 0 on error
        }
    }

    private float[] rgbToXyz(int r, int g, int b) {
        float rLinear = r / 255.0f;
        float gLinear = g / 255.0f;
        float bLinear = b / 255.0f;

        // Apply gamma correction
        rLinear = (rLinear > 0.04045) ? (float) Math.pow((rLinear + 0.055) / 1.055, 2.4) : rLinear / 12.92f;
        gLinear = (gLinear > 0.04045) ? (float) Math.pow((gLinear + 0.055) / 1.055, 2.4) : gLinear / 12.92f;
        bLinear = (bLinear > 0.04045) ? (float) Math.pow((bLinear + 0.055) / 1.055, 2.4) : bLinear / 12.92f;

        float x = (float) (rLinear * 0.4124564 + gLinear * 0.3575761 + bLinear * 0.1804375);
        float y = (float) (rLinear * 0.2126729 + gLinear * 0.7151522 + bLinear * 0.0721750);
        float z = (float) (rLinear * 0.0193339 + gLinear * 0.1191920 + bLinear * 0.9503041);

        return new float[]{x * 100, y * 100, z * 100};
    }

    private float[] rgbToCmyk(int r, int g, int b) {
        float c = 1 - (r / 255.0f);
        float m = 1 - (g / 255.0f);
        float y = 1 - (b / 255.0f);
        float k = Math.min(c, Math.min(m, y));

        if (k < 1) {
            c = (c - k) / (1 - k);
            m = (m - k) / (1 - k);
            y = (y - k) / (1 - k);
        } else {
            c = 0;
            m = 0;
            y = 0;
        }

        return new float[]{c, m, y, k};
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ColorConverterApp app = new ColorConverterApp();
            app.setVisible(true);
        });
    }
}