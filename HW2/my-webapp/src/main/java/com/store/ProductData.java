package com.store;

import java.util.HashMap;
import com.store.Product;

public class ProductData {

    public HashMap<Integer, Product> initialProducts = new HashMap<>();

    public HashMap<Integer, Product> getInitialProducts() {
        
        
        initialProducts.put(1, new Product(1, "Powered Speaker (500W)", 50.00));
        initialProducts.put(2, new Product(2, "Powered Speaker (1000W)", 75.00));
        initialProducts.put(3, new Product(3, "Passive Speaker (12in)", 30.00));
        initialProducts.put(4, new Product(4, "Subwoofer (18in)", 60.00));
        initialProducts.put(5, new Product(5, "Wireless Handheld Microphone", 25.00));
        initialProducts.put(6, new Product(6, "Wireless Lavalier Microphone", 30.00));
        initialProducts.put(7, new Product(7, "Wired Dynamic Microphone", 15.00));
        initialProducts.put(8, new Product(8, "Condenser Microphone (Studio)", 40.00));
        initialProducts.put(9, new Product(9, "Microphone Stand (Tripod)", 10.00));
        initialProducts.put(10, new Product(10, "Boom Microphone Stand", 12.00));
        initialProducts.put(11, new Product(11, "Digital Mixer (8-Channel)", 50.00));
        initialProducts.put(12, new Product(12, "Digital Mixer (16-Channel)", 75.00));
        initialProducts.put(13, new Product(13, "Analog Mixer (4-Channel)", 25.00));
        initialProducts.put(14, new Product(14, "AV Receiver (5.1 Surround)", 45.00));
        initialProducts.put(15, new Product(15, "AV Receiver (7.2 Surround)", 60.00));
        initialProducts.put(16, new Product(16, "Bluetooth Audio Receiver", 20.00));
        initialProducts.put(17, new Product(17, "DSLR Camera (Canon EOS)", 80.00));
        initialProducts.put(18, new Product(18, "Mirrorless Camera (Sony Alpha)", 90.00));
        initialProducts.put(19, new Product(19, "Camera Tripod (Heavy Duty)", 25.00));
        initialProducts.put(20, new Product(20, "Camera Stabilizer (Gimbal)", 50.00));
        initialProducts.put(21, new Product(21, "LED Video Light Panel", 30.00));
        initialProducts.put(22, new Product(22, "Ring Light (18in)", 35.00));
        initialProducts.put(23, new Product(23, "Projector (3000 Lumens)", 60.00));
        initialProducts.put(24, new Product(24, "Projector (5000 Lumens)", 85.00));
        initialProducts.put(25, new Product(25, "Projection Screen (100in)", 40.00));
        initialProducts.put(26, new Product(26, "Projection Screen (120in)", 50.00));
        initialProducts.put(27, new Product(27, "HDMI Splitter (4-Port)", 20.00));
        initialProducts.put(28, new Product(28, "HDMI Switch (3-Port)", 15.00));
        initialProducts.put(29, new Product(29, "XLR Cable (25ft)", 5.00));
        initialProducts.put(30, new Product(30, "Speaker Cable (50ft)", 8.00));
        initialProducts.put(31, new Product(31, "Audio Interface (2-Channel)", 35.00));
        initialProducts.put(32, new Product(32, "Audio Interface (4-Channel)", 50.00));
        initialProducts.put(33, new Product(33, "Portable PA System", 70.00));
        initialProducts.put(34, new Product(34, "Wireless In-Ear Monitor System", 60.00));
        initialProducts.put(35, new Product(35, "Headset Microphone", 25.00));
        initialProducts.put(36, new Product(36, "Shotgun Microphone", 40.00));
        initialProducts.put(37, new Product(37, "Camcorder (4K)", 100.00));
        initialProducts.put(38, new Product(38, "Action Camera (GoPro)", 45.00));
        initialProducts.put(39, new Product(39, "Camera Lens (50mm)", 35.00));
        initialProducts.put(40, new Product(40, "Camera Lens (Zoom 70-200mm)", 50.00));
        initialProducts.put(41, new Product(41, "LED Spotlight (50W)", 25.00));
        initialProducts.put(42, new Product(42, "Fog Machine (1000W)", 40.00));
        initialProducts.put(43, new Product(43, "Laser Light Projector", 30.00));
        initialProducts.put(44, new Product(44, "Audio Recorder (Portable)", 35.00));
        initialProducts.put(45, new Product(45, "Video Switcher (4-Input)", 75.00));
        initialProducts.put(46, new Product(46, "Streaming Encoder", 60.00));
        initialProducts.put(47, new Product(47, "Green Screen Kit (10x12ft)", 45.00));
        initialProducts.put(48, new Product(48, "Lighting Stand (Adjustable)", 15.00));
        initialProducts.put(49, new Product(49, "Power Distribution Box", 25.00));
        initialProducts.put(50, new Product(50, "Wireless Presenter Remote", 10.00));

        return initialProducts;
    }
}