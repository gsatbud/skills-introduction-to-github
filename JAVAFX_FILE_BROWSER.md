# JavaFX File Browser

A simple JavaFX GUI application for browsing files and directories.

## Application Layout

```
┌─────────────────────────────────────────────────────────────────┐
│ JavaFX File Browser                                        [_][□][×]│
├─────────────────────────────────────────────────────────────────┤
│ [Browse Directory...] [Home Directory] [Refresh]                │
├─────────────────────────┬───────────────────────────────────────┤
│ File Tree               │ File Information                      │
│                         │                                       │
│ ▼ /home/user            │ Name: Documents                       │
│   ▶ Desktop             │ Path: /home/user/Documents            │
│   ▼ Documents           │ Type: Directory                       │
│     • file1.txt         │ Readable: true                        │
│     • file2.pdf         │ Writable: true                        │
│     ▶ Projects          │ Executable: true                      │
│   ▶ Downloads           │ Last Modified: Nov 20 2025 02:15 AM   │
│   ▶ Pictures            │                                       │
│                         │ Contents:                             │
│                         │   Directories: 1                      │
│                         │   Files: 2                            │
├─────────────────────────┴───────────────────────────────────────┤
│ Status: Loaded /home/user                                       │
└─────────────────────────────────────────────────────────────────┘
```

## Features

- Browse directories using a tree view
- Navigate through the file system
- View detailed file/directory information
- Display file sizes in human-readable format
- Show file permissions and last modified dates
- Lazy loading of directory contents for performance

## Requirements

- Java 17 or higher
- Maven 3.6+

## Building

To compile the project:

```bash
mvn clean compile
```

## Running

To run the JavaFX application using Maven:

```bash
mvn javafx:run
```

Alternatively, after building, you can run the application from the JAR:

```bash
mvn clean package
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp target/file-browser-1.0-SNAPSHOT.jar com.github.skills.filebrowser.FileBrowserApp
```

Note: Replace `$PATH_TO_FX` with the path to your JavaFX SDK lib directory if not using Maven.

## Usage

1. When the application starts, it displays the user's home directory
2. Use the "Browse Directory..." button to select a different directory
3. Use the "Home Directory" button to return to the home directory
4. Use the "Refresh" button to reload the current directory
5. Click on any file or folder in the tree view to see its details
6. Expand folders to see their contents

## Project Structure

```
src/main/java/com/github/skills/filebrowser/
└── FileBrowserApp.java  - Main application class
```

## Implementation Details

- Uses JavaFX TreeView for hierarchical file display
- Implements lazy loading for better performance with large directories
- Shows file metadata including size, permissions, and timestamps
- Split pane layout with tree view and information panel
