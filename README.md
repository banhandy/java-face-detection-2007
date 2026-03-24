# Java Face Detection — Neural Network Based

> **Bachelor Thesis · Ban Handy · December 2007**
>
> A complete, trainable neural network face detection system
> built entirely from scratch in pure Java — with a full Swing GUI,
> multi-scale sliding window scanner, backpropagation training
> engine, and Javadoc HTML documentation.
>
> No OpenCV. No external libraries. Just Java standard library and math.

---

## 📄 Documentation

This project includes **Javadoc-generated HTML documentation**
(`/docs/index.html`) produced in January 2008.

The source code comments are written in **Bahasa Indonesia**
(the original thesis language). The generated Javadoc HTML
renders in English using standard Javadoc formatting —
class names, method signatures, parameter tables, and
return value descriptions are all fully readable.

To browse the docs locally:
```
open docs/index.html
```

---

## 🧠 What This Is — And Why It Matters

This is **not** a skin-tone filter or a rule-based detector.

The core of this system is a **trained Multi-Layer Perceptron (MLP)
neural network** that classifies 18×27 pixel image windows
as either *face* or *non-face*. The network is trained using
**backpropagation with momentum**, run against a dataset of
face and non-face images that you prepare yourself.

This places the project firmly in the category of
**machine learning-based computer vision** — the same
fundamental approach that underlies modern deep learning
face detectors, implemented entirely by hand in 2007.

---

## 🏗️ System Architecture

### `FaceDetector.java` — The Core Engine

The main component. A reusable Java class designed to be
embedded in other applications.

**Neural Network:**
- Wraps a custom `MyMlp` (Multi-Layer Perceptron) implementation
- Trained via **incremental backpropagation** with configurable:
  - Learning rate `alpha` (default: `0.1`)
  - Momentum `m` (default: `0.7`)
  - Error tolerance `errToleran` (default: `0.05`)
- Training stops automatically when **Sum Squared Error** drops
  below the tolerance threshold
- Positive samples (face patches) → target output `0.9`
- Negative samples (non-face patches) → target output `-0.9`

**Input Normalization:**
Each 18×27 pixel window is flattened to a **400-dimensional
feature vector** and normalized using pre-computed mean and
standard deviation arrays:

```java
normalized[i] = (pixel[i] - mean[i]) / std[i]
```

These 400-element `mean[]` and `std[]` arrays embedded in the
source were computed from the original training dataset.
This is equivalent to what modern frameworks call
`transforms.Normalize(mean, std)`.

**Multi-Scale Sliding Window Detection:**
```
For scale level 0 to N:
  Resize image by (scaleFactor × level)
  Slide an 18×27 window across the image (step = 2px)
  Normalize each window's 400 pixel features
  Pass through trained MLP
  If output > threshold → record bounding box
  Scale bounding box back to original image coordinates
```

**Data Augmentation:**
When building the training set, every face image is automatically
mirrored horizontally — doubling the positive training examples.
This is still standard practice in 2025.

**Weight Persistence:**
Trained network weights can be saved to and loaded from `.dat`
files, allowing the trained model to be reused without retraining.

**Threading:**
Both training and scanning run in dedicated `Thread` instances,
keeping the UI responsive during long operations.

---

### `FdApp.java` — The Desktop Application

A complete Java Swing GUI for interacting with the detector.

**Training Panel:**
- Configure learning rate, momentum, error tolerance
- Start / Stop training
- Real-time Sum Squared Error display

**Scan Panel:**
- Configure scale levels, scale factor, detection threshold
- Trigger scan on loaded image
- Detected faces shown as **blue bounding rectangles** drawn
  directly on the image using `Graphics.drawRect()`

**Create Set Panel:**
- Specify training image folder
- Set number of face images, non-face images, and patches per image
- Loads and preprocesses the full training dataset

**System Panel:**
- Save trained weights to file
- Load previously saved weights

---

### `ImageFilter.java` — Image Preprocessing

Handles pixel-level image operations:
- Extracts 18×27 pixel windows from images at given coordinates
- Scales images to target dimensions for multi-level scanning
- `mirrorX()` — horizontal mirror for training data augmentation
- `getProcWindow()` — returns flattened 400-element pixel array

---

### `MyMlp.java` — Neural Network Implementation

The backpropagation MLP implementation:
- `IncrementalTrain(alpha, input[], target, momentum)` — single sample weight update
- `simmulate(input[])` — forward pass, returns network output
- `sumSquaredError(inputs[][], targets[])` — computes total training error
- `saveWeight(path)` / `loadWeight(path)` — weight serialization

---

## 🖥️ How to Run

### Requirements
```
Java JDK 1.4 or higher
(Originally built with JDK 1.4 — compatible with all modern JDKs)
Pure standard library — no external dependencies
```

