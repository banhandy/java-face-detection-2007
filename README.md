# Java Face Detection — Neural Network Based

> **Bachelor Thesis · Ban Handy · December 2007**
>
> A complete, trainable neural network face detection system
> built entirely from scratch in pure Java. Includes a full Swing GUI,
> multi-scale sliding window scanner, backpropagation training engine,
> Javadoc HTML documentation, and training + test image datasets.
>
> **No OpenCV. No external libraries. No frameworks.**
> Just the Java standard library and the mathematics of neural networks,
> implemented line by line.

---

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/banhandy/java-face-detection-2007.git
cd java-face-detection-2007

# Compile all source files
javac *.java

# Launch the application
java FdApp
```

Training images and test images are included in the `train set/`
and `test set/` folders — the app is ready to train and scan
immediately after cloning.

---

## 📄 Documentation

Full **Javadoc HTML documentation** is included in the `/doc` folder,
generated on **January 22, 2008**.

```bash
# Browse documentation locally
open doc/index.html
```

Source comments are written in **Bahasa Indonesia** (the original
thesis language). The Javadoc HTML renders in English — all class
names, method signatures, parameter tables, and return descriptions
are fully readable in the generated docs.

---

## 🧠 What This Is

This is **not** a skin-tone filter or a rule-based detector.

The core classifier is a **trained Multi-Layer Perceptron (MLP)
neural network** that decides whether any given 18×27 pixel window
in an image contains a face or not. The network is trained from
scratch using **backpropagation with momentum** on a dataset of
face and non-face image patches.

This is **machine learning-based computer vision** — the same
fundamental approach that underlies modern deep learning face
detectors — implemented entirely by hand in December 2007,
five years before AlexNet changed the field.

---

## 🏗️ Architecture

The system is built as a clean **object-oriented layered architecture**,
from the lowest-level neuron up to the full GUI application:

```
┌─────────────────────────────────────────────────────┐
│  FdApp.java          — Swing GUI application         │
│    uses ↓                                            │
│  FaceDetector.java   — Detection & training engine   │
│    uses ↓            uses ↓                          │
│  ImageFilter.java    MyMlp.java  — Neural network    │
│                        uses ↓                        │
│                      Weight.java — Weight management │
│                        uses ↓                        │
│                      Neuron.java — Single neuron     │
└─────────────────────────────────────────────────────┘
```

---

## 📁 File Reference

### `Neuron.java` — The Building Block
The lowest-level component. Represents a single artificial neuron:
- Stores the neuron's output value
- Applies the **sigmoid activation function**: `f(x) = 1 / (1 + e^-x)`
- Holds the delta value used during backpropagation weight updates

---

### `Weight.java` — Connection Weights
Manages the weighted connections between neurons in adjacent layers:
- Stores the current weight value for each connection
- Stores the **previous weight delta** for momentum calculation
  during backpropagation
- Enables the momentum term: `Δw(t) = α·δ·input + m·Δw(t-1)`

---

### `MyMlp.java` — The Neural Network
Assembles neurons and weights into a working **Multi-Layer Perceptron**:
- `IncrementalTrain(alpha, input[], target, momentum)` —
  performs one forward pass + backpropagation weight update
  for a single training sample
- `simmulate(input[])` — forward pass only, returns network output
- `sumSquaredError(inputs[][], targets[])` —
  computes total SSE across the entire training set
- `saveWeight(path)` / `loadWeight(path)` —
  serializes and restores all network weights to/from a `.dat` file

---

### `ImageFilter.java` — Image Preprocessing
Handles all pixel-level image operations:
- Extracts 18×27 pixel windows at specified coordinates
- Scales images to target dimensions for multi-level detection
- `mirrorX()` — horizontal mirror for training data augmentation
- `getProcWindow()` — returns the window as a flattened
  400-element `double[]` array ready for network input

---

### `FaceDetector.java` — The Core Engine
The main reusable component. Designed to be embedded in any
Java application that needs face detection capability.

**Neural network management:**
- Wraps `MyMlp` with configurable training parameters:
  - Learning rate `alpha` (default: `0.1`)
  - Momentum `m` (default: `0.7`)
  - Error tolerance `errToleran` (default: `0.05`)
- Training continues until **Sum Squared Error** drops below tolerance

**Input normalization:**
Each 18×27 pixel window is flattened to a **400-feature vector**
and normalized using pre-computed mean and standard deviation arrays:

```java
normalized[i] = (pixel[i] - mean[i]) / std[i]
```

These 400-element `mean[]` and `std[]` arrays were computed from
the original training dataset and embedded directly in the source.
This is identical in concept to `transforms.Normalize(mean, std)`
in modern PyTorch.

**Multi-scale sliding window scanning:**
```
For each scale level 0 → N:
  1. Resize image by (scaleFactor × level)
  2. Slide an 18×27 window across every 2 pixels
  3. Normalize the 400-pixel feature vector
  4. Pass through the trained MLP
  5. If output > threshold → record as detected face
  6. Scale bounding box back to original image coordinates
