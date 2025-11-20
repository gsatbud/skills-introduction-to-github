# JavaFX File Browser

A simple JavaFX GUI application for browsing files and directories.

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

To run the JavaFX application:

```bash
mvn javafx:run
```

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