### Compile all files
```bash
javac FdApp.java FaceDetector.java ImageFilter.java MyMlp.java
```

### Launch the application
```bash
java FdApp
```

---

## 🔄 Typical Workflow

### Option A — Use Pre-trained Weights
1. Launch the app
2. **System panel** → Click **Load Weight** → select a `.dat` file
3. **Create Set panel** → Click **Load Image** → select a photo
4. **Scan panel** → adjust Threshold, Scale Level, Scale Factor
5. Click **Scan** → blue rectangles appear on detected faces

### Option B — Train on Your Own Dataset
Prepare a folder with this structure:
```
train set/
├── s1.jpg        ← face image 1
├── s2.jpg        ← face image 2
├── ...
├── sN.jpg        ← face image N
├── 1.jpg         ← non-face image 1
├── 2.jpg         ← non-face image 2
└── ...
```

Then in the app:
1. **Create Set panel** → set folder path, face count, non-face count
2. Click **Create Train Set** — loads and normalizes all images
3. **Training panel** → set learning rate, momentum, error tolerance
4. Click **Start Training** — watch SSE decrease in real time
5. Click **Stop Training** when SSE is acceptably low
6. **System panel** → **Save Weight** — save your trained model
7. Load an image and **Scan**

---

## 📁 Repository Structure

```
java-face-detection-2007/
│
├── FaceDetector.java       ← Core detection engine (main component)
├── FdApp.java              ← Swing GUI application
├── ImageFilter.java        ← Image preprocessing & window extraction
├── MyMlp.java              ← MLP neural network with backpropagation
│
├── docs/
│   ├── index.html          ← Javadoc frameset entry point (Jan 2008)
│   ├── FaceDetector.html   ← FaceDetector class documentation
│   ├── FdApp.html          ← FdApp class documentation
│   └── allclasses-frame.html
│
└── README.md
```

---

## 🕰️ Historical Context

| Year | Event |
|------|-------|
| 2001 | Viola-Jones face detection paper published (Haar Cascades) |
| 2006 | OpenCV 1.0 released — Java bindings immature and limited |
| **Dec 2007** | **This project completed** |
| Jan 2008 | Javadoc HTML documentation generated |
| 2009 | OpenCV 2.0 — better Java support arrives |
| 2012 | AlexNet — deep learning transforms computer vision |
| 2015 | Google Vision API — cloud face detection as a service |
| 2017 | MTCNN — accurate multi-task CNN face detection |
| 2024 | MediaPipe — real-time on-device detection on mobile |

In late 2007, a Java developer wanting to do face detection had
essentially one option: implement it from scratch.
OpenCV's Java bindings were not production-ready.
Deep learning did not exist in practical form.
Cloud APIs were a decade away.

This project demonstrates what that implementation looked like.

---

## 🔗 Mapping to Modern ML Concepts

Every concept implemented manually here is now abstracted
by modern frameworks:

| This Project (2007) | Modern Equivalent |
|---|---|
| `MyMlp.IncrementalTrain()` | `optimizer.step()` |
| `myNet.sumSquaredError()` | `nn.MSELoss()` |
| `mean[]` / `std[]` arrays | `transforms.Normalize(mean, std)` |
| Multi-scale image resizing | Feature Pyramid Network (FPN) |
| Sliding window + threshold | Anchor boxes + Non-Maximum Suppression |
| `saveWeight()` / `loadWeight()` | `torch.save()` / `torch.load()` |
| `mirrorX()` augmentation | `transforms.RandomHorizontalFlip()` |
| Binary face/non-face output | Binary classification head |
| Thread-based async training | Background training worker |

The abstractions are different. The mathematics is identical.

---

## 📚 Academic Information

| | |
|---|---|
| **Author** | Ban Handy |
| **Institution** | Bunda Mulia University |
| **Degree** | Sarjana Komputer (S.Kom) — Bachelor of Computer Science |
| **Year Completed** | 2007 |
| **Docs Generated** | January 22, 2008 (Javadoc timestamp) |
| **Source Language** | Bahasa Indonesia |
| **Documentation** | English (Javadoc HTML) |

---

## 👤 Author

**Ban Handy** — Full-stack developer based in Surabaya, Indonesia.
Building software continuously since 2007 across medical technology,
industrial ERP, fintech, mobile apps, e-commerce, and financial
automation.

| | |
|---|---|
| 🌐 | [banhandy.vercel.app](https://banhandy.vercel.app) |
| 📧 | banhandy@gmail.com |
| 📈 | [MQL5 Marketplace](https://www.mql5.com/en/users/HandyBan) |
| 💼 | [github.com/banhandy](https://github.com/banhandy) |

---

## 📄 License

MIT — free to study, reference, and build upon.

Archived academic project. Preserved as a historical record
of neural network-based computer vision work from 2007.

---

*"Before you use the abstraction, understand what it abstracts."*