```

**Training set creation:**
- Loads face images (`s1.jpg` … `sN.jpg`) → target output `0.9`
- Loads non-face images, extracts random patches → target output `-0.9`
- Automatically mirrors all face images horizontally
  (doubles positive training samples — data augmentation)

**Threading:**
Both training and scanning run in dedicated `Thread` instances,
keeping the GUI responsive during long operations.

---

### `FdApp.java` — The Desktop Application
A complete Java Swing GUI that drives the entire system.

**Training Panel** — configure and run neural network training:
- Learning rate, momentum, error tolerance fields
- Start Training / Stop Training buttons
- Real-time **Sum Squared Error** display during training

**Scan Panel** — run face detection on loaded images:
- Scale levels, scale factor, threshold fields
- Scan button triggers multi-scale detection
- Detected faces shown as **blue bounding rectangles**
  drawn directly onto the image display

**Create Set Panel** — build training datasets:
- Specify training image folder, face image count,
  non-face image count, patches per non-face image
- Loads, preprocesses, and normalizes all images into
  the training matrix

**System Panel** — weight persistence:
- **Save Weight** — serialize trained network to `.dat` file
- **Load Weight** — restore a previously trained network

---

## 📂 Repository Contents

```
java-face-detection-2007/
│
├── Neuron.java            ← Single neuron implementation
├── Weight.java            ← Neural connection weight management
├── MyMlp.java             ← Multi-layer perceptron network
├── ImageFilter.java       ← Image preprocessing & window extraction
├── FaceDetector.java      ← Core face detection engine
├── FdApp.java             ← Swing GUI application
│
├── doc/                   ← Javadoc HTML documentation (Jan 2008)
│   ├── index.html         ← Entry point (frameset)
│   ├── FaceDetector.html  ← FaceDetector class docs
│   ├── FdApp.html         ← FdApp class docs
│   └── ...
│
├── train set/             ← Training image dataset
│   ├── s1.jpg … sN.jpg    ← Face images
│   └── 1.jpg … M.jpg      ← Non-face images
│
├── test set/              ← Test images for scanning
│   └── *.jpg
│
└── README.md
```

---

## 🔄 Usage Workflow

### Scan with Pre-Trained Weights

1. Launch: `java FdApp`
2. **System** → **Load Weight** → select a `.dat` file
3. **Create Set** → **Load Image** → select any `.jpg`
4. **Scan** → set Threshold, Scale Level, Scale Factor
5. Click **Scan** — blue rectangles mark detected faces

### Train on the Included Dataset

1. Launch: `java FdApp`
2. **Create Set** → set folder to `train set`, set counts
3. Click **Create Train Set** — loads and normalizes all images
4. **Training** → set learning rate, momentum, error tolerance
5. Click **Start Training** — watch Sum Squared Error decrease
6. Click **Stop Training** when SSE reaches acceptable level
7. **System** → **Save Weight** — save your trained model

### Train on Your Own Dataset

Prepare a folder with:
```
your-folder/
├── s1.jpg, s2.jpg, ... sN.jpg   ← face image crops (18×27 or larger)
└── 1.jpg, 2.jpg, ... M.jpg      ← non-face scene images
```
Then follow the same steps above using your folder path.

---

## 🕰️ Historical Context

| Year | Event |
|------|-------|
| 2001 | Viola-Jones Haar Cascade paper published |
| 2006 | OpenCV 1.0 — Java bindings immature |
| **Dec 2007** | **This project completed as bachelor thesis** |
| **Jan 2008** | **Javadoc HTML documentation generated** |
| 2009 | OpenCV 2.0 — better Java support |
| 2012 | AlexNet — deep learning transforms computer vision |
| 2015 | Google Vision API — cloud face detection |
| 2017 | MTCNN — modern CNN-based face detection |
| 2024 | MediaPipe — real-time on-device detection |

In December 2007, a Java developer wanting face detection had
essentially one path: implement every component from scratch.
No practical ML framework existed for Java. No pretrained models
to download. No `model.fit()`.

This repository is what that looked like — a complete,
working implementation from neuron to GUI.

---

## 🔗 Concepts vs. Modern Equivalents

Every idea implemented manually here is now a one-line API call:

| This Project (2007) | Modern Equivalent |
|---|---|
| `Neuron.java` sigmoid activation | `nn.Sigmoid()` |
| `Weight.java` delta storage | `tensor.grad` |
| `MyMlp.IncrementalTrain()` | `optimizer.step()` |
| `myNet.sumSquaredError()` | `nn.MSELoss()` |
| `mean[]` / `std[]` embedded arrays | `transforms.Normalize(mean, std)` |
| Multi-scale image pyramid | Feature Pyramid Network (FPN) |
| Sliding window + threshold | Anchor boxes + NMS |
| `mirrorX()` augmentation | `transforms.RandomHorizontalFlip()` |
| `saveWeight()` / `loadWeight()` | `torch.save()` / `torch.load()` |
| Threaded training loop | Background `DataLoader` worker |

The abstractions changed. The mathematics did not.

---

## 📚 Academic Information

| | |
|---|---|
| **Author** | Ban Handy |
| **Institution** | Bunda Mulia University |
| **Degree** | Sarjana Komputer (S.Kom) — Bachelor of Computer Science |
| **Completed** | December 2007 |
| **Docs Generated** | January 22, 2008 |
| **Source Language** | Bahasa Indonesia |
| **Documentation** | English (standard Javadoc HTML output) |

---

## 👤 About the Author

**Ban Handy** — Full-stack developer and software engineer
based in Surabaya, Indonesia. Building software continuously
since 2007 across medical technology, industrial ERP, fintech,
mobile apps, e-commerce, and financial automation.

| | |
|---|---|
| 🌐 Website | [banhandy.vercel.app](https://banhandy.vercel.app) |
| 📧 Email | banhandy@gmail.com |
| 📈 MQL5 | [mql5.com/en/users/HandyBan](https://www.mql5.com/en/users/HandyBan) |
| 💼 GitHub | [github.com/banhandy](https://github.com/banhandy) |

---

## 📄 License

MIT — free to study, reference, and build upon.

This is an archived academic project, preserved as a historical
record of neural network-based computer vision work from 2007.
Not intended as production software.

---

*"Before you use the abstraction, understand what it abstracts."*
