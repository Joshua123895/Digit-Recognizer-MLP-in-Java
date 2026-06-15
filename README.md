# Handwritten Digit Recognizer (MLP in Java)

A handwritten digit recognition system built from scratch using a Multi-Layer Perceptron (MLP) implemented entirely in Java. The model is trained on the MNIST dataset and includes training, testing, visualization, and an interactive drawing interface.

---

## Overview

This project implements a neural network without using external machine learning libraries. It is designed for educational purposes to understand how neural networks work internally, including forward propagation, backpropagation, and gradient-based learning.

The system allows users to:
- Train a neural network on MNIST digits
- Test model accuracy
- Draw digits manually and get predictions
- Visualize neural network structure
- Modify and experiment with architectures

---

## Features

- Custom neural network implementation (from scratch in Java)
- Multi-layer perceptron (MLP) architecture
- Configurable hidden layers
- Activation functions (Sigmoid, ReLU)
- Training and testing using MNIST dataset
- Drawing canvas for real-time prediction
- Save and load trained models
- Neural network visualization
- Configurable training settings

---

## Main Menu Functions

| Option | Feature | Description |
|--------|--------|-------------|
| 0 | Reset Network | Resets the neural network (all weights will be lost) |
| 1 | View Network | Displays structure, layers, neurons, and weights |
| 2 | Edit Network | Modify architecture (disabled after training starts) |
| 3 | Load Model | Load a saved model from file |
| 4 | Save Model | Save current model to file |
| 5 | Train Network | Train using MNIST dataset |
| 6 | Test Network | Evaluate model accuracy |
| 7 | Draw Test | Draw a digit and get prediction |
| 8 | Settings | Configure training parameters |
| 9 | Help | Open this help menu |

---

## Neural Network Architecture

Input Layer → Hidden Layer(s) → Output Layer

### Input Layer
- 784 neurons
- Represents 28×28 pixel MNIST images

### Hidden Layers
- Fully configurable
- Performs feature extraction
- Uses activation functions

### Output Layer
- 10 neurons (digits 0–9)
- Highest activated neuron is the prediction

### Example Output
[0.01, 0.02, 0.90, 0.46, 0.01, 0.78, ...]
Prediction: 2

---

## Activation Functions

### Sigmoid
Range: (0, 1)

Formula:
f(x) = 1 / (1 + e^-x)

Pros:
- Smooth output
- Easy to understand

Cons:
- Vanishing gradient problem
- Slower training

---

### ReLU (Rectified Linear Unit)
Range: [0, ∞)

Formula:
f(x) = max(0, x)

Pros:
- Fast computation
- Reduces vanishing gradient issue

Cons:
- Dead neurons problem

---

### Future Improvements
- Leaky ReLU
- Softmax

---

## Training Settings

### Batch Size
Number of samples processed before weight update.

- Smaller batch: noisier but may generalize better
- Larger batch: more stable but requires more memory
- Recommended: 64–128 (powers of 2)

---

### Learning Rate Decay

Formula:
lr = lr * e^(-decayRate × epoch)

Purpose:
- High learning rate early in training
- Lower learning rate later for stability

Recommended decay rate: 0.001 – 0.05

---

## Purpose

This project was built to:
- Understand neural networks from scratch
- Learn backpropagation and gradient descent
- Experiment with architecture design
- Visualize machine learning behavior

---

## Technologies Used

- Java
- MNIST Dataset
- Java AWT / Swing (UI and drawing canvas)

---

## Key Learnings

- Neural network fundamentals
- Backpropagation implementation
- Gradient descent optimization
- Machine learning pipeline design
- UI integration with ML models

---

## Note

This project avoids using ML libraries to focus on understanding core machine learning concepts.