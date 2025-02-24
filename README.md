# Portfolio Projects ReadMe

## 1. Baseball Performance Analytics
### Overview
Analyzed offseason pitching and hitting data using Python and visualized performance metrics using Tableau. Created automated scripts to clean, process, and generate insights from large datasets.

### Technologies Used
- **Python (Pandas, NumPy)** for data processing
- **CSV data management** for handling large baseball datasets
- **Statistical analysis** of key performance metrics (e.g., exit velocity, strike rates, OPS)
- **Tableau** for data visualization

### Features
- Automated data cleaning and standardization
- Computation of key baseball stats (strikeout rate, walk rate, slugging percentage, etc.)
- Generation of CSV reports for visualization
- Interactive dashboards in Tableau

### How to Run
1. Ensure all dependencies (`pandas`, `numpy`) are installed.
2. Place the cleaned baseball dataset in the project directory.
3. Run the Python scripts to generate statistical insights.
4. Load the CSV outputs into Tableau for visualization.

---

## 2. Autonomous Racing Bots
### Overview
Developed Python-based control systems for autonomous robots competing in the One-Meter Dash and Marathon events. The robots were programmed to navigate autonomously using motor control, line-following algorithms, and obstacle detection.

### Technologies Used
- **MicroPython** for embedded programming
- **PWM motor control** for precise speed adjustments
- **Ultrasonic distance sensors** for obstacle detection
- **Async programming** using `asyncio` for state management

### Features
- Line-following using real-time sensor data
- Obstacle detection and avoidance
- Optimized motor control for efficient movement
- State-based decision-making for navigation

### How to Run
1. Flash the MicroPython firmware onto the microcontroller.
2. Upload the provided scripts (`one_meter_dash.py`, `marathon.py`).
3. Power on the bot and press the start button to begin.

---

## 3. CSCI 205 - Software Engineering and Design
### Overview
Developed a modified clone of the NYT game **Connections** with multiple difficulty levels, built using the **Model-View-Controller (MVC)** design pattern.

### Technologies Used
- **Java (JavaFX)** for UI and game logic
- **JUnit** for testing
- **CSS** for UI styling
- **Git/GitLab** for version control

### Features
- Interactive 4x4 word grid gameplay
- 5 difficulty levels: Easy, Medium, Hard, Extreme, Hollywood
- Shuffle and return buttons for better gameplay control
- Hints to guide players when close to a correct category
- Modular MVC architecture for scalability

### How to Run
1. Install JavaFX and ensure Java 11+ is set up.
2. Clone the repository and navigate to the main project directory.
3. Run `ConnectionsMain.java` to start the game.
4. Select a difficulty level and start playing.

### Package Structure
- **Model Package**: Manages game logic and board configurations.
- **View Package**: Handles UI rendering and user interactions.
- **Controller Package**: Manages game state and user inputs.
- **Resources Package**: Contains CSS stylesheets for UI design.
- **Test Package**: Includes JUnit tests for validation.

### Demo Video
[Watch here](https://drive.google.com/file/d/1YaXpaJU_iNMpkE3Z3V0fNuwSTUqKZHjT/view?t=1)
