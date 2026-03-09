# Magic Painter


## Overview

**Magic Painter** is an interactive, Java-based generative art application built using the Processing library. It utilizes particle systems driven by Perlin noise flow fields to dynamically redraw uploaded images in a creative, algorithmic style. The application features a custom multi-window setup: a main drawing canvas and a dedicated Java Swing Control Panel for real-time parameter tuning.

## How It Works

At its core, Magic Painter operates on two main physical concepts:

1. **Flow Fields:** A grid of vectors (currents) is generated across the canvas using 3D Perlin noise. These vectors dictate the direction and force applied to any particle that enters their grid space.


2. **Particle Systems ("Painters"):** When you click the canvas, a "Painter" (a collection of particles) is spawned. These particles move smoothly across the screen, propelled by the underlying flow field.


3. **Generative Rendering:** As the particles move, they sample the color of the loaded original image at their current coordinates. They then draw strokes on the canvas using the extracted color data, modified by the user's hue, saturation, and brightness settings. If particles hit the edge of the canvas, they wrap around to the opposite side.



## Features

**Interactive Particle Spawning:** Click anywhere on the canvas to instantiate a new swarm of particles.


**Real-time Flow Field Manipulation:** The underlying forces continuously evolve. You can pause, resume, or adjust the scale and magnitude of the currents on the fly.

 
**Advanced Color Controls:** Override or enhance the original image's colors by adjusting Alpha, minimum/maximum Hue, Saturation coefficients, and Brightness coefficients.

 
**Custom UI & Scrollbars:** Includes bespoke horizontal and vertical scrollbars built from scratch to navigate canvases larger than your display.

 
**Save & Load:** Easily load a base image (`pic.jpg` by default) to trace, and save your generated artwork directly to a `/generated_images/` directory.


## UI Controls

The project includes a robust control panel with several modules:


**IMAGE:** Load a source image to base the generative strokes on, or save your current canvas output.


**CANVAS:** Dynamically resize the canvas width and height, or erase the current drawings to start fresh.



**COLORS:** 

* Adjust the `Alpha` transparency.

* Toggle `Original Hue` to use the image's exact colors, or map colors to a custom hue spectrum (`min HUE` and `max HUE`).

* Multiply saturation and brightness using `x SAT` and `x BRI` text fields.

* Choose the drawing blend mode (`Blend`, `Replace`).




**PAINTERS:** 

* Define the number of `Particles` spawned per click and their maximum `Velocity`.

* Delete the first, last, or all active particle systems to manage performance.

* Pause/Resume painter movement.



**FORCES:** 

* Adjust the Perlin noise `Scale` and vector `Mag` (Magnitude).

* Toggle `Show Forces` to visualize the underlying vector grid driving the particles.

* Pause/Resume the flow field evolution.



## Installation & Running

*(Note: Adjust these instructions based on your exact build system, e.g., Maven, Gradle, or raw IDE compilation)*

1. Clone the repository:
```bash
git clone https://github.com/yourusername/magic-painter.git

```


2. Ensure you have Java 8+ installed. The project relies on the core `processing.core.*` library and `javax.swing.*`.


3. Place a sample image named `pic.jpg` in the root directory (or the designated execution path) to get started.


4. Compile and run the `Main.java` class.


## Screenshots

<img width="1802" height="787" alt="Screenshot_20260309_224506" src="https://github.com/user-attachments/assets/cc650589-0612-48d1-8752-c936b907420e" />
<img width="1802" height="787" alt="Screenshot_20260309_224534" src="https://github.com/user-attachments/assets/55a3f894-e555-4e15-a869-7d87dddaa455" />
<img width="1806" height="983" alt="Screenshot_20260309_225614" src="https://github.com/user-attachments/assets/82e5e591-7d47-4581-9264-06997df240de" />
<img width="1806" height="983" alt="Screenshot_20260309_225658" src="https://github.com/user-attachments/assets/bfa4e5b6-1a28-4539-9b24-e701732e0229" />
<img width="1806" height="983" alt="Screenshot_20260309_225848" src="https://github.com/user-attachments/assets/58a4d901-bd09-4c56-baf0-669ea8086940" />
<img width="1806" height="983" alt="Screenshot_20260309_225957" src="https://github.com/user-attachments/assets/54f9444b-e7c0-48d6-85b0-c4acba524167" />
<img width="1806" height="983" alt="Screenshot_20260309_230004" src="https://github.com/user-attachments/assets/e9fef1f2-a959-40de-a5fa-fa31987d5c2c" />
